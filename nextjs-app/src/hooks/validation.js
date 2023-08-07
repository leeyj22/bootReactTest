import React, { useEffect } from "react";
export const Validation = {
    isEmpty: (targetStr) => {
        if (
            targetStr === undefined ||
            targetStr.toString().replace(/\s/g, "") === ""
        ) {
            return true;
        }
        return false;
    },
    onlyNumber: (str) => {
        return /^\d+$/.test(str);
    },
    isChk: (bool) => {
        return bool;
    },
    isEmptyObject: (obj) => {
        return Object.keys(obj).length === 0;
    },
    juminCheck: (str) => {
        //주민번호 정규식
        const regExp1 = /\d{6}-\d{7}/;
        const regExp2 = /\d{13}/;
        if (str.match(regExp1) != null || str.match(regExp2) != null) {
            return true;
        }
        return false;
    },
    passportCheck: (str) => {
        //여권번호 정규식
        const regExp = /^[a-zA-Z]{2}-?([0-9]{7})$/;
        if (str.match(regExp) != null) {
            return true;
        }
        return false;
    },
    licenseCheck: (str) => {
        //운전면허번호 정규식
        const regExp = /^[0-9]{2}-?[0-9]{2}-?([0-9]{6})-?([0-9]{2})$/; //지역번호
        const regExp2 = /^[0-9]{2}-?([0-9]{6})-?([0-9]{2})$/;

        if (str.match(regExp) != null || str.match(regExp2) != null) {
            return true;
        }
        return false;
    },
    healthCheck: (str) => {
        //건강보험번호 정규식
        const regExp = /^[1|2|5|7]-?([0-9]{10})$/;

        if (str.match(regExp) != null) {
            return true;
        }
        return false;
    },
    regexCheck: (str) => {
        //주민,운전,신용카드,여권번호,건강보험 체크
        const exp1 = /\d{6}-\d{7}/;
        const exp2 = /\d{13}/;

        if (str.match(exp1) != null || str.match(exp2) != null) {
            alert("주민등록번호를 입력할 수 없습니다.");
            return true;
        }

        const exp3 = /^[a-zA-Z]{2}-?([0-9]{7})$/;

        if (str.match(exp3) != null) {
            alert("여권번호를 입력할 수 없습니다.");
            return true;
        }

        const exp4 = /^[0-9]{2}-?[0-9]{2}-?([0-9]{6})-?([0-9]{2})$/; //지역번호
        const exp5 = /^[0-9]{2}-?([0-9]{6})-?([0-9]{2})$/;

        if (str.match(exp4) != null || str.match(exp5) != null) {
            alert("운전면허번호를 입력할 수 없습니다.");
            return true;
        }

        /*const exp6 = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;

		if(str.match(exp6) != null){
			alert("핸드폰번호를 입력할 수 없습니다.");
			return true;
		}*/

        const exp7 =
            /^([3|4|5|6|9][0-9]{3})-?([0-9]{4})-?([0-9]{4})-?([0-9]{4})$/;

        if (str.match(exp7) != null) {
            alert("신용카드번호를 입력할 수 없습니다.");
            return true;
        }

        const exp8 = /^[1|2|5|7]-?([0-9]{10})$/;

        if (str.match(exp8) != null) {
            alert("건강보험번호를 입력할 수 없습니다.");
            return true;
        }
        return false;
    },
    emailCheck: () => {
        //이메일 정규식
        //알파벳+숫자@알파벳+숫자.알파벳+숫자 형식
        const regExp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

        if (str.match(regExp) != null) {
            return true;
        }
        return false;
    },
};
