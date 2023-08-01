import React, { useEffect, useState } from "react";
import AppLayout from "../components/AppLayout";
import Breadcrumb from "../components/breadcrumb";
import { Container, LoginContainer } from "../style/AppCommonStyle";
import { useDispatch, useSelector } from "react-redux";
import { LOGIN_URL_REQUEST } from "../reducers/user";
import CertifyCommon from "../components/certify/certify_common";

const login = () => {
    const dispatch = useDispatch();

    const { loginUrlDone, loginUrl } = useSelector((state) => state.user);
    const [startCertify, setStartCertify] = useState(false);

    useEffect(() => {
        if (loginUrlDone) {
            // location.href = loginUrl;
            location.href =
                "https://tauth.bodyfriend.com/auth/common/login?client_id=tbodyfriend&redirect_uri=http://localhost:3000/";
        }
    }, [loginUrlDone]);

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
