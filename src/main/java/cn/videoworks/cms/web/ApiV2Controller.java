package cn.videoworks.cms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import cn.videoworks.cms.dto.ApiClassificationDto;
import cn.videoworks.cms.dto.ApiContentDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.entity.ApkVersion;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.enumeration.ApkStatus;
import cn.videoworks.cms.enumeration.ApkType;
import cn.videoworks.cms.enumeration.BoothsResponseStatusCode;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.service.ApkVersionService;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.util.CommonsUtil;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.FileUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * @ClassName:ApiController.java
 * @Description:给盒子端提供接口的Api
 * @author whl
 * @param <E>
 * @param <V>
 * @date 2017年11月28日 上午9:45:32
 * 
 */
@Controller
@RequestMapping(value = "/booths/api/v2")
public class ApiV2Controller<E, V> {
	private static final Logger log = LoggerFactory.getLogger(ApiV2Controller.class);

	@Autowired
	private ClassificationService classificationService;
	@Autowired
	private ContentService contentService;

	@Autowired
	private ClassificationContentMappingService classificationContentService;

	@Autowired
	private Properties databaseConfig;

//	@Autowired
//	private TokenAuthorizeService tokenAuthorizeService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ApkVersionService apkVersionService;
	
	@Resource
	private MediaService mediaServiceImpl;
	/**
	 * 
	 * 内容列表---根据栏目
	 * 
	 * @param classification_id
	 *            分类的id
	 * @return
	 */
	@RequestMapping(value = "classifications/{classification_id}/contents")
	@ResponseBody
	public RestResponse contentsByClassification(@PathVariable(value = "classification_id") String classification_id,
			@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		List<ApiContentDto> dtos = null;

		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "classificationsByClass_v2_" + classification_id + "_" + limit + "_" + page;
		String value = null;
		boolean redisError = false;
		try {
			value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Redis连接错误【" + e.getMessage() + "】");
			redisError = true;
		}

		Page page1 = null;
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			dtos = JsonConverter.asList(value, ApiContentDto.class);

			String string = opsForValue.get("page_v2");
			page1 = JsonConverter.parse(string, Page.class);
		} else {

			List<ClassificationContentMapping> lists = classificationContentService
					.listContentByClassIdOrderByPublishTime(classification_id, limit, page);

			dtos = new ArrayList<ApiContentDto>();
			for (ClassificationContentMapping mapping : lists) {
				Content content = mapping.getContent();
				ApiContentDto dto = ConvertDto.convertContentToContentDto(content);
				dto.setSequence(mapping.getSequence());
				dto.setRecommend(mapping.getRecommend());
				dtos.add(dto);
			}
			page1 = new Page();
			page1 = classificationContentService.paging(classification_id, page1);

			String property = databaseConfig.getProperty("redis.expire");
			Integer expire = Integer.valueOf(property);

			if (!redisError) {
				opsForValue.set(redisKey, JsonConverter.format(dtos), expire, TimeUnit.SECONDS);
				opsForValue.set("page_v2", JsonConverter.format(page1), expire, TimeUnit.SECONDS);
			}

		}
		RestResponse restResponse = new RestResponse();
		restResponse.setMessage("执行成功");
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.getData().put("contents", dtos);
		restResponse.getData().put("total", page1.getRecordCount());
		return restResponse;
	}

	/**
	 * 内容列表------标题首字母
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "contents")
	@ResponseBody
	public RestResponse contentsByTitleAbbr(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
			@RequestParam(value = "publish_time", required = false) String publish_time) {
		log.info("根据标题首字母搜索内容keyword=" + keyword);

		List<ApiContentDto> dtos = null;
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "contents_" + keyword + "_" + limit + "_" + publish_time;
		String value = null;

		boolean redisError = false;
		Integer total = 0;
		try {
			value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Redis连接错误【" + e.getMessage() + "】");
			redisError = true;
		}
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			dtos = JsonConverter.asList(value, ApiContentDto.class);
			String string = opsForValue.get("contents_search_total");
			total = JsonConverter.parse(string, Integer.class);

		} else {
			List<Content> contents = contentService.findByTitleAbbr(keyword, limit, publish_time);
			dtos = new ArrayList<ApiContentDto>();
			for (Content content : contents) {
				ApiContentDto dto = ConvertDto.convertContentToContentDto(content);
				dtos.add(dto);
			}
			total = contentService.count(keyword);
			if (!redisError) {
				String property = databaseConfig.getProperty("redis.expire");
				Integer expire = Integer.valueOf(property);
				opsForValue.set(redisKey, JsonConverter.format(dtos), expire, TimeUnit.SECONDS);
				opsForValue.set("contents_search_total", JsonConverter.format(total), expire, TimeUnit.SECONDS);
			}
		}

		RestResponse restResponse = new RestResponse();
		restResponse.setMessage("执行成功");
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.getData().put("contents", dtos);
		restResponse.getData().put("total", total);
		return restResponse;
	}

	/**
	 * 分类列表的子级
	 * 
	 * @param classification_id
	 *            分类的name
	 */
	@RequestMapping(value = "classifications/childClassificationByNameSimple")
	@ResponseBody
	public RestResponse childClassificationsByNameNotContainContents(
			@RequestParam(value = "classification_name") String classification_name,
			@RequestParam(value = "limit", defaultValue = "20") Integer limit,
			@RequestParam(value = "areaName", required = false) String areaName,
			@RequestParam(value = "areaValue", required = false) String areaValue) {
		log.info("获取分类列表的子级classification_id=" + classification_name);

		List<ApiClassificationDto> buildFullTree = null;
		Classification classificationParent = classificationService.getByNameApi(classification_name);
		ArrayList<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
		if (classificationParent != null) {

			List<Classification> childs = classificationService.listChild(classificationParent.getId(),
					Status.VALID.getValue());
			for (Classification classification : childs) {
				String name = classification.getName();
				if (areaName != null && areaValue != null) {
					if (areaName.trim().equals(name)) {// 如果该栏目是本地就转成areaValue对应的栏目
						Classification classification2Local = classificationService.getByNameApi(areaValue.trim());
						if (classification2Local != null) {
							classification.setId(classification2Local.getId());
							classification.setName(areaValue.trim());
						}
					}
				}
				if (areaValue != null) {

					if (!areaValue.trim().equals(name)) {
						ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification);
						dtos.add(dto);
					}
				} else {
					ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification);
					dtos.add(dto);
				}
			}
		}
		// buildFullTree = ConvertDto.buildFullTreeListApiNoRoot(dtos);
		buildFullTree = ConvertDto.buildClassification(dtos);
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.setMessage("执行成功");
		restResponse.getData().put("classifications", buildFullTree);
		return restResponse;
	}

	@RequestMapping(value = "/downLoad", method = RequestMethod.GET)
	public void downLoadPicture(String version, HttpServletResponse response, HttpServletRequest request) {
		String fileName = "";
		String property = databaseConfig.getProperty("apk.dir") + File.separator + version;
		String filePath = "";
		File file = new File(property);
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();

			for (int i = 0; i < listFiles.length; i++) {
				File file2 = listFiles[i];
				fileName = file2.getName();
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (suffix.equals("qml")) {
					filePath = file2.getParent() + File.separator + fileName;
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

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getApk", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> getApk(HttpServletResponse response, HttpServletRequest request) {
		String property = databaseConfig.getProperty("apk.dir")+File.separator+"version.txt";
		File file = new File(property);
		String version =FileUtil.readFileContent(file).trim();
		String filePath = databaseConfig.getProperty("apk.dir") + File.separator + version;
		HashMap<String, String> hashMap = new HashMap<>();
		Map apkMap = apkPath(filePath,hashMap);
		String apkPath = (String) apkMap.get("apkPath"); //D:\test\1\news\main.qml
//		String contextPath = request.getContextPath();
//		String serverName = request.getServerName();
//		int serverPort = request.getServerPort();
//		String scheme = request.getScheme();
		
		String apkDir = databaseConfig.getProperty("apk.dir");
		String apkVirtualPath = databaseConfig.getProperty("apk.virtualPath");
//		int indexOf = apkPath.lastIndexOf(apkDir);
		apkPath = apkPath.replace(apkDir, "");
		apkPath= apkPath.replace(File.separator, "/");
//		String returnPath = scheme+"://"+serverName+":"+serverPort+apkVirtualPath+apkPath;
		String returnPath = apkVirtualPath+apkPath;
		HashMap<String, String> hashMap2 = new HashMap<String,String>();
		hashMap2.put("apk", returnPath);
		return hashMap2;

	}
	
	/**
	 * apk:(apk调用升级文件)
	 * @author   meishen
	 * @Date	 2018	2018年11月9日		下午5:56:09 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value = "/apk/{type}", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse apk(@PathVariable(value="type")String type,HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
		Integer typeValue=-1;
		if(ApkType.RELEASE.getName().equals(type)) {
			typeValue = ApkType.RELEASE.getValue();
		}else if(ApkType.TEST.getName().equals(type)) {
			typeValue = ApkType.TEST.getValue();
		}
		RestResponse restResponse = new RestResponse();
		ApkVersion apkVersion =apkVersionService.get(typeValue,ApkStatus.ENABLE.getValue());
		HashMap<String, Object> hashMap = new HashMap<>();
		if(apkVersion!=null) {
			String name = FileUtil.readProperties("database.properties", "apk.name");
			hashMap.put("name", name);
			String author = FileUtil.readProperties("database.properties", "apk.author");
			hashMap.put("author", author);
			String mainVersion = apkVersion.getMainVersion();
			Integer childVersion = apkVersion.getChildVersion();
			hashMap.put("majorVersion",mainVersion);
			hashMap.put("minorVersion",String.valueOf(childVersion));
			hashMap.put("md5sum", apkVersion.getMd5());
			hashMap.put("description", apkVersion.getDescription());
			Timestamp inserted_at = apkVersion.getInserted_at();
			String timeStampConvertStr = DateUtil.timeStampConvertStr(inserted_at);
			hashMap.put("date", timeStampConvertStr);
			String archvie = apkVersion.getArchvie();
			String entry = apkVersion.getEntry();
			String apkVirtualPath = databaseConfig.getProperty("apk.virtualPath");
			archvie=apkVirtualPath+"/"+type+"/"+mainVersion+"."+childVersion+"/"+archvie;
			entry= apkVersion.getEntry();
			hashMap.put("archive",archvie);
			hashMap.put("entry","/"+entry);
			hashMap.put("size",apkVersion.getSize());
			hashMap.put("force",apkVersion.getIsForce());
			restResponse.setStatusCode(ResponseStatusCode.OK);
			restResponse.getData().put("apk", hashMap);
		}else {
			restResponse.setStatusCode(ResponseStatusCode.NOT_FOUND);
			restResponse.getData().put("apk", hashMap);
		}
		return restResponse;
	}
	
	/**
	 * randomContent:(随机获取分类下内容)
	 * @author   meishen
	 * @Date	 2018	2018年11月12日		下午2:49:14 
	 * @return RestResponse    
	 * @throws
	 */
	@RequestMapping(value = "/classification/{classification_id}/random/contents/{limit}", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse randomContent(@PathVariable(value="classification_id")String classification_id,@PathVariable(value="limit") Integer limit) {
		RestResponse restResponse = new RestResponse();
		List<ClassificationContentMapping> lists = classificationContentService.getContents(classification_id, limit);
		List<ApiContentDto> dtos = new ArrayList<ApiContentDto>();
		for (ClassificationContentMapping mapping : lists) {
			Content content = mapping.getContent();
			ApiContentDto dto = ConvertDto.convertContentToContentDto(content);
			dto.setSequence(mapping.getSequence());
			dto.setRecommend(mapping.getRecommend());
			dtos.add(dto);
		}
		restResponse.setMessage("执行成功");
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.getData().put("contents", dtos);
		return restResponse;
	}
	
	/**
	 * 通过贵州广电智能推荐系统获取推荐数据
	 * @param scene_id
	 * @param u_id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="recommend",method=RequestMethod.GET)
	@ResponseBody
	public RestResponse  recommend(@RequestParam(value="scene_id") String scene_id, @RequestParam(value="u_id") String u_id)  {
		RestResponse restResponse = new RestResponse();
		String customer = databaseConfig.getProperty("customer");
		if ("qian".equals(customer)) {
			String url = databaseConfig.getProperty("recommend_url");
			url=url+"?u_id="+u_id+"&scene_id="+scene_id+"&number="+databaseConfig.getProperty("recommend.number");
			SimpleClientHttpRequestFactory requestFactory =new SimpleClientHttpRequestFactory ();
			int timeout = Integer.valueOf(databaseConfig.getProperty("recommend.timeout"));
			requestFactory.setConnectTimeout(timeout);
			requestFactory.setReadTimeout(timeout);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			try {
				Map response = restTemplate.getForObject(url,Map.class);
				log.debug("获取智能推荐结果【"+JsonConverter.format(response)+"】");
				if(response!=null) {
					Object code_obj = response.get("code");
					if(code_obj!=null) {
						Integer code = Integer.valueOf(String.valueOf(code_obj));
						if(code!=null) {
							if(code.intValue()!=0) {//失败
								restResponse.setStatusCode(BoothsResponseStatusCode.INTERNAL_SERVER_ERROR);
								restResponse.setMessage(String.valueOf(response.get("msg")));
							}else {
								Object data_obj = response.get("data");
								if(data_obj!=null) {
									String data_str = JsonConverter.format(data_obj);
									List<String> innerIds = JsonConverter.asList(data_str, String.class);
									List<ApiContentDto> contents = getContentByMediaInnerKey(innerIds);
									restResponse.getData().put("contents", contents);
									restResponse.setStatusCode(BoothsResponseStatusCode.OK);
									restResponse.setMessage("请求成功");
								}
							}
						}else {
							restResponse.setStatusCode(BoothsResponseStatusCode.OUTER_INTERFACE_BAD_REQUEST);
							restResponse.setMessage("请求接口的返回结果有误");
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				restResponse.setStatusCode(BoothsResponseStatusCode.OUTER_INTERFACE_BAD_REQUEST);
				restResponse.setMessage("请求失败"+e.getMessage());
				log.error("智能推荐接口调用异常，原因【"+e.getMessage()+"】");
			}
		}else {//兼容推荐功能
			restResponse.getData().put("contents", new ArrayList());
			restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		}
		return restResponse;
	}
	
	/**
	 * 通过内外键获取推荐结果
	 * @param innerIds
	 * @return
	 */
	private List<ApiContentDto> getContentByMediaInnerKey(List<String> innerIds){
		ArrayList<ApiContentDto> dtos = new ArrayList<ApiContentDto>();
		for (String innerId : innerIds) {
			Media media = mediaServiceImpl.getByInnerKey(innerId);
			if(media!=null) {
				if(media.getContent() != null) {
					ApiContentDto dto = ConvertDto.convertContentToContentDto(media.getContent());
					dtos.add(dto);
				}
			}else {
				//如果为空，则通过内外键转换结构获取--适用老数据的情况
				String cdnKey = CommonsUtil.getCdnKey(innerId);
				Media byCdnKeyMedia = mediaServiceImpl.getByCdnKey(cdnKey);
				if(null != byCdnKeyMedia && null != byCdnKeyMedia.getContent()) {
					ApiContentDto dto = ConvertDto.convertContentToContentDto(byCdnKeyMedia.getContent());
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}
	
	private Map<String ,String> apkPath(String filePath,HashMap<String, String> hashMap) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				File file2 = listFiles[i];
				if (file2.isDirectory()) {
					apkPath(file2.getAbsolutePath(),hashMap);
				} else {
					String fileName = file2.getName();
					if (fileName.equals("main.qml")) {
						
						String parent = file2.getParent();
//						File parentFile = file2.getParentFile();
						String returnPath = parent + File.separator + fileName;
						hashMap.put("apkPath", returnPath);
					}
				}
			}
		}
		return hashMap;
	}

}
