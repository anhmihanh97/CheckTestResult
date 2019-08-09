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

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
	String input = "C:\\Users\\nmanh\\Desktop\\Result\\【H820PF】システムテスト_METER_Cooperation_Function_検出型_00_テスト成績書.xlsm";
	public String timeStart = "2019/07/01";
	public String timeEnd = "2019/07/08";
	public String ST = "ST4";	
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
	
	public void writeWrongResult() throws EncryptedDocumentException, InvalidFormatException, IOException {
		try {
			FileInputStream inputStream = new FileInputStream(new File(input));
			// khoi tao file vi du tham chieu den file da co
			// XSSFWorkbook workbook = new XSSFWorkbook(file);
			Workbook workbook = WorkbookFactory.create(inputStream);
			// lay bang seet dau dien
			// XSSFSheet sheet = workbook.getSheetAt(0);
			Sheet sheet = workbook.getSheetAt(0);

			Map<String, TestCaseDTO> listWrong = this.validateResult(sheet, workbook, inputStream, timeStart, timeEnd, ST);
			System.out.println("danh sanh la: ");
			for (Map.Entry<String, TestCaseDTO> entry : listWrong.entrySet()) {
				System.out.println("cac loi o row : " + entry.getKey() 
//						+ " --- FirstTime : "+ entry.getValue().getFirstTime() 
//						+ " --------- Last time : " + entry.getValue().getLastTime()
//						+ "------- ket qua test(TestResults) : " + entry.getValue().getTestResults()
//						+ "----- OK va RTC(IncidentNumber) : " + entry.getValue().getIncidentNumber()
//						+ "------ free_description(description) : " + entry.getValue().getDescription()
//						+ "--- Test duration : " + entry.getValue().getTestDuration() 
						+ "---- Evaluation Software : "+ entry.getValue().getEvaluationSoftware()
						+"------Evaluation Hard : "+entry.getValue().getEvaluationHard()
						+"------Peripheral : "+entry.getValue().getPeripheralDeviceNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File Excel không tồn tại hoặc đăng được sử dụng!!!");
		}

	}

	public Map<String, TestCaseDTO> validateResult(Sheet sheet, Workbook workbook, FileInputStream inputStream, String timeStart, String timeEnd, String ST)
			throws IOException, ParseException {
		// List<TestCaseDTO> listWrongItems = new ArrayList<TestCaseDTO>();

		Map<String, TestCaseDTO> listWrongItems = new HashMap<String, TestCaseDTO>();

		Row headerRow = sheet.getRow(0);
		// int RTCcode = TestResultUtils.getColumnIndexByName(headerRow, RTC);
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
		Cell cell;
		TestCaseDTO caseDTO = null;
		final DataFormatter formatter = new DataFormatter();
//			for (int i = 1; i < sheet.getLastRowNum(); i++) {
		for (int i = 80; i < 111; i++) {
			Row row = sheet.getRow(i);
			caseDTO = new TestCaseDTO();
			// start check first time, last time
			String ftime = formatter.formatCellValue(row.getCell(firsttime));
			String ltime = formatter.formatCellValue(row.getCell(lasttime));
			if (StringUtils.isBlank(ftime) == true || StringUtils.isBlank(ltime) == true || Integer.parseInt(ftime) < 0
					|| Integer.parseInt(ltime) <= 0) {
				if (StringUtils.isBlank(ftime) == true || Integer.parseInt(ftime) < 0) {
					caseDTO.setFirstTime("loi");
					listWrongItems.put(String.valueOf(i), caseDTO);
				}
				if (StringUtils.isBlank(ltime) == true || Integer.parseInt(ltime) <= 0) {
					caseDTO.setLastTime("loi");
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
				caseDTO.setTestResults("loi");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check latest result

			// start check rtc code (IncidentNumber)
			String rcode = formatter.formatCellValue(row.getCell(rtccode));
			if (rtcCodeService.verifyRtcCode(lresult, rcode) == false) {
				caseDTO.setIncidentNumber("loi");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check rtc code

			// start check Free_Description(Description)
			String fdescription = formatter.formatCellValue(row.getCell(free_desciption));
			if (LanguageUtils.checkVNCharacter(fdescription)) {
				caseDTO.setDescription("loi");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check Free_Description(Description)

			// start check Test Duration (TestDuration)
			String tDuration = formatter.formatCellValue(row.getCell(test_duration));
			String contenremarks = formatter.formatCellValue(row.getCell(remarks));
			if (!testDurationService.validateDuration(tDuration, contenremarks, fdescription)) {
				caseDTO.setTestDuration("loi");
				listWrongItems.put(String.valueOf(i), caseDTO);
			}
			// end check Test Duration
			
			// start check Evaluation Software
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String implementtime = formatter.formatCellValue(row.getCell(imtime));
			String esoftware = formatter.formatCellValue(row.getCell(evaluationsoftware));		
			Date imdate = sdf.parse(implementtime);
			Date startdate = sdf.parse(timeStart);	
			Date enddate = sdf.parse(timeEnd);
			if (latestResultService.isValidResult(lresult)) { // not error latest Result
				if (evaluationSoftwareService.isInvalidEvaluationSoftware(lresult) == true) {
					if (!evaluationSoftwareService.checkEvaluationSoftware(esoftware, startdate, enddate, imdate, ST)) {
						caseDTO.setEvaluationSoftware("loi");
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
						caseDTO.setEvaluationHard("loi");
						listWrongItems.put(String.valueOf(i), caseDTO);
					}
				}
			}
			// end check evaluation hard 
			
			// start check peripheral device number
			String pDeviceNumber = formatter.formatCellValue(row.getCell(peripheraldevicenumber));
			if (latestResultService.isValidResult(lresult)) { // not error latest Result
				if (peripheralDeviceNumberService.isPeripheralDeviceNumberService(lresult) == true) {
					if (!peripheralDeviceNumberService.checkEvaluationSoftware(pDeviceNumber, startdate, enddate, imdate, ST)) {
						caseDTO.setPeripheralDeviceNumber("loi");
						listWrongItems.put(String.valueOf(i), caseDTO);
					}
				}
			}
			// end check peripheral device number
			
		}
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(input);
		workbook.write(outputStream);
		outputStream.close();
		return listWrongItems;
	}
}
