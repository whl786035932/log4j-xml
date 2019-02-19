/**
 * PosterController.java
 * cn.videoworks.cms.web
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月28日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.videoworks.cms.client.StorageClient;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.LogDto;
import cn.videoworks.cms.dto.PPDto;
import cn.videoworks.cms.dto.Page;
import cn.videoworks.cms.dto.StorageRequestDto;
import cn.videoworks.cms.dto.StorageResponseDto;
import cn.videoworks.cms.entity.Poster;
import cn.videoworks.cms.entity.Source;
import cn.videoworks.cms.enumeration.BusinessType;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.enumeration.ContentType;
import cn.videoworks.cms.enumeration.LogLevel;
import cn.videoworks.cms.enumeration.LogSource;
import cn.videoworks.cms.enumeration.LogType;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.SourceService;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.Md5Util;
import cn.videoworks.cms.util.UserUtil;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * ClassName:PosterController
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月28日		下午2:25:54
 *
 * @see 	 
 */
@Controller
@RequestMapping("posters")
public class PosterController {

	@Resource
	private PosterService posterServiceImpl;
	
	@Resource
	private SourceService sourceServiceImpl;
	
	@Resource
	private Properties databaseConfig;
	
	private static final Logger log = LoggerFactory.getLogger(PosterController.class);
	
	@RequestMapping(value="",method = RequestMethod.GET)
	public String list(ModelMap map) {
		List<Source> sources = sourceServiceImpl.list("insertedAt");
		map.put("sources", sources);
		return "site.cms.poster.index";
	}
	
	@RequestMapping(value="ajax",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> ajax(Integer index,String source) {
		Page page =new Page();
		if(null != index)
			page.setOffSet(index*10);
		page.setSize(10);
		Map<String,String> q = new HashMap<String,String>();
		q.put("source", source.trim());
		List<Poster> poster  = posterServiceImpl.list(q,"insertedAt", page);
		List<PPDto> posters  =null;
		if(poster!=null){
			for(Poster p:poster ){
				PPDto pp= new PPDto (p);
				if(posters==null){
					posters = new ArrayList<>();
				}
				posters.add(pp);
			}
		}
		Page pp = posterServiceImpl.paging(q,page);
		pp.setIndex(index+1);
		Map<String,Object> result =new HashMap<String,Object>();
		result.put("page", pp);
		result.put("posters", posters);
		return ConvertDto.buildRestResponse(ResponseStatusCode.OK, "成功", result);
	}
	
	@RequestMapping(value="/photo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatePhoto(@RequestParam("poster_file") MultipartFile file,
			@RequestParam("description") String description,HttpServletRequest request) {
		//记录添加站点-操作日志
		LogDto logDto = ConvertDto.buildOperationLog("CMS-上传海报", BusinessType.ADD.getValue(), "海报管理", UserUtil.getUserUtil(request).getUsername(), LogType.OPERATIONLOG.getValue(), LogSource.CMS.getValue(), LogLevel.INFO.getValue(), new HashMap<String,Object>());
		log.info(JsonConverter.format(logDto));
		
		Map<String,Object> map = null;
		try {
			if(file.getSize() >0){
				String checkSum = Md5Util.getMD5(file.getBytes());
				Map<String, String> param = uploadFile(file);
				if(param == null||!param.get("statusCode").toString().equals("200")){
					map = new HashMap<String,Object>();
					map.put("statusCode",ResponseDictionary.ERROR);
					if(param.containsKey("message")){
						map.put("message",param.get("message"));
					}else{
						map.put("message","图片上传临时服务器失败!");
					}
					log.info("图片上传临时服务器失败!");
					return map;
				}
				// 存库
				Poster poster = bulidPoster(param);
				// 图片加密
				poster.setCheckSum(checkSum);
				// 图片大小
				poster.setSize(new Long(file.getSize()).intValue());
				Source source = sourceServiceImpl.get(cn.videoworks.cms.enumeration.Source.LOCAL.getValue());
				if(null == source) {
					source = ConvertDto.convertSource(cn.videoworks.cms.enumeration.Source.LOCAL.getName(),cn.videoworks.cms.enumeration.Source.LOCAL.getValue());
					sourceServiceImpl.save(source);
				}
				poster.setSource(source.getName());//文件源
				poster.setDescription(description);// 描述
				// 数据存库
				posterServiceImpl.save(poster);
				ApiResponse storeResult = storageImg(poster);
				if(storeResult==null){
					map = new HashMap<String,Object>();
					map.put("statusCode",ResponseDictionary.ERROR);
					map.put("message","图片上传临时服务器失败!");
					log.info("图片存储失败!");
				}
				log.info("存储work响应数据："+JsonConverter.format(storeResult));
				map = checkStoreResult(storeResult,poster);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 上传到临时服务器.
	 */
	private Map<String, String> uploadFile(MultipartFile file) {
		Map<String, String> result = null;
		if (file != null && file.getSize() > 0) {
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String logImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
			/** 拼成完整的文件保存路径加文件* */
			String dir = databaseConfig.getProperty("poster.dir");
			File fdir = new File(dir);
			if (!fdir.exists() && !fdir.isDirectory()) {
				// 文件夹不存在,创建文件夹
				fdir.mkdir();
			}
	        String fileName = dir + File.separator + logImageName;
	        File files = new File(fileName);
	        try {
	        	// 获取图片大小
				BufferedImage bi = ImageIO.read(file.getInputStream());
				if(bi==null){
					result = new HashMap<>();
		        	result.put("statusCode", "500");
		        	result.put("message", "非图片文件");
		        	return result;
				}
				int width = bi.getWidth();
				int hight = bi.getHeight();
				file.transferTo(files);
				result = new HashMap<>();
				result.put("statusCode", "200");
				result.put("fileName", fileName);
				result.put("fileWidth", String.valueOf(width));
				result.put("fileHight", String.valueOf(hight));
				result.put("fileRealName", file.getOriginalFilename());
	        } catch (Exception e) {
	        	result = new HashMap<>();
	        	result.put("statusCode", "500");
	            e.printStackTrace();
	        }
	        log.info("传到服务器的文件的绝对路径:"+fileName);
		}
		return result;
	}

	/**
	 * 生成poster 实体.
	 * 
	 * @param param
	 * @return
	 */
	private Poster bulidPoster(Map<String, String> param) {
		Poster poster = new Poster();
		poster.setFileName(param.get("fileRealName"));// 图片名称
		poster.setHeight(Integer.valueOf(param.get("fileHight")));// 图片高度
		poster.setInsertedAt(new Date());// 插入时间
		poster.setStatus(1);// 状态
		poster.setUpdatedAt(new Date());// 更新时间
//		poster.setUrl(param.get("fileName"));// 图片路径
		poster.setSourceUrl(param.get("fileName"));// 图片路径
		poster.setWidth(Integer.valueOf(param.get("fileWidth")));// 图片宽度
		poster.setCdnSyncStatus(CdnStatus.UNSYNCHRONIZED.getValue());
		return poster;
	}
	/**
	 * 图片正式存储.
	 */
	private ApiResponse storageImg(Poster poster) {
		ApiResponse response = null;
		List<StorageRequestDto> res = new ArrayList<StorageRequestDto>();
		StorageRequestDto dto = ConvertDto.convertStorageRequestDto(poster);
		res.add(dto);
		try {
			response = StorageClient.getStorageClient(
					databaseConfig.getProperty("gearman.ip"),
					Integer.valueOf(databaseConfig.getProperty("gearman.port")))
					.execute(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 存储返回结果.
	 */
	private Map<String, Object> checkStoreResult(ApiResponse result,
			Poster poster) {
		// 数据正常返回
		try {
			deleteFile(poster); // 删除临时服务器文件
			if (result.getStatusCode() != ResponseDictionary.SUCCESS) {
				// 清除内容、海报、媒体关系
				posterServiceImpl.delete(poster.getId());
				return ConvertDto.buildRestResponse(
						ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION,
						result.getMessage(), null);
			} else {// 处理成功
				ApiResponse rest = JsonConverter.parse(result.getData().toString(), ApiResponse.class);
				List<StorageResponseDto> storageResponses = JsonConverter
						.asList(JsonConverter.format(rest.getData()),
								StorageResponseDto.class);
				for (StorageResponseDto dto : storageResponses) {
					int type = dto.getType();
					if (type == ContentType.POSTER.getValue()) {
						Poster p = posterServiceImpl.get(dto.getId());
						p.setSourceUrl(dto.getUrl());
						p.setUpdatedAt(DateUtil.getNowTimeStamp());
						posterServiceImpl.update(p);
					} else if (type == ContentType.AD.getValue()) {

					} else {
						log.error("存储work响应未知类型，无法进行数据处理！");
					}
				}
				return ConvertDto.buildRestResponse(ResponseDictionary.SUCCESS,
						result.getMessage(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			posterServiceImpl.delete(poster.getId());// 清除内容、海报、媒体关系
			deleteFile(poster); // 删除临时服务器文件
			log.error("解析存储 work响应数据失败：" + e.getMessage());
		}
		return null;
	}
	/**
	 * 删除临时服务器上文件.
	 */
	private void deleteFile(Poster poster){
		try {
			if(poster!=null){
//				File file = new File(poster.getUrl());
				File file = new File(poster.getSourceUrl());
				if(file.exists()){
					file.delete();
				}
				log.info("删除临时服务器文件成功");
			}
		} catch (Exception e) {
			log.info("删除临时服务器文件失败");
			e.printStackTrace();
		}
	}
}

