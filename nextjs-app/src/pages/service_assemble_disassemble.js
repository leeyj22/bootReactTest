import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useEffect, useState } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import { useSelector } from "react-redux";
import { useSaveBeforePathname } from "../hooks/useSaveBeforePathname";
import { useRouter } from "next/router";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import { CERTIFY_USER_INFO_REQUEST } from "../reducers/user";
import NoticeService from "../components/service/noticeService";
import ServiceProgress from "../components/service/serviceProgress";
import ServiceAssembleDisassemble1 from "../components/service/serviceAssembleDisassemble1";
//이전설치 service2

const ServiceAssembleDisassemble = () => {
    const router = useRouter();
    useSaveBeforePathname();
    const { loginDone, certifyState } = useSelector((state) => state.user);
    const [progress, setProgress] = useState(1);
    const serviceForms = [ServiceAssembleDisassemble1];
    const CurrentForm = serviceForms[progress - 1];
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

    const handlePrev = () => {
        setProgress(progress - 1);
    };

    const handleSubmit = () => {
        let result = false;
        switch (progress) {
            case 1:
                result = checkValidation(formData);
                if (result) {
                    handleFormChange(formData);
                }
                break;
            case 2:
                result = checkValidation(formData);
                if (result) {
                    handleFormChange(formData);
                }
                break;
            case 3:
                result = checkValidation(formData);
                if (result) {
                    handleFormChange(formData);
                }
                break;
            default:
                break;
        }
    };

    console.log("formData", formData);

    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service3" />
            {/* {(loginDone || certifyState) && ( */}
            <Container>
                <PageName title="분해/조립 접수" />

                <ServiceProgress step="3" progress={progress} />

                {/* 분해/조립접수 */}
                {CurrentForm && <CurrentForm onFormChange={handleFormChange} />}
            </Container>
            {/* )} */}
        </AppLayout>
    );
};

export const getServerSideProps = wrapper.getServerSideProps(
    async (context) => {
        const cookie = context.req ? context.req.headers.cookie : "";

        axios.defaults.headers.Cookie = "";
        if (context.req && cookie) {
            axios.defaults.headers.Cookie = cookie;
        }

        // context.store.dispatch({
        //     type: CERTIFY_USER_INFO_REQUEST,
        // });

        context.store.dispatch(END);
        await context.store.sagaTask.toPromise();
    }
);

export default ServiceAssembleDisassemble;
