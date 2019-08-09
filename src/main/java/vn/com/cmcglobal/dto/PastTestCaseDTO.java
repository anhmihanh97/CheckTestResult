package vn.com.cmcglobal.dto;

public class PastTestCaseDTO {
	private String testResults;// column(CL) last DC
	private String incidentNumber;// column(CM) rtcCode
	private String testerRemark;
	private String remarks;//
	private String description;
	private String correctionRequest;
	private String creatorCompany;
	private String testDuration;
	private String evaluationSoftware;
	private String evaluationHard;
	private String peripheralDeviceNumber;

	public String getTestResults() {
		return testResults;
	}

	public void setTestResults(String testResults) {
		this.testResults = testResults;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public String getTesterRemark() {
		return testerRemark;
	}

	public void setTesterRemark(String testerRemark) {
		this.testerRemark = testerRemark;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCorrectionRequest() {
		return correctionRequest;
	}

	public void setCorrectionRequest(String correctionRequest) {
		this.correctionRequest = correctionRequest;
	}

	public String getCreatorCompany() {
		return creatorCompany;
	}

	public void setCreatorCompany(String creatorCompany) {
		this.creatorCompany = creatorCompany;
	}

	public String getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(String testDuration) {
		this.testDuration = testDuration;
	}

	public String getEvaluationSoftware() {
		return evaluationSoftware;
	}

	public void setEvaluationSoftware(String evaluationSoftware) {
		this.evaluationSoftware = evaluationSoftware;
	}

	public String getEvaluationHard() {
		return evaluationHard;
	}

	public void setEvaluationHard(String evaluationHard) {
		this.evaluationHard = evaluationHard;
	}

	public String getPeripheralDeviceNumber() {
		return peripheralDeviceNumber;
	}

	public void setPeripheralDeviceNumber(String peripheralDeviceNumber) {
		this.peripheralDeviceNumber = peripheralDeviceNumber;
	}

}
