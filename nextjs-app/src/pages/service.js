import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useEffect, useState, useCallback } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import NoticeService from "../components/service/noticeService";
import Button from "../components/form/button";
import ServiceForm from "../components/service/serviceForm";
import { useDispatch, useSelector } from "react-redux";
import { useSaveBeforePathname } from "../hooks/useSaveBeforePathname";
import { useRouter } from "next/router";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import { CERTIFY_USER_INFO_REQUEST } from "../reducers/user";
import { Validation } from "../hooks/validation";
import { SUBMIT_SERVICE_REQUEST } from "../reducers/service";
//서비스 접수 service1

const Service = () => {
    const router = useRouter();
    const dispatch = useDispatch();
    useSaveBeforePathname();
    const { loginDone, certifyState, certifyStateError } = useSelector(
        (state) => state.user
    );
    const [formData, setFormData] = useState({
        userIdx: "",
        userId: "",
        name: "",
        custCode: "", //제품 코드
        prdtName: "", // 제품명
        prdtCate: "0", //제품 카테고리
        zip: "",
        addr1: "",
        addr2: "",
        contact: "",
        serviceGroup: "", //서비스 유형
        title: "",
        content: "",
        prdtShop: "", //제품 구매처
        ex_filename: [],
    });

    useEffect(() => {
        //회원이 아닌 본인인증의 경우 certifyState true
        //회원일 경우 loginDone true , user 데이터 있음.
        // if (!certifyState && !loginDone) {
        const certifyTimer = setTimeout(() => {
            if (!certifyState) {
                router.push("/login");
            }

            if (certifyStateError !== null) {
                certifyStateError == 400 && router.push("/error/error404");
            }
        }, 200);

        return () => {
            clearTimeout(certifyTimer);
        };
    }, [loginDone, certifyState, certifyStateError]);

    const handleFormChange = useCallback((changedData) => {
        setFormData(changedData);
    }, []);

    //입력 값 체크
    const checkValidation = useCallback(
        (data) => {
            // setFormData({
            //     ...formData,
            //     contact: data.cell1 + data.cell2 + data.cell3,
            // });
            if (Validation.isEmptyObject(data)) {
                alert("서비스접수 작성을 해주세요.");
                return false;
            }

            if (data.productSelector == "none") {
                alert("신청하실 제품을 선택하세요.");
                return false;
            }
            if (data.productSelector === "" && data.prdtCate === "0") {
                alert("제품카테고리를 선택해주세요.");
                return false;
            }
            if (
                data.productSelector === "" &&
                data.prdtCate !== "0" &&
                data.prdtName === ""
            ) {
                alert("제품명을 작성해주세요.");
                return false;
            }

            if (Validation.isEmpty(data.serviceGroup)) {
                alert("서비스 유형을 선택하세요.");
                return false;
            }
            if (Validation.isEmpty(data.title)) {
                alert("제목을 작성해주세요.");
                return false;
            }
            if (Validation.isEmpty(data.content)) {
                alert("내용을 작성해주세요.");
                return false;
            }
            if (Validation.isEmpty(data.prdtShop)) {
                alert("제품 구매처를 선택해주세요.");
                return false;
            }
            if (Validation.isEmpty(data.name)) {
                alert("이름을 작성해주세요.");
                return false;
            }
            if (
                Validation.isEmpty(data.cell2) ||
                Validation.isEmpty(data.cell3)
            ) {
                alert("연락처를 입력해주세요.");
                return false;
            }
            if (
                !Validation.onlyNumber(data.cell2) ||
                !Validation.onlyNumber(data.cell3)
            ) {
                alert("연락처는 숫자만 입력해주세요.");
                return false;
            }

            if (
                Validation.isEmpty(data.zip) ||
                Validation.isEmpty(data.addr1) ||
                Validation.isEmpty(data.addr2)
            ) {
                alert("주소를 작성해주세요.");
                return false;
            }

            if (!Validation.isChk(data.policy)) {
                alert("개인정보 수집 · 이용 동의 안내에 체크해주세요.");
                return false;
            }

            if (
                Validation.regexCheck(data.addr2) ||
                Validation.regexCheck(data.content) ||
                Validation.regexCheck(data.title)
            ) {
                return false;
            }

            formData.contact = data.cell1 + data.cell2 + data.cell3;

            return true;
        },
        [formData]
    );

    //접수하기
    const handleSubmit = useCallback(() => {
        if (checkValidation(formData)) {
            console.log(
                "Button 클릭! checkValidation완료 FormData 전달 : ",
                formData
            );
            //제외 데이터
            delete formData.productSelector;
            delete formData.cell1;
            delete formData.cell2;
            delete formData.cell3;

            //데이터 전달
            dispatch({
                type: SUBMIT_SERVICE_REQUEST,
                data: formData,
            });
        }
    }, [formData]);
    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
            {/* {(loginDone || certifyState) && ( */}
            <Container>
                <PageName title="서비스 접수" />

                {/* 서비스 작성 */}
                <ServiceForm
                    formData={formData}
                    onFormChange={handleFormChange}
                />

                {/* 약관동의 */}
                <Term
                    allChk="N"
                    termslist={["policy"]}
                    formData={formData}
                    onFormChange={handleFormChange}
                />

                {/* 유의사항 */}
                <NoticeService noticeName="service" />

                {/* 버튼 */}
                <div
                    className={`type1 btn-wrap`}
                    style={{ textAlign: "center" }}
                >
                    <button onClick={handleSubmit}>접수하기</button>
                </div>
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

        context.store.dispatch({
            type: CERTIFY_USER_INFO_REQUEST,
        });

        context.store.dispatch(END);
        await context.store.sagaTask.toPromise();
    }
);

export default Service;
