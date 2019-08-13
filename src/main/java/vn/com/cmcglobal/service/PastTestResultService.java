package vn.com.cmcglobal.service;

import org.springframework.stereotype.Service;

@Service
public class PastTestResultService {
	public boolean CompareTestResult(String lastestResult, String pastestResult) {
		if(!pastestResult.isEmpty()) {
			if(pastestResult.equals("OK") || pastestResult.equals("DL")) {
				if(pastestResult.equals(lastestResult)) {
					return true;
				}
				else {
					return false;
				}
			}else {
				return false;
			}
		}
		return false;
	}
}
