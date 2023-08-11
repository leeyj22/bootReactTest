import { Validation } from "../func/validation";

// 본인인증
export const Certify = {
    getPageType: () => {},
    openCert: (url) => {
        if (Certify.deviceCheck() == "MOBILE") {
            window.open(url, "popupCert");
        } else {
            const certFrame = document.getElementById("certFrame");
            if (certFrame) {
                certFrame.remove();
            }
            const $iframe = document.createElement("iframe");
            $iframe.src = url;
            $iframe.width = "0";
            $iframe.height = "0";
            $iframe.id = "certFrame";
            document.body.appendChild($iframe);
        }
    },
    // PC, MOBILE 구별
    deviceCheck: () => {
        let device = null;

        const UserAgent = navigator.userAgent;

        // 접속한 디바이스 환경
        if (navigator.platform) {
            if (
                UserAgent.match(
                    /iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i
                ) != null ||
                UserAgent.match(/LG|SAMSUNG|Samsung/) != null
            ) {
                device = "MOBILE";
            } else {
                device = "PC";
            }
        }

        return device;
    },
    openKMCISWindow: (target, result) => {
        if (Validation.isEmpty(target)) {
            console.log("모바일 인증 타겟 없음");
            return;
        }

        if (result == undefined) {
            console.log("모바일 인증 오류");
            alert("잠시뒤 다시 이용하시기 바랍니다.");
            return;
        }

        const formElement = document.getElementsByName(target)[0];
        formElement.elements["tr_cert"].value = result.tr_cert;
        formElement.elements["tr_url"].value = result.tr_url;
        formElement.elements["tr_add"].value = result.tr_add;

        var UserAgent = navigator.userAgent;
        /* 모바일 접근 체크*/
        // 모바일일 경우 (변동사항 있을경우 추가 필요)
        if (
            UserAgent.match(
                /iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i
            ) != null ||
            UserAgent.match(/LG|SAMSUNG|Samsung/) != null
        ) {
            formElement.target = "";
        }

        // 모바일이 아닐 경우
        else {
            const KMCIS_window = window.open(
                "",
                "KMCISWindow",
                "width=425, height=550, resizable=0, scrollbars=no, status=0, titlebar=0, toolbar=0, left=435, top=250"
            );

            if (KMCIS_window == null) {
                alert(
                    " ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다."
                );
            }

            formElement.target = "KMCISWindow";
        }

        formElement.action = "https://www.kmcert.com/kmcis/web/kmcisReq.jsp";
        formElement.submit();
    },
};
