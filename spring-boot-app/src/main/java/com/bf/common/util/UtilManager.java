package com.bf.common.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bf.common.Constants;
import com.bf.common.Consts;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class UtilManager {

    public static String aes256Key;
    @Value(value = "aes256.key")
    public void setAes256Key(String value){
        this.aes256Key = value;
    }

    public static String queryStr(String[] var){
        String res = "";

        try{
            StringBuilder sb = new StringBuilder();
            for(String s : var){
                sb.append(s);
            }

            res = sb.toString();
        } catch (Exception e){
            e.getMessage();
        }

        return res;
    }

    public static boolean checkQueryParam(Object var, Map paramMap){
        boolean res = false;
        try{
            if(var == null || paramMap == null) return res;

            if(paramMap.containsKey(var) && null != paramMap.get(var)){
                res = true;
            }

        } catch (Exception e){
            e.getMessage();
        }

        return res;
    }

    public  static Properties loadProperties(String path) {
        Properties properties = new Properties();
        try {
            properties.load(UtilManager.class.getClassLoader().getResourceAsStream(path));
        } catch (FileNotFoundException e) {
            log.error("Properties file not found(" + path + ")");
            return null;
        } catch (IOException e) {
            log.error("Properties file load failure(" + path + ") : " + e.getMessage());
            return null;
        }

        return properties;
    }


    /**
     * SimpleDateFormat 파라미터에 의한 현재 년/월/일/시/분/초 정보 리턴
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getNow(String dateFormat){

        if("".equals(dateFormat)){
            return null;
        }

        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat);

        return dateformat.format(date);
    }

    public static String getCvtDateFormat(String format, String date){

        String res = "";

        try {
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date cvtDate = targetFormat.parse(date);

            DateFormat dateFormat = new SimpleDateFormat(format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(cvtDate);

            res = dateFormat.format(cal.getTime());
//			res = dateFormat.format(cvtDate);
        } catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }
    /**
     * 요청한 Interval에 대한 이전 분정보 리턴
     *
     * @param interval
     * @param format : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getInterValNow(String targetDate, int interval, String format){
        if(interval < 0 || null == targetDate || null == format) return null;

        String resStr 			= "";
        Date date 				= null;
        DateFormat dateFormat 	= new SimpleDateFormat(format);

        try{
            date = dateFormat.parse(targetDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, - interval);

            resStr = dateFormat.format(cal.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        return resStr;
    }

    /**
     * 요청 일자에 대한 요일 리턴
     * yyyy-MM-dd HH:mm:ss
     * 1 : 일요일 ~ 7 : 토요일
     * @param date
     * @return
     */
    public static int getWeekDay(String date, String dateFormat){
        int res = 0;

        try{
            SimpleDateFormat cvtDateFormat = new SimpleDateFormat(dateFormat);
            Date cvtDate = cvtDateFormat.parse(date);

            Calendar cal= Calendar.getInstance();
            cal.setTime(cvtDate);

            res = cal.get(Calendar.DAY_OF_WEEK);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String dateFormat(Date date, String format) {
        if (isEmptyOrNull(date)) {
            date = new Date();
        }
        if (isEmptyOrNull(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    public static Date dateAdd(Date date, String type, int addValue) {
        if (isEmptyOrNull(date)) {
            date = new Date();
        }

        if (isEmptyOrNull(type)) {
            type = "DATE";
        }

        if (isEmptyOrNull(addValue)) {
            addValue = 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (type) {
            case "MINUTE" :
                calendar.add(Calendar.MINUTE, addValue);
                break;
            case "HOUR" :
                calendar.add(Calendar.HOUR_OF_DAY, addValue);
                break;
            case "DATE" :
                calendar.add(Calendar.DATE, addValue);
                break;
            case "MONTH" :
                calendar.add(Calendar.MONTH, addValue);
                break;
            case "YEAR" :
                calendar.add(Calendar.YEAR, addValue);
                break;
            default :
                calendar.add(Calendar.DATE, addValue);
                break;
        }

        return calendar.getTime();
    }

    public static String dateAdd(boolean plusType, int handleDate, String dateformatStr) {
        if (isEmptyOrNull(plusType)) {
            plusType = true;
        }
        if (isEmptyOrNull(handleDate)) {
            handleDate = 0;
        }

        if (isEmptyOrNull(dateformatStr)) {
            dateformatStr = "yyyyMMdd";
        }

        String date 				= null;
        SimpleDateFormat dateformat = new SimpleDateFormat(dateformatStr);
        Date today 					= new Date();

        Calendar cal 				= Calendar.getInstance();
        cal.setTime(today);

        if (plusType) {
            cal.add(Calendar.DATE, + handleDate);
        } else {
            cal.add(Calendar.DATE, - handleDate);
        }

        date 						= dateformat.format(cal.getTime());

        return date;
    }

    /**
     * 객체가 null인 경우 ""을 반환
     */
    public static Object nullToEmpty(Object obj)
    {
        //		if (obj == null) return "";
        return obj == null ? "" : obj;
    }

    /**
     * 객체가 null인 경우 "0"을 반환 -
     */
    public static Object nullToZero(Object obj)
    {
        return obj == null ? "0" : obj;
    }

    /**
     * null or "" check
     * @param value
     * @return
     */
    public static boolean isEmptyOrNull(Object value) {
        if (value == null) {
            return true;
        }
        return value.toString().trim().isEmpty();
    }

    /**
     * 마스킹 처리
     * @param str
     * @return
     */
    public static String setMaskProcess(String str, String div) {
        String maskStr = str;
        int strLen = str.length();
        if (strLen > 4 && div != "tax") {
            maskStr =  (str.substring(0, strLen - 4)).replaceAll("[0-9]", "*") + str.substring(strLen - 4);
        }

        if (div == "tax") {
            int divLen = strLen/3;
            maskStr = str.substring(0, divLen) + (str.substring(divLen, divLen+divLen+1)).replaceAll("[0-9]", "*") + str.substring(divLen+divLen+1);
        }

        if (div == "id") {
            if (strLen < 5) {
                maskStr = str.substring(0, strLen - 1) +  "*";
            } else {
                maskStr = str.substring(0, strLen - 4) + "****";
            }
        }

        if (div == "name") {
            maskStr = UtilManager._nameMasking(str);
        }

        if(div == "tel"){
            maskStr = UtilManager._phoneMasking(str);
        }

        return maskStr;
    }

    /**
     * <pre>
     * 1. 개요:	GET|DELETE 방식의 파라미터를 이용하여 필수 파라미터 여부 확인
     * 2. 처리내용: ServletRequest를 이용한 파라미터 존재 여부 확인
     * </pre>
     * @Method Name : checkMandantoryWithReturn
     * @param request 전달 받은 REQUEST 객체
     * @param list 필수로 존재해야 하는 파라미터 목록
     * @return 필수 파라미터 유효성 여부 + 파라미터 명
     */
    public static Map checkMandantoryWithReturn (HttpServletRequest request, String[] list) {

        Map<String, Object> checkValue = new HashMap<String, Object>();

        for (String item : list) {
            Object obj = request.getParameter(item);
            if (obj == null) {
                // 파라미터가 존재하는지 확인한다.
                checkValue.put("check", false);
                checkValue.put("key", item);
                return checkValue;
            }
            if (obj instanceof String) {
                if (isEmptyOrNull(obj.toString())) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else if (obj instanceof Integer) {
            } else if (obj instanceof List) {
                if (((List) obj).size() == 0) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else if (obj instanceof Map) {
                if (((Map)obj).isEmpty()) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else {
                log.info("해석할 수 없는 파라미터 값 유형입니다. ");
                checkValue.put("check", false);
                checkValue.put("key", item);
                return checkValue;
            }

        }
        checkValue.put("check", true);
        return checkValue;
    }

    /**
     * <pre>
     * 1. 개요:	POST|PUT 방식의 파라미터를 이용하여 필수 파라미터 여부 확인
     * 2. 처리내용: RequestBody의 자동 변환 기능을 이용한 Map객체 에서 파라미터 존재 여부 확인
     * </pre>
     * @Method Name : checkMandantoryWithReturn
     * @param paramMap 전달 받은 파라미터 객체 정보
     * @param list 필수로 존재해야 하는 파라미터 목록
     * @return 필수 파라미터 유효성 여부 + 파라미터 명
     */
    public static Map checkMandantoryWithReturn (Map paramMap, String[] list) {

        Map<String, Object> checkValue = new HashMap<String, Object>();

        if (null == paramMap) {
            checkValue.put("check", false);
            return checkValue;
        }
        for(String item : list) {
            if (!paramMap.containsKey(item)) {
                checkValue.put("check", false);
                checkValue.put("key", item);
                return checkValue;
            } else {
                Object obj = paramMap.get(item);
                if (obj instanceof String) {
                    if (isEmptyOrNull(obj.toString())) {
                        checkValue.put("check", false);
                        checkValue.put("key", item);
                        return checkValue;
                    }
                } else if (obj instanceof Integer) {
                } else if (obj instanceof List) {
                    if (((List) obj).size() == 0) {
                        checkValue.put("check", false);
                        checkValue.put("key", item);
                        return checkValue;
                    }
                } else if (obj instanceof Map) {
                    if (((Map)obj).isEmpty()) {
                        checkValue.put("check", false);
                        checkValue.put("key", item);
                        return checkValue;
                    }
                } else {
                    log.info("해석할 수 없는 파라미터 값 유형입니다. ");
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            }
        }
        checkValue.put("check", true);
        return checkValue;
    }

    /**
     * <pre>
     * 1. 개요:	GET|DELETE 방식의 파라미터를 이용하여 필수 파라미터 여부 확인
     * 2. 처리내용: ServletRequest를 이용한 파라미터 존재 여부 확인
     * </pre>
     * @Method Name : checkMandantoryWithReturn
     * @param session 전달 받은 SESSION 객체
     * @param list 필수로 존재해야 하는 파라미터 목록
     * @return 필수 파라미터 유효성 여부 + 파라미터 명
     */
    public static Map checkMandantoryWithReturn (HttpSession session, String[] list) {

        Map<String, Object> checkValue = new HashMap<String, Object>();

        if (null == session) {
            checkValue.put("check", false);
            return checkValue;
        }

        for (String item : list) {
            Object obj = session.getAttribute(item);
            if (obj == null) {
                // 파라미터가 존재하는지 확인한다.
                checkValue.put("check", false);
                checkValue.put("key", item);
                return checkValue;
            }
            if (obj instanceof String) {
                if (isEmptyOrNull(obj.toString())) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else if (obj instanceof Integer) {
            } else if (obj instanceof List) {
                if (((List) obj).size() == 0) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else if (obj instanceof Map) {
                if (((Map)obj).isEmpty()) {
                    checkValue.put("check", false);
                    checkValue.put("key", item);
                    return checkValue;
                }
            } else {
                log.info("해석할 수 없는 파라미터 값 유형입니다. ");
                checkValue.put("check", false);
                checkValue.put("key", item);
                return checkValue;
            }

        }
        checkValue.put("check", true);
        return checkValue;
    }

    public static int getLastDay(String date, String dateFormat) throws Exception {
        SimpleDateFormat cvtDateFormat = new SimpleDateFormat(dateFormat);
        Date cvtDate = cvtDateFormat.parse(date);

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(cvtDate);

        return  cal.getActualMaximum(Calendar.DATE);
    }

    public static String getLastDate(String date, String dateFormat) throws Exception {
        int lastDay = getLastDay(date, dateFormat);
        return String.format("%s-%02d", date.substring(0,7));
    }

    public static synchronized String getyyyyMMddHHmmss(){
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }



    /**
     * IP
     * @param request
     * @return
     * @throws Exception
     */
    public static String ipaddr(HttpServletRequest request) throws Exception {
        // IP 가져오기
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * Html 태그 적용및 개행 처리
     *
     * @param src 원본 문자열
     * @return 처리 후 문자열
     */
    public static String unescape(String src) {
        if (src == null) {
            return null;
        }

        src = src.replaceAll("&amp;", "&");
        src = src.replaceAll("&#38;", "&");
        src = src.replaceAll("&quot;", "\"");
        src = src.replaceAll("&lt;", "<");
        src = src.replaceAll("&gt;", ">");
        src = src.replaceAll("&#40;", "(").replaceAll("&#41;", ")");

        return src;
    }

    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public static JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }

        return jsonObject;
    }

    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    public static JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, Object> map : list ) {
            jsonArray.put( getJsonStringFromMap( map ) );
        }

        return jsonArray;
    }

    /**
     * JSON String To JSONObject
     */
    public static JSONObject getJsonFromJsonString (String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (Exception e) {}
        return jsonObject;
    }

    /**
     * XML String To JSONObject
     */
    public static JSONObject getJsonFromXmlString (String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = XML.toJSONObject(str);
        } catch (Exception e) {}
        return jsonObject;
    }

    public static String getRandomString(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 비밀번호 랜덤 생성
    public static String getRamdomPassword(int len) {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        int idx = 0;

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i++) {
            idx = (int) (charSet.length * Math.random()); // 36 * 생성된 난수를 Int로 추출 (소숫점제거)
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }

    /**
     * 두 날짜와의 개월수 차이 구하기
     * yyyy-MM-DD -> yyyyMMdd
     */
    public static String diffDate(String paramMonth, String paramMonth2) throws ParseException {
        // 무상as종료 날짜 - 현재날짜
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyyMMdd");

        Date date1 = sdf.parse(paramMonth);
        Date date2 = sdf.parse(paramMonth2);
        String szSDate = sdf2.format(date1);
        String szEDate = sdf2.format(date2);
        int sYear= Integer.parseInt(szSDate.substring(0,4));
        int sMonth = Integer.parseInt(szSDate.substring(4,6));
        int eYear = Integer.parseInt(szEDate.substring(0,4));
        int eMonth = Integer.parseInt(szEDate.substring(4,6));
        int month_diff = (eYear - sYear)* 12 + (eMonth - sMonth);

        String returnMonth = Integer.toString(Math.abs(month_diff));
        return returnMonth;
    }

    public static Boolean paramChk (String patn, String param) {
        boolean b = true;
        try {
            Pattern pattern = Pattern.compile(patn);
            Matcher matcher = pattern.matcher(param);
            b = matcher.matches();
        } catch (PatternSyntaxException e) {
        }

        return b;
    }

    public static String readJSONStringFromRequestBody(HttpServletRequest request){
        StringBuffer json = new StringBuffer();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }

        }catch(Exception e) {
            log.error("Error reading JSON string: " + e.toString());
        }
        return json.toString();
    }

    public static String[] parsePhone(String phoneNumber){
        String[] phones = new String[3];
        int mid 		= phoneNumber.length() == 10 ? 6 : 7;
        phones[0] = phoneNumber.substring(0, 3);
        phones[1] = phoneNumber.substring(3, mid);
        phones[2] = phoneNumber.substring(mid, phoneNumber.length());
        return phones;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static void setHandshake() throws NoSuchAlgorithmException, KeyManagementException {
        // 1.인증서 체크과정을 무시하고 임의의 인증서를 생성하여 통신
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
                // TODO Auto-generated method stub
            }
        } };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // 2.SSL증명서 에러 무시 (서버 호스트 이름이 다를 경우 무시)
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }

    public static Map<String,Object> getMapFromRequest(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();

        Enumeration<String> enumber = request.getParameterNames();

        while (enumber.hasMoreElements()) {
            String key = enumber.nextElement().toString();
            String value = request.getParameter(key);

            map.put(key, value);
        }

        return map;
    }

    /**
     * 알림톡 전송에 쓰이는 스트링 복호화
     * @param encStr
     * @return
     * @throws Exception
     */
    public static String decAES(String encStr) throws Exception {
        Decoder decoder = Base64.getDecoder();
        AES256Util aes = new AES256Util(aes256Key); // 변경시 알림톡 전송 부분도 변경 필요
        String str = aes.dec(new String(decoder.decode(encStr.getBytes())));
        return str;
    }

    /**
     * 알림톡 전송에 쓰이는 스트링 암호화
     * @param str
     * @return
     * @throws Exception
     */
    public static String encAES(String str) throws Exception {
        AES256Util aes = new AES256Util(aes256Key);
        Encoder encoder = Base64.getEncoder();
        String encStr = new String(encoder.encode(aes.enc(str).getBytes()));
        return encStr;
    }

    /**
     * 본인 인증 또는 로그인 여부 확인
     * @param session
     * @return
     */
    public static Map checkCertOrLogin (HttpSession session) {
        Map<String, Object> checkValue = new HashMap<String, Object>();

        if (null == session) {
            checkValue.put(Consts.CHECK, false);
            return checkValue;
        }

        int cert = 0;
        String userName = "";
        String phoneNo = "";

        // 본인인증 확인
        Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_CERT, Constants.SESSION_CERT_NAME, Constants.SESSION_CERT_PHONE});
        if ((boolean) paramsCheck.get(Consts.CHECK) && session.getAttribute(Constants.SESSION_CERT).equals("Y")) {
            cert += 1;
            userName = session.getAttribute(Constants.SESSION_CERT_NAME).toString();
            phoneNo = session.getAttribute(Constants.SESSION_CERT_PHONE).toString();
        }

        // 로그인
        paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX, Constants.SESSION_NAME, Constants.SESSION_PHONE});
        if ((boolean) paramsCheck.get(Consts.CHECK)) {
            cert += 1;
            userName = session.getAttribute(Constants.SESSION_NAME).toString();
            phoneNo = session.getAttribute(Constants.SESSION_PHONE).toString();
        }

        if (cert > 0) {
            checkValue.put(Consts.CHECK, true);
            checkValue.put(Consts.USER_NAME, userName);
            checkValue.put(Consts.USER_PHONE, phoneNo);
        } else {
            checkValue.put(Consts.CHECK, false);
        }

        return checkValue;
    }

    /**
     * 디바이스 확인
     * @param request
     * @return
     */
    public static String getDevice(HttpServletRequest request) {
        String IS_MOBILE = "MOBI";
        String userAgent = request.getHeader("User-Agent").toUpperCase();

        if(userAgent.indexOf(IS_MOBILE) > -1) {
            return Constants.Device.MOBILE;
        } else {
            return Constants.Device.PC;
        }
    }

    /**
     * 사이트 구분 확인
     * @param request
     * @return
     */
    public static String getSiteType(HttpServletRequest request) {
        String device = UtilManager.getDevice(request);
        String siteType = "";

        if (device.equals(Constants.Device.MOBILE)) {
            siteType = Constants.SiteType.MOBILE;
        } else {
            siteType = Constants.SiteType.PC;
        }

        return siteType;
    }

    /**
     * 이름 마스킹
     * @param name
     * @return
     */
    private static String _nameMasking(String name) {
        // 한글만 (영어, 숫자 포함 이름은 제외)
        String regex = "(^[가-힣]+)$";

        Matcher matcher = Pattern.compile(regex).matcher(name);
        if(matcher.find()) {
            int length = name.length();

            String middleMask = "";
            if(length > 2) {
                middleMask = name.substring(1, length - 1);
            } else {    // 이름이 외자
                middleMask = name.substring(1, length);
            }

            String dot = "";
            for(int i = 0; i<middleMask.length(); i++) {
                dot += "*";
            }

            if(length > 2) {
                return name.substring(0, 1)
                        + middleMask.replace(middleMask, dot)
                        + name.substring(length-1, length);
            } else { // 이름이 외자 마스킹 리턴
                return name.substring(0, 1)
                        + middleMask.replace(middleMask, dot);
            }
        }
        return name;
    }

    /**
     * 전회번호 마스킹
     * @param phoneNo
     * @return
     */
    private static String _phoneMasking(String phoneNo) {
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

        Matcher matcher = Pattern.compile(regex).matcher(phoneNo);
        if(matcher.find()) {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return phoneNo.replace(target, String.valueOf(c));
        }
        return phoneNo;
    }

    public static Map<String, String> getQueryMap(String query) {
        if (query==null) return null;

        int pos1=query.indexOf("?");
        if (pos1>=0) {
            query=query.substring(pos1+1);
        }

        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /**
     * 사용자 검증
     * @param session
     * @param name
     * @return
     */
    public static boolean verifyMember(HttpSession session, String name) {
        return session.getAttribute(Constants.SESSION_NAME).equals(name);
    }

    /**
     * 사용자 검증
     * @param session
     * @param name
     * @param phone
     * @return
     */
    public static boolean verifyMember(HttpSession session, String name, String phone) {
        return session.getAttribute(Constants.SESSION_NAME).equals(name)
                && session.getAttribute(Constants.SESSION_PHONE).equals(phone);
    }
}
