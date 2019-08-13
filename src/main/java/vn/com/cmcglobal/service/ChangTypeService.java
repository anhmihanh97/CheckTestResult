package vn.com.cmcglobal.service;

import org.springframework.stereotype.Service;

@Service
public class ChangTypeService {
	public boolean checkResultTestCMC(String changeType, String latestResult) {
		if (latestResult.isEmpty()) {
			return false;
		} else {
			if ("DL(削除)".contains(changeType) && "OK".contains(latestResult)) {
				return true;
			} else if ("DL(削除)".contains(changeType) && "NG".contains(latestResult)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
