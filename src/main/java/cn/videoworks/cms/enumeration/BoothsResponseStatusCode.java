package cn.videoworks.cms.enumeration;

public interface BoothsResponseStatusCode {
	//请求成功
	public static final int OK = 100000;
	//服务端异常
	public static final int INTERNAL_SERVER_ERROR = 500;
	
	//连接超时
	public static final int  TIEMOUT = 100002;
	//拒绝请求
	public static final int UNAUTHORIZED = 100003;
	//请求失败
	public static final int NOT_FOUND = 100004;
	//参数验证异常
	public static final int BAD_REQUEST = 100005;
	//外部接口调用异常
	public static final int OUTER_INTERFACE_BAD_REQUEST=100006;
	//外部接口notound
	public static final int OUTER_INTERFACE_NOT_FOUND=100007;
	
	public static final int DWONLOAD_EXCEPTION = 100008;
}
