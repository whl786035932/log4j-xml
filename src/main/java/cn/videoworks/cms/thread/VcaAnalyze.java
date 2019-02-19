package cn.videoworks.cms.thread;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.NewsdbContentDto;
import cn.videoworks.cms.service.RestTemplateService;
import cn.videoworks.cms.util.RestResponse;

public class VcaAnalyze implements Runnable {
	
	private Properties databaseConfig;
	private RestTemplateService restTemplateServiceImpl;
	private NewsdbContentDto newsdbContentDto;
	
	private static final Logger log = LoggerFactory.getLogger(VcaAnalyze.class);
	
	public VcaAnalyze() {}
	public VcaAnalyze(Properties databaseConfig,RestTemplateService restTemplateServiceImpl,NewsdbContentDto newsdbContentDto) {
		this.databaseConfig = databaseConfig;
		this.restTemplateServiceImpl  = restTemplateServiceImpl;
		this.newsdbContentDto = newsdbContentDto;
	}

	@Override
	public void run() {
		String url = databaseConfig.getProperty("newsdb.api.url");
		RestResponse rr = restTemplateServiceImpl.postForObject(newsdbContentDto,url);
		/**
		 * 修改内容、分类、媒体、海报为已同步
		 * 为了保证数据一致性，只有同步成功后方可修改对应状态
		 */
		if(rr.getStatusCode() == ResponseDictionary.SUCCESS) {
			log.info("【"+newsdbContentDto.getTitle()+"】注入新闻通成功【"+rr.getMessage()+"】");
		}else {
			log.error("【"+newsdbContentDto.getTitle()+"】注入新闻通失败【"+rr.getMessage()+"】");
		}
	}
	public RestTemplateService getRestTemplateServiceImpl() {
		return restTemplateServiceImpl;
	}
	public void setRestTemplateServiceImpl(RestTemplateService restTemplateServiceImpl) {
		this.restTemplateServiceImpl = restTemplateServiceImpl;
	}

}
