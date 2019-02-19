package cn.videoworks.cms.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.dto.ConvertDto;
import cn.videoworks.cms.dto.OperationDto;
import cn.videoworks.cms.dto.PermissionDto;
import cn.videoworks.cms.entity.Operation;
import cn.videoworks.cms.entity.Permission;
import cn.videoworks.cms.entity.PermissionOperatoinMapping;
import cn.videoworks.cms.enumeration.PermissionType;
import cn.videoworks.cms.service.OperationService;
import cn.videoworks.cms.service.PermissionOperationService;
import cn.videoworks.cms.service.PermissionService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.commons.util.json.JsonConverter;
import cn.videoworks.commons.webdev.constant.ResponseStatusCode;

@Controller
@RequestMapping(value="operation")
public class OperationController {

	@Resource
	private OperationService operationService;
	
	@Resource
	private PermissionService permissionService;
	
	@Resource
	private PermissionOperationService permissionOperationService;
	
	
	
	
	@RequestMapping(value="index")
	public String index(ModelMap modelMap) {
		List<Operation> all = operationService.list("sequence");
		List<OperationDto> dtos = convertOperationTodtos(all);
		OperationDto operationTree = getOperationTree(dtos);
		modelMap.put("treeJson", JsonConverter.format( operationTree));
		
		List<Permission> permissions =  permissionService.list();
		ArrayList<PermissionDto> permissionDtos = new ArrayList<>();
		for (Permission permission : permissions) {
			PermissionDto convertPermission = ConvertDto.convertPermission(permission);
			permissionDtos.add(convertPermission);
		}
		
		modelMap.put("permissons", permissionDtos);
		
		return "site.cms.operation.index";
	}
	
	
	
	/**
	 * 获取栏目树.
	 */
	public OperationDto getOperationTree(List<OperationDto> all) {
		// 获取分类根节点
				if (all != null && all.size() > 1) {
					for (int i = 1; i < all.size(); i++) {
						for (int j = i - 1; j >= 0; j--) {
							if (all.get(j).getId().equals(all.get(i).getParent())) {
								all.get(j).getChildren().add(all.get(i));
							}
							if (all.get(i).getId().equals(all.get(j).getParent())) {
								all.get(i).getChildren().add(all.get(j));
							}
						}
					}
				}
				// 设置根节点父id
				if (all != null && all.size() > 0) {
					OperationDto tree = null;
					for (OperationDto d : all) {
						Integer parentTId = d.getParentTId();
						if(parentTId==null){
							tree = d;
							break;
						}
					}
					if(tree!=null) {
						tree.setNocheck(true);
					}
					return tree;
				}
				return null;
	}
	
	
	public List<OperationDto> convertOperationTodtos(List<Operation> operations){
		
		ArrayList<OperationDto> dtos = new ArrayList<OperationDto>();
		for (Operation operation : operations) {
			OperationDto operationDto = 	ConvertDto.convertOperationToDto(operation);
			dtos.add(operationDto);
			
		}
		return dtos;
	}
	
	
	@RequestMapping(value="info")
	@ResponseBody
	public Map<String,Object> info(@RequestParam(value="id") Integer id) {
		Operation operation = operationService.get(id);
		OperationDto convertOperationToDto = ConvertDto.convertOperationToDto(operation);
		
		List<Permission> permissions =  permissionService.list();
		ArrayList<PermissionDto> permissionDtos = new ArrayList<>();
		for (Permission permission : permissions) {
			PermissionDto convertPermission = ConvertDto.convertPermission(permission);
			permissionDtos.add(convertPermission);
		}
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("permissions", permissionDtos);
		
		
		hashMap.put("operation",convertOperationToDto);
		return hashMap;
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public Object delete(@RequestParam(value="id") Integer id) {
		Operation operation = operationService.get(id);
		if(operation!=null&& operation.getParent()==null) {
			//删除了根节点，就删除全部的节点
			permissionOperationService.deleteAll();
			operationService.deleteAll();
		}else {
			List<PermissionOperatoinMapping> permissionOperationMappings = operation.getPermissionOperationMappings();
			if(permissionOperationMappings!=null&permissionOperationMappings.size()>0) {
				PermissionOperatoinMapping permissionOperatoinMapping = permissionOperationMappings.get(0);
				Permission permisson = permissionOperatoinMapping.getPermisson();
				permissionOperationMappings.clear();
				permissionService.delete(permisson);
			}
			operationService.delete(id);
		}
		return null;
	}
	
	/**
	 * 新增节点，在操作表中添加一条数据，在权限表中添加一条数据，在权限和操作表中添加一条数据
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String,Object> save(@RequestBody OperationDto query){
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		Integer parentId = query.getParentTId();
		List<Operation> operations = operationService.getChildOperationByParentId(parentId);
		int sequence = operations.size()+1;
		Operation operation = new Operation();
		operation.setParent(parentId);
		operation.setName(query.getName());
		operation.setCode(query.getCode());
		operation.setUrl(query.getUrl());
		operation.setInserted_at(nowTimeStamp);
		operation.setUpdated_at(nowTimeStamp);
		operation.setSequence(sequence);
		operationService.save(operation);   
		
		
		
		//在权限表中添加一条数据
		Permission permission = new  Permission();
		permission.setType(PermissionType.OPERATION.getValue());
		permissionService.save(permission);
		
		
		
		//添加权限分类
		
		PermissionOperatoinMapping permissionOperatoinMapping = new PermissionOperatoinMapping();
		permissionOperatoinMapping.setOperation(operation);
		permissionOperatoinMapping.setPermisson(permission);
		permissionOperationService.save(permissionOperatoinMapping);
		
		Map<String, Object> buildResponse = buildResponse("",ResponseStatusCode.OK,null);
		return  buildResponse;
	}
	
	public Map<String,Object> buildResponse(String message, Integer statusCode, Object data){
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("statusCode", statusCode);
		hashMap.put("message", message);
		hashMap.put("data",data);
		return hashMap;
	}
	
	/**
	 * 修改节点
	 * @return
	 */
	@RequestMapping(value="edit")
	@ResponseBody
	public Map<String,Object> edit(@RequestBody OperationDto query){
		Timestamp nowTimeStamp = DateUtil.getNowTimeStamp();
		Integer parentTId = query.getParentTId();
		Operation operation = operationService.get(query.getId());
		operation.setName(query.getName());
		operation.setCode(query.getCode());
		operation.setUpdated_at(nowTimeStamp);
		operation.setUrl(query.getUrl());

		//		添加权限分类
//		operation.getPermissionOperationMappings().clear();
//		Permission permission = permissionService.get(query.getPermission());
//		PermissionOperatoinMapping permissionOperatoinMapping = new PermissionOperatoinMapping();
//		permissionOperatoinMapping.setOperation(operation);
//		permissionOperatoinMapping.setPermisson(permission);
//		operation.getPermissionOperationMappings().add(permissionOperatoinMapping);
		
		operationService.update(operation);
		Map<String, Object> buildResponse = buildResponse("",ResponseStatusCode.OK,null);
		return  buildResponse;
	}
	
	
	@RequestMapping(value="saveDrop")
	@ResponseBody
	public Map<String,Object> saveDrop(@RequestBody Map<String,String> data){
		System.out.println(JsonConverter.format(data));
		String sourceId = data.get("sourceId");
		String targetId = data.get("targetId");
		String moveType = data.get("moveType");
		Operation operation_source = operationService.get(Integer.valueOf(sourceId));
		Operation operation_target = operationService.get(Integer.valueOf(targetId));
		Integer target_sequence = operation_target.getSequence();
		Integer source_sequence = operation_source.getSequence();
		Integer target_parent = operation_target.getParent();
		Integer source_parent = operation_source.getParent();
		if("prev".equals(moveType)|| "next".equals(moveType)) {  //向前移动
			
			operation_source.setSequence(target_sequence);
			operation_source.setParent(target_parent);
			operation_target.setSequence(source_sequence);
		}else if("inner".equals(moveType)) {
			operation_source.setParent(Integer.valueOf(targetId));
			List<Operation> target_childs = operationService.getChildOperationByParentId(Integer.valueOf(targetId));
			operation_source.setSequence(target_childs.size()+1);
		}
		operationService.update(operation_source);
		operationService.update(operation_target);
		Map<String, Object> buildResponse = buildResponse("",ResponseStatusCode.OK,null);
		return  buildResponse;
		
	}
	
}
