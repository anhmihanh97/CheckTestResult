package vn.com.cmcglobal.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LatestResultService {

	public boolean isValidResult(String latestResult) {
		if (StringUtils.isEmpty(latestResult)) {
			return true;
		}
		if ("OK".equals(latestResult) || "NG".equals(latestResult) || "PN(Bug)".equals(latestResult)
				|| "PN(QA)".equals(latestResult) || "PN(Internal)".equals(latestResult)
				|| "PN(Equipment)".equals(latestResult) || "PN(DG)".equals(latestResult) || "DG".equals(latestResult)
				|| "DL".equals(latestResult) || "NT".equals(latestResult)) {
			return true;
		}

		return false;
	}

}
