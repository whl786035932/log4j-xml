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
import cn.videoworks.cms.dto.StorageRemoveDto;
import cn.videoworks.cms.util.ApiResponse;
import cn.videoworks.commons.util.json.JsonConverter;

public class RemoveStorageClient {

	private  GearmanClient client = null;
	
	private  Gearman gearman = null;
	
	private static RemoveStorageClient removeStorageClient = null;
	
	private static final Logger log = LoggerFactory.getLogger(StorageClient.class);
	
	private RemoveStorageClient() {}
	
	private RemoveStorageClient(String ip,int port) {
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
	
	public static synchronized RemoveStorageClient getRemoveStorageClient(String ip,int port) {
		if(null == removeStorageClient) {
			removeStorageClient = new RemoveStorageClient(ip,port);
		}
		return removeStorageClient;
	}
	
	@SuppressWarnings("incomplete-switch")
	public ApiResponse execute(List<StorageRemoveDto> res) throws Exception{
		ApiResponse resultMap = new ApiResponse();
		if(null != res) {
			String params = JsonConverter.format(res);
//			log.info("删除存储参数："+params);
			GearmanJobReturn jobReturn = client.submitJob(GearmanFunctionConstant.REMOVE_STORAGE,params.getBytes());
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
