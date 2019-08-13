package vn.com.cmcglobal.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.com.cmcglobal.Constants;
import vn.com.cmcglobal.dto.TestCaseDTO;
import vn.com.cmcglobal.util.LanguageUtils;
import vn.com.cmcglobal.util.TestResultUtils;

@Service
public class TestResultServiceV2 {
	@Autowired
	ExecuteCountService excuteCountService;
	@Autowired
	LatestResultService latestResultService;
	@Autowired
	RTCCodeService rtcCodeService;
	@Autowired
	TestDurationService testDurationService;
	@Autowired
	EvaluationSoftwareService evaluationSoftwareService;
	@Autowired
	EvaluationHardService evaluationHardService;
	@Autowired
	PeripheralDeviceNumberService peripheralDeviceNumberService;
	@Autowired
	PastTestResultService pastTestResultService;
	@Autowired
	ChangTypeService changTypeService;
	// String input = "C:\\Users\\nmanh\\Desktop\\Result\\【H820PF】システムテスト_METER_Cooperation_Function_検出型_00_テスト成績書.xlsm";
	// String outputFolder = "C:\\Users\\nmanh\\Desktop\\Result";
	String outputFolder = Constants.DESKTOP;
	// public String timeStart = "2019/07/01";
	// private String timeStart;
	// public String timeEnd = "2019/07/08";
	// private String timeEnd;
	// public String ST = "ST4";
	@Value("${column.header.RTC}")
	public String RTC;
	@Value("${colum.header.firsttime}")
	public String firstTime;
	@Value("${colum.header.lasttime}")
	public String lastTime;
	@Value("${column.header.latestResult}")
	public String latestResult;
	@Value("${column.header.rtccode}")
	public String rtcCode;
	@Value("${column.header.implementationDate}")
	public String implementationdate;
	public static final String REMARKS = "備考";
	public static final String Free_DESCRIPTION = "自由記述欄";
	public static final String TEST_DURATION = "実施時間";
	public static final String imTime = "実施日";
	public static final String Evaluation_Software = "評価ソフト";
	public static final String Evaluation_Hard = "評価ハード";
	public static final String Peripheral_Device_Number = "持込み機器";
	public static final String PAST_TEST_RESULT = "（前回）テスト結果";
	public static final String CHANGE_TYPE = "変更区分";
	public static final String Change_Type = "(CHANGE_TYPE)";
	

	private static String[] columns = { "項番(No.)", "回数", "テスト結果", "インシデント番号",
	"備考","自由記述欄","修正要望","当初計画日","最新計画日","実施日","会社名","担当者","実施時間","評価バリエーション","評価ソフト","評価ハード","持込み機器",
	"（前回）テスト計画","（前回）回数","（前回）テスト結果","（前回）インシデント番号","（前回）備考","（前回）自由記述欄","（前回）修正要望","（前回）当初計画日",
	"（前回）最新計画日","（前回）実施日","（前回）会社名","（前回）担当者","（前回）実施時間","（前回）評価バリエーション","（前回）評価ソフト","（前回）評価ハード","（前回）持込み機器",
	"Cột CY （前回）テスト結果","変更区分(CHANGE_TYPE)"};
	
	public void writeWrongResult(MultipartFile file, String timeStart, String timeEnd, String ST) throws EncryptedDocumentException, InvalidFormatException, IOException {
		try {
			FileInputStream inputStream = new FileInputStream(new File(Constants.DESKTOP+file.getOriginalFilename()));
			// khoi tao file vi du tham chieu den file da co
			// XSSFWorkbook workbook = new XSSFWorkbook(file);
			Workbook workbook = WorkbookFactory.create(inputStream);
						
			XSSFWorkbook outputWorkbook = new XSSFWorkbook(); 
			
			// lay bang seet dau dien
			// XSSFSheet sheet = workbook.getSheetAt(0);
			Sheet sheet = workbook.getSheetAt(0);
					
			TreeMap<String, TestCaseDTO> listWrong = this.validateResult(sheet, workbook, inputStream, timeStart, timeEnd,ST, file);
			
			System.out.println("danh sanh la: ");
			for (Map.Entry<String, TestCaseDTO> entry : listWrong.entrySet()) {
				System.out.println("cac loi o row : " + entry.getKey()
						+ " --- FirstTime : "+ entry.getValue().getFirstTime() 
						+ " --------- Last time : " + entry.getValue().getLastTime()
						+ "------- ket qua test(TestResults) : " + entry.getValue().getTestResults()
						+ "----- OK va RTC(IncidentNumber) : " + entry.getValue().getIncidentNumber()
						+ "------ free_description(description) : " + entry.getValue().getDescription()
						+ "--- Test duration : " + entry.getValue().getTestDuration() 
						+ "---- Evaluation Software : " + entry.getValue().getEvaluationSoftware()
						+ "------Evaluation Hard : " + entry.getValue().getEvaluationHard()
						+ "------Peripheral : "+ entry.getValue().getPeripheralDeviceNumber() 
						+ "------Past test result : "+ entry.getValue().getPassTestResult() 
						+ "----Change Type : "+ entry.getValue().getChangeType());
			}
				
				// write wrong result
			SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
				Sheet outputSheet = outputWorkbook.createSheet(Constants.WRONG_SHEET);
				writeHeader(outputSheet);
				int rowNum = 1;
			
				for (Map.Entry<String, TestCaseDTO> entrys : listWrong.entrySet()) {					
					Row row = outputSheet.createRow(rowNum);
					row.createCell(0).setCellValue(entrys.getKey());
					// write wrong Execute count (回数)
					ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("checkNull(entrys.getValue().getFirstTime())");
				    PatternFormatting fill1 = rule1.createPatternFormatting();
				    fill1.setFillBackgroundColor(IndexedColors.BLUE.index);
				    fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
				    
					row.createCell(1).setCellValue(entrys.getValue().getFirstTime());				
					row.createCell(18).setCellValue(entrys.getValue().getLastTime());
					
					
					// write wrong TestResults テスト結果
					row.createCell(2).setCellValue(entrys.getValue().getTestResults());
					//write wrong OK va RTC(IncidentNumber) インシデント番号
					row.createCell(3).setCellValue(entrys.getValue().getIncidentNumber());
					//write wrong free_description(description) "自由記述欄"
					row.createCell(5).setCellValue(entrys.getValue().getDescription());
					//write wrong Test duration (実施時間)
					row.createCell(12).setCellValue(entrys.getValue().getTestDuration());
					// write wrong Evaluation Software (評価ソフト) 					
					row.createCell(14).setCellValue(entrys.getValue().getEvaluationSoftware());
					// write wrong Evaluation Hard (評価ハード)
					row.createCell(15).setCellValue(entrys.getValue().getEvaluationHard());
					// write wrong Peripheral Device Number (持込み機器)
					row.createCell(16).setCellValue(entrys.getValue().getPeripheralDeviceNumber());
					// write wrong kết quả quá khứ (Cột CY （前回）テスト結果)
					row.createCell(34).setCellValue(entrys.getValue().getPassTestResult());
					// write wrong 変更区分(CHANGE_TYPE)
					row.createCell(35).setCellValue(entrys.getValue().getChangeType());
					rowNum++;
				}
				FileOutputStream fileOut = new FileOutputStream(new File(outputFolder + "\\" + Constants.REVIEW_FILE));
				outputWorkbook.write(fileOut);
				fileOut.close();
				// Closing the workbook
				inputStream.close();
				
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File Excel không tồn tại hoặc đăng được sử dụng!!!");
		}

	}
	
	private void writeHeader(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columns.length; i++) {
			Cell c = headerRow.createCell(i);
			c.setCellValue(columns[i]);
		}
	}
	private boolean checkNull(String text) {
		if(text!=null) {
			return true;
		}else {
			return false;
		}
	}
	public void okkkkkkkkk(Sheet sheet) {
		Row headerRows = sheet.getRow(0);
		final DataFormatter formatter = new DataFormatter();
		for (int num = 0; num < 120; num++) {
			String ftime = formatter.formatCellValue(headerRows.getCell(num));
			System.out.println(ftime);
		}
	}

	public TreeMap<String, TestCaseDTO> validateResult(Sheet sheet, Workbook workbook, FileInputStream inputStream,
			String timeStart, String timeEnd, String ST, MultipartFile file) throws IOException, ParseException {

		Map<String, TestCaseDTO> listWrongItems = new TreeMap<String, TestCaseDTO>();

		Row headerRow = sheet.getRow(0);
		int firsttime = TestResultUtils.getColumnIndexByName(headerRow, firstTime);
		int lasttime = TestResultUtils.getColumnIndexByName(headerRow, lastTime);
		int latestresult = TestResultUtils.getColumnIndexByName(headerRow, latestResult);
		int rtccode = TestResultUtils.getColumnIndexByName(headerRow, rtcCode);
		int remarks = TestResultUtils.getColumnIndexByName(headerRow, REMARKS);
		int free_desciption = TestResultUtils.getColumnIndexByName(headerRow, Free_DESCRIPTION);
		int test_duration = TestResultUtils.getColumnIndexByName(headerRow, TEST_DURATION);
		int imtime = TestResultUtils.getColumnIndexByName(headerRow, imTime);
		int evaluationsoftware = TestResultUtils.getColumnIndexByName(headerRow, Evaluation_Software);
		int evaluationhard = TestResultUtils.getColumnIndexByName(headerRow, Evaluation_Hard);
		int peripheraldevicenumber = TestResultUtils.getColumnIndexByName(headerRow, Peripheral_Device_Number);
		int passtestresult = TestResultUtils.getColumnIndexByName(headerRow, PAST_TEST_RESULT);
		int changetype = TestResultUtils.getColumnIndexByName2(headerRow, Change_Type, CHANGE_TYPE);
		Cell cell;
		TestCaseDTO caseDTO = null;
		final DataFormatter formatter = new DataFormatter();
			// for (int i = 1; i < sheet.getLastRowNum(); i++) {
		 for (int i = 1; i < 20; i++) {
			Row row = sheet.getRow(i);
			caseDTO = new TestCaseDTO();
			// start check first time, last time
			String ftime = formatter.formatCellValue(row.getCell(firsttime));
			String ltime = formatter.formatCellValue(row.getCell(lasttime));
			if (StringUtils.isBlank(ftime) == true || StringUtils.isBlank(ltime) == true || Integer.parseInt(ftime) < 0
					|| Integer.parseInt(ltime) <= 0) {
				if (StringUtils.isBlank(ftime) == true || Integer.parseInt(ftime) < 0) {
					caseDTO.setFirstTime("eror first time");
					listWrongItems.put(String.valueOf(i), caseDTO);
				}
				if (StringUtils.isBlank(ltime) == true || Integer.parseInt(ltime) <= 0) {
					caseDTO.setLastTime("eror last time");
					listWrongItems.put(String.valueOf(i), caseDTO);
				}
			} else if (excuteCountService.isValidExecuteCount(ftime, ltime) == false || Integer.parseInt(ftime) <= 0) {
				// update file input
				cell = sheet.getRow(i).getCell(firsttime);
				cell.setCellValue(Integer.parseInt(ltime) + 1);
			}
			// end check first time, last time

			// start check latest result
			String lresult = formatter.formatCellValue(row.getCell(latestresult));
			if (!latestResultService.isValidResult(lresult)) {
				caseDTO.setTestResults("eror latest result");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check latest result

			// start check rtc code (IncidentNumber)
			String rcode = formatter.formatCellValue(row.getCell(rtccode));
			if (rtcCodeService.verifyRtcCode(lresult, rcode) == false) {
				caseDTO.setIncidentNumber("eror rtc code");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check rtc code

			// start check Free_Description(Description)
			String fdescription = formatter.formatCellValue(row.getCell(free_desciption));
			if (LanguageUtils.checkVNCharacter(fdescription)) {
				caseDTO.setDescription("eror Free_Description");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check Free_Description(Description)

			// start check Test Duration (TestDuration)
			String tDuration = formatter.formatCellValue(row.getCell(test_duration));
			String contenremarks = formatter.formatCellValue(row.getCell(remarks));
			if (!testDurationService.validateDuration(tDuration, contenremarks, fdescription)) {
				caseDTO.setTestDuration("eror Test Duration");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check Test Duration

			// start check Evaluation Software
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String implementtime = formatter.formatCellValue(row.getCell(imtime));
			String esoftware = formatter.formatCellValue(row.getCell(evaluationsoftware));
			if (implementtime.isEmpty()) {
				caseDTO.setImTime("eror implement time");
				listWrongItems.put(String.valueOf(i), caseDTO);
			} else {
				Date imdate = sdf.parse(implementtime);
				Date startdate = sdf.parse(timeStart);
				Date enddate = sdf.parse(timeEnd);
				if (latestResultService.isValidResult(lresult)) { // not error latest Result
					if (evaluationSoftwareService.isInvalidEvaluationSoftware(lresult) == true) {
						if (!evaluationSoftwareService.checkEvaluationSoftware(esoftware, startdate, enddate, imdate,
								ST)) {
							caseDTO.setEvaluationSoftware("eror Evaluation Software");
							listWrongItems.put(String.valueOf(i), caseDTO);
						}
					}
				}
				// end check Evaluation Software

				// start check evaluation hard
				String ehard = formatter.formatCellValue(row.getCell(evaluationhard));
				if (latestResultService.isValidResult(lresult)) { // not error latest Result
					if (evaluationHardService.isInvalidEvaluationHard(lresult) == true) {
						if (!evaluationHardService.checkEvaluationHardware(ehard, startdate, enddate, imdate, ST)) {
							caseDTO.setEvaluationHard("eror evaluation hard");
							listWrongItems.put(String.valueOf(i), caseDTO);
						}
					}
				}
				// end check evaluation hard

				// start check peripheral device number
				String pDeviceNumber = formatter.formatCellValue(row.getCell(peripheraldevicenumber));
				if (latestResultService.isValidResult(lresult)) { // not error latest Result
					if (peripheralDeviceNumberService.isPeripheralDeviceNumberService(lresult) == true) {
						if (!peripheralDeviceNumberService.checkEvaluationSoftware(pDeviceNumber, startdate, enddate,
								imdate, ST)) {
							caseDTO.setPeripheralDeviceNumber("eror peripheral device number");
							listWrongItems.put(String.valueOf(i), caseDTO);
						}
					}
				}
			}
			// end check peripheral device number

			// start check past test result
			String ptresult = formatter.formatCellValue(row.getCell(passtestresult));
			if (pastTestResultService.CompareTestResult(lresult, ptresult)) {
				caseDTO.setPassTestResult("eror past test result");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check past test result

			// start check change type
			String changeTypes = formatter.formatCellValue(row.getCell(changetype));
			if (changTypeService.checkResultTestCMC(changeTypes, lresult)) {
				caseDTO.setChangeType("eror change type");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check change type
		}
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(Constants.DESKTOP+file.getOriginalFilename());
		workbook.write(outputStream);
		outputStream.close();
		return (TreeMap<String, TestCaseDTO>) listWrongItems;
	}
}
