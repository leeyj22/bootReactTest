package com.bf.common;

import org.springframework.beans.factory.annotation.Value;

public final class Constants {

	public Constants() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static class Code {
		public static final int FAIL = 0;
		public static final int SUCCESS = 1;
		public static final int ERROR = 2;
		public static final int TIMEOUT = 999;
	}

    /** COMPONENT SCAN 대상 PACKAGE **/
    public static final String COMMON_PACKAGE = "com.bf.common";
    public static final String WEB_PACKAGE = "com.bf.web";
    public static final String UNIERP_PACKAGE = "com.bf.unierp";
    public static final String SVC_PACKAGE = "com.bf.svc";
    public static final String CONFIG_PACKAGE = "com.bf.config";
    public static final String SCHEDULER_PACKAGE = "com.bf.scheduler";

    /** MYBATIS 설정 경로 **/
    public static final String MYBATIS_CONFIG_LOCATION = "classpath:sqlmap/sqlmap-config.xml";
    /** MYBATIS MAPPER 경로 **/
    public static final String MYBATIS_MAPPER_LOCATIONS = "classpath:sqlmap/mysql/*.xml";


	public static final String RESULT_DATA = "resultData";
	public static final String RESULT_MSG = "resultMsg";
	public static final String RESULT_CODE = "resultCode";
	
	public static final int TEMP_PASSWD_SIZE = 8;
	
	public static final String SESSION_USER_ID = "user_id";
    public static final String SESSION_USER_IDX = "user_idx";
    public static final String SESSION_LEVEL_CODE = "level_code";
    public static final String SESSION_LEVEL_NAME = "level_name";
    public static final String SESSION_ADDR_1 = "addr_1";
    public static final String SESSION_ADDR_2 = "addr_2";
    public static final String SESSION_NAME = "name";
    public static final String SESSION_EMAIL = "email";
    public static final String SESSION_TOKEN = "token";
    public static final String SESSION_TYPE = "type";
    public static final String SESSION_MEMBER_TYPE = "member_type";
    public static final String SESSION_PHONE = "phone";
    public static final String SESSION_DI = "userDI";
    public static final String SESSION_SKIN_MODE = "skinMode";
    public static final String SESSION_CERT_PHONE = "cert_phone";
    public static final String SESSION_CERT_NAME = "cert_name";
    public static final String SESSION_CERT = "cert_yn";
    
    public final static String UTF_8							= "UTF-8";
	public final static String APPLICATION						= "application";
	public final static String JSON								= "json";
	public final static String FORM_URLENCODED					= "x-www-form-urlencoded";
	
	public static final String FAIL = "0";
	public static final String SUCCESS = "1";
    public static final String ERROR = "2";
    
    public static final int REFRESH_COUNT = 2;
    
    public static final String ORDER_NUMBER = "orderNumber";
    
    // 디바이스
    public static class Device {
        public static final String MOBILE = "mobile";
        public static final String PC = "pc";
    } 
    
    // 사이트 구분
    public static class SiteType {
        public static final String MOBILE = "BM";
        public static final String PC = "B";
    }
    
    // 카테고리
    public final class Category {
        private Category() {}
        
        public static final String BODYFRIEND = "299";
        public static final String LACLOUD = "300";
        public static final String WATER = "301";
        public static final String EVENT = "62";
        
        public final class Bodyfriend {
            public static final String MASSAGE_CHAIR = "302"; 
            public static final String MEDICAL = "303";
            public static final String MASSAGE_BED = "320";
            public static final String HUG = "304"; 
            public static final String SMALL = "305"; 
            public static final String ETC = "306";
            public static final String SALE = "317";
            public static final String REFURB = "332";
            public static final String GLOBAL = "278";
        }
        
        public final class Lacloud {
            public static final String MATTRESS = "307"; 
            public static final String BEDFRAME = "308"; 
            public static final String SOFA = "309"; 
            public static final String BEDDING = "310"; 
            public static final String ETC = "311";
            public static final String SALE = "318";
        }
        
        public final class Water {
            public static final String PURIFIER = "312"; 
            public static final String ETC = "313"; 
            public static final String SALE = "319"; 
        }
    }
    
    // 마케팅 동의 페이지 (marketing_agree_page)
    public final class MarketingPage {
        public static final int SHOWROOM = 1;
        public static final int SERVICE = 2;
        public static final int TRANSFER = 3;
        public static final int ASSEMBLE = 4;
        public static final int ONLINE_SERVICE = 5;
        public static final int SHOPPING = 6;
        public static final int CHAGNE_NAME = 7;
        public static final int RENTAL = 8;
    }
    
    /**
     * View 정의
     */
    public final class View {

        /**
         * 생성자
         */
        private View() {
            // default constructor
        }

        /**
         * JSON
         */
        public static final String JSON_VIEW = "jsonView";

        /**
         * JSON resultData
         */
        public static final String RESULT_DATA_KEY = "resultData";

        /**
         * Create, Modify, Delete 결과 성공여부
         */
        public static final String RESULT_CODE = "resultCode";

        /**
         * Create, Modify, Delete 결과 성공여부
         */
        public static final String RESULT_MESSAGE = "resultMsg";

        /**
         * exception code
         */
        public static final String EXCEPTION_KEY = "exceptionKey";

        public static final String EXCEPTION_VALUE = "exceptionValue";

        /**
         * Excel Download
         */
        public static final String EXCEL_DOWNLOAD_VIEW = "excelDownloadView";

        /**
         * File Download
         */
        public static final String FILE_DOWNLOAD_VIEW = "downloadView";

    }
     
    /**
     * Message 정의
     */
    public final class Message {

        /**
         * 생성자
         */
        private Message() {
            // default constructor
        }

        /**
         * 성공 관련 메세지
         */
        public static final String INSERT_SUCCESS = "등록에 성공 하였습니다";

        public static final String UPDATE_SUCCESS = "수정에 성공 하였습니다";

        public static final String DELETE_SUCCESS = "삭제에 성공 하였습니다.";

        public static final String OUT_SUCCESS = "탈퇴 처리가 완료되었습니다.";
        
        public static final String DIVIDE_SUCCESS = "분배에 성공 하였습니다";
        
        public static final String REQUEST_SUCCESS = "접수에 성공 하였습니다";
        
        public static final String FILEDOWN_SUCCESS = "파일 다운로드에 성공 하였습니다.";

        /**
         * 실패 관련 메세지
         */
        public static final String INSERT_FAIL = "등록에 실패 하였습니다";

        public static final String UPDATE_FAIL = "수정에 실패 하였습니다.";

        public static final String DELETE_FAIL = "삭제에 실패 하였습니다.";

        public static final String OUT_FAIL = "탈퇴에 실패 하였습니다.";
        
        public static final String DIVIDE_FAIL = "분배에 실패 하였습니다";
        
        public static final String REQUEST_FAIL = "접수에 실패 하였습니다";
        
        public static final String FILEDOWN_FAIL = "파일 다운로드에 실패 하였습니다.";
    }
    
    // file
    public final static class File {
        @Value(value="${system.webroot.filePath}")
        public static String filePath;

        private File() { }

        public static final String UPLOAD_PATH = filePath;
    }

}
