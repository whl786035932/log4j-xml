/**
 * FileUtil.java
 * cn.videoworks.ldapadmin.util
 * 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2014年7月30日 		meishen
 *
 * Copyright (c) 2014, TNT All Rights Reserved.
*/

package cn.videoworks.cms.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

//import com.github.junrar.Archive;
//import com.github.junrar.exception.RarException;
//import com.github.junrar.rarfile.FileHeader;

/**
 * ClassName:FileUtil
 * 
 * @author meishen
 * @version Ver 1.0.0
 * @Date 2014年7月30日 下午6:11:27
 */
public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static final int BUFFER = 1024;  
	  
	private static final String EXT = ".tar";  

	/**
	 * 把一个文件转化为字节
	 * 
	 * @param file
	 * @return byte[]
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static byte[] getByte(File file) throws Exception {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = new FileInputStream(file);
			int length = (int) file.length();
			if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
			{
				System.out.println("this file is max ");
				return null;
			}
			bytes = new byte[length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// 如果得到的字节长度和file实际的长度不一致就可能出错了
			if (offset < bytes.length) {
				System.out.println("file length is error");
				return null;
			}
			is.close();
		}
		return bytes;
	}

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public static void byteToFile(String path, byte[] b) {
		try {
			File apple = new File(path);// 把字节数组的图片写到另一个地方
			FileOutputStream fos = new FileOutputStream(apple);
			fos.write(b);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void removeFile1(String path) {
		try {
			File file = new File(path);
			String[] allFile = file.list();
			File temp = null;
			for (String f : allFile) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + f);
				} else {
					temp = new File(path + File.separator + f);
				}
				if (temp.isFile()) {
					temp.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkImage(String path) {
		boolean flag = false;
		try {
			File file = new File(path);
			if (file.exists()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据byte数组，生成文件
	 */
	public static void buildFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * buildFile:(写入到文件)
	 * 
	 * @author meishen
	 * @Date 2015 2015年4月29日 下午4:03:58
	 * @return void
	 * @throws @since
	 *             CodingExample Ver 1.0.0
	 */
	public static void buildFile(String path, byte[] byt) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static String execGenerateFile(MultipartFile multipartFile, String storagePath, String httpUrl) {
		// TODO Auto-generated method stub.
		String uuid = UUID.randomUUID().toString();
		String url = "";
		String dateStr = df.format(new Date());
		String imgPath = storagePath + dateStr + "/" + uuid + multipartFile.getOriginalFilename();
		boolean result = generateFile(imgPath, multipartFile);
		if (result) {
			url = httpUrl + storagePath + "upload/avator/" + dateStr + "/" + uuid + multipartFile.getOriginalFilename();
		} else {
			log.error("generate poster faild,imgPath is " + imgPath);
		}
		return url;
	}

	/**
	 * 
	 * 生成图片
	 *
	 * @param path
	 * @param mf
	 * @return
	 */
	public static boolean generateFile(String path, MultipartFile mf) {
		log.warn("make dirs starting,the file path is " + path);
		if (path == null) {
			return false;
		}
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			boolean reslut = file.getParentFile().mkdirs();
			if (!reslut) {
				log.error("make dirs faild,the file path is " + path);
			}

		}
		FileOutputStream out = null;
		InputStream in = null;
		try {
			file.createNewFile();
			// 将字符串转换为byte数组
			byte[] buffer = new byte[1024];
			out = new FileOutputStream(path);
			int byteread = 0;
			in = mf.getInputStream();
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread); // 文件写操作
			}
			return true;
		} catch (IOException exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	public static boolean generateFile(String path, byte[] fileByte) {
		log.warn("make dirs starting,the file path is " + path);
		if (path == null) {
			return false;
		}
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			boolean reslut = file.getParentFile().mkdirs();
			if (!reslut) {
				log.error("make dirs faild,the file path is " + path);
			}

		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(fileByte);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return false;
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return false;
				}
			}

		}
		return true;

	}

	/**
	 * 解压文件zip格式
	 * 
	 * @param zis
	 * @param basePath
	 * @throws IOException
	 */
	public static boolean unzip(ZipInputStream zis, String basePath) {
		ZipEntry entry;
		try {
			entry = zis.getNextEntry();
			if (entry != null) {
				File file = new File(basePath + File.separator + entry.getName());
				if (file.isDirectory()) {
					// 可能存在空文件夹
					if (!file.exists())
						file.mkdirs();
					unzip(zis, basePath);
				} else {
					File parentFile = file.getParentFile();
					if (parentFile != null && !parentFile.exists())
						parentFile.mkdirs();
					FileOutputStream fos = new FileOutputStream(file);// 输出流创建文件时必须保证父路径存在
					int len = 0;
					byte[] buf = new byte[1024];
					while ((len = zis.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					fos.flush();
					fos.close();
					zis.closeEntry();
					unzip(zis, basePath);
				}
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 解压zip文件
	 * 
	 * @param sourceFile,待解压的zip文件;
	 *            toFolder,解压后的存放路径
	 * @throws Exception
	 **/

	public static void zipToFile(String sourceFile, String toFolder)  {
		String toDisk = toFolder;// 接收解压后的存放路径
		ZipFile zfile;
		try {
			zfile = new ZipFile(sourceFile);
			Enumeration zList = zfile.entries();// 得到zip包里的所有元素
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					log.info("打开zip文件里的文件夹:" + ze.getName() + "skipped...");
					continue;
				}
				OutputStream outputStream = null;
				InputStream inputStream = null;
				try {
					// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
					outputStream = new BufferedOutputStream(new FileOutputStream(getRealFileName(toDisk, ze.getName())));
					inputStream = new BufferedInputStream(zfile.getInputStream(ze));
					int readLen = 0;
					while ((readLen = inputStream.read(buf, 0, 1024)) != -1) {
						outputStream.write(buf, 0, readLen);
					}
					inputStream.close();
					outputStream.close();
				} catch (Exception e) {
					log.info("解压失败：" + e.toString());
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException ex) {

						}
					}
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					inputStream = null;
					outputStream = null;
				}

			}
			zfile.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// 连接待解压文件
		
	}

	private static File getRealFileName(String zippath, String absFileName) {
		log.info("文件名：" + absFileName);
		String[] dirs = absFileName.split("/", absFileName.length());
		File ret = new File(zippath);// 创建文件对象
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				ret = new File(ret, dirs[i]);
			}
		}
		if (!ret.exists()) {// 检测文件是否存在
			ret.mkdirs();// 创建此抽象路径名指定的目录
		}
		ret = new File(ret, dirs[dirs.length - 1]);// 根据 ret 抽象路径名和 child 路径名字符串创建一个新 File 实例
		return ret;
	}

//	public static void main(String[] args) {
//		File file = new File("D:\\test\\nohup.zip");
//		String basePath = "D:\\test\\nohup11";
//		FileInputStream fis;
//		try {
//			fis = new FileInputStream(file);
//			ZipInputStream zis = new ZipInputStream(fis);
//			boolean unzip = FileUtil.unzip(zis, basePath);
//			System.out.println(unzip);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	  public static String readFileContent(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	  
	  
	  public static void tarToFile(String sourceFile, String toFolder)  {
		  File srcFile = new File(sourceFile);
		  	TarArchiveInputStream tais = null;
			try {
				tais = new TarArchiveInputStream(  
				        new FileInputStream(srcFile));
				File destFile = new File(toFolder);
				dearchive(destFile, tais);  
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					tais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 

	  }

	  
	  /**
		 * 解压tar包
		 * 
		 * @author: kpchen@iflyte.com
		 * @createTime: 2015年9月23日 下午5:41:56
		 * @history:
		 * @param filename
		 *            tar文件
		 * @param directory
		 *            解压目录
		 * @return
		 */
	  private static void dearchive(File destFile, TarArchiveInputStream tais)  
	            throws Exception {  
	  
	        TarArchiveEntry entry = null;  
	        while ((entry = tais.getNextTarEntry()) != null) {  
	  
	            // 文件  
	            String dir = destFile.getPath() + File.separator + entry.getName();  
	  
	            File dirFile = new File(dir);  
	  
	            // 文件检查  
	            fileProber(dirFile);  
	  
	            if (entry.isDirectory()) {  
	                dirFile.mkdirs();  
	            } else {  
	                dearchiveFile(dirFile, tais);  
	            }  
	  
	        }  
	    } 
	  /** 
	     * 文件解归档 
	     *  
	     * @param destFile 
	     *            目标文件 
	     * @param tais 
	     *            TarArchiveInputStream 
	     * @throws Exception 
	     */  
	    private static void dearchiveFile(File destFile, TarArchiveInputStream tais)  
	            throws Exception {  
	  
	        BufferedOutputStream bos = new BufferedOutputStream(  
	                new FileOutputStream(destFile));  
	  
	        int count;  
	        byte data[] = new byte[BUFFER];  
	        while ((count = tais.read(data, 0, BUFFER)) != -1) {  
	            bos.write(data, 0, count);  
	        }  
	  
	        bos.close();  
	    }
		
	  private static void fileProber(File dirFile) {  
		  
	        File parentFile = dirFile.getParentFile();  
	        if (!parentFile.exists()) {  
	  
	            // 递归寻找上级目录  
	            fileProber(parentFile);  
	  
	            parentFile.mkdir();  
	        }  
	  
	    }
		public static void main(String[] args) {
			File file = new File("D:\\test\\news.tar");
			String basePath = "D:\\test\\newstar";
			FileInputStream fis;
			
		}
		
		public static String  readProperties(String propertyPath,String key)  {
//			Properties pps = new Properties();
//			try {
//				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
//				pps.load(new InputStreamReader(in, "UTF-8"));
//				String property = pps.getProperty(key);
//				return property;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "";
			
			Properties prop=new Properties();         
			try {
				prop.load(new InputStreamReader(FileUtil.class.getClassLoader().getResourceAsStream(propertyPath), "UTF-8"));
				String property = prop.getProperty(key);
				return property;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return "";
		}

}
