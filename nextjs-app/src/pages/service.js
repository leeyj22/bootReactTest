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

//서비스 접수 service1

const Service = () => {
    useSaveBeforePathname();
    const { loginDone } = useSelector((state) => state.user);
    const [formData, setFormData] = useState({});

    useEffect(() => {
        if (!loginDone) {
            location.href = "/login";
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

export default Service;
