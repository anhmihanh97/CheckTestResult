package vn.com.cmcglobal.service;

import org.springframework.stereotype.Service;

@Service
public class ExecuteCountService {

	public boolean isValidExecuteCount(String firstTime, String lastTime) {
		int ftime = Integer.parseInt(firstTime);
		int ltime = Integer.parseInt(lastTime);
		if (ftime <= ltime) {
			return false; // update file input
		} else {
			return true;
		}
	}
}
