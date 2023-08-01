import { useEffect, useState } from "react";

export const Certify = {
    // 본인인증
    openCert: (url) => {
        if (Certify.deviceCheck() == "MOBILE") {
            console.log("모바일!");
            window.open(url, "popupCert");
        } else {
            console.log("피씨!");
            // const certFrame = document.getElementById("certFrame");
            // if (certFrame) {
            //     certFrame.remove();
            // }
            // const $iframe = document.createElement("iframe");
            // $iframe.src = url;
            // $iframe.width = "0";
            // $iframe.height = "0";
            // $iframe.id = "certFrame";
            // document.body.appendChild($iframe);
            window.open(url, "popupCert");
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
    callbackReturnUrl: () => {},
};
