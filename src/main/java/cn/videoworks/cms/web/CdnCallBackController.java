package cn.videoworks.cms.web;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.entity.Content;
import cn.videoworks.cms.entity.Media;
import cn.videoworks.cms.entity.Task;
import cn.videoworks.cms.enumeration.CdnStatus;
import cn.videoworks.cms.service.ClassificationContentMappingService;
import cn.videoworks.cms.service.ClassificationService;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.RestTemplateService;
import cn.videoworks.cms.service.SourceService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.util.CommonsUtil;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.util.json.JsonConverter;

@Controller
@RequestMapping(value="/booths/adi/v1")
public class CdnCallBackController {
	@Resource
	private ContentService contentServiceImpl;
	
	@Resource
	private PosterService posterServiceImpl;
	
	@Resource
	private MediaService mediaServiceImpl;
	
	@Resource
	private ContentPosterMappingService contentPosterMappingServiceImpl;
	
	@Resource
	private SourceService sourceServiceImpl;
	
	@Resource
	private ClassificationService classificationServiceImpl;
	
	@Resource
	private ClassificationContentMappingService classificationContentMappingServiceImpl;
	
	@Resource
	private PosterService posterServcieImpl;
	
	@Resource
	private RestTemplateService restTemplateServiceImpl;
	
	@Resource
	private TaskService taskServiceImpl;
	
	@Resource
	private Properties databaseConfig;
	
	private static final Logger log = LoggerFactory.getLogger(CdnCallBackController.class);
	
	/**
	 * cdn正式回调
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "cdnCallBack", method = RequestMethod.POST)
	@ResponseBody
	public String cdnCallBack(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String contentType = request.getContentType();
		log.info("cdnCallBack回调结果的Content-Type=" + contentType);
		
		Enumeration parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String value = (String) parameterNames.nextElement();// 调用nextElement方法获得元素
			log.info("cdnCallBack回调的ParameterNames=" + value);
		}

		Map parameterMap = request.getParameterMap();
		log.info("cdnCallBack的parameterMap====" + JsonConverter.format(parameterMap));

		request.setCharacterEncoding("utf-8");

		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");

		// 转码，防止乱码
		String cmsresult = request.getParameter("cmsresult");
		// cmsresult = new String(cmsresult.getBytes("iso-8859-1"), "utf-8");
		log.info("cdn的回调结果=" + cmsresult);

		// 开始解析xml
		Map<String, Object> map = parsexml(cmsresult);

		if (map == null) {
			log.debug("回调：处理CDN发来的XML异常");
			return "<result Result=\"-1\" ErrorDescription=\"处理XML异常\"/>";
		}

		log.info("解析回调参数为map【" + map.toString() + "】");

		try {
			Integer xmlType = (Integer) map.get("xmlType")==null?-1:(Integer) map.get("xmlType");
			String msgId = (String) map.get("msgid");

			if (xmlType == 1) { // 处理工单的下载结果
				String CmsResult = (String) map.get("CmsResult");
				String ResultMsg = (String) map.get("ResultMsg");
				if ("0".equals(CmsResult)) {// 工单成功
					log.info("工单与海报下载成功，工单msgid=" + msgId + ";处理结果：" + ResultMsg);
				} else if ("-1".equals(CmsResult)) {
					log.debug("工单与海报下载失败,工单msgid=" + msgId + ";处理结果：" + ResultMsg);
					// 设置task的为失败----------------TO---DO
					Task task = taskServiceImpl.getByMsgId(msgId);
					updateTaskStatus(task, CdnStatus.SYNCHRONIZEDFAILED.getValue(),"失败原因:工单与海报下载失败msgid="+msgId);
				}
				// 是否需要发送xml response-----------------------------TO DO------------------
				return "<result Result=\"0\" ErrorDescription=\"消息任务处理异步反馈接收成功\"/>";
			} else if (xmlType == 2) { // 文件下载和切片失败 and 最终cdn的注入结果
				String state = (String) map.get("state");
				String msg = (String) map.get("msg");
				String asset_id = (String) map.get("asset_id");
				String part_id = (String) map.get("part_id");
				if ("1".equals(state)) { // 成功
					log.debug("文件下载与切片成功，工单msgid=" + msgId + ";原因=" + msg);
					// 修改内容的cdn注入状态为成功过
					if (asset_id != null) {
						Task task = taskServiceImpl.getByMsgId(msgId);
						if(null != task && null != task.getContent() && task.getContent().getMedias() != null && task.getContent().getMedias().size() >0) {
							updateContentCDNStatus(task.getContent().getId(), CdnStatus.SYNCHRONIZED.getValue());
							// 修改媒体的cdn注入状态为成功
							updateMediaCDNStatus(task.getContent().getMedias().get(0).getId(), CdnStatus.SYNCHRONIZED.getValue(), part_id,asset_id);
							// 修改任务的cdn注入状态为成功------------TO DO
							updateTaskStatus(task, CdnStatus.SYNCHRONIZED.getValue(),null);
						}
					}
				} else {// cdn注入失败
					log.debug("文件下载与切片失败，工单msgid=" + msgId + ";原因=" + msg);
					Task task = taskServiceImpl.getByMsgId(msgId);
					// 修改内容的cdn注入状态为成功过
					if (null != task && null != task.getContent() && task.getContent().getMedias() != null && task.getContent().getMedias().size() >0) {
						updateContentCDNStatus(task.getContent().getId(), CdnStatus.SYNCHRONIZEDFAILED.getValue());
						
						// 修改媒体的cdn注入状态为成功
						updateMediaCDNStatus(task.getContent().getMedias().get(0).getId(), CdnStatus.SYNCHRONIZEDFAILED.getValue(), part_id,null);

						// 设置task的任务为失败-------------------TO DO
						updateTaskStatus(task, CdnStatus.SYNCHRONIZEDFAILED.getValue(),"失败原因:"+msg);

					} else {
						log.info("Adicontroller的asset_id获取有问题asset_id=" + asset_id);
					}
				}
				log.info("cdnCallBack的xmlType=" + xmlType + "state=" + state + ";asset_id=" + asset_id + ";part_id="
						+ part_id);
				return "<result Result=\"0\" ErrorDescription=\"注入CDN成功异步反馈接收成功\"/>";
			} else {
				log.debug("回调：未知XML类型");
				return "<result Result=\"-1\" ErrorDescription=\"未知XML类型\"/>";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "<result Result=\"-1\" ErrorDescription=\"服务器处理XML异常\"/>";
		}
	}
	
	/***
	 * cdn的回调地址
	 * 
	 * @param contentId
	 * @param taskId
	 * @param mediaId
	 * @param posterId
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "cdnCallBack", method = RequestMethod.POST)
//	@ResponseBody
//	public String cdnCallBack(HttpServletRequest request, HttpServletResponse response)
//			throws UnsupportedEncodingException {
//		String contentType = request.getContentType();
//		log.info("cdnCallBack回调结果的Content-Type=" + contentType);
//		
//		Enumeration parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//			String value = (String) parameterNames.nextElement();// 调用nextElement方法获得元素
//			log.info("cdnCallBack回调的ParameterNames=" + value);
//		}
//
//		Map parameterMap = request.getParameterMap();
//		log.info("cdnCallBack的parameterMap====" + JsonConverter.format(parameterMap));
//
//		request.setCharacterEncoding("utf-8");
//
//		response.setContentType("text/xml");
//		response.setCharacterEncoding("utf-8");
//
//		// 转码，防止乱码
//		String cmsresult = request.getParameter("cmsresult");
//		// cmsresult = new String(cmsresult.getBytes("iso-8859-1"), "utf-8");
//		log.info("cdn的回调结果=" + cmsresult);
//
//		// 开始解析xml
//		Map<String, Object> map = parsexml(cmsresult);
//
//		if (map == null) {
//			log.debug("回调：处理CDN发来的XML异常");
//
//			return "<result Result=\"-1\" ErrorDescription=\"处理XML异常\"/>";
//		}
//
//		log.info("解析回调参数为map【" + map.toString() + "】");
//
//		try {
//			Integer xmlType = (Integer) map.get("xmlType")==null?-1:(Integer) map.get("xmlType");
//			String msgId = (String) map.get("msgid");
//
//			if (xmlType == 1) { // 处理工单的下载结果
//
//				String CmsResult = (String) map.get("CmsResult");
//				String ResultMsg = (String) map.get("ResultMsg");
//				if ("0".equals(CmsResult)) {// 工单成功
//					log.info("工单与海报下载成功，工单msgid=" + msgId + ";处理结果：" + ResultMsg);
//				} else if ("-1".equals(CmsResult)) {
//					log.debug("工单与海报下载失败,工单msgid=" + msgId + ";处理结果：" + ResultMsg);
//
//					// 设置task的为失败----------------TO---DO
//					updateTaskStatus(msgId, CdnStatus.SYNCHRONIZEDFAILED.getValue(),"失败原因:工单与海报下载失败msgid="+msgId);
//
//				}
//				// 是否需要发送xml response-----------------------------TO DO------------------
//				return "<result Result=\"0\" ErrorDescription=\"消息任务处理异步反馈接收成功\"/>";
//
//			} else if (xmlType == 2) { // 文件下载和切片失败 and 最终cdn的注入结果
//				String state = (String) map.get("state");
//				String msg = (String) map.get("msg");
////				String asset_type = (String) map.get("asset_type");
//				String asset_id = (String) map.get("asset_id");
//				String part_id = (String) map.get("part_id");
//				String file_id = (String) map.get("file_id");
//				if ("1".equals(state)) { // 成功
//					log.debug("文件下载与切片成功，工单msgid=" + msgId + ";原因=" + msg);
//					// 修改内容的cdn注入状态为成功过
//					if (asset_id != null) {
//
//						String contentIdStr = asset_id.substring(7);
//						updateContentCDNStatus(contentIdStr, CdnStatus.SYNCHRONIZED.getValue());
//
//						// 修改媒体的cdn注入状态为成功
//						updateMediaCDNStatus(file_id, CdnStatus.SYNCHRONIZED.getValue(), part_id);
//					}
//
//					// 修改任务的cdn注入状态为成功------------TO DO
//					updateTaskStatus(msgId, CdnStatus.SYNCHRONIZED.getValue(),null);
//
//				} else {// cdn注入失败
//					log.debug("文件下载与切片失败，工单msgid=" + msgId + ";原因=" + msg);
//
//					// 修改内容的cdn注入状态为成功过
//					if (asset_id != null && !"".equals(file_id) && file_id.length() >= 7) {
//						asset_id = asset_id.substring(7);
//						updateContentCDNStatus(asset_id, CdnStatus.SYNCHRONIZEDFAILED.getValue());
//
//					} else {
//						log.info("Adicontroller的asset_id获取有问题asset_id=" + asset_id);
//					}
//					// 修改媒体的cdn注入状态为成功
//					updateMediaCDNStatus(file_id, CdnStatus.SYNCHRONIZEDFAILED.getValue(), part_id);
//
//					// 设置task的任务为失败-------------------TO DO
//					updateTaskStatus(msgId, CdnStatus.SYNCHRONIZEDFAILED.getValue(),"失败原因:"+msg);
//
//				}
//				log.info("cdnCallBack的xmlType=" + xmlType + "state=" + state + ";asset_id=" + asset_id + ";part_id="
//						+ part_id);
//
//				return "<result Result=\"0\" ErrorDescription=\"注入CDN成功异步反馈接收成功\"/>";
//
//			} else {
//				log.debug("回调：未知XML类型");
//				return "<result Result=\"-1\" ErrorDescription=\"未知XML类型\"/>";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			return "<result Result=\"-1\" ErrorDescription=\"服务器处理XML异常\"/>";
//		}
//
//	}

	/**
	 * 解析cdn的返回结果
	 * 
	 * @param document
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public Map<String, Object> parsexml(String cmsResult) {

		Map<String, Object> map = new HashMap<String, Object>();
		Document document = null;
		if (!StringUtils.isNotBlank(cmsResult)) {
			return null;
		}
		try {
			document = DocumentHelper.parseText(cmsResult);
		} catch (DocumentException e) {
			e.printStackTrace();
			log.debug("xml 解析失败");
			return null;
		}
		Element element = document.getRootElement();

		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			if (employee.getName().equals("msgid")) {
				map.put("msgid", employee.getText());
			}
			if (employee.getName().equals("CmsResult")) {
				map.put("CmsResult", employee.getText());
				map.put("xmlType", 1); // 工单与海报下载cdn的回调
			}
			if (employee.getName().equals("ResultMsg")) {
				map.put("ResultMsg", employee.getText());
			}
			if (employee.getName().equals("state")) {
				map.put("state", employee.getText());
				String state = employee.getText();
				if(!"1".equals(state)) {
					//代表失败
					map.put("xmlType", 2); 
				}
				
				
			}
			if (employee.getName().equals("msg")) {
				map.put("msg", employee.getText());
			}
			if (employee.getName().equals("info")) {
				for (Iterator i_info = employee.elementIterator(); i_info.hasNext();) {
					Element element2 = (Element) i_info.next();
					if (element2.getName().equals("asset_type")) {
						String asset_type = element2.getText();
						map.put("asset_type",asset_type );
						if("3".equals(asset_type)) {
//							asset _type>1  表示主媒资注入成功
//							asset _type>2  表示分集注入成功
//							asset _type>3  表示片源注入成功 
//							视达科-康辉 2018/8/24 16:28:16
//							要片源注入成功了整个流程才算完成

							map.put("xmlType", 2); // cdn最终回调结果||文件下载和切片
						}
						
					}
					if (element2.getName().equals("asset_id")) {
						map.put("asset_id", element2.getText());
					}
					if (element2.getName().equals("part_id")) {
						map.put("part_id", element2.getText());
					}
					if (element2.getName().equals("file_id")) {
						map.put("file_id", element2.getText());
					}
				}
			}
			
			
		}

		return map;
	}
	
	/***
	 * 根据提交cdn任务的处理结果获取工单号，并修改task的工单号
	 * 
	 * @param taskId
	 * @param msgId
	 */
//	public void udpateTaskWithMsgId(Long taskId, String msgId) {
//		Task task = taskServiceImpl.get(taskId);
//		if (task != null) {
//			task.setMsgid(msgId);
//			taskServiceImpl.update(task);
//		}
//	}
	
	/**
	 * 修改内容cdn状态
	 * @param contentId
	 * @param cdnStatus
	 */
	public void updateContentCDNStatus(Long contentId, int cdnStatus) {
		Content content = contentServiceImpl.get(contentId);
		if (content != null) {
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			content.setCdn_sync_status(cdnStatus);
			content.setUpdatedAt(nowTimeStamp);
			contentServiceImpl.update(content);
		}
	}

	/**
	 * 修改内容的cdn状态
	 * 
	 * @param asset_id
	 * @param cdnStatus
	 */
//	public void updateContentCDNStatus(String asset_id, int cdnStatus) {
//		Long contentId = Long.parseLong(asset_id);
//		Content content = contentServiceImpl.get(contentId);
//		if (content != null) {
//			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
//			content.setCdn_sync_status(cdnStatus);
//			content.setUpdatedAt(nowTimeStamp);
//			contentServiceImpl.update(content);
//		}
//	}
	
	/**
	 * 修改媒体的cdn状态和cdnKey、InnerKey
	 * @param file_id
	 * @param cdnStatus
	 * @param asset_id
	 */
	public void updateMediaCDNStatus(Long mediaId, int cdnStatus, String part_id, String asset_id) {
		Media media = mediaServiceImpl.get(mediaId);
		if (media != null) {
			media.setCdn_sync_status(cdnStatus);
			media.setCdnKey(part_id);
			
			if(null != asset_id)
				media.setInnerKey(CommonsUtil.getInnerKey(asset_id));
			
			mediaServiceImpl.update(media);
		}else {
			log.debug("媒体【"+mediaId+"】不存在");
		}
	}

	/**
	 * 修改媒体的cdn状态
	 * 
	 * @param file_id
	 * @param cdnStatus
	 */
//	public void updateMediaCDNStatus(String file_id, int cdnStatus, String asset_id) {
//		if (file_id != null && !"".equals(file_id) && file_id.length() >= 7) {
//			String mediaIdStr = file_id.substring(7);
//			Long mediaId = Long.parseLong(mediaIdStr);
//			Media media = mediaServiceImpl.get(mediaId);
//			if (media != null) {
//
//				media.setCdn_sync_status(cdnStatus);
//				media.setCdnKey(asset_id);
//				media.setInnerKey(CommonsUtil.getInnerKey(asset_id));
//				mediaServiceImpl.update(media);
//			}
//
//		} else {
//			log.info("Adicontroller的file_id获取有问题file_id=" + file_id);
//		}
//	}
	
	/**
	 * 修改任务状态
	 * @param task
	 * @param cdnStatus
	 * @param reason
	 */
	public void updateTaskStatus(Task task, int cdnStatus,String reason) {
		if (task != null) {
			task.setStatus(cdnStatus);
			if(reason!=null) {
				task.setMessage(reason);
			}
			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
			task.setUpdated_at(nowTimeStamp);
			taskServiceImpl.update(task);
		}
	}

	/**
	 * 根据内容的ID,海报的id,获取任务，修改task的cdn状态
	 */
//	public void updateTaskStatus(String msgId, int cdnStatus,String reason) {
//		Task task = taskServiceImpl.getByMsgId(msgId);
//		if (task != null) {
//			task.setStatus(cdnStatus);
//			if(reason!=null) {
//				task.setMessage(reason);
//			}
//			Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
//			task.setUpdated_at(nowTimeStamp);
//			taskServiceImpl.update(task);
//		}
//	}

}
