package com.zcc.admin.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel工具类
 * 
 * @author zhangcc
 *
 */
public class ExcelUtil {

	public static void excelLocal(String path, String fileName, String[] headers, List<Object[]> datas) {
		Workbook workbook = getWorkbook(headers, datas);
		if (workbook != null) {
			ByteArrayOutputStream byteArrayOutputStream = null;
			FileOutputStream fileOutputStream = null;
			try {
				byteArrayOutputStream = new ByteArrayOutputStream();
				workbook.write(byteArrayOutputStream);

				String suffix = ".xls";
				File file = new File(path + File.separator + fileName + suffix);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (byteArrayOutputStream != null) {
						byteArrayOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 导出excel
	 * 
	 * @param fileName
	 * @param headers
	 * @param datas
	 * @param response
	 */
	public static void excelExport(String fileName, String[] headers, List<Object[]> datas,
			HttpServletResponse response) {
		Workbook workbook = getWorkbook(headers, datas);
		if (workbook != null) {
			ByteArrayOutputStream byteArrayOutputStream = null;
			try {
				byteArrayOutputStream = new ByteArrayOutputStream();
				workbook.write(byteArrayOutputStream);

				String suffix = ".xls";
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String((fileName + suffix).getBytes(), "iso-8859-1"));

				OutputStream outputStream = response.getOutputStream();
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (byteArrayOutputStream != null) {
						byteArrayOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param headers
	 *            列头
	 * @param datas
	 *            数据
	 * @return
	 */
	public static Workbook getWorkbook(String[] headers, List<Object[]> datas) {
		Workbook workbook = new HSSFWorkbook();

		Sheet sheet = workbook.createSheet();
		Row row = null;
		Cell cell = null;
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER_SELECTION);

		Font font = workbook.createFont();

		int line = 0, maxColumn = 0;
		if (headers != null && headers.length > 0) {// 设置列头
			row = sheet.createRow(line++);
			row.setHeightInPoints(23);
			font.setBold(true);
			font.setFontHeightInPoints((short) 13);
			style.setFont(font);

			maxColumn = headers.length;
			for (int i = 0; i < maxColumn; i++) {
				cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
		}

		if (datas != null && datas.size() > 0) {// 渲染数据
			for (int index = 0, size = datas.size(); index < size; index++) {
				Object[] data = datas.get(index);
				if (data != null && data.length > 0) {
					row = sheet.createRow(line++);
					row.setHeightInPoints(20);

					int length = data.length;
					if (length > maxColumn) {
						maxColumn = length;
					}

					for (int i = 0; i < length; i++) {
						cell = row.createCell(i);
						cell.setCellValue(data[i] == null ? null : data[i].toString());
					}
				}
			}
		}

		for (int i = 0; i < maxColumn; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	/**
	 * 读取单元格内容
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {

		if (cell == null) {
			return "";
		}
		if ("NUMERIC".equals(cell.getCellType().name())) {
			return new BigDecimal(cell.getNumericCellValue()).toString();
		} else if ("STRING".equals(cell.getCellType().name()))
			return StringUtils.trimToEmpty(cell.getStringCellValue());
		else if ("FORMULA".equals(cell.getCellType().name())) {
			return StringUtils.trimToEmpty(cell.getCellFormula());
		} else if ("BLANK".equals(cell.getCellType().name())) {
			return "";
		} else if ("BOOLEAN".equals(cell.getCellType().name())) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if ("ERROR".equals(cell.getCellType().name())) {
			return "ERROR";
		} else {
			return cell.toString().trim();
		}
	}
}
