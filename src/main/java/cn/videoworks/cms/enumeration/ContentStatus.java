/**
 * ContentStatus.java
 * cn.videoworks.cms.enumeration
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年5月29日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.enumeration;
/**
 * ClassName:ContentStatus
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年5月29日		下午1:46:04
 *
 * @see 	 
 */
public enum ContentStatus {

	DELETE(0,"删除"),
	UNSHELVE(1,"未上架"),
	SHELVED(2,"已上架"),
	ILLEGAL(3,"数据不合法"),
	DELETESTORAGE(4,"已删除存储"),
	OTHER(-1,"全部不包括3");
	
	private ContentStatus(int value, String name){
		this.value=value;
		this.name=name;
	}
	
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
}

