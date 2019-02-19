package cn.videoworks.cms.dto;

import java.util.List;

/**
 * ClassName:StationStorageDto
 * Function: TODO ADD FUNCTION
 * Reason:	 站点注入接口
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018	2018年5月31日		下午2:46:38
 *
 * @see 	 
 */
public class StationStorageDto {

	private List<Object> objects;
	private List<Object> mappings;
	public List<Object> getObjects() {
		return objects;
	}
	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}
	public List<Object> getMappings() {
		return mappings;
	}
	public void setMappings(List<Object> mappings) {
		this.mappings = mappings;
	}
	
}
