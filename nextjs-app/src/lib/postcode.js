import React, { useEffect, useState } from "react";

export const PostCode = () => {
    let _popOpt = "";
    const [postData, setPostData] = useState({});
    const [currentOS, setCurrentOS] = useState(null);

    const jusoCallBack = (data) => {
        setPostData(data);
    };
    const handleMessage = (e) => {
        console.log("e.origin", e.origin);
        if (e.origin === "https://api.bodyfriend.co.kr") {
            jusoCallBack(e.data);
        }
    };

    const searchPostcode = (visibleState) => {
        visibleState &&
            window.open(
                "https://api.bodyfriend.co.kr/address/v2/juso?currentOS=" +
                    currentOS,
                "pop",
                _popOpt
            );
    };

    useEffect(() => {
        // OS 판별
        const broswerInfo = navigator.userAgent;
        const userAgent = broswerInfo.toLowerCase();
        const mobile = /iphone|ipad|ipod|android/i.test(userAgent);

        if (mobile) {
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

        // 팝업 옵션 설정
        if (currentOS === "nomobile") {
            // pc
            _popOpt = "width=570,height=420, scrollbars=yes, resizable=yes";
        } else {
            // mobile
            _popOpt = "scrollbars=yes, resizable=yes";
        }

        // 이벤트 리스너 등록
        window.addEventListener("message", handleMessage);

        searchPostcode();

        // 컴포넌트 언마운트 시 이벤트 리스너 해제
        return () => {
            window.removeEventListener("message", handleMessage);
        };
    }, [currentOS]); // currentOS가 변경될 때마다 useEffect가 호출되도록 설정

    return { postData, searchPostcode };
};
