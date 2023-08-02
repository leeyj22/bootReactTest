import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useEffect, useState } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import NoticeService from "../components/service/noticeService";
import Button from "../components/form/button";
import ServiceForm from "../components/service/serviceForm";
import { useSelector } from "react-redux";
import { useSaveBeforePathname } from "../hooks/useSaveBeforePathname";
import { useRouter } from "next/router";
import Cookies from "js-cookie";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import { LOAD_USER_INFO_REQUEST } from "../reducers/user";
//서비스 접수 service1

const Service = () => {
    const router = useRouter();
    useSaveBeforePathname();
    const { loginDone } = useSelector((state) => state.user);
    const [formData, setFormData] = useState({});

    useEffect(() => {
        if (!loginDone) {
            // router.push("/login");
        }
    }, [loginDone]);

    const handleFormChange = (changedData) => {
        setFormData(changedData);
    };
    const checkValidation = (data) => {
        console.log("checkValidation data", data);
        return data;
    };

    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
            {loginDone && (
                <Container>
                    <PageName title="서비스 접수" />

                    {/* 서비스 작성 */}
                    <ServiceForm onFormChange={handleFormChange} />

                    {/* 약관동의 */}
                    <Term
                        allChk="Y"
                        termslist={["policy", "marketing"]}
                        onFormChange={handleFormChange}
                    />

                    {/* 유의사항 */}
                    <NoticeService noticeName="service" />

                    {/* 버튼 */}
                    <Button
                        btnName="service"
                        pos="center"
                        formData={formData}
                        checkValidation={checkValidation}
                    />
                </Container>
            )}
        </AppLayout>
    );
};

export const getServerSideProps = wrapper.getServerSideProps(
    (store) =>
        async ({ req, res, ...data }) => {
            const cookie = req ? req.headers.cookie : "";

            console.log("cookie", cookie);
            axios.defaults.headers.Cookie = "";
            if (req && cookie) {
                axios.defaults.headers.Cookie = cookie;
            }
            store.dispatch({
                type: LOAD_USER_INFO_REQUEST,
            });
            store.dispatch(END);

            await store.sagaTask.toPromise();
        }
);

export default Service;
