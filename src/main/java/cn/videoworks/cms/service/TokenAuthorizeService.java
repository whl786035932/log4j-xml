package cn.videoworks.cms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.videoworks.cms.util.WxMappingJackson2HttpMessageConverter;

@Service
public class TokenAuthorizeService {

	
	@Autowired
	private Properties databaseConfig;

	private static final Logger log = LoggerFactory.getLogger(TokenAuthorizeService.class);

	/**
	 * 认证接口
	 * 
	 * @param nns_tag
	 * @param nns_version
	 * @param nns_func
	 * @param nns_mac_id
	 * @param nns_device_id
	 * @param nns_output_type
	 * @return
	 */
	public Map<String, Object> token(String nns_tag, String nns_version, String nns_func, String nns_mac_id,
			String nns_device_id, String nns_output_type) {

		Integer state = null;
		String web_token = null;
		RestTemplate restTemplate = new RestTemplate();
		String qian_token_url = databaseConfig.getProperty("qian_token_url");
		qian_token_url = qian_token_url + "?nns_tag=" + nns_tag + "&nns_version=" + nns_version + "&nns_func="
				+ nns_func + "&nns_mac_id=" + nns_mac_id + "&nns_device_id=" + nns_device_id + "&nns_output_type="
				+ nns_output_type;

//		log.info("请求贵州的token_url=" + qian_token_url);
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		Map responseMap = restTemplate.getForObject(qian_token_url, Map.class);
//		log.info("请求贵州的token的返回结果是=" + JsonConverter.format(responseMap));
		if (responseMap != null) {

			Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("result");
			if (resultMap != null) {
				state = (Integer) resultMap.get("state");
			}
			Map<String, Object> authMap = (Map<String, Object>) responseMap.get("auth");
			if (authMap != null) {
				web_token = (String) authMap.get("web_token");

			}
		} else {
			log.debug("请求贵州的token获取的结果是空null---------get  webtoken   is  null---------------");
		}
//		log.info("获取到贵州的state=" + state + ";token=" + web_token);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("state", state);
		hashMap.put("web_token", web_token);
		return hashMap;
	}

	/***
	 * 获取真实id接口，贵州现场
	 * 
	 * @param nns_ids
	 * @param nns_type
	 * @param nns_video_type
	 * @param nns_id_func
	 * @param nns_url_func
	 * @param nns_mac
	 * @param nns_mac_id
	 * @param nns_version
	 * @param nns_user_id
	 * @param nns_webtoken
	 * @param nns_output_type
	 * @return
	 */

	public String authorzieId(String nns_ids, String nns_type, String nns_video_type, String nns_id_func,
			String nns_url_func, String nns_mac, String nns_mac_id, String nns_version, String nns_user_id,
			String nns_webtoken, String nns_output_type) {
		RestTemplate restTemplate = new RestTemplate();
		String video_id = null;
		String qian_getrealid_url = databaseConfig.getProperty("qian_get_realid_url");
		qian_getrealid_url = qian_getrealid_url + "?nns_ids=" + nns_ids + "&nns_type=" + nns_type + "&nns_video_type="
				+ nns_video_type + "&nns_func=" + nns_id_func + "&nns_mac=" + nns_mac + "&nns_mac_id=" + nns_mac_id
				+ "&nns_version=" + nns_version + "&nns_user_id=" + nns_user_id + "&nns_webtoken=" + nns_webtoken
				+ "&nns_output_type=" + nns_output_type;
//		log.info("获取贵州的真实的id接口的url=" + qian_getrealid_url);


		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
//		Object object = restTemplate.getForObject(qian_getrealid_url, Object.class);
//		log.info("======================================转换Object成功======================");
//		Map<String, Object> asMap = JsonConverter.asMap(JsonConverter.format(object), String.class, Object.class);
//		
//		log.info("====================================Object==转换Map成功======================");
		
		
		Map getIdMap = restTemplate.getForObject(qian_getrealid_url, Map.class);
//		log.info("获取贵州真实的id接口的返回结果是=" + JsonConverter.format(getIdMap));

		if (getIdMap != null) {

			Map<String, Object> lMap = (Map<String, Object>) getIdMap.get("l");
			List ilList = (List) lMap.get("il");
			if (ilList != null & ilList.size() > 0) {
				Map<String, Object> arg_list_map = (Map<String, Object>) ilList.get(0);
				if (arg_list_map != null) {
					Map<String, Object> arg_list = (Map<String, Object>) arg_list_map.get("arg_list");
					if (arg_list != null) {
						video_id = (String) arg_list.get("video_id");

						return video_id;
					}
				}
			}
		} else {
			log.debug("获取贵州真实的id的返回结果是空null-----------------get real video_id   is  null------------------");
		}

//		log.info("获取贵州的真实的video_id=" + video_id);
		return video_id;
	}

	/**
	 * 换串接口，贵州现场
	 * 
	 * @param video_id
	 * @param nns_video_type
	 * @param nns_url_func
	 * @param nns_version
	 * @param nns_user_id
	 * @param nns_webtoken
	 * @param nns_output_type
	 * @return
	 */
	public Map<String, Object> getUrl(String video_id, String nns_video_type, String nns_url_func, String nns_version,
			String nns_user_id, String nns_webtoken, String nns_output_type) {

		Integer state = null;
		String url = null;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();
		String huanchuan_url = databaseConfig.getProperty("qian_get_realurl_url");
		huanchuan_url = huanchuan_url + "?nns_video_id=" + video_id + "&nns_video_type=" + nns_video_type + "&nns_func="
				+ nns_url_func+"&nns_version="+nns_version+"&nns_user_id="+nns_user_id+"&nns_webtoken="+nns_webtoken+"&nns_output_type="+nns_output_type;

//		log.info("获取贵州的换串接口的url=" + huanchuan_url);
		
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		Map huanchuanMap = restTemplate.getForObject(huanchuan_url, Map.class);
//		log.info("获取贵州的换串接口的url的返回结果是=" + JsonConverter.format(huanchuanMap));
		if (huanchuanMap != null) {

			Map<String, Object> resultMap = (Map<String, Object>) huanchuanMap.get("result");
			if (resultMap != null) {
				state = Integer.parseInt((String) resultMap.get("state"));
				Map<String, Object> videoMap = (Map<String, Object>) huanchuanMap.get("video");

				if (videoMap != null) {
					Map<String, Object> indexMap = (Map<String, Object>) videoMap.get("index");
					if (indexMap != null) {
						Map<String, Object> mediaMap = (Map<String, Object>) indexMap.get("media");
						if (mediaMap != null) {
							url = (String) mediaMap.get("url");

						}else {
							log.info("mediaMap  is 空########################################");
						}
					}else {
						log.info("indexMap  is 空########################################");
						
					}

				}else {
					log.info("videoMap  is 空########################################");
					
				}
			}
		}
//		log.info("贵州的换串接口的state=" + state + ";url=" + url);
		hashMap.put("state", state);
		hashMap.put("url", url);
		return hashMap;
	}

}
