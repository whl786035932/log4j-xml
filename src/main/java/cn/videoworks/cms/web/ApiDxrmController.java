package cn.videoworks.cms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.ApiContentDto;
import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.enumeration.BoothsResponseStatusCode;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.util.json.JsonConverter;

@Controller
@RequestMapping(value="booths/api/v1/dxrm")
public class ApiDxrmController {
	private static final Logger log = LoggerFactory.getLogger(ApiDxrmController.class);
	@Autowired
	private Properties databaseConfig;
	@Autowired
	private ContentService contentService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	/**
	 * 内容列表------标题首字母
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "contents")
	@ResponseBody
	public RestResponse contentsByTitleAbbr(@RequestParam(value = "channel", required = false,defaultValue="") String channel,
			@RequestParam(value = "publish_time", required = false) String publish_time,@RequestParam(value="column",required=false,defaultValue="") String column,@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

		List<ApiContentDto> dtos = null;
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String redisKey = "contents_dxrm" + channel+"_"+column + "_" + limit + "_" + publish_time;
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
			List<Content> contents = contentService.findByChannleColumnPublishTime(channel,column, limit, publish_time);
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


}
