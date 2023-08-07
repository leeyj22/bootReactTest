import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import AppLayout from "../components/AppLayout";
import Breadcrumb from "../components/breadcrumb";
import { Container, LoginContainer } from "../style/AppCommonStyle";
import { useDispatch, useSelector } from "react-redux";
import { CERTIFY_SAVE_REQUEST, LOGIN_URL_REQUEST } from "../reducers/user";
import CertifyCommon from "../components/certify/certify_common";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import Cookies from "js-cookie";

const login = () => {
    const dispatch = useDispatch();
    const router = useRouter();

    const { loginUrlDone, certifyInfo, certifyState } = useSelector(
        (state) => state.user
    );
    const [startCertify, setStartCertify] = useState(false);

    useEffect(() => {
        if (loginUrlDone) {
            // location.href = loginUrl;
            location.href =
                "https://tauth.bodyfriend.com/auth/common/login?client_id=tbodyfriend&redirect_uri=http://localhost:3000/";
        }
    }, [loginUrlDone]);

    useEffect(() => {
        const getCertifyResult = (event) => {
            if (event.origin === window.location.origin) {
                //본인인증 완료 후 이벤트 동작.
                if (
                    event.data.certifyInfoState !== undefined &&
                    event.data.certifyInfoState
                ) {
                    console.log("event.data", event.data);
                    //본인인증 데이터 저장 요청
                    dispatch({
                        type: CERTIFY_SAVE_REQUEST,
                        data: event.data,
                    });
                }
            }
        };

        // 팝업 창으로부터 메시지를 받는 이벤트 리스너
        window.addEventListener("message", getCertifyResult);

        return () => {
            window.removeEventListener("message", getCertifyResult);
        };
    }, []);

    useEffect(() => {
        if (certifyInfo !== null && certifyInfo !== undefined && certifyState) {
            //기존 데이터 삭제
            sessionStorage.removeItem("CertUserName");
            sessionStorage.removeItem("CertPhoneNo");
            sessionStorage.removeItem("CertGender");
            sessionStorage.removeItem("CertBirthDay");
            //기존 쿠키 삭제
            Cookies.remove("cert_user_di");
            //본인인증 데이터 저장
            sessionStorage.setItem("CertUserName", certifyInfo.name);
            sessionStorage.setItem("CertPhoneNo", certifyInfo.phoneNo);
            sessionStorage.setItem(
                "CertGender",
                certifyInfo.gender == "1" ? 0 : 1
            );
            // 본인인증시 : 여자 1, 남자0 들어옴.
            // 홈페이지저장은 :여자 0 남자 1
            sessionStorage.setItem("CertBirthDay", certifyInfo.birthDay);

            //기존 URL 이동
            const beforeUrl = sessionStorage.getItem("beforeUrl");

            console.log("certifyInfo", certifyInfo);
            console.log("certifyInfo.userDI", certifyInfo.userDI);

            //쿠키저장
            Cookies.set("cert_user_di", certifyInfo.userDI, {
                expires: 0.5,
                path: `"${beforeUrl}"`,
            });

            //이전 페이지 이동
            console.log("beforeUrl", beforeUrl);
            if (beforeUrl == null || beforeUrl == undefined) {
                router.push("/");
            } else {
                router.push(beforeUrl);
            }
        }
    }, [certifyInfo !== null && certifyInfo !== undefined, certifyState]);

    //통합 로그인 이동
    const handleLinkLogin = () => {
        dispatch({
            type: LOGIN_URL_REQUEST,
        });
    };

    //본인인증 이동
    const handleLinkCertify = () => {
        console.log("startCertify", startCertify);
        setStartCertify(true);
    };

    return (
        <AppLayout>
            <Container>
                <LoginContainer>
                    <div>
                        <strong>회원</strong>
                        <p>
                            회원으로 로그인 하시면
                            <br />
                            마이페이지에서 다양한 정보를
                            <br />
                            확인하실 수 있습니다.
                        </p>
                        <button onClick={handleLinkLogin} customer="Y">
                            회원 로그인
                        </button>
                    </div>
                    <div>
                        <strong>비회원</strong>
                        <p>
                            본인인증 후 서비스 이용이 가능하며, <br />
                            접수 이력은 회원으로 로그인 하셔야 <br />
                            확인하실 수 있습니다.
                        </p>
                        <button onClick={handleLinkCertify} customer="N">
                            비회원 로그인
                        </button>
                    </div>
                </LoginContainer>
                {/* ▼▼▼ 본인인증 컴포넌트 ▼▼▼ */}
                {startCertify && <CertifyCommon />}
            </Container>
        </AppLayout>
    );
};

export default login;
