/**
 * BDUploadDto.java
 * cn.videoworks.edge.dto
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年7月20日 		meishen
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package cn.videoworks.cms.dto;
/**
 * ClassName:BDUploadDto
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2017年7月20日		下午3:46:42
 *
 */
public class BDUploadDto {
	
	private String file;
	private String objectKey;
	public BDUploadDto(){}
	public BDUploadDto(String file, String objectKey) {
		this.file = file;
		this.objectKey = objectKey;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	
	

}

