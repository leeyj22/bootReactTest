import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import AppLayout from "../components/AppLayout";
import Breadcrumb from "../components/breadcrumb";
import { Container, LoginContainer } from "../style/AppCommonStyle";
import { useDispatch, useSelector } from "react-redux";
import {
    CERTIFY_SAVE_LOCALSTOREGE_REQUEST,
    LOGIN_URL_REQUEST,
} from "../reducers/user";
import CertifyCommon from "../components/certify/certify_common";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import Cookies from "js-cookie";

const login = () => {
    const dispatch = useDispatch();
    const router = useRouter();

    const { loginUrlDone, loginUrl, certifyInfo, loginDone } = useSelector(
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
                // 팝업 창으로부터 받은 데이터를 사용
                if (
                    event.data.certifyInfoState !== undefined &&
                    event.data.certifyInfoState
                ) {
                    //object 상태 다시 저장
                    console.log("event.data", event.data);
                    dispatch({
                        type: CERTIFY_SAVE_LOCALSTOREGE_REQUEST,
                        data: event.data,
                    });
                }
            }
        };

        window.addEventListener("message", getCertifyResult);

        return () => {
            window.removeEventListener("message", getCertifyResult);
        };
    }, []);

    useEffect(() => {
        if (certifyInfo !== null && certifyInfo !== undefined && loginDone) {
            // 팝업 창으로부터 메시지를 받는 이벤트 리스너
            console.log("login!! certifyInfo", certifyInfo);
            //기존 데이터 삭제
            sessionStorage.removeItem("CertUserName");
            sessionStorage.removeItem("CertPhoneNo");
            sessionStorage.removeItem("CertGender");
            sessionStorage.removeItem("CertBirthDay");
            sessionStorage.removeItem("CertUserDI");

            //본인인증 데이터 저장
            sessionStorage.setItem("CertUserDI", certifyInfo.userDI);
            sessionStorage.setItem("CertUserName", certifyInfo.name);
            sessionStorage.setItem("CertPhoneNo", certifyInfo.phoneNo);
            sessionStorage.setItem(
                "CertGender",
                certifyInfo.gender == "1" ? 0 : 1
            );
            // 본인인증시 : 여자 1, 남자0 들어옴.
            // 신용평가,렌탈가능여부 : 여자0, 남자1 사용해서 변환.(저장)
            sessionStorage.setItem("CertBirthDay", certifyInfo.birthDay);

            //기존 URL 이동
            const beforeUrl = sessionStorage.getItem("beforeUrl");

            console.log("certifyInfo.userDI", certifyInfo.userDI);

            //쿠키저장
            Cookies.set("CertUserDI", certifyInfo.userDI, {
                expires: 0.5,
                path: `"${beforeUrl}"`,
            });
            console.log("beforeUrl", beforeUrl);
            if (beforeUrl == null) {
                router.push("/");
            } else {
                router.push(beforeUrl);
            }
        }
    }, [certifyInfo !== null && certifyInfo !== undefined, loginDone == true]);

    //통합 로그인 이동
    const handleLinkLogin = () => {
        dispatch({
            type: LOGIN_URL_REQUEST,
        });
    };

    //본인인증 이동
    const handleLinkCertify = () => {
        setStartCertify(true);
    };
    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
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
                {startCertify && <CertifyCommon />}
            </Container>
        </AppLayout>
    );
};

export default login;
