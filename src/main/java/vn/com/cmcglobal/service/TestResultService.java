package vn.com.cmcglobal.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import vn.com.cmcglobal.Constants;
import vn.com.cmcglobal.dto.TestCaseDTO;
import vn.com.cmcglobal.util.LanguageUtils;
import vn.com.cmcglobal.util.TestResultUtils;

@Service
public class TestResultService {

	@Autowired
	private ExecuteCountService excuteCountService;
	@Autowired
	private LatestResultService latestResultService;

	@Autowired
	private EvaluationSoftwareService evaluationSoftwareService;

	private String testResultFile;

	private String outputFolder;

	private String nameMarket;

	private Date startTimeST;

	private Date endTimeST;

	private String nameST;

	public Date getStartTimeST() {
		return startTimeST;
	}

	public void setStartTimeST(Date startTimeST) {
		this.startTimeST = startTimeST;
	}

	public Date getEndTimeST() {
		return endTimeST;
	}

	public void setEndTimeST(Date endTimeST) {
		this.endTimeST = endTimeST;
	}

	public String getNameST() {
		return nameST;
	}

	public void setNameST(String nameST) {
		this.nameST = nameST;
	}

	public String getNameMarket() {
		return nameMarket;
	}

	public void setNameMarket(String nameMarket) {
		this.nameMarket = nameMarket;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getTestResultFile() {
		return testResultFile;
	}

	public void setTestResultFile(String testResultFile) {
		this.testResultFile = testResultFile;
	}

	@Value("${column.header.testerName}")
	public String testerNameColumn;

	@Value("${column.header.latestResult}")
	public String latestResultColumn;

	@Value("${column.header.id}")
	public String idColumn;

	@Value("${colum.header.firsttime}")
	public String firstTimeColumn;

	@Value("${colum.header.lasttime}")
	public String lastTimeColumn;

	@Value("${column.header.freeDescription}")
	public String freeDescription;

	@Value("${column.header.evaluationSoftware}")
	public String evaluationSoftware;

	@Value("${column.header.evaluationHard}")
	public String evaluationHard;

	@Value("${column.header.peripheralDeviceNumber}")
	public String peripheralDeviceNumber;

	@Value("${column.header.implementationDate}")
	public String implementationDate;

	private static String[] columns = { "Component", "Name", "Wrong test result", "Wrong test result(preivous)",
			"Wrong test Descript free" };
	
	public void writeWrongResult() throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook inputWorkbook = WorkbookFactory.create(new File(testResultFile));

		Workbook outputWorkbook = new XSSFWorkbook();

		Sheet sheet = inputWorkbook.getSheetAt(0);
		List<TestCaseDTO> lTestCaseDTO = this.validateResult(TestResultUtils.getComponentFromAbsolutePath(testResultFile), sheet, testResultFile, inputWorkbook);
		// write wrong result

		Sheet outputSheet = outputWorkbook.createSheet(Constants.WRONG_SHEET);
		writeHeader(outputSheet);
		int rowNum = 1;
		for (TestCaseDTO testCaseDTO : lTestCaseDTO) {
			Row row = outputSheet.createRow(rowNum);
			row.createCell(0).setCellValue(testCaseDTO.getComponent());
			row.createCell(1).setCellValue(testCaseDTO.getTesterName());
			row.createCell(2).setCellValue(testCaseDTO.getTestResults());
			row.createCell(3).setCellValue(testCaseDTO.getDescription());
			rowNum++;
		}
		FileOutputStream out = new FileOutputStream(new File(outputFolder + "\\" + Constants.REVIEW_FILE));
		outputWorkbook.write(out);
		out.close();
		((FileOutputStream) outputWorkbook).close();

		// Closing the workbook
		((FileOutputStream) inputWorkbook).close();
	}

	private void writeHeader(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columns.length; i++) {
			Cell c = headerRow.createCell(i);
			c.setCellValue(columns[i]);
		}

	}
	
	public List<TestCaseDTO> validateResult(String fileName, Sheet sheet, String nameFileInput, Workbook inputWorkbook)
			throws IOException {
		List<TestCaseDTO> lWrongItems = new ArrayList<TestCaseDTO>();

		Row headerRow = sheet.getRow(0);
		int latestResultPos = TestResultUtils.getColumnIndexByName(headerRow, latestResultColumn);
		int idPos = TestResultUtils.getColumnIndexByName(headerRow, idColumn);
		int testerNamePos = TestResultUtils.getColumnIndexByName(headerRow, testerNameColumn);
		int firstTime = TestResultUtils.getColumnIndexByName(headerRow, firstTimeColumn);
		int lastTime = TestResultUtils.getColumnIndexByName(headerRow, lastTimeColumn);
		int description = TestResultUtils.getColumnIndexByName(headerRow, freeDescription);
		int eSoftware = TestResultUtils.getColumnIndexByName(headerRow, evaluationSoftware);
		int imDate = TestResultUtils.getColumnIndexByName(headerRow, implementationDate);
		Cell cell;
		// int resultFirstTime=
		TestCaseDTO testCaseDTO = null;
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			// lay ra ten tester trong hang do voi cot tuong ung
			String testerName = row.getCell(testerNamePos).getStringCellValue();
			if (!TestResultUtils.isBelongToCMC(testerName)) {
				continue;
			}
			String latestResult = row.getCell(latestResultPos).getStringCellValue();
			testCaseDTO = new TestCaseDTO();
			testCaseDTO.setId((int) row.getCell(idPos).getNumericCellValue());
			testCaseDTO.setComponent(fileName);
			testCaseDTO.setTesterName(row.getCell(testerNamePos).getStringCellValue());
			// check latest result
			if (!latestResultService.isValidResult(latestResult)) {
				testCaseDTO.setTestResults(latestResult);
				lWrongItems.add(testCaseDTO);
			}
			// start execute count
			DataFormatter formatter = new DataFormatter();
			String ftime = formatter.formatCellValue(row.getCell(firstTime));
			String ltime = formatter.formatCellValue(row.getCell(lastTime));
			// check excute count
			if (!excuteCountService.isValidExecuteCount(ftime, ltime)) {
				// update file
				cell = sheet.getRow(i).getCell(firstTime);
				cell.setCellValue(Integer.parseInt(ltime) + 1);
			}
	
			String contentFreeDescription = row.getCell(description).getStringCellValue();
			if (LanguageUtils.checkVNCharacter(contentFreeDescription)) {
				testCaseDTO.setDescription(contentFreeDescription);
				lWrongItems.add(testCaseDTO);
			}
			
			Date date = (Date) row.createCell(imDate).getDateCellValue();
			if (latestResultService.isValidResult(latestResult)) { // not error latest Result
				if (evaluationSoftwareService.isInvalidEvaluationSoftware(latestResult)) {
					cell = sheet.getRow(i).getCell(imDate);
					cell.setCellValue(
							evaluationSoftwareService.checkMarket(nameMarket, startTimeST, endTimeST, date, nameST));
				}

			}

		}
		FileOutputStream out = new FileOutputStream(new File(testResultFile));
		inputWorkbook.write(out);
		out.close();
		return lWrongItems;
	}
}
