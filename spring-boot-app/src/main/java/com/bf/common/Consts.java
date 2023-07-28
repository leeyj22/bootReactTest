package com.bf.common;

public class Consts {
	
	public final static String URL 								= "url";
	public static final String CODE 							= "code";
	public final static String DATA 							= "data";
	public final static String RESULT_CODE 						= "result_cd";
	public final static String CHECK 							= "check";
	public final static String IS_CERTIFY 						= "is_certify";
	
	public final static String POST 							= "POST";
	public final static String PUT 								= "PUT";
	public final static String DELETE 							= "DELETE";
	public final static String GET 								= "GET";
	
	public final static String USER								= "USER";
	
	public final static String USER_ID							= "user_id";
	public final static String USER_IDX							= "user_idx";
	public final static String USER_PW							= "user_pw";
	public final static String USER_TYPE						= "user_type";
	public final static String USERTYPE							= "userType";
	public final static String USER_NAME						= "user_name";
	public final static String USER_PHONE						= "user_phone";
	public final static String USER_EMAIL						= "user_email";
	public final static String USER_BIRTHDAY					= "user_birthday";
	public final static String CUST_CODE						= "cust_code";
	public final static String CUST_NAME						= "cust_name";
	public final static String LEVEL_CODE						= "level_code";
	public final static String LEVEL_NAME						= "level_name";
	
	public final static String SESSION_ID						= "session_id";
	public final static String SESSION_USER_ID					= "session_user_id";
	public final static String SESSION_USER_IDX					= "session_user_idx";
	public final static String SESSION_USER_NAME				= "session_user_name";
	public final static String SESSION_USER_PHONE				= "session_user_phone";
	public final static String SESSION_USER_EMAIL				= "session_user_email";
	public final static String SESSION_USER_TYPE				= "session_user_type";
	public final static String SESSION_USER_BIRTHDAY			= "session_user_birthday";
	public final static String SESSION_LEVEL_CODE				= "session_level_code";
	public final static String SESSION_LEVEL_NAME				= "session_level_name";
	
	//
	public final static String RADIUS							= "radius";
	public final static String LATITUDE							= "lat";
	public final static String LONGITUDE						= "lng";
	
	
	
	public final static String UTF_8							= "UTF-8";
	public final static String APPLICATION						= "application";
	public final static String JSON								= "json";
	public final static String FORM_URLENCODED					= "x-www-form-urlencoded";
	
	public final static String DEPOSIT_NAME						= "depositNm";
	public final static String DEFAULT_USER						= "dujang";
	public final static String PAY_CNT							= "payCnt";
	public final static String END_PAY_CNT						= "endPayCnt";
	public final static String START_PAY_CNT					= "startPayCnt";
	public final static String AMT								= "amt";
	public final static String GOODS_NAME						= "goodsName";
	public final static String GOODS_CNT						= "goodsCnt";
	public final static String GOODS_CL							= "goodsCl";
	public final static String CATEGORY_IDX						= "categoryIdx";
	public final static String GOODS_IDX						= "goodsIdx";
	public static final String REVIEW_IDX 						= "reviewIdx";
	public static final String PWD		 						= "pwd";
	public final static String MERCHANTKEY						= "merchantKey";
	public final static String PAY_METHOD						= "payMethod";
	public final static String RETURN_URL						= "returnURL";
	public final static String CHARSET							= "charSet";
	public final static String USER_IP							= "userIP";
	public final static String MALL_IP							= "mallIP";
	public final static String ENCODE_PARAMETERS				= "encodeParameters";
	public final static String TR_KEY							= "charset";
	public final static String REG_DATE							= "regDate";
	public final static String CUSTCODE							= "custCode";
	public final static String CUSTNAME							= "custName";
	public final static String EXTRA_INFOS						= "extraInfos";
	public final static String PAY_RESULT_CD					= "payResultCd";
	public final static String PAY_RESULT_INFOS					= "payResultInfo";
	public final static String PAY_ROOT							= "payRoot";
	
	public static final String RESULT_DATA 						= "resultData";
	public static final String RESULT_MSG 						= "resultMsg";
	public static final String CALLBACK_URL 					= "callbackUrl";
	
	public static final String AUTHORIZATION 					= "Authorization";
	public static final String BEARER 							= "Bearer";
	public static final String ORD_NBR 							= "ordNbr";
	
	public static class NicePayResult {
		public static final String
		AUTH_RESULT_CODE		= "AuthResultCode",		// 인증 결과 코드
		AUTH_RESULT_MSG			= "AuthResultMsg",		// 인증 결과 메시지
		PAY_METHOD				= "PayMethod",			// 결제수단
		GOODS_NAME				= "GoodsName",			// 상품 명
		PAY_RESULT_CODE			= "ResultCode",			// 결제 결과 코드
		PAY_RESULT_MSG			= "ResultMsg",			// 결제 결과 메시지
		AMT						= "Amt",				// 금액
		TID						= "TID",				// 거래번호
		AUTH_DATE				= "AuthDate",			// 승인일시
		AUTH_CODE				= "AuthCode",			// 승인번호
		CARD_CODE				= "CardCode",			// 카드사 코드
		CARD_NAME				= "CardName",			// 카드사 명
		VBANK_CODE				= "VbankBankCode",		// 가상계좌 은행 코드
		VBANK_NAME				= "VbankBankName",		// 가상계좌 은행 명
		VBANK_NUMBER			= "VbankNum",			// 가상계좌 계좌번호
		VBANK_EXPIRE_DATE		= "VbankExpDate",		// 가상계좌 입금 마감일
		MALL_RESERVED			= "MallReserved";		// 예비 필드
	}
	
	public static class PayParams {
		public static final String
		AUTH_RESULT_CODE		= "authResultCode",		// 인증 결과 코드
		AUTH_RESULT_MSG			= "authResultMsg",		// 인증 결과 메시지
		PAY_METHOD				= "payMethod",			// 결제수단
		MOID					= "moid",				// 주문번호
		BUYER_NAME				= "buyerName",			// 입금자 명
		GOODS_NAME				= "goodsName",			// 상품 명
		PAY_RESULT_CODE			= "resultCode",			// 결제 결과 코드
		PAY_RESULT_MSG			= "resultMsg",			// 결제 결과 메시지
		AMT						= "amt",				// 구매/결제 금액
		TID						= "tid",				// 거래번호
		AUTH_DATE				= "authDate",			// 승인일시
		AUTH_CODE				= "authCode",			// 승인코드
		CARD_CODE				= "cardCode",			// 카드사 코드
		CARD_NAME				= "cardName",			// 카드사 명
		VBANK_CODE				= "vBankCode",			// 가상계좌 은행 코드
		VBANK_NAME				= "vBankName",			// 가상계좌 은행 명
		VBANK_NUMBER			= "vbankNum",			// 가상계좌 계좌번호
		VBANK_EXPIRE_DATE		= "vbankExpDate",		// 가상계좌 입금 마감일
		MALL_RESERVED			= "mallReserved",		// 예비 필드
		ENCRYPT_DATA			= "encryptData",	 	// 해쉬값
		ENCODE_PARAMETERS 		= "encodeParameters",	// 암호화 대상 항목
		GOODS_CL 				= "goodsCl", 			// 상품구분 실물(1), 컨텐츠(2)
		GOODS_CNT				= "goodsCnt",	 		// 결제 수량
		MID						= "mID",	 			// 상점ID
		BUYER_TEL				= "buyerTel",	 		// 구매/결제자 번호
		BUYER_EMAIL				= "buyerEmail",	 		// 구매/결제자 이메일
		USER_IP					= "userIP",	 			// 회원사고객IP
		MALL_IP					= "mallIP",	 			// 상점서버IP
		TR_KEY					= "trKey",				// 전문 생성 일시
		EDI_DATE				= "ediDate",			// 전문 생성 일시
		PAY_STATUS				= "payStatus",			// 결제 결과
		EXTRA_INFOS				= "extraInfos";			// 추가 파라미터
	}
	
	public static class TransferParams {
		public static final String
		BODY_NO					= "bodyNo",				
		CUST_NAME				= "custName",			
		AGREE_NAME				= "agreeName",			
		ZIP_CODE				= "zipCode",			
		ZIP_CODE2				= "zipCode2",			
		INS_ADDR				= "insAddr",			
		INS_ADDR2				= "insAddr2",			
		INS_ADDR_DETAIL			= "insAddrDetail",		
		INS_ADDR_DETAIL2		= "insAddrDetail2",		
		TEL_NO					= "telNo",				
		PHONE_NO				= "phoneNo",			
		TEL_NO2					= "telNo2",				
		PHONE_NO2				= "phoneNo2",			
		PRODUCT_CODE			= "productCode",		
		PRODUCT_TYPE			= "productType",	
		PRODUCT_NAME			= "productName",
		COST_AMOUNT				= "costAmount",
		PAYMENT_TYPE			= "paymentType",	
		SERVICE_MONTH			= "serviceMonth",	
		ORDER_TYPE				= "orderType",	
		SHIPPING_TYPE			= "shippingType",	
		DUE_DATE				= "dueDate",	
		DUE_DATE2				= "dueDate2",	
		COMPANY_CHECK			= "companyCheck",	
		RETURN_BANK_NAME		= "returnBankName",	
		RETURN_BANK_NO			= "returnBankNo";
	}
	
	public static class PaymentHistoryParams {
		public static final String
		SEQ						= "seq",
		AMT						= "Amt",
		ETC_MESSAGE				= "etcMessage",
		AUTH_CODE				= "authCode",
		IN_USER					= "inUser",
		UP_USER					= "upUser",
		CASH_GUBUN				= "cashGubun",
		CUST_CODE				= "custCode",
		IN_DATE					= "inDate",
		FROM_CNT				= "fromCnt",
		TO_CNT					= "toCnt";
	}
	
	public static class PayMethod {
		public static final String
		CARD					= "CARD",
		VBANK					= "VBANK";
	}
	
	public static class userInfoParam {
		public static final String
		USER_ID				= "userId",
		USER_IDX			= "userIdx",
		USER_PW				= "userPw",
		USER_TYPE			= "userType",
		USER_NAME			= "userName",
		USER_EMAIL			= "userEmail",
		USER_BIRTHDAY		= "userBirthday",
		USER_PHONE			= "userPhone",
		LEVEL_CODE			= "levelCode",
		LEVEL_NAME			= "levelName",
		NICK				= "nick",
		APPROVAL			= "approval",
		POINT				= "point",
		CERT_YN				= "certYn",
		IS_USER_CERTIFY		= "isUserCertify";
	}
	
	public static class payInfoParam {
		public static final String
		CUST_CODE			= "custCode",
		PAYCNT				= "paycnt",
		GUBUN				= "gubun",
		DRAW_DATE			= "drawDate",
		PAY_TERM			= "payTerm",
		PAY_PRICE			= "payPrice",
		PART_PRICE			= "partPrice",
		ISSUE_DATE			= "issueDate",
		PAY_KIND			= "payKind",
		PAY_DATE			= "payDate",
		CHK_GIRO			= "chkGiro",
		TAX_YN				= "taxYn",
		TAX_NO				= "taxNo",
		LIMT_AMT			= "limtAmt",
		SAUPNO				= "saupno",
		PAYMENT_NAME		= "paymentName",
		PAY_KIND_NAME  		= "payKindName",
		OVER_MONTH			= "overMonth";
	}

	public static class ProductInfoParam {
		public static final String
		CUST_CODE			= "custCode",
		CUST_NAME			= "custName",
		CUST_MOBILE			= "custMobile",
		INSTALL_CUSTNAME	= "installCustName",
		INSTALL_ADDRESS1	= "installAddress1",
		INSTALL_ADDRESS2	= "installAddress2",
		INSTALL_TEL			= "installTel",
		INSTALL_MOBILE		= "installMobile",
		INSTALL_DATE		= "installDate",
		PREPAYMENTS			= "prepayments",
		RENTAL_PRICE1		= "rentalPrice1",
		RENTAL_PRICE2		= "rentalPrice2",
		MODEL_CODE			= "modelCode",
		ERP_MODEL_NAME		= "erpModelName",
		SALE_KIND			= "saleKind",
		SALE_KIND_NAME		= "saleKindName",
		RENTAL_TERM			= "rentalTerm",
		FREE_AS_START_DATE	= "freeAsStartDate",
		FREE_AS_END_DATE	= "freeAsEndDate",
		RENTAL_START_DATE	= "rentalStartDate",
		RENTAL_END_DATE		= "rentalEndDate",
		MONTH_PAY_DAY		= "monthPayDay",
		RECEIPT				= "receipt",
		PAY_WAY				= "payWay",
		TAX_NUMBER			= "taxNumber",
		TAX_GUBUN			= "taxGubun",
		TAX_NAME			= "taxName",
		BAL_GUBUN			= "balGubun",
		CUST_KIND			= "custKind",
		MODEL_NAME			= "modelName",
		RENTAL_PRICE		= "rentalPrice",
		BANK_NAME			= "bankName",
		BANK_ACCOUNT		= "bankAccount",
		PAN_GUBUN			= "panGubun";
	}
	
	public static class EditMethod {
		public static final String
		CUST_CODE			= "custCode",
		BANK_NO				= "bankNo",
		BANK_NAME			= "bankName",
		BANK_CODE			= "bankCode",
		EXP_YEAR			= "expYear",
		EXP_MONTH			= "expMonth",
		TEL_NUM_01			= "telNum01",
		TEL_NUM_02			= "telNum02",
		TEL_NUM_03			= "telNum03",
		ID_NUM				= "idNum";
	}
	
	public static class OrderInfoParam {
		public static final String
		ORD_NBR				= "ordNbr",
		USER_IDX			= "userIdx",
		PRODUCT_NAME		= "productName",
		AGREE_NAME			= "agreeName",
		ORD_EMAIL			= "ordEmail",
		ORD_TEL1			= "ordTel1",
		ORD_TEL2			= "ordTel2",
		ORD_TEL3 			= "ordTel3",
		ORD_CELL1			= "ordCell1",
		ORD_CELL2			= "ordCell2",
		ORD_CELL3			= "ordCell3",
		SMS_STATE			= "smsState",
		INS_ADDR			= "insAddr",
		INS_ADDR_DETAIL		= "insAddrDetail",
		CUST_NAME			= "custName",
		REC_TEL1			= "recTel1",
		REC_TEL2			= "recTel2",
		REC_TEL3 			= "recTel3",
		REC_CELL1			= "recCell1",
		REC_CELL2			= "recCell2",
		REC_CELL3			= "recCell3",
		REC_ZIP2			= "recZip2",
		INS_ADDR2			= "insAddr2",
		INS_ADDR_DETAIL2	= "insAddrDetail2",
		COMPANY_CHECK		= "companyCheck",
		COST_AMOUNT			= "costAmount",
		USE_POINT			= "usePoint",
		USE_DEPOSIT			= "useDeposit",
		SAVE_POINT			= "savePoint",
		DELIVERY_PRICE		= "deliveryPrice",
		FREIGHT_COLLECT_PRICE	= "freightCollectPrice",
		PAYMENT_TYPE		= "paymentType",
		PAYMENT_NAME		= "paymentName",
		CASH_RECEIPT		= "cashReceipt",
		ESCROW_STATE		= "escrowState",
		TRAN_STATE			= "tranState",
		ORD_IP				= "ordIp",
		COUPON_DISCOUNT		= "couponDiscount",
		COUPON_POINT		= "couponPoint",
		ORDER_TYPE			= "orderType",
		COUPON_IDX			= "couponIdx",
		BASKET_IDXS			= "basketIdxs";
	}
	
	public static class RentalParam {
		public static final String
		MODEL_CODE			= "modelCode",
		CUST_NAME			= "custName",
		CUST_CELL			= "custCell",
		GROUP_CODE			= "groupCode",
		CUST_CODE			= "custCode"
		;
	}
	
	public static String emailContent(String div, String name, String id, String pw) {
		String content = "";
		
		content += "<table style=\"border-collapse: collapse;border-spacing: 0;margin:0 auto;padding:30px 38px;border:0;width:740px;font-family: MalgunGothic, Arial, Helvetica, sans-serif;\">";
		content += "<colgroup>";
		content += "<col width=\"220px\" />";
		content += "<col width=\"520px\" />";
		content += "</colgroup>";
		content += "<tr>";
		content += "<td style=\"border-bottom: 2px solid #202020;padding-bottom: 9px;\"><a href=\"#\"><img style=\"display:block\" src=\"https://c.bodyfriend.co.kr/resources/img/email/logo.png\" /></a></td>";
		content += "<td style=\"border-bottom: 2px solid #202020;vertical-align:bottom;padding-bottom:5px;text-align:right;\"><a href=\"#\" style=\"font-size:13px;color: #696969;text-decoration:none\">www.bodyfriend.co.kr</a></td>";
		content += "</tr>";
		content += "<tr>";
		content += "<td colspan=\"2\" style=\"font-size: 18px;font-weight:bold;padding-top:28px;padding-bottom:20px;\">";
		content += "안녕하세요. <span style=\"color: #ab945b;\">" + name + "</span> 회원님.";
		content += "</td>";
		content += "</tr>";
		content += "<tr>";
		content += "<td colspan=\"2\" style=\"font-size:15px;line-height:1.5;\">";
		if ("I".equals(div)) {
			content += "요청하신 바디프랜드 아이디를 알려 드립니다.<br />보다 더 나은 서비스를 위해 항상 최선을 다하겠습니다.<br />감사합니다.";
		} else {
			content += "요청하신 바디프랜드 임시 비밀번호를 발급해 드립니다.<br />보다 더 나은 서비스를 위해 항상 최선을 다하겠습니다.<br />감사합니다.";
		}
		content += "</td>";
		content += "</tr>";
		content += "<tr>";
		content += "<td style=\"height:22px\"></td>";
		content += "</tr>";
		content += "<tr>";
		content += "<td style=\"font-size:16px;background:#f6f6f6;padding: 14px 10px 12px 28px;line-height:1;vertical-align:middle;border-top:1px solid #d8d8d8;border-bottom:1px solid #d8d8d8;border-right:1px solid #d8d8d8\">아이디</td>";
		content += "<td style=\"font-size:16px;padding-left:20px;border-top:1px solid #d8d8d8;border-bottom:1px solid #d8d8d8;padding: 13px 10px 13px 28px;vertical-align:middle;color:#5b5b5b;\">" + id + "</td>";
		if ("P".equals(div)) {
			content += "</tr>";
			content += "<tr>";
			content += "<td style=\"font-size:16px;background:#f6f6f6;padding: 14px 10px 12px 28px;line-height:1;vertical-align:middle;border-top:1px solid #d8d8d8;border-bottom:1px solid #d8d8d8;border-right:1px solid #d8d8d8\">비밀번호</td>";
			content += "<td style=\"font-size:16px;padding-left:20px;border-top:1px solid #d8d8d8;border-bottom:1px solid #d8d8d8;padding: 13px 10px 13px 28px;vertical-align:middle;color:#5b5b5b;\">" + pw + "</td>";
		}
		content += "</tr>";
		content += "<tr>";
		content += "</tr>";
		content += "</table>";
		
		return content;
	}
	
	public enum ReturnStatus {
		C200("Success"),
		C201("처리 되었습니다."),
		C202("조회 되었습니다."),
		C203("등록 되었습니다."),
		C204("삭제 되었습니다."),
		C205("요청 되었습니다."),
		C300("기타 데이터 관련 오류"),
		C301("검색된 데이터가 존재하지 않습니다."),
		C302("처리된 데이터가 존재하지 않습니다."),
		C303("수정된 데이터가 존재하지 않습니다."),
		C304("삭제된 데이터가 존재하지 않습니다."),
		C305("데이터가 이미 존재합니다."),
		C400("데이터베이스 연결 오류 입니다."),
		C401("알수 없는 에러입니다."),
		C402("이미 요청되었습니다."),
		C403("응답이 없습니다."),
		C404("XML Parse 에러.");
		private String msg;
		private ReturnStatus() {}
		private ReturnStatus(String msg) { this.msg = msg; }
		public String getMsg() {
			return msg;
		}
	}
}
 