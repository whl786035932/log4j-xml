package cn.videoworks.cms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.service.RestTemplateService;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.util.json.JsonConverter;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {

	@Override
	public RestResponse postForObject(Object object, String url) {
		RestResponse rr = new RestResponse();
		if(StringUtils.isNotBlank(url)) {
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject(url, object, String.class);
			if(!result.equals(""))
				rr= JsonConverter.parse(result, RestResponse.class);
			if(rr.getStatusCode() == ResponseDictionary.SUCCESS) {
				rr.setMessage("注入新闻通成功");
				rr.setStatusCode(ResponseDictionary.SUCCESS);
			}else {
				rr.setMessage("注入新闻通失败，响应信息为【"+rr.getMessage()+"】");
				rr.setStatusCode(ResponseDictionary.EXTERNALINTERFACEREQUESTSEXCEPTION);
			}
		}else {
			rr.setMessage("注入新闻通失败,请求地址不能为空");
			rr.setStatusCode(ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION);
			return rr;
		}
		return rr;
	}

}
