package cn.videoworks.cms.service;

import cn.videoworks.cms.util.RestResponse;

public interface RestTemplateService {

	RestResponse postForObject(Object object,String url);
}
