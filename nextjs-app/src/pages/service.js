import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useEffect, useState, useCallback } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import NoticeService from "../components/service/noticeService";
import ServiceForm from "../components/service/serviceForm";
import { useDispatch, useSelector } from "react-redux";
import { useSaveBeforePathname } from "../hooks/useSaveBeforePathname";
import { useRouter } from "next/router";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../store/configureStore";
import {
    CERTIFY_USER_INFO_REQUEST,
    GET_MARKETING_AGREE_REQUEST,
} from "../reducers/user";
import { Validation } from "../func/validation";
import { SUBMIT_SERVICE_REQUEST } from "../reducers/service";
import { formDataConsoleChk } from "../func/common";
//서비스 접수 service1

const formInit = {
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
};

const Service = () => {
    const router = useRouter();
    const dispatch = useDispatch();
    useSaveBeforePathname();
    const {
        loginDone,
        certifyState,
        certifyStateError,
        submitServiceDone,
        getMarketingDone,
        marketingAgree,
    } = useSelector((state) => state.user);
    const [formData, setFormData] = useState(formInit);
    const [termslist, setTermsList] = useState(["policy"]);

    //본인인증 | 로그인 체크
    useEffect(() => {
        //회원이 아닌 본인인증의 경우 certifyState true
        //회원일 경우 loginDone true , user 데이터 있음.
        const certifyTimer = setTimeout(() => {
            // if (!certifyState && !loginDone) {
            if (!certifyState) {
                router.push("/login");
            }

            if (certifyStateError !== null) {
                certifyStateError == 400 && router.push("/error/error404");
            }

            if (certifyState || loginDone) {
                dispatch({
                    type: GET_MARKETING_AGREE_REQUEST,
                });
            }
        }, 200);

        return () => {
            clearTimeout(certifyTimer);
        };
    }, [loginDone, certifyState, certifyStateError]);

    //마케팅 동의 여부
    useEffect(() => {
        if (marketingAgree === 0) {
            setTermsList([...termslist, "marketing"]);
        }
    }, [getMarketingDone]);

    //접수완료 초기화
    useEffect(() => {
        if (submitServiceDone) {
            alert("접수가 완료되었습니다.");
            setFormData(formInit);
        }
    }, [submitServiceDone]);

    //작성시 데이터 set
    const handleFormChange = useCallback((changedData) => {
        setFormData(changedData);
    }, []);

    //입력 값 체크
    const checkValidation = useCallback(
        (data) => {
            if (Validation.isEmptyObject(data)) {
                alert("서비스접수 작성을 해주세요.");
                return false;
            }

            if (data.productSelector == "none") {
                alert("신청하실 제품을 선택하세요.");
                return false;
            }
            if (
                (data.productSelector === "" && data.prdtCate === "0") ||
                (data.productSelector === "" && data.prdtCate === null)
            ) {
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

            return true;
        },
        [formData]
    );

    //접수하기
    const handleSubmit = useCallback(() => {
        if (checkValidation(formData)) {
            const serviceData = new FormData();
            serviceData.append("userIdx", formData.userIdx);
            serviceData.append("userId", formData.userId);
            serviceData.append("name", formData.name);
            serviceData.append("prdtName", formData.prdtName);
            serviceData.append("prdtCate", formData.prdtCate);
            serviceData.append("zip", formData.zip);
            serviceData.append("addr1", formData.addr1);
            serviceData.append("addr2", formData.addr2);
            serviceData.append(
                "contact",
                formData.cell1 + formData.cell2 + formData.cell3
            );
            serviceData.append("serviceGroup", formData.serviceGroup);
            serviceData.append("title", formData.title);
            serviceData.append("content", formData.content);
            serviceData.append("custCode", formData.custCode);
            serviceData.append("prdtShop", formData.prdtShop);
            serviceData.append("ex_filename", formData.ex_filename);
            //마케팅 체크 여부
            serviceData.append("marketingYn", formData.marketing ? "Y" : "N");

            //폼데이터 콘솔 확인(운영 삭제)
            formDataConsoleChk(serviceData);

            //데이터 전달
            dispatch({
                type: SUBMIT_SERVICE_REQUEST,
                data: serviceData,
            });
        }
    }, [formData]);
    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
            {(loginDone || certifyState) && (
                <Container>
                    <PageName title="서비스 접수" />

                    {/* 서비스 작성 */}
                    <ServiceForm
                        formData={formData}
                        onFormChange={handleFormChange}
                    />

                    {/* 약관동의 */}
                    {/* 마케팅 동의 했을 경우 ?
                 allChk="N"
                termslist={["policy", "marketing"]}
                 */}
                    <Term
                        allChk="N"
                        termslist={termslist}
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
            )}
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
