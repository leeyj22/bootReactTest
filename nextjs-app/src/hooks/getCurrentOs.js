import { useEffect, useState } from "react";

export const getCurrentOs = () => {
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
    }, []); // 빈 배열을 넣어 최초 렌더링 시에만 실행되도록 합니다.

    return currentOS;
};
