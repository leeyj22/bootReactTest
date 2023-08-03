import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useEffect, useState } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import NoticeService from "../components/service/noticeService";
import Button from "../components/form/button";
import ServiceTransferForm1 from "../components/service/serviceTransferForm1";
import ServiceTransferForm2 from "../components/service/serviceTransferForm2";
import ServiceTransferForm3 from "../components/service/serviceTransferForm3";
import ServiceTransferProgress from "../components/service/serviceTransferProgress";
import { useSelector } from "react-redux";
import { useSaveBeforePathname } from "../hooks/useSaveBeforePathname";
import { useRouter } from "next/router";
import Cookies from "js-cookie";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import {
    CERTIFY_USER_INFO_REQUEST,
    LOAD_USER_INFO_REQUEST,
} from "../reducers/user";
//이전설치 service2

const ServiceTransfer = () => {
    const router = useRouter();
    useSaveBeforePathname();
    const { loginDone, certifyState } = useSelector((state) => state.user);
    const [progress, setProgress] = useState(2);
    const serviceTransferForm = [
        ServiceTransferForm1,
        ServiceTransferForm2,
        ServiceTransferForm3,
    ];
    const CurrentForm = serviceTransferForm[progress - 1];
    const [formData, setFormData] = useState({
        //본인인증 데이터
        tr_cert: "",
        tr_url: "",
        tr_add: "",
        plusInfo: "",
        noListUser: "", //본인인증 사용자 여부 Y,N
        name: "", //고객명
        phoneNo: "", //핸드폰 번호
        certYn: "", //인증여부
    });

    useEffect(() => {
        // 스크립트를 동적으로 생성하여 <head> 태그의 자식으로 추가
        const script = document.createElement("script");
        script.src = "https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js";
        document.head.appendChild(script);
        return () => {
            // 컴포넌트가 언마운트될 때 스크립트 요소를 제거하여 메모리 누수 방지
            document.head.removeChild(script);
        };
    }, []);
    useEffect(() => {
        //회원이 아닌 본인인증의 경우 certifyState true
        //회원일 경우 loginDone true , user 데이터 있음.
        if (!certifyState && !loginDone) {
            // router.push("/login");
        }
    }, [loginDone, certifyState]);

    const handleFormChange = (changedData) => {
        setFormData(changedData);
    };
    const checkValidation = (data) => {
        console.log("checkValidation data", data);
        return data;
    };

    console.log("formData", formData);

    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service2" />
            {/* {(loginDone || certifyState) && ( */}
            <Container>
                <PageName title="이전/설치 접수" />

                <ServiceTransferProgress step="3" progress={progress} />

                {/* 이전설치접수1 */}
                {CurrentForm && <CurrentForm onFormChange={handleFormChange} />}

                {/* 약관동의 */}
                {/* <Term
                    allChk="Y"
                    termslist={["policy", "marketing"]}
                    onFormChange={handleFormChange}
                /> */}

                {/* 유의사항 */}
                {/* <NoticeService noticeName="service" /> */}

                {/* 버튼 */}
                {/* <Button
                    btnName="service"
                    pos="center"
                    formData={formData}
                    checkValidation={checkValidation}
                /> */}
            </Container>
            {/* )} */}
        </AppLayout>
    );
};

export default ServiceTransfer;
