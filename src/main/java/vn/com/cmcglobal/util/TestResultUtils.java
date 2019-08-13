package vn.com.cmcglobal.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class TestResultUtils {
	// set dong theo header , so sanh giong nhau thi tra ve vi tri cua dong giong 
	public static Integer getColumnIndexByName(Row headerRow, String text) {
		Integer index = null; 
		for (int cn = 0; cn < headerRow.getLastCellNum(); cn++) {
			Cell c = headerRow.getCell(cn);
			if (text.equals(c.getStringCellValue())) {
				index = cn;
				break;
			}
		}
		return index;
	}
	
	public static Integer getColumnIndexByName2(Row headerRow, String text1, String text2) {
		Integer index = null; 
		for (int cn = 0; cn < headerRow.getLastCellNum(); cn++) {
			Cell c = headerRow.getCell(cn);
			if (c.getStringCellValue().contains(text1) && c.getStringCellValue().contains(text2)) {
				index = cn;
				break;
			}
		}
		return index;
	}
	
	// check name, neu rong va khong chua du5 tra ve false
	public static boolean isBelongToCMC(String testerName) {
		if (StringUtils.isEmpty(testerName)) {
			return false;
		}
		return testerName.toLowerCase().contains("du5");
	}
	// lay ten file
	public static String getComponentFromAbsolutePath(String absolutePath) {
		int i = absolutePath.lastIndexOf("\\");
		return absolutePath.substring(i + 1);
	}
}
