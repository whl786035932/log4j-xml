package cn.videoworks.cms.client;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobPriority;
import org.gearman.GearmanJoin;
import org.gearman.GearmanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.videoworks.cms.constant.ResponseDictionary;
import cn.videoworks.cms.service.CdnCallbackService;
import cn.videoworks.cms.service.ContentPosterMappingService;
import cn.videoworks.cms.service.ContentService;
import cn.videoworks.cms.service.MediaService;
import cn.videoworks.cms.service.PosterService;
import cn.videoworks.cms.service.TaskService;
import cn.videoworks.cms.util.ApiResponse;

/**
 * Hello world!
 */
public class CDNGearmanClient {
	
	private static final Logger log = LoggerFactory
			.getLogger(CDNGearmanClient.class);
	public CDNGearmanClient() {
		super();
	}
	private  Gearman gearman = null;
	private  GearmanClient client = null;
	private static CDNGearmanClient cdnGearmnClient = null;
	private String functionName;
	private String host;
	private Integer port;
	private ContentService contentService;

	private PosterService posterService;

	private ContentPosterMappingService contentPosterMappingService;

	private MediaService mediaService;

	private TaskService taskService;
	
	private Properties databaseConfig;

	public CDNGearmanClient(String host, Integer port, String functionName) {
		this.host = host;
		this.port = port;
		this.functionName = functionName;
	}
	public CDNGearmanClient(String host, Integer port, String functionName,ContentService contentService, PosterService posterService,ContentPosterMappingService contentPosterMappingService, MediaService mediaService,TaskService taskService ,Properties databaseConfig) {
		this.host = host;
		this.port = port;
		this.functionName = functionName;
		this.contentService=contentService;
		this.posterService=posterService;
		this.contentPosterMappingService=contentPosterMappingService;
		this.mediaService=mediaService;
		this.taskService=taskService;
		this.databaseConfig=databaseConfig;
		
		gearman = Gearman.createGearman();
		 
        client = gearman.createGearmanClient();
 
        GearmanServer server = gearman.createGearmanServer(host, port);
        
        client.addServer(server);		
		
	}
	public static synchronized CDNGearmanClient getCDNGearmanClient(String ip,int port,String functionName,ContentService contentService, PosterService posterService,ContentPosterMappingService contentPosterMappingService, MediaService mediaService,TaskService taskService ,Properties databaseConfig) {
		if(null == cdnGearmnClient) {
			cdnGearmnClient = new CDNGearmanClient(ip,port,functionName,contentService,posterService,contentPosterMappingService,mediaService,taskService,databaseConfig);
		}
		return cdnGearmnClient;
	}
	public ApiResponse submitJob(String data) {
		ApiResponse resultMap = new ApiResponse();
		try {
			GearmanJoin<String> submitJob = client.submitJob(functionName, data.getBytes("UTF-8"), GearmanJobPriority.NORMAL_PRIORITY, null, new CdnCallbackService(contentService,posterService,contentPosterMappingService,mediaService,taskService,databaseConfig));
			System.out.println(submitJob);

			
			//异步任务提交成功过
			resultMap.setStatusCode(ResponseDictionary.SUCCESS);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return resultMap;
	}

}
