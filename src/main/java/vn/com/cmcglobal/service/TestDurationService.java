package vn.com.cmcglobal.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TestDurationService {
	public boolean validateDuration(String strTestDuration, String remark, String freeDesciption) {
		boolean isValid = false;
		if (StringUtils.isBlank(strTestDuration)) {
			return false;
		}
		if (strTestDuration.contains(".")) {
			return false;
		}
		if (strTestDuration.contains("-")) {
			return false;
		}
		int testDuration = Integer.parseInt(strTestDuration);
		if (testDuration < 1) {
			return false;
		}
		if (testDuration > 1) {
			isValid = true;
		}
		if (testDuration > 25) {
			if (!StringUtils.isBlank(remark) || !StringUtils.isBlank(freeDesciption)) {
				isValid = true;
			} else {
				isValid = false;
			}
		}
		return isValid;
	}
}
