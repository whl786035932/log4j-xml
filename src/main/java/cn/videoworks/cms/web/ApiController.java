package cn.videoworks.cms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.ApiClassificationDto;
import cn.videoworks.cms.dto.ApiContentDto;
import cn.videoworks.cms.dto.ClassificationDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.entity.Classification;
import cn.videoworks.cms.entity.ClassificationContentMapping;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.enumeration.BoothsResponseStatusCode;
import cn.videoworks.cms.enumeration.Status;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.TokenAuthorizeService;
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
@RequestMapping(value = "/booths/api/v1")
public class ApiController<E, V> {
	private static final Logger log = LoggerFactory.getLogger(ApiController.class);
	

	@Autowired
	private ClassificationService classificationService;
	@Autowired
	private ContentService contentService;

	@Autowired
	private ClassificationContentMappingService classificationContentService;

	@Autowired
	private Properties databaseConfig;

	@Autowired
	private TokenAuthorizeService tokenAuthorizeService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	
	

	/**
	 * 分类列表
	 * 
	 * @author whl
	 * @return
	 */
	@RequestMapping(value = "/classifications")
	@ResponseBody
	public RestResponse classfications() {

		List<Classification> lists = classificationService.list(Status.VALID.getValue());
		ArrayList<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
		for (Classification classification : lists) {
			ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification);
			dtos.add(dto);
		}
		List<ApiClassificationDto> buildFullTree = ConvertDto.buildFullTreeListApiWithRoot(dtos);

		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.setMessage("执行成功");
		restResponse.getData().put("classifications", buildFullTree);
		return restResponse;

	}

	/**
	 * 分类列表的子级
	 * 
	 * @param classification_id
	 *            分类的id
	 */
	@RequestMapping(value = "classifications/{classification_id}")
	@ResponseBody
	public RestResponse childClassifications(@PathVariable(value = "classification_id") String classification_id) {
		log.info("获取分类列表的子级classification_id=" + classification_id);

		List<ApiClassificationDto> buildFullTree = null;
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "classifications_" + classification_id;
		String value = null;
		boolean redisError = false;
		try {
			value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			log.error("Redis连接错误【" + e.getMessage() + "】");
			redisError = true;
		}
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			List<ApiClassificationDto> asList = JsonConverter.asList(value, ApiClassificationDto.class);
			buildFullTree = asList;
		} else {
			Classification classificationParent = classificationService.get(classification_id);
			if (classificationParent != null) {

				List<Classification> childs = classificationService.listChild(classification_id, Status.VALID.getValue());
				ArrayList<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
				for (Classification classification : childs) {
					ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification);
					dtos.add(dto);
				}
				buildFullTree = ConvertDto.buildClassification(dtos);
				if (!redisError) {
					String property = databaseConfig.getProperty("redis.expire");
					Integer expire = Integer.valueOf(property);
					opsForValue.set(redisKey, JsonConverter.format(buildFullTree),expire,TimeUnit.SECONDS);
				}
			}
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.setMessage("执行成功");
		restResponse.getData().put("classifications", buildFullTree);
		return restResponse;
	}

	/**
	 * 分类列表的子级
	 * 
	 * @param classification_id
	 *            分类的name
	 */
	@RequestMapping(value = "classifications/childClassificationByName")
	@ResponseBody
	public RestResponse childClassificationsByName(
			@RequestParam(value = "classification_name") String classification_name,
			@RequestParam(value = "limit", defaultValue = "20") Integer limit,
			@RequestParam(value = "areaName", required = false) String areaName,
			@RequestParam(value = "areaValue", required = false) String areaValue) {
		List<ApiClassificationDto> buildFullTree = null;

		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "";
		if (areaName != null && areaValue != null) {
			redisKey = "childClassificationByName_v1_" + classification_name + "_" + areaName + "_" + areaValue + "_"
					+ limit;
		} else {
			redisKey = "childClassificationByName_v1_" + classification_name + "_" + limit;
		}

		String value = null;

		boolean redisError = false;
		try {
			 value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Redis连接错误【"+e.getMessage()+"】");
			redisError = true;
		}
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			List<ApiClassificationDto> asList = JsonConverter.asList(value, ApiClassificationDto.class);
			buildFullTree = asList;
		} else {

			Classification classificationParent = classificationService.getByNameApi(classification_name);
			ArrayList<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
			if (classificationParent != null) {

				List<Classification> childs = classificationService.listChild(classificationParent.getId(), Status.VALID.getValue());
				for (Classification classification : childs) {
					String name = classification.getName();
					if (areaName != null) {
						if (areaName.trim().equals(name)) {// 如果该栏目是本地就转成areaValue对应的栏目
							if(areaValue!=null) {
								
								Classification classification2Local = classificationService.getByNameApi(areaValue.trim());
								if (classification2Local != null) {
									classification.setId(classification2Local.getId());
									classification.setName(areaValue.trim());
								}
							}
						}
					}
					List<ClassificationContentMapping> mappings = classificationContentService
							.listContentByClassId(classification.getId(), limit, null);
					ArrayList<ApiContentDto> contents = new ArrayList<ApiContentDto>();
					for (ClassificationContentMapping mapping : mappings) {
						Content content = mapping.getContent();
						ApiContentDto contentDto = ConvertDto.convertContentToContentDto(content);
						contentDto.setSequence(mapping.getSequence());
						contentDto.setRecommend(mapping.getRecommend());
						contents.add(contentDto);
					}
					List<Classification> classificationF = classificationService.getClassificationF(classification.getId(),classificationParent.getId());
					ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification,classificationF,areaValue);
					dto.setContents(contents);
					dtos.add(dto);
					
					List<Classification> inner_childs = classificationService.listChild(classification.getId(), Status.VALID.getValue());
					if(inner_childs!=null&inner_childs.size()>0) {
						List<ApiClassificationDto> diguiClassification = diguiClassification(inner_childs,limit,classificationParent.getId(),areaValue);
						dtos.addAll(diguiClassification);
					}
					
				}
				buildFullTree = ConvertDto.buildFullTreeListApiNoRoot(dtos,classificationParent);
				if(!redisError) {
					String property = databaseConfig.getProperty("redis.expire");
					Integer expire = Integer.valueOf(property);
					opsForValue.set(redisKey, JsonConverter.format(buildFullTree),expire,TimeUnit.SECONDS);
				}

			}
		}

		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.setMessage("执行成功");
		restResponse.getData().put("classifications", buildFullTree);
		return restResponse;
	}
	
	
	public List<ApiClassificationDto> diguiClassification(	List<Classification> childs,Integer limit,String parentId,String areaValue){
		ArrayList<ApiClassificationDto> dtos = new ArrayList<ApiClassificationDto>();
		for (Classification classification : childs) {
			List<ClassificationContentMapping> mappings = classificationContentService
					.listContentByClassId(classification.getId(), limit, null);
			ArrayList<ApiContentDto> contents = new ArrayList<ApiContentDto>();
			for (ClassificationContentMapping mapping : mappings) {
				Content content = mapping.getContent();
				ApiContentDto contentDto = ConvertDto.convertContentToContentDto(content);
				contentDto.setSequence(mapping.getSequence());
				contentDto.setRecommend(mapping.getRecommend());
				contents.add(contentDto);
			}
			List<Classification> classificationF = classificationService.getClassificationF(classification.getId(),parentId);
			ApiClassificationDto dto = ConvertDto.convertClassificationDtoSingle(classification,classificationF,areaValue);
			dto.setContents(contents);
			dtos.add(dto);
			
			List<Classification> inner_childs = classificationService.listChild(classification.getId(), Status.VALID.getValue());
			if(inner_childs!=null&inner_childs.size()>0) {
				List<ApiClassificationDto> diguiClassification = diguiClassification(inner_childs,limit,parentId,areaValue);
				dtos.addAll(diguiClassification);
			}
			
		}
		return dtos;
	}
	
	
	
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
			@RequestParam(value = "sequence", required = false) Integer sequence) {

		List<ApiContentDto> dtos = null;
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "classificationsGetByClass_" + classification_id + "_contents_" + limit + "_" + sequence;
		String value =null;
		boolean redisError = false;
		try {
			 value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Redis连接错误【"+e.getMessage()+"】");
			redisError = true;
		}
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			dtos = JsonConverter.asList(value,ApiContentDto.class);

		} else {
			List<ClassificationContentMapping> lists = classificationContentService
					.listContentByClassId(classification_id, limit, sequence);

			dtos = new ArrayList<ApiContentDto>();
			for (ClassificationContentMapping mapping : lists) {
				Content content = mapping.getContent();
				ApiContentDto dto = ConvertDto.convertContentToContentDto(content);
				dto.setSequence(mapping.getSequence());
				dto.setRecommend(mapping.getRecommend());
				dtos.add(dto);
			}
			if(!redisError) {
				String property = databaseConfig.getProperty("redis.expire");
				Integer expire = Integer.valueOf(property);
				opsForValue.set(redisKey, JsonConverter.format(dtos), expire, TimeUnit.SECONDS);
			}
		}

		RestResponse restResponse = new RestResponse();
		restResponse.setMessage("执行成功");
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.getData().put("contents", dtos);
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
		String value =null;
		
		boolean redisError = false;
		try {
			 value = opsForValue.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Redis连接错误【"+e.getMessage()+"】");
			redisError = true;
		}
		if (value != null && StringUtils.isNotBlank(value)) {
			// 直接返回数据
			dtos = JsonConverter.asList(value, ApiContentDto.class);

		} else {
			List<Content> contents = contentService.findByTitleAbbr(keyword, limit, publish_time);
			dtos = new ArrayList<ApiContentDto>();
			for (Content content : contents) {
				ApiContentDto dto = ConvertDto.convertContentToContentDto(content);
				dtos.add(dto);
			}
			if(!redisError) {
				String property = databaseConfig.getProperty("redis.expire");
				Integer expire = Integer.valueOf(property);
				opsForValue.set(redisKey, JsonConverter.format(dtos), expire, TimeUnit.SECONDS);
			}
		}

		RestResponse restResponse = new RestResponse();
		restResponse.setMessage("执行成功");
		restResponse.setStatusCode(BoothsResponseStatusCode.OK);
		restResponse.getData().put("contents", dtos);
		return restResponse;
	}


	/**
	 * 认证接口
	 */
	@RequestMapping(value = "token")
	@ResponseBody
	public Map<String, Object> boothsToken(@RequestParam(value = "nns_tag") String nns_tag,
			@RequestParam(value = "nns_version") String nns_version, @RequestParam(value = "nns_func") String nns_func,
			@RequestParam(value = "nns_mac_id") String nns_mac_id,
			@RequestParam(value = "nns_device_id") String nns_device_id,
			@RequestParam(value = "nns_output_type") String nns_output_type) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		// log.info("认证接口的参数有nns_tag=" + nns_tag + ";nns_version=" + nns_version +
		// ";nns_func=" + nns_func + ";nns_mac_id="
		// + nns_mac_id + ";nns_device_id=" + nns_device_id + ";nns_output_type=" +
		// nns_output_type);
		String customer = databaseConfig.getProperty("customer");
		if ("wangbo".equals(customer)) {
			hashMap.put("state", 300000);
			hashMap.put("web_token", "wangbo_token");
			return hashMap;

		} else if ("qian".equals(customer)) {
			Map<String, Object> responseMap = tokenAuthorizeService.token(nns_tag, nns_version, nns_func, nns_mac_id,
					nns_device_id, nns_output_type);
			return responseMap;
		}

		return null;
	}

	/**
	 * 换串接口
	 */
	@RequestMapping(value = "authorize")
	@ResponseBody
	public Map<String, Object> boothsAuthorize(@RequestParam(value = "nns_ids") String nns_ids,
			@RequestParam(value = "nns_type") String nns_type,
			@RequestParam(value = "nns_video_type") String nns_video_type,
			@RequestParam(value = "nns_id_func") String nns_id_func,
			@RequestParam(value = "nns_url_func") String nns_url_func, @RequestParam(value = "nns_mac") String nns_mac,
			@RequestParam(value = "nns_mac_id") String nns_mac_id,
			@RequestParam(value = "nns_version") String nns_version,
			@RequestParam(value = "nns_user_id") String nns_user_id,
			@RequestParam(value = "nns_webtoken") String nns_webtoken,
			@RequestParam(value = "nns_output_type") String nns_output_type) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String customer = databaseConfig.getProperty("customer");
		// log.info("customer==============" + customer);
		// log.info("换串接口的参数nns_ids=" + nns_ids + ";nns_type=" + nns_type +
		// ";nns_video_type=" + nns_video_type
		// + ";nns_id_func=" + nns_id_func + ";nns_url_func=" + nns_url_func +
		// ";nns_mac=" + nns_mac
		// + ";nns_mac_id=" + nns_mac_id + ";nns_version=" + nns_version +
		// ";nns_user_id=" + nns_user_id
		// + ";nns_webtoken=" + nns_webtoken + ";nns_output_type=" + nns_output_type);
		if ("wangbo".equals(customer)) {
			String url = "";
			Content content = contentService.get(Long.parseLong(nns_ids));
			List<Media> medias = content.getMedias();
			if (medias != null && medias.size() > 0) {
				Media media = medias.get(0);
				url = media.getUrlKey();
			}
			hashMap.put("state", 0);
			hashMap.put("url", url);
			return hashMap;

		} else if ("qian".equals(customer)) {
			String video_id = tokenAuthorizeService.authorzieId(nns_ids, nns_type, nns_video_type, nns_id_func,
					nns_url_func, nns_mac, nns_mac_id, nns_version, nns_user_id, nns_webtoken, nns_output_type);
			if (video_id != null) {
				Map<String, Object> urlMap = tokenAuthorizeService.getUrl(video_id, nns_video_type, nns_url_func,
						nns_version, nns_user_id, nns_webtoken, nns_output_type);
				return urlMap;

			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String jsonStr = "[{\"id\":\"1a6e1070-a11a-4087-a5b1-9f720cdd8548\",\"name\":\"测试1\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":null,\"children\":[]},{\"id\":\"6225e81c-7b34-48f9-bae7-5b7612266046\",\"name\":\"推荐\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":null,\"children\":[{\"id\":\"e4813f7f-8705-41e7-a4e8-da9c01f1a78e\",\"name\":\"电视剧\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":\"6225e81c-7b34-48f9-bae7-5b7612266046\",\"children\":[]}]},{\"id\":\"2059d638-4d65-45b5-980a-bd6edabdf0fb\",\"name\":\"电影\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":null,\"children\":[{\"id\":\"26787182-4919-44a1-9a6c-d0f842753b59\",\"name\":\"周润发\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":\"2059d638-4d65-45b5-980a-bd6edabdf0fb\",\"children\":[]}]},{\"id\":\"cffdb489-18c2-479a-a971-69b385299c5d\",\"name\":\"综艺\",\"icon\":null,\"status\":1,\"type\":null,\"pId\":null,\"children\":[]}]";
		List<ClassificationDto> parse = JsonConverter.parse(jsonStr, List.class);
		for (ClassificationDto classificationDto : parse) {
			System.out.println(classificationDto);
		}
	}

	/**
	 * 推荐排序
	 */

	@SuppressWarnings("unused")
	private List<ClassificationContentMapping> recommendSort(List<ClassificationContentMapping> list) {
		List<ClassificationContentMapping> result = null;
		List<ClassificationContentMapping> append = null;
		ClassificationContentMapping ccm1 = null;
		ClassificationContentMapping ccm2 = null;
		ClassificationContentMapping ccm3 = null;
		if (list != null && list.size() > 0) {
			for (ClassificationContentMapping ccm : list) {
				if (append == null) {
					append = new ArrayList<>();
				}

				switch (ccm.getRecommend()) {
				case 0:
					append.add(ccm);
					break;
				case 1:
					ccm1 = ccm;
					break;
				case 2:
					ccm2 = ccm;
					break;
				case 3:
					ccm3 = ccm;
					break;
				default:
					append.add(ccm);
					break;
				}
			}
		}
		if (result == null) {
			result = new ArrayList<>();
		}
		if (ccm1 != null) {
			result.add(ccm1);
		}
		if (ccm2 != null) {
			result.add(ccm2);
		}
		if (ccm3 != null) {
			result.add(ccm3);
		}
		if (append != null) {
			result.addAll(append);
		}
		return result;
	}

	@RequestMapping(value = "ping")
	@ResponseBody
	public RestResponse ping() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		return restResponse;
	}
	

	

}
