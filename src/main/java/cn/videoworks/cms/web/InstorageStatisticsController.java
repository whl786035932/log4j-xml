package cn.videoworks.cms.web;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.videoworks.cms.entity.InstorageStatistics;
import cn.videoworks.cms.service.InstorageStatisticsService;
import cn.videoworks.cms.util.DateUtil;
import cn.videoworks.cms.util.RestResponse;
import cn.videoworks.cms.vo.InstorageStatisticsVo;
import cn.videoworks.commons.util.json.JsonConverter;

@Controller
@RequestMapping("instorageStatistics")
public class InstorageStatisticsController {
	private static final Logger log = LoggerFactory.getLogger(InstorageStatisticsController.class);
	@Resource
	private InstorageStatisticsService service;
	/**
	 *
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "site.cms.statistcs.index";
	}
	
	@RequestMapping(value = "ajax", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse get(@RequestBody Map<String,String> param) {
		RestResponse response = new RestResponse();
		if(param==null){
			log.info("参数为空");
			response.setStatusCode(500);
			response.setMessage("参数为空为空");
			return response;
		}
		String date = param.get("date");
		date = date.replace("\n", "");
		// 获取跟结点s
		List<InstorageStatistics> list = service.getRoot(date);
		// 获取标签树
		List<InstorageStatisticsVo> vos = null;
		if (list != null && list.size() > 0) {
			for (InstorageStatistics is : list) {
				InstorageStatisticsVo tree = getTree(is);
				if (vos == null) {
					vos = new ArrayList<>();
				}
				vos.add(tree);
			}
		}
		if (vos != null) {
			Map<String,Object>result = new HashMap<String,Object>();
			result.put("name", date.replace("\n", ""));
			result.put("children", vos);
			response.setData(result);
			response.setStatusCode(200);
		} else {
			response.setStatusCode(500);
			response.setMessage("数据获取为空");
		}
		return response;
	}
	
	/**
	 * 通过根节点获取树.
	 */
	private InstorageStatisticsVo getTree(InstorageStatistics c) {
		InstorageStatisticsVo vo = new InstorageStatisticsVo();
		vo.setId(c.getId());// id
		vo.setName(c.getName());// 名称
		Integer pId = null;
		if (c.getParent() != null) {
			pId = c.getParent().getId();
		}
		vo.setpId(pId);// 父id
		vo.setValue(c.getValue());// 值
		String statisticeAt = "";
		if (c.getStatisticeAt() != null) {
			statisticeAt = c.getStatisticeAt();
		}
		vo.setStatisticeAt(statisticeAt);// 当前时间
		String insertedAt = "";
		if(c.getInsertedAt()!=null){
			insertedAt = DateUtil.format.format(c.getInsertedAt());
		}
		vo.setInsertedAt(insertedAt);// 创建时间
		String updatedAt = "";
		if(c.getUpdatedAt()!=null){
			updatedAt = DateUtil.format.format(c.getUpdatedAt());
		}
		vo.setUpdatedAt(updatedAt);// 更新时间
		getChildren(vo, c.getChildren());// 设置子节点
		return vo;

	}

	private void getChildren(InstorageStatisticsVo dto,Set<InstorageStatistics> children) {
		List<InstorageStatisticsVo> list = new ArrayList<>();
		if (children != null && children.size() > 0) {
			List<InstorageStatistics>chids = new ArrayList<>(children);
			for(InstorageStatistics cf : chids){
				InstorageStatisticsVo d = new InstorageStatisticsVo();
				d.setId(cf.getId());// id
				d.setName(cf.getName());//名称
				d.setpId(cf.getParent().getId());// 父id
				d.setValue(cf.getValue());// 值
				String statisticeAt = "";
				if (cf.getStatisticeAt() != null) {
					statisticeAt = cf.getStatisticeAt();
				}
				d.setStatisticeAt(statisticeAt);// 当前时间
				String insertedAt = "";
				if(cf.getInsertedAt()!=null){
					insertedAt = DateUtil.format.format(cf.getInsertedAt());
				}
				d.setInsertedAt(insertedAt);// 创建时间
				String updatedAt = "";
				if(cf.getUpdatedAt()!=null){
					updatedAt = DateUtil.format.format(cf.getUpdatedAt());
				}
				d.setUpdatedAt(updatedAt);// 更新时间
				list.add(d);
				getChildren(d, cf.getChildren());// 设置子节点
			}
		}
		dto.setChildren(list);
	}
	
	/**
	 * 导出数据到Excel.
	 */
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response,String data) {
		if (data == null) {
			return;
		}
		try {
			Map<String,String> result = JsonConverter.asMap(data, String.class, String.class);
			String dateBegin = result.get("startDate");
			String dateEnd = result.get("endDate");
			// 获取跟结点
			List<InstorageStatistics> list = service.getRoots(dateBegin,dateEnd);
			// 获取树
			List<InstorageStatisticsVo> vos = null;
			if (list != null && list.size() > 0) {
				for (InstorageStatistics is : list) {
					InstorageStatisticsVo tree = getTree(is);
					if (vos == null) {
						vos = new ArrayList<>();
					}
					vos.add(tree);
				}
			}

			List<Date> listDate = null;
			Date dBegin = DateUtil.formatYMD.parse(dateBegin);
			Date dEnd = DateUtil.formatYMD.parse(dateEnd);
			listDate = getDatesBetweenTwoDate(dBegin, dEnd);
			List<String>listTimes = null;
			if (listDate != null && listDate.size() > 0) {
				for (int i = 0; i < listDate.size(); i++) {
					log.info("间隔时间为:"+DateUtil.formatYMD.format(listDate.get(i)));
					if (listTimes == null) {
						listTimes = new ArrayList<String>();
					}
					listTimes.add(DateUtil.formatYMD.format(listDate.get(i)));
				}
			}
			// 数据按时间分组
			List<Map<String, List<InstorageStatisticsVo>>> array = null;
			if (listTimes != null) {
				for(String time : listTimes){
					Map<String,List<InstorageStatisticsVo>> map = new HashMap<>();
					List<InstorageStatisticsVo>listv = null;
					for (InstorageStatisticsVo vo : vos) {
						if(time.equals(vo.getStatisticeAt())){
							if(listv == null){
								listv = new ArrayList<>();
							}
							listv.add(vo);
						}
					}
					map.put(time, listv);
					if(array == null){
						array = new ArrayList<>();
						array.add(map);
					}
				}
			}
			createExcel(vos, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void createExcel(List<InstorageStatisticsVo> dtos,HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("入库统计");
		sheet.setColumnWidth(0, 5120);
		sheet.setColumnWidth(1, 14336);
		sheet.setColumnWidth(2, 14336);
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell(0);
		String[] titles = { "日期", "频道", "新闻" };
		for (int i = 0; i < titles.length; i++) {
			cell.setCellValue(titles[i]);
			cell.setCellStyle(style);
			cell = row.createCell(i + 1);
		}
		if (dtos != null && dtos.size() != 0) {
			for (int i = dtos.size() - 1; i >= 0; i--) {
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(dtos.get(i).getStatisticeAt());
				String nameV = "";
				if(dtos.get(i).getName()!=null){
					nameV += dtos.get(i).getName();
				}
				if(dtos.get(i).getValue()!=null){
					nameV += "("+ dtos.get(i).getValue() + ")";
				}
				row.createCell(1).setCellValue(nameV);
				String names = "";
				if (dtos.get(i).getChildren() != null && dtos.get(i).getChildren().size() > 0) {
					for(int j=0;j< dtos.get(i).getChildren().size();j++){
						InstorageStatisticsVo v = dtos.get(i).getChildren().get(j);
						names +=v.getName()+"("+v.getValue()+")";
						if (j < dtos.get(i).getChildren().size() - 1) {
							names += "\r\n";
						}
					}
				}
				HSSFCell createCell = row.createCell(2);
				HSSFCellStyle cellStyle = createCell.getCellStyle();
				cellStyle.setWrapText(true);
				createCell.setCellStyle(cellStyle);
				createCell.setCellValue(new HSSFRichTextString(names));
			}
		}
		try {
			this.setResponseHeader(response);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("数据写入Excel表格异常", e);
		}
	}
	
	  //发送响应流方法
    public void setResponseHeader(HttpServletResponse response) {
        try {
        	String fileName = new String(("storage-export("+ DateUtil.format.format(new Date()) + ")").getBytes(), "UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	/**
	 * 根据开始时间和结束时间返回时间段内的时间集合
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return List
	 */
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(beginDate);// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(beginDate);
		boolean bContinue = true;
		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (endDate.after(cal.getTime())) {
				lDate.add(cal.getTime());
			} else {
				break;
			}
		}
		lDate.add(endDate);// 把结束时间加入集合
		return lDate;
	}

}
