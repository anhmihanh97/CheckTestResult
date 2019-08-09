package vn.com.cmcglobal.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class RTCCodeService {
	public boolean verifyRtcCode(String lresult, String rtcCode) {
		if ("OK".equals(lresult) && StringUtils.isEmpty(rtcCode) == true) {
			return true;
		}else if ("NG".equals(lresult) || "PN(Bug)".equalsIgnoreCase(lresult) || "PN(Internal)".equalsIgnoreCase(lresult)
				|| "PN(equipment)".equalsIgnoreCase(lresult) || "PN(QA)".equalsIgnoreCase(lresult) || "PN(DG)".equalsIgnoreCase(lresult) 
				|| "DG".equals(lresult)|| "DL".equals(lresult) || "NT".equals(lresult)) {
			return checkFormatRTC(rtcCode);	
		}
		else {
		return false;
		}
	}

	public boolean checkFormatRTC(String rtcCode) {
		if (StringUtils.isEmpty(rtcCode)) {
			return false;
		}
		String regex = "^RT[0-9]{5,20}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(rtcCode);
		return matcher.matches();
	}
}
