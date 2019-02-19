/**
 * CommonsUtil.java
 * cn.videoworks.despotui.util
 * 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2014年1月14日 		meishen
 *
 * Copyright (c) 2014, TNT All Rights Reserved.
*/

package cn.videoworks.cms.util;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.videoworks.commons.util.json.JsonConverter;


/**
 * ClassName:CommonsUtil
 * @author   meishen
 * @version  Ver 1.0.0    
 * @Date	 2014年1月14日		上午10:49:20 
 */
public class CommonsUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CommonsUtil.class);
	
	/**
	 * getHttpEntity:(得到entity)
	 * @author   meishen
	 * @Date	 2014年1月14日		上午10:50:58
	 * @return HttpEntity<?>    
	 * @throws
	 */
	public static HttpEntity<?> getHttpEntity(String parameter) throws Exception{
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(parameter, headers);
		return entity;
	}
	
	public static HttpEntity<?> getHttpEntity() throws Exception{
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}
	
	/**
	 * getRestTemplate:(适合DELETE发送方法体)
	 * @author   meishen
	 * @Date	 2017	2017年7月21日		下午2:19:22
	 * @return RestTemplate    
	 * @throws 
	 * @since  CodingExample　Ver 1.0.0
	 */
	public static RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory() {
	        @Override
	        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
	            if (HttpMethod.DELETE == httpMethod) {
	                return new HttpEntityEnclosingDeleteRequest(uri);
	            }
	            return super.createHttpUriRequest(httpMethod, uri);
	        }
	    });
		return restTemplate;
	}
	
	/**
	 * 获取cdn 内部key 进行智能推荐上报使用,内外键转换文档见doc
	 * @param cdnKey
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getInnerKey(String asset_id) {
		String innerKey = "";
		if(StringUtils.isNotBlank(asset_id)) {
			String url ="http://epg.interface.gzgd/nn_cms/nn_cms_view/mgtv/n39_a.php?nns_func=transformat_keys&nns_output_type=json&nns_ids="+asset_id+"&nns_index=0&nns_mode=2&nns_type=video&nns_video_type=0";
			SimpleClientHttpRequestFactory requestFactory =new SimpleClientHttpRequestFactory ();
			requestFactory.setConnectTimeout(3000);
			requestFactory.setReadTimeout(3000);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			try {
				String response = restTemplate.getForObject(new URI(url), String.class);
				log.debug("通过CDN换取智能推荐上报内部key结果【"+response+"】");
				Map<String, Map> asMap = JsonConverter.asMap(response, String.class, Map.class);
				if(null != asMap) {
					Map map = asMap.get("l");
					Object object = map.get("il");
					List<Object> asList = JsonConverter.asList(JsonConverter.format(object),Object.class);
					for(Object o : asList) {
						Map<String, Object> asMap2 = JsonConverter.asMap(JsonConverter.format(o), String.class, Object.class);
						if(String.valueOf(asMap2.get("type")) .equals("video")) {
							innerKey = String.valueOf(asMap2.get("id"));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("通过CDN换取智能推荐上报内部key失败，原因【"+e.getMessage()+"】");
			}
		}
		return innerKey;
	}
	
	/**
	 * 获取cdnkey 进行智能推荐上报使用,内外键转换文档见doc
	 * @param cdnKey
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getCdnKey(String innerKey) {
		String cdnKey = "";
		if(StringUtils.isNotBlank(innerKey)) {
			String url ="http://epg.interface.gzgd/nn_cms/nn_cms_view/mgtv/n39_a.php?nns_func=transformat_keys&nns_output_type=json&nns_ids="+innerKey+"&nns_index=0&nns_mode=1&nns_type=video&nns_video_type=0";
			SimpleClientHttpRequestFactory requestFactory =new SimpleClientHttpRequestFactory ();
			requestFactory.setConnectTimeout(3000);
			requestFactory.setReadTimeout(3000);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			try {
				String response = restTemplate.getForObject(new URI(url), String.class);
				log.debug("通过CDN换取智能推荐上报内部key结果【"+response+"】");
				Map<String, Map> asMap = JsonConverter.asMap(response, String.class, Map.class);
				if(null != asMap) {
					Map map = asMap.get("l");
					Object object = map.get("il");
					List<Object> asList = JsonConverter.asList(JsonConverter.format(object),Object.class);
					for(Object o : asList) {
						Map<String, Object> asMap2 = JsonConverter.asMap(JsonConverter.format(o), String.class, Object.class);
						if(String.valueOf(asMap2.get("type")) .equals("video")) {
							Object object2 = asMap2.get("arg_list");
							Map<String, Object> asMap3 = JsonConverter.asMap(JsonConverter.format(object2), String.class, Object.class);
							if(null != asMap3) {
								cdnKey = String.valueOf(asMap3.get("import_id"));
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("通过CDN换取智能推荐上报内部key失败，原因【"+e.getMessage()+"】");
			}
		}
		return cdnKey;
	}
	
	public static void main(String[] args) {
//		getInnerKey("CMSSERI116257");
		System.out.println(getCdnKey("5c1ddbec3d5ca98c27037a8a002f4113"));
	}
	
}

