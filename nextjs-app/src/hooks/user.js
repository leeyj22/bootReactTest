/*
    user hooks
 * user 공통
 * login : 통합로그인.
 * logout : 통합 로그아웃
 * joinPage : 회원가입
 */

export const user = {
    login: function (returnUrl) {
        if (returnUrl == void 0) {
            returnUrl = window.location.href;
        }
        //통합로그인
        location.href = "/member/login?returnUrl=" + returnUrl;
    },
    logout: function () {
        localStorage.clear();
        user.deleteCookie();
        location.href = "/member/logout";
    },
    deleteCookie: function () {
        var expdate = new Date();
        expdate.setDate(expdate.getDate() - 1);
        document.cookie =
            "bodyfriend_saveid= " +
            "; expires=" +
            expdate.toGMTString() +
            "; path=/";
        document.cookie =
            "bodyfriend_autoid= " +
            "; expires=" +
            expdate.toGMTString() +
            "; path=/";
        document.cookie =
            "bodyfriend_autopw= " +
            "; expires=" +
            expdate.toGMTString() +
            "; path=/";
    },
    joinPage: function () {
        var filter = "win16|win32|win64|macintel|mac|";
        location.href = "/member/joinSignup";
    },
};
