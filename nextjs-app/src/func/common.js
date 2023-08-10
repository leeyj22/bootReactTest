export const common = {
    getSesstionStorageCertUser: () => {
        const CertUserName = sessionStorage.getItem("CertUserName");
        const CertPhoneNo = sessionStorage.getItem("CertPhoneNo");
        const CertGender = sessionStorage.getItem("CertGender");
        const CertBirthDay = sessionStorage.getItem("CertBirthDay");

        return {
            CertUserName,
            CertPhoneNo,
            CertGender,
            CertBirthDay,
        };
    },
    fileToSize: (fileSize) => {
        if (fileSize === 0) return "0 바이트";

        const units = ["바이트", "KB", "MB", "GB", "TB"];
        const base = 1024;
        const unitIndex = Math.floor(Math.log(fileSize) / Math.log(base));
        const sizeInUnit = (fileSize / Math.pow(base, unitIndex)).toFixed(2);

        return `${sizeInUnit} ${units[unitIndex]}`;
    },
    toDotNumber: (n) => {
        if (n == undefined) {
            return "";
        }
        const reg = /(^[+-]?\d+)(\d{3})/; // 정규식
        n += ""; // 숫자를 문자열로 변환
        while (reg.test(n)) n = n.replace(reg, "$1" + "," + "$2");
        return n;
    },
    areaChange: (data) => {
        //사용중 페이지 : 이전/설치
        let areaCode = "";
        switch (data) {
            case "서울특별시":
                areaCode = "11";
                break;
            case "부산광역시":
                areaCode = "26";
                break;
            case "대구광역시":
                areaCode = "27";
                break;
            case "인천광역시":
                areaCode = "28";
                break;
            case "광주광역시":
                areaCode = "29";
                break;
            case "대전광역시":
                areaCode = "30";
                break;
            case "울산광역시":
                areaCode = "31";
                break;
            case "세종특별자치시":
                areaCode = "36";
                break;
            case "경기도":
                areaCode = "41";
                break;
            case "강원도":
                areaCode = "42";
                break;
            case "충청북도":
                areaCode = "43";
                break;
            case "충청남도":
                areaCode = "44";
                break;
            case "전라북도":
                areaCode = "45";
                break;
            case "전라남도":
                areaCode = "46";
                break;
            case "경상북도":
                areaCode = "47";
                break;
            case "경상남도":
                areaCode = "48";
                break;
            case "제주특별자치도":
                areaCode = "50";
                break;
            default:
                areaCode = "";
        }
        return areaCode;
    },
};

export const formDataConsoleChk = (FormData) => {
    //폼데이터 콘솔 확인
    FormData.forEach(function (value, key) {
        console.log(key + ": " + value);
    });
};
