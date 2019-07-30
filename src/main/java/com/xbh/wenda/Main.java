package com.xbh.wenda;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import com.alibaba.fastjson.JSON;
import com.xbh.wenda.Utils.ConfUtils;
import com.xbh.wenda.assemble.Assemble;
import com.xbh.wenda.bean.WdBean;
import com.xbh.wenda.spider.BaiduResultUrlSpider;
import com.xbh.wenda.spider.QAndAResultPageSpider;
import com.xbh.wenda.spider.QAndAUrlSpider;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xbh
 * @date 2019年7月25日13:45:17
 * @Description 主类
 */
public class Main {
	private static Integer i = 0;

	private static String inputName = "C:\\Users\\Administrator\\Desktop\\3w.xlsx";
	private static Integer title = 2;
	private static Integer sex = 3;
	private static Integer age = 4;
	private static Integer issue = 5;
	private static Integer result = 7;
	private static List<WdBean> beanList;
	private static String site = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=";
	private static int depth = 2;

	/**
	*@author xbh
	*@date 2019/7/25 15:15
	*@param  * @param inputName excel文件名
	*@return java.util.List<com.wd.bean.WdBean>
	*@description 读取execl每列，并分装到list中返回
	*/
	public static List<WdBean> readExcel(String inputName, List<WdBean> list) throws Exception {
		final FileInputStream inputStream = new FileInputStream(inputName);

		final XSSFWorkbook xss = new XSSFWorkbook(inputStream);
		final Sheet sheet = xss.getSheetAt(0);
		final Row rowTest = sheet.getRow(0);
		for (int j = 0; j < rowTest.getLastCellNum(); j++) {
			final String s = rowTest.getCell(j).toString();
			if (s.length() > 1) {
				final String s1 = s.substring(0, 2);
				if (s1.equals("标题")) {
					Main.title = j;
				} else if (s1.equals("性别")) {
					Main.sex = j;
				} else if (s1.equals("年龄")) {
					Main.age = j;
				} else if (s1.equals("描述")) {
					Main.issue = j;
				} else if (s1.equals("答案")) {
					Main.result = j;
				}
			}
		}


		for (int j = 0; j < sheet.getLastRowNum(); j++) {
			final WdBean bean = new WdBean();
			final Row row = sheet.getRow(j);

			final Cell title = row.getCell(Main.title);
			final Cell sex = row.getCell(Main.sex);
			final Cell age = row.getCell(Main.age);
			final Cell issue = row.getCell(Main.issue);
			final Cell result = row.getCell(Main.result);

			if (title != null) {
				bean.setTitle(title.getStringCellValue());
			} else {
				bean.setTitle("");
			}


			if (sex != null) {
				bean.setSex(sex.getStringCellValue());
			}  else {
				bean.setSex("");
			}


			if (age != null) {
				bean.setAge(age.getStringCellValue());
			}  else {
				bean.setAge("");
			}


			if (issue != null) {
				bean.setIssue(issue.getStringCellValue());
			}  else {
				bean.setIssue("");
			}


			if (result != null) {
				bean.setResult(result.getStringCellValue());
			}  else {
				bean.setResult("");
			}

			bean.setCxeclRowIndex(String.valueOf(j));

			list.add(bean);
		}

		return list;
	}

	public static void write(String outputName, List<WdBean> list) throws IOException {
		final XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(outputName));
		final Sheet sheet1 = workbook.getSheetAt(0);
		final int num = sheet1.getLastRowNum();
//		if (workbook.get == 0) {
//			final Sheet sheet = workbook.createSheet("test123");
//			for (WdBean bean : list) {
//				final Row row = sheet.createRow(Integer.valueOf(bean.getCxeclRowIndex()));
//				row.createCell(Main.sex).setCellValue(bean.getSex());
//				row.createCell(Main.age).setCellValue(bean.getAge());
//				row.createCell(Main.issue).setCellValue(bean.getIssue());
//				row.createCell(Main.result).setCellValue(bean.getResult());
//			}
//		} else {
			final Sheet sheet = workbook.getSheetAt(0);

			for (WdBean bean : list) {
				Row row = sheet.getRow(Integer.valueOf(bean.getCxeclRowIndex()));
				if (row == null) {
					row = sheet.createRow(Integer.valueOf(bean.getCxeclRowIndex()));
					row.createCell(Main.sex).setCellValue(bean.getSex());
					row.createCell(Main.age).setCellValue(bean.getAge());
					row.createCell(Main.issue).setCellValue(bean.getIssue());
					row.createCell(Main.result).setCellValue(bean.getResult());
				} else {
					row.getCell(Main.age).setCellValue(bean.getAge());
					row.getCell(Main.sex).setCellValue(bean.getSex());
					row.getCell(Main.issue).setCellValue(bean.getIssue());
					final Cell cell = row.getCell(Main.result);
					if (cell == null) {
						row.createCell(Main.result).setCellValue(bean.getResult());
					} else {
						row.getCell(Main.result).setCellValue(bean.getResult());
					}


				}
//				final Cell cellSex = row.getCell(Main.sex);
//				if (cellSex == null) {
//					row.createCell(Main.sex).setCellValue(bean.getSex());
//				}
//
//				final Cell cellAge = row.getCell(Main.age);
//				if (cellAge == null) {
//					row.createCell(Main.age).setCellValue(bean.getSex());
//				}
//
//				row.getCell(Main.age).setCellValue(bean.getAge());
//
//				row.getCell(Main.issue).setCellValue(bean.getIssue());
//				final Cell cell = row.getCell(Main.result);

			}
//		}

		workbook.write(new FileOutputStream(outputName));
	}

	public static List<WdBean> readJson(File jsonFile) throws IOException {
		final String s = FileUtils.readFileToString(jsonFile, Charset.defaultCharset());
		return JSON.parseArray(s, WdBean.class);
	}

	public static void test() {
//		try {
//			if (beanList == null) {
//				beanList = new ArrayList();
//			}
//			final ConcurrentLinkedDeque<File> linkedDeque = new ConcurrentLinkedDeque<>();
//			final CountDownLatch downLatch = new CountDownLatch(jsonCount);
//			for (int j = 0; j < 6; j++) {
//				linkedDeque.addLast(new File("C:\\Users\\Administrator\\Desktop\\dome\\" + String.valueOf(j) + ".json"));
//			}
//
//			for (File file : linkedDeque) {
//				new Thread(() -> {
//					try {
//						final List<WdBean> beans = readJson(file);
//						Main.beanList.addAll(beans);
//						downLatch.await();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}).start();
//			}
//		} catch (Exception e) {
//
//		}
//
//		System.out.println(beanList);
//		beanList.addAll();
	}

	/**
	*@author xbh
	*@date 2019/7/28 11:13
	*@param  * @param  获取百度搜索结果页
	*@return void
	*@description 
	*/
	public static void getBaiduResultUrl() throws Exception {
		for (int j = 1; j < beanList.size(); j++) {
			String s = site + beanList.get(j).getTitle() + "有问必答快速问医生";
			for (int i = 0; i < depth; i++) {
				beanList.get(j).getOneUrl().add(s + "&pn=" + i * 10);
			}
		}

		final BaiduResultUrlSpider baiduResultSpider = new BaiduResultUrlSpider();

		for (WdBean bean : Main.beanList) {
			final CrawlDatums datum = baiduResultSpider.addSeedAndReturn(bean.getOneUrl()).type("ksBaidu");
			datum.forEach(d-> d.meta("id", Main.beanList.indexOf(bean)));
		}
		baiduResultSpider.start(3);
	}

	public static void getQAndAUrl() throws Exception {
		final QAndAUrlSpider qaSpider = new QAndAUrlSpider();
		for (WdBean bean : Main.getBeanList()) {
			for (CrawlDatum datum : qaSpider.addSeedAndReturn(bean.getTowUrl()).type("ksQA")) {
				if (datum.url().trim() != null) {
					datum.meta("id", Main.getBeanList().indexOf(bean));
				}
			}
		}
		qaSpider.start(3);
	}

	public static void getQAndAPage(List<WdBean> beans) throws Exception {
		final QAndAResultPageSpider pageSpider = new QAndAResultPageSpider();
		for (WdBean bean : beans) {
			final CrawlDatums datums = pageSpider.addSeedAndReturn(bean.getThreeUrl()).type("ksPage");
			for (CrawlDatum datum : datums) {
				datum.meta("id", beans.indexOf(bean));
			}
		}
		pageSpider.start(3);
	}

	public static void assemble(List<WdBean> beans) {
		final Assemble assemble = new Assemble();
		assemble.assemble(beans);
	}

	public static void main(String[] args) {
		if (Main.beanList == null) {
			Main.beanList = new ArrayList<WdBean>();
		}

		try {
			readExcel(inputName, Main.getBeanList());

			// 获取百度结果页
			Main.getBaiduResultUrl();

			// 获取问答页面url
			getQAndAUrl();

//			for (int j = 0; j < 7; j++) {
//				final String s = FileUtils.readFileToString(new File(String.valueOf(j)+".json"), Charset.defaultCharset());
//				Main.beanList.addAll(JSON.parseArray(s, WdBean.class));
//			}

			// 获取最终页面
			getQAndAPage(Main.beanList);

			write(inputName, Main.beanList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<WdBean> getBeanList() {
		return beanList;
	}
}
