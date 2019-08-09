package vn.com.cmcglobal.util;

public class FunctionAction {
	private static final String formatcreatorCompany = "PRDCV";

	public static String checkExecute(String firstTime, String lastTime) {
		if (firstTime == null || lastTime == null) {
			return "1"; // 1. Mandatory field
		} else {

			try {
				int fTime = Integer.parseInt(firstTime);
				int lTime = Integer.parseInt(lastTime);
				if (fTime > lTime && fTime > 0 && lTime > 0) {
					return null;
				} else {

					return "2";
				}

			} catch (Exception e) {
				// TODO: handle exception
				return "2,3";
			}
		}
	}

	// OK/NG/PN(Bug)/PN(QA)/PN(Internal)/PN(Equipment)/PN(DG)/DG/DL/NT
	public static String checkLatestResult(String result) {
		switch (result) {
		case "OK":
		case "NG":
		case "PN(Bug)":
		case "PN(QA)":
		case "PN(Internal)":
		case "PN(Equipment)":
		case "PN(DG)":
		case "DG":
		case "DL":
		case "NT":
			return null;

		default:
			return "No format for Latest Result";
		}
	}

	public static boolean rtcCodeFormat(String rtcCode) {
		if (rtcCode == null) {
			return false;
		} else {

			if ("RT".equals(rtcCode.substring(0, 2))) {
				try {// is number
					if (Integer.parseInt(rtcCode.substring(2)) > 9999) {
						return true;
					} else
						return false;
				}

				catch (Exception e) {
					// not number
					return false;

				}
			}
			return false;
		}
	}

	public static String checkColumnRtcCode(String result, String content) {
		if ("OK".equals(result)) {
			return null;
		} else {
			if (rtcCodeFormat(content)) {
				return null;
			} else {
				return "Not format rtccode ";
			}
		}

	}

	public static String testerRemark(String contentColumnRemark, String contenCorrectionRequest) {
		if ("o".equals(contenCorrectionRequest)) {

		}

		return null;
	}

	// 10 rq
	public static String creatorCompany(String contenCompany) {
		if (formatcreatorCompany.equals(contenCompany)) {
			return null;
			// y 2
		} else {
			return "not format: " + formatcreatorCompany;
		}

	}

	public static String duration(String contentDuration) {
		try {
			int number = Integer.parseInt(contentDuration);
			if (number > 1 && number < 25) {
				return null;
			}
			if (number > 25) {
				return "Input colum CJ (Tester remark) or column CK (Free desciption field)";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "error format ";
		}

		return null;
	}

	public static String evaluation(String result, String market) {// OK, NG, PN(Bug)
		switch (result) {
		case "OK":
		case "NG":
		case "PN(Bug)": {
			switch (market) {
			case "TYFA":
				return "【TYFA】システムテスト_ST1向け_保証型_CMC_評価ハード.xlsx_ハード_xxxxx";
			case "TZAA":
				return "【TZAA】システムテスト_ST1向け_保証型_CMC_評価ハード.xlsx_ハード_xxxxx";
			case "T20A":
				return "【T20A】システムテスト_ST1向け_保証型_CMC_評価ハード.xlsx_ハード_xxxxx";
			default:
				return null;

			}

		}

		default:
			return null;

		}

	}

	public static String checkColumnCY(String key) {
		switch (key) {
		case "OK":
		case "DL":
			return "Need confirm Tester, If true, it is necessary to delete the past results from the CX ~ DM column and edit the CG column to 1";

		default:
			return null;
		}
	}

	public static String changeTypeColumnZ(String content, String key) {
		if ("DL(削除)".equals(content)) {
			switch (key) {
			case "OK":
			case "NG":
				return null;

			case "PN":
				return "Delete columns CO~DM, report back to group test without processing if there is a change of DL (削除)";

			default:
				return "Confirm tester can check wrongly";
			}
		}
		return null;
	}

	public static String TestAngle(String content) {
		if ("S".equals(content)) {
			return "Not rank S";
		} else {
			return null;
		}
	}

	public static String testSetion(String content) {
		switch (content) {
		case "ベンチ(手動)":
		case "ベンチ_手動":
			return null;

		default:
			return "Not ベンチ(手動) or  ベンチ_手動 "; // "Not ベンチ(手動) or ベンチ_手動 "
		}
	}

	public static String ChangeTypeColumnY(String content) {
		switch (content) {
		case "ADD(追加)":
		case "DV（流用）":
		case "CHG(変更)":
		case "NEW(新規)":
			return null;
		default:
			return "Not format column 変更区分 last day st";// not format column
		}
	}

	public static String checkExtra2(String content) {
		if ("ST1".equals(content)) {
			return "Choose ST1 or ST2";
		} else {
			if ("ST3".equals(content)) {
				return "Choose ST1,ST2,ST3";
			} else {
				return null;
			}
		}

	}

	public static String checkExtra3(String content) {
		if ("PRDCV".equals(content)) {
			return null;
		} else {
			return "not format PRDCV";
		}
	}

}
