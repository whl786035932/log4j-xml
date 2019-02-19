package cn.videoworks.cms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.CountFileDto;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

/**
 * 贵州统计的controller
 * @author whl
 *
 */
@Controller
@RequestMapping(value="/booths/statistics/v1",method=RequestMethod.POST)
public class StatisticsController {
	private static final Logger countLog = LoggerFactory.getLogger("count");

	@RequestMapping(value="countFile")
	@ResponseBody
	public RestResponse countFile(@RequestBody CountFileDto dto) {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatusCode(ResponseStatusCode.OK);
		String deviceId = dto.getDeviceId();
		String playContent = dto.getPlayContent();
		String playTime = dto.getPlayTime();
		Integer playDuration = dto.getPlayDuration();
		String category = dto.getCategory();
		String assetId = dto.getAssetId();
		countLog.info(deviceId+","+category+","+assetId+","+playContent+","+playTime+","+playDuration);
		return restResponse;
	}

}
