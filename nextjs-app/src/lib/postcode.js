export const PostCode = {
    currentOS: null,
    callback: null,
    _popOpt: null,
    init: (callback) => {
        window.addEventListener("message", PostCode.handleMessage);

        PostCode.getCurrentOs();
        if (PostCode.currentOS === "nomobile") {
            // pc
            PostCode._popOpt =
                "width=570,height=420, scrollbars=yes, resizable=yes";
        } else {
            // mobile
            PostCode._popOpt = "scrollbars=yes, resizable=yes";
        }
        PostCode.callback = callback;
        PostCode.searchPostcode();
    },
    handleMessage: (e) => {
        if (e.origin == "https://api.bodyfriend.co.kr") {
            PostCode.jusoCallBack(e.data);
        }
    },
    searchPostcode: () => {
        window.open(
            "https://api.bodyfriend.co.kr/address/v2/juso?currentOS=" +
                PostCode.currentOS,
            "pop",
            PostCode._popOpt
        );
    },
    jusoCallBack: (data) => {
        if (PostCode.callback != void 0) {
            PostCode.callback != void 0 && PostCode.callback(data);
        }
    },
    getCurrentOs: () => {
        const broswerInfo = navigator.userAgent,
            userAgent = broswerInfo.toLowerCase(),
            mobile = /iphone|ipad|ipod|android/i.test(userAgent);

        if (mobile) {
            // 유저에이전트를 불러와서 OS를 구분합니다.
            if (userAgent.search("android") > -1) this.currentOS = "android";
            else if (
                userAgent.search("iphone") > -1 ||
                userAgent.search("ipod") > -1 ||
                userAgent.search("ipad") > -1
            )
                PostCode.currentOS = "ios";
            else PostCode.currentOS = "else";
        } else {
            // 모바일이 아닐 때
            PostCode.currentOS = "nomobile";
        }
    },
};
