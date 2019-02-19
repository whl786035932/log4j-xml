package cn.videoworks.cms.client;

import java.util.List;

import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobReturn;
import org.gearman.GearmanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.videoworks.cms.constant.GearmanFunctionConstant;
import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.dto.StorageRequestDto;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.commons.util.json.JsonConverter;

/**
 * ClassName:StorageClient
 * Function: TODO ADD FUNCTION
 * Reason:	 对象存储Gearman 客户端
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018	2018年6月5日		上午11:04:52
 *
 * @see 	 
 */
public class StorageClient {
	
	private  GearmanClient client = null;
	
	private  Gearman gearman = null;
	
	private static StorageClient storageClient = null;
	
	private static final Logger log = LoggerFactory.getLogger(StorageClient.class);
	
	private StorageClient() {}
	
	private StorageClient(String ip,int port) {
		try {
			gearman = Gearman.createGearman();
				 
	        client = gearman.createGearmanClient();
	 
	        GearmanServer server = gearman.createGearmanServer(ip, port);
	        
	        client.addServer(server);
		} catch (Exception e) {
			e.printStackTrace();
			 gearman.shutdown();
		}
	}
	
	public static synchronized StorageClient getStorageClient(String ip,int port) {
		if(null == storageClient) {
			storageClient = new StorageClient(ip,port);
		}
		return storageClient;
	}
	
	@SuppressWarnings("incomplete-switch")
	public ApiResponse execute(List<StorageRequestDto> res) throws Exception{
		ApiResponse resultMap = new ApiResponse();
		if(null != res) {
			String params = JsonConverter.format(res);
//			log.info("注入存储参数："+params);
			GearmanJobReturn jobReturn = client.submitJob(GearmanFunctionConstant.WRITE_STORAGE,params.getBytes());
	        while (!jobReturn.isEOF()) {
	            GearmanJobEvent event = jobReturn.poll();
	            switch (event.getEventType()) {
		            case GEARMAN_JOB_SUCCESS: 
		            	String result = new String(event.getData(),"utf-8");
		            	ApiResponse rest = JsonConverter.parse(result, ApiResponse.class);
		        		if(rest.getStatusCode() == ResponseDictionary.SUCCESS) {
		        			resultMap.setStatusCode(ResponseDictionary.SUCCESS);
		        			resultMap.setData(result);
		        		}else {
		        			resultMap.setStatusCode(ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION);
		        			resultMap.setMessage("注入存储失败，存储响应失败原因【"+rest.getMessage()+"】");
		        		}
		                break;
		            case GEARMAN_SUBMIT_FAIL: 
		            	String message = "注入存储失败！原因【"+event.getEventType()+":"+new String(event.getData())+"】";
		            	log.error(message);
		            	resultMap.setMessage(message);
		            	resultMap.setStatusCode(ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION);
		            	break;
		            case GEARMAN_JOB_FAIL: 
		            	String message1 ="注入存储失败！原因【"+event.getEventType()+":"+new String(event.getData())+"】";
		            	log.error(message1);
		            	resultMap.setStatusCode(ResponseDictionary.EXTERNALINTERFACECALLSEXCEPTION);
		            	break;
	            }
	        }
		}else {
			resultMap.setStatusCode(ResponseDictionary.ERROR);
		}
		return resultMap;
	}
	
}
