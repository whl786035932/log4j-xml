package cn.videoworks.cms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.videoworks.cms.dto.ApkDto;
import cn.videoworks.cms.dto.ApkStatusDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.DataDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ApkVersion;
import cn.videoworks.cms.enumeration.ApkStatus;
import cn.videoworks.cms.enumeration.ApkType;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.service.ApkVersionService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.FileUtil;
import cn.videoworks.cms.util.Md5Util;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.cms.vo.UserVo;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;
/**
 * sssss
 * @author whl
 *
 */
@Controller
@RequestMapping(value = "apkmanager")
public class ApkManagerController {

	@Resource
	private Properties databaseConfig;
	
	@Resource 
	private ApkVersionService apkVersionService;

//	private static ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);

	private static final Logger log = LoggerFactory.getLogger(ApkManagerController.class);

	@RequestMapping(value = "release", method = RequestMethod.GET)
	public String releaseIndex(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo userVo = (UserVo) session.getAttribute("user");
		userVo.getOperations();
		model.addAttribute("type", ApkType.RELEASE.getValue());
		return "site.cms.apkmanager.index";
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String testIndex(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo userVo = (UserVo) session.getAttribute("user");
		userVo.getOperations();
		model.addAttribute("type", ApkType.TEST.getValue());
		return "site.cms.apkmanager.index";
	}

	@RequestMapping(value = "/apkpackage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updatePhoto(@RequestParam("apk_file") MultipartFile file,
			@RequestParam("mainVersion") String mainVersion,
			@RequestParam("childVersion") Integer childVersion,
			@RequestParam("entry") String entry,
			@RequestParam("md5") String md5,
			@RequestParam(name="description",required=false) String description,
			@RequestParam("type") Integer type, @RequestParam(name="force") Boolean force,HttpServletRequest request) {
		LogDto logDto = ConvertDto.buildOperationLog("CMS-上传apk压缩文件", BusinessType.ADD.getValue(), "apk管理",
				UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(),
				LogLevel.INFO.getValue(), new HashMap<String, Object>());
		log.info(JsonConverter.format(logDto));
		Map<String, Object> map = new HashMap<String, Object>();
		ApkVersion apkVersion = apkVersionService.getByType(type,mainVersion,childVersion);
		if(apkVersion!=null) {
			map.put("statusCode", ResponseStatusCode.CONFLICT);
			map.put("message", "上传失败，该版本已经存在！");
			return map;
		}
		try {
			if (file.getSize() > 0) {
				byte[] bytes = file.getBytes();
				String md5File = Md5Util.getMD5(bytes);
				Long size = file.getSize();
				String originalFilename = file.getOriginalFilename();
				
				if(!md5.equals(md5File)) {
					map.put("statusCode", ResponseStatusCode.CONFLICT);
					map.put("message", "上传失败，文件MD5值不匹配，请确认MD5值是否正确！");
					return map;
				}
				
				HashMap<String, Object> uploadMap = (HashMap<String, Object>) uploadFile(file, mainVersion,childVersion,type);
				Boolean uploadFlag = (Boolean) uploadMap.get("flag");
				if (!uploadFlag) {
					map.put("statusCode", ResponseStatusCode.INTERNAL_SERVER_ERROR);
					map.put("message", "图片上传临时服务器失败!");
					log.info("图片上传临时服务器失败!");
				} else {
					addApkDb(mainVersion,childVersion,force, description,type,entry,md5File,size,originalFilename);
//					String filePath = (String) uploadMap.get("filePath");
//					unzipApkPackage(filePath, version);
					map.put("statusCode", ResponseStatusCode.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void addApkDb(String mainVersion,Integer childVersion,Boolean force, String description, Integer type, String entry,String md5,Long size,String originalFilename)  {
		//没有记录启用，有记录但是没有启动的就是启用，其他是禁用
		ApkVersion apkVersion = new ApkVersion();
		apkVersion.setMainVersion(mainVersion);
		apkVersion.setChildVersion(childVersion);
		apkVersion.setIsForce(force);
		apkVersion.setDescription(description);
		try {
			apkVersion.setMd5(md5);
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			apkVersion.setInserted_at(nowTimeStamp);
			apkVersion.setUpdated_at(nowTimeStamp);
			apkVersion.setType(type);
			Boolean status = getStatus(type);
			if(status) {
				apkVersion.setStatus(ApkStatus.ENABLE.getValue());
			}else {
				apkVersion.setStatus(ApkStatus.DISABLE.getValue());
			}
			apkVersion.setSize(size);
			apkVersion.setArchvie(originalFilename);
			apkVersion.setEntry(entry);
			apkVersionService.add(apkVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean  getStatus(Integer type) {
		List<ApkVersion> lists = apkVersionService.list(-1,type);
		if(lists.size()==0) {
			return true;
		}else  {
			 lists = apkVersionService.list(ApkStatus.ENABLE.getValue(),type);
			 if(lists.size()==0) {
				 return true;
			 }
			 return false;
		}
	}
	
	/**
	 * delete:(删除)
	 * @author   meishen
	 * @Date	 2018	2018年11月9日		下午5:28:27 
	 * @return Map<String,Object>    
	 * @throws
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,HttpServletRequest request) {
		LogDto logDto = ConvertDto.buildOperationLog("CMS-删除apk", BusinessType.ADD.getValue(), "apk管理",
				UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(),
				LogLevel.INFO.getValue(), new HashMap<String, Object>());
		log.info(JsonConverter.format(logDto));
		Map<String, Object> map = new HashMap<String, Object>();
		ApkVersion apkVersion = apkVersionService.get(id);
		if(null == apkVersion) {
			map.put("statusCode", ResponseStatusCode.CONFLICT);
			map.put("message", "删除失败，该版本不存在！");
			return map;
		}
			
		if(apkVersion !=null && apkVersion.getStatus() == ApkStatus.ENABLE.getValue()) {
			map.put("statusCode", ResponseStatusCode.CONFLICT);
			map.put("message", "删除失败，该版本为启用状态，禁止删除！");
			return map;
		}
		apkVersionService.delete(id);
		map.put("statusCode", ResponseStatusCode.OK);
		map.put("message", "删除成功！");
		return map;
	}
	
	/**
	 * 上传到临时服务器.
	 */
	private Map<String, Object> uploadFile(MultipartFile file, String mainVersion,Integer childVersion,Integer type) {
		String apkType="";
		if(type.intValue()==ApkType.RELEASE.getValue()) {
			apkType = "release";
		}else if(type.intValue()== ApkType.TEST.getValue()) {
			apkType = "test";
		}
		
		String property = databaseConfig.getProperty("apk.dir") + File.separator + apkType+File.separator+mainVersion+"."+childVersion;
		String name = file.getOriginalFilename();
		String path = property + File.separator + name;
		boolean generateFile = FileUtil.generateFile(path, file);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("flag", generateFile);
		hashMap.put("filePath", path);
		return hashMap;
	}
	
//	public void unzipApkPackage(final String filePath, final String version) {
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//				// 解压文件
//				File file = new File(filePath);
//				String fileName = file.getName();
//				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//				if(suffix.equals("zip")) {
//					String basePath = filePath;
//					basePath = file.getParent();
//					FileUtil.zipToFile(filePath,basePath);
//				}else if(suffix.equals("rar")) {
//					String basePath = filePath;
//					basePath = file.getParent();
//				}else if(suffix.equals("tar")) {
//					String basePath = filePath;
//					basePath = file.getParent();
//					FileUtil.tarToFile(filePath,basePath);
//				}
//			}
//		};
//		newFixedThreadPool.execute(runnable);
//	}
	
	@RequestMapping("/ajax")
	@ResponseBody
	public Map<String, Object> ajax(String data) {
		Map<String, Object> rr = new HashMap<String, Object>();
		Map<String, Object> q = ConvertDto.convertDataTableSearchForApk(data);
		Page page = ConvertDto.convertPage(data);
		List<ApkDto> dtos = new ArrayList<ApkDto>();
		List<ApkVersion> apkversions = apkVersionService.list(q, "inserted_at", page);
		for (ApkVersion apkVersion : apkversions) {
			dtos.add(ConvertDto.convertApkVersion(apkVersion));
		}
		page = apkVersionService.paging(q, page);
		buildRR(rr, dtos, page);
		return rr;
	}

	private void buildRR(Map<String, Object> rr, List<ApkDto> userDtos, Page page) {
		rr.put("data", userDtos);
		rr.put("iTotalRecords", userDtos.size());
		rr.put("iTotalDisplayRecords", page.getRecordCount());
	}

//	public List<ApkDto> findApkDtos(String filePath, List<ApkDto> useDtos) {
//		List<ApkDto> dtos = new ArrayList<ApkDto>();
//		File file = new File(filePath);
//		if (file.isDirectory()) {
//			File[] listFiles = file.listFiles();
//			for (int i = 0; i < listFiles.length; i++) {
//				File file2 = listFiles[i];
//				if (file2.isDirectory()) {
//					findApkDtos(file2.getAbsolutePath(), dtos);
//				} else {
//					String fileName = file2.getName();
//					if (fileName.equals("main.qml")) {
//						ApkDto apkDto = new ApkDto();
//						apkDto.setApkName(file2.getName());
//						String parent = file2.getParent();
//						File parentFile = file2.getParentFile();
//						if(parentFile!=null) {
//							parentFile=parentFile.getParentFile();
//						}
//						apkDto.setApkPath(parent);
//						// 获取版本号
//						String name = parentFile.getName();
//						apkDto.setVersion(name);
//						dtos.add(apkDto);
//					}
//				}
//			}
//		}
//		useDtos.addAll(dtos);
//		return useDtos;
//	}

	@RequestMapping(value = "/downLoad", method = RequestMethod.GET)
	public void downLoadPicture(String version, HttpServletResponse response, HttpServletRequest request) {
		String fileName = "";
		String property = databaseConfig.getProperty("apk.dir") + File.separator + version;
		String filePath ="";
		File file = new File(property);
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();

			for (int i = 0; i < listFiles.length; i++) {
				File file2 = listFiles[i];
				fileName = file2.getName();
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (suffix.equals("qml")) {
					filePath =  file2.getParent()+File.separator+fileName;
					response.setCharacterEncoding("utf-8");
					response.setContentType("multipart/form-data");
					response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
					// 用于记录以完成的下载的数据量，单位是byte
					try {
						// 打开本地文件流
						InputStream inputStream = new FileInputStream(filePath);
						// 激活下载操作
						OutputStream os = response.getOutputStream();

						// 循环写入输出流
						byte[] b = new byte[2048];
						int length;
						while ((length = inputStream.read(b)) > 0) {
							os.write(b, 0, length);
						}

						// 这里主要关闭。
						os.close();
						inputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
		

	}
	
	
	
	
	@RequestMapping(value = "updateApkStatus",method=RequestMethod.POST)
	@ResponseBody
	public RestResponse disableUser(@RequestBody ApkStatusDto dto) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		Integer apkId = dto.getApkId();
		Integer status = dto.getStatus();
		Integer type = dto.getType();
		
		if(status.intValue()==ApkStatus.DISABLE.getValue()) {
			
			apkVersionService.updateOtherStatus(type, ApkStatus.ENABLE.getValue());
		}else if(status.intValue()==ApkStatus.ENABLE.getValue()) {
			
			apkVersionService.updateOtherStatus(type, ApkStatus.DISABLE.getValue());
		}
		
		ApkVersion apkVersion = apkVersionService.get(apkId);
		if (apkVersion != null) {
			apkVersion.setStatus(status);
			apkVersionService.update(apkVersion);
		}
		return restResponse;
	}
	
	
	public static Map<String, Object> convertDataTableSearchForUser(String data) {
		Map<String, Object> q = new HashMap<String, Object>();
		if (null != data && 0 != data.length()) {
			List<DataDto> datas = (List<DataDto>) JsonConverter.asList(data, DataDto.class);
			String username = "";
			String nickname = "";
			String status = "";
			for (DataDto ao : datas) {
				if (ao.getName().equals("username")) {
					username = ao.getValue();
				}
				if (ao.getName().equals("status")) {
					status = ao.getValue();
				}
				if (ao.getName().equals("nickname")) {
					nickname = ao.getValue();
				}
			}
			if (!username.equals("")) {
				q.put("username", username);
			}
			if (!status.equals("")) {
				q.put("status", status);
			}
			
			if (!nickname.equals("")) {
				q.put("nickname", nickname);
			}
		}
		return q;
	}
	



}
