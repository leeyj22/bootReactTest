package com.bf.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.bf.common.util.Util.isEmptyOrNull;
import com.bf.common.Consts;
import com.bf.common.Constants;;

@Slf4j
public class UtilManager {
    @Value(value="${system.environment}")
    public static String environment;

    public static String getSystemEnvironment () {
        return environment;
    }

    public static String getNow(String dateFormat){
        if("".equals(dateFormat)){
            return null;
        }
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat);

        return dateformat.format(date);
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
     * @param session 전달 받은 Session
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

}
