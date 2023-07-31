import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useState } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import NoticeService from "../components/service/noticeService";
import Button from "../components/form/button";
import ServiceForm from "../components/service/serviceForm";

//서비스 접수 service1

const Service = () => {
    const [formData, setFormData] = useState({});
    const handleFormChange = (changedData) => {
        setFormData(changedData);
    };
    const checkValidation = (data) => {
        console.log("checkValidation data", data);
        return data;
    };
    const handleSubmit = () => {};
    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
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
                    handleSubmit={handleSubmit}
                />
            </Container>
        </AppLayout>
    );
};

export default Service;
