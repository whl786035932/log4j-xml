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
public enum PermissionType {
	
	MENU(1,"菜单"),
	OPERATION(2,"功能操作"),
	CLASSIFICATION(3,"分类");
	private int value;
	private String name;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private PermissionType(int value, String name){
		this.value=value;
		this.name=name;
	}

}

