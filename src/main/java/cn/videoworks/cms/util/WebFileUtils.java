package cn.videoworks.cms.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class WebFileUtils {
	private static final Logger log = LoggerFactory
			.getLogger(WebFileUtils.class);

	/**
	 * 根据url拿取file
	 * 
	 * @param url
	 * @param suffix
	 *            文件后缀名
	 * */
	public static File createFileByUrl(String url, String suffix) {
		log.debug("微信头像地址:" + url + ";头像格式:" + suffix);
		byte[] byteFile = getImageFromNetByUrl(url);
		if (byteFile != null) {
			File file = getFileFromBytes(byteFile, suffix);
			return file;
		} else {
			log.info("生成文件失败！");
			return null;
		}
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	private static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			log.debug("微信头像地址创建远程连接成功");
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			log.debug("头像字节码长度:" + btImg.length);
			return btImg;
		} catch (Exception e) {
			log.debug("微信头像地址创建远程连接失败");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	// 创建临时文件
	private static File getFileFromBytes(byte[] b, String suffix) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = File.createTempFile("pattern", "." + suffix);
			log.debug("临时文件位置:" + file.getCanonicalPath());
			System.out.println("临时文件位置：" + file.getCanonicalPath());
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static MultipartFile createImg(String url) throws Exception {
		try {
			// File转换成MutipartFile
			File file = createFileByUrl(url, "png");
			if (file == null) {
				return null;
			}
			FileInputStream inputStream = new FileInputStream(file);
			byte[] data = readInputStream(inputStream);
			log.debug("数据流长度:" + data.length);
			// MultipartFile multipartFile = new
			// MockMultipartFile(file.getName(), inputStream);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(),
					file.getName(), "png", data);
			// 注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则
			// MultipartFile.getName()只能拿到name参数，但是originalFilename是空。
			return multipartFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		// WebFileUtils
		// .createImg("http://wx.qlogo.cn/mmopen/UAqwJ95HSLycmQktIqAYuexoytJ3kJzknQ4icJkNpfUvxfqoNRDY2esKQj3YvxXuQacsu9fYKDQ1VUSVBxspic4MwNDTF4Z4zu/0",11);
	}
}
