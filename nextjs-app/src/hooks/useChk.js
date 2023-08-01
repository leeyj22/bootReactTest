import { useEffect, useState } from "react";

export const useChkOsDevice = () => {
    const [currentDevice, setCurrentDevice] = useState(null);
    const [currentOS, setCurrentOS] = useState(null);

    useEffect(() => {
        const userAgent = navigator.userAgent.toLowerCase();
        if (/iphone|ipad|ipod|android/i.test(userAgent)) {
            if (userAgent.search("android") > -1) setCurrentOS("android");
            else if (
                userAgent.search("iphone") > -1 ||
                userAgent.search("ipod") > -1 ||
                userAgent.search("ipad") > -1
            )
                setCurrentOS("ios");
            else setCurrentOS("else");
        } else {
            setCurrentOS("nomobile");
        }

        // 접속한 디바이스 환경
        if (navigator.platform) {
            const isMobile = userAgent.match(
                /iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson|LG|SAMSUNG|Samsung/i
            );

            // PC, MOBILE 구별
            setCurrentDevice(isMobile ? "MOBILE" : "PC");
        }
    }, []); // 빈 배열을 넣어 최초 렌더링 시에만 실행되도록 합니다.

    return { currentOS, currentDevice };
};

//모바일 여부 판단
// 리액트 navigator.platform대체하여 사용
export const usePlatformCheck = () => {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const mediaQuery = window.matchMedia("(max-width: 768px)");

        const handleMediaQueryChange = (event) => {
            setIsMobile(event.matches);
        };

        // 초기 설정
        handleMediaQueryChange(mediaQuery);

        mediaQuery.addEventListener("change", handleMediaQueryChange);

        return () => {
            mediaQuery.removeEventListener("change", handleMediaQueryChange);
        };
    }, []);

    console.log("isMobile", isMobile ? "MOBILE" : "PC");

    return isMobile ? "MOBILE" : "PC";
};
