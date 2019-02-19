/**
 * Status.java
 * cn.com.cri.dolphin.d
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年11月28日 		meishen
 *
 * Copyright (c) 2017, VideoWorks All Rights Reserved.
*/

package cn.videoworks.cms.enumeration;
/**
 * ClassName:Status
 *
 * @author   meishen
 * @version  Ver 1.0.0 
 * @Date	 2017年11月28日 上午10:31:02
 *
 */
public enum CdnReturnType {
	
	POSTER(2,"海报任务"),
	
	MEDIA(1,"媒体任务"),
	;
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private Integer value;
	private String name;
	
	private CdnReturnType(int value, String name){
		this.value=value;
		this.name=name;
	}

}

