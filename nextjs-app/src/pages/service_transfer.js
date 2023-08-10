import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React, { useCallback, useEffect, useState } from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import Term from "../components/term/term";
import ServiceTransferForm1 from "../components/service/serviceTransferForm1";
import ServiceTransferForm2 from "../components/service/serviceTransferForm2";
import ServiceTransferForm3 from "../components/service/serviceTransferForm3";
import ServiceProgress from "../components/service/serviceProgress";
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
import NoticeService from "../components/service/noticeService";
import { Validation } from "../func/validation";
//이전설치 service2

const formInit = {
    //본인인증 데이터
    tr_cert: "",
    tr_url: "",
    tr_add: "",
    plusInfo: "",
    noListUser: "", //본인인증 사용자 여부 Y,N
    name: "", //고객명
    phoneNo: "", //핸드폰 번호
    certYn: "", //인증여부
    installTxt: "", //회수 또는 철거 텍스트
    unInstallTxt: "", //설치 또는 진행 텍스트
};

const ServiceTransfer = () => {
    const router = useRouter();
    const dispatch = useDispatch();
    useSaveBeforePathname();
    const {
        loginDone,
        certifyState,
        certifyStateError,
        getMarketingDone,
        marketingAgree,
    } = useSelector((state) => state.user);
    const [progress, setProgress] = useState(3);
    const serviceForms = [
        ServiceTransferForm1,
        ServiceTransferForm2,
        ServiceTransferForm3,
    ];
    const CurrentForm = serviceForms[progress - 1];
    const [formData, setFormData] = useState(formInit);
    const [termslist, setTermsList] = useState([
        "policy",
        "agreeServiceTrans1",
        "agreeServiceTrans2",
        "agreeServiceTrans3",
        // "agreeServiceTrans4",
    ]);

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
        let updateTermList = [];
        if (marketingAgree === 0) {
            updateTermList.push("marketing");
        }

        if (formData.payMethod !== undefined && formData.payMethod == "VBANK") {
            updateTermList.push("agreeServiceTrans4");
            setTermsList([...termslist, ...new Set(updateTermList)]);
        } else {
            updateTermList = termslist.filter(
                (term) => term !== "agreeServiceTrans4"
            );
            setTermsList([...new Set(updateTermList)]);
        }
    }, [getMarketingDone, formData.payMethod]);

    //작성시 데이터 set
    const handleFormChange = useCallback(
        (changedData) => {
            setFormData(changedData);
        },
        [formData]
    );
    const checkValidation = useCallback(
        (step, data) => {
            console.log("checkValidation data", data);
            switch (step) {
                case 1: // step1
                    if (Validation.isEmptyObject(data)) {
                        alert("이전/설치 접수 작성을 해주세요.");
                        return false;
                    }
                    if (data.productSelector == "none") {
                        alert("신청하실 제품을 선택하세요.");
                        return false;
                    }
                    if (
                        (data.productSelector === "" &&
                            data.prdtCate === "0") ||
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
                    if (
                        Validation.isEmpty(data.orderType) ||
                        Validation.isEmpty(data.orderTypeSub)
                    ) {
                        alert("접수 유형을 선택하세요.");
                        return false;
                    }
                    break;
                case 2: //step1
                    //회수(철거)정보만 있을 경우 - 설치정보 제외.
                    if (
                        data.orderTypeSub !== "7" &&
                        data.orderTypeSub !== "4"
                    ) {
                        if (Validation.isEmpty(data.orderPerson)) {
                            alert(
                                `${data.unInstallTxt}자 이름을 입력해주세요.`
                            );
                            return false;
                        }
                        if (
                            Validation.isEmpty(data.telNumb01_01) ||
                            Validation.isEmpty(data.telNumb01_02)
                        ) {
                            alert(
                                `${data.unInstallTxt}자 연락처를 입력해주세요.`
                            );
                            return false;
                        }
                        if (
                            Validation.isEmpty(data.zipCode1) ||
                            Validation.isEmpty(data.orderAddr1) ||
                            Validation.isEmpty(data.orderAddr2)
                        ) {
                            alert(`${data.unInstallTxt} 주소를 입력해주세요.`);
                            return false;
                        }
                    }

                    //회수 정보가 있을 경우.포장운반, 철거 시 (희망일)
                    if (
                        data.orderTypeSub !== "7" &&
                        data.orderTypeSub !== "4" &&
                        data.orderTypeSub !== "3"
                    ) {
                        if (Validation.isEmpty(data.unInsDate)) {
                            alert(
                                `${data.unInstallTxt} 희망일자를 입력해주세요.`
                            );
                            return false;
                        }
                    }

                    //설치만 있는 경우(포장)
                    if (data.orderTypeSub !== "6") {
                        if (Validation.isEmpty(data.receiver)) {
                            alert(`${data.installTxt}자 이름을 입력해주세요.`);
                            return false;
                        }
                        if (
                            Validation.isEmpty(data.telNumb02_01) ||
                            Validation.isEmpty(data.telNumb02_02)
                        ) {
                            alert(
                                `${data.installTxt}자 연락처를 입력해주세요.`
                            );
                            return false;
                        }
                        if (
                            Validation.isEmpty(data.zipCode2) ||
                            Validation.isEmpty(data.receiveAddr1) ||
                            Validation.isEmpty(data.receiverAddr2)
                        ) {
                            alert(`${data.installTxt} 주소를 입력해주세요.`);
                            return false;
                        }
                        if (Validation.isEmpty(data.insDate)) {
                            alert(
                                `${data.installTxt} 희망일자를 입력해주세요.`
                            );
                            return false;
                        }
                    }

                    //회수(철거) + 재설치 정보 입력시 날짜 체크
                    if (data.orderTypeSub == "1" || data.orderTypeSub == "5") {
                        if (data.insDate == data.unInsDate) {
                            alert(
                                `${data.unInstallTxt} 희망일과 ${data.installTxt} 희망일은 다르게 지정하셔야 합니다.`
                            );

                            return false;
                        }
                    }

                    //다른 층 이동의 경우
                    if (data.orderTypeSub == "3") {
                        if (data.orderAddr1 !== data.receiveAddr1) {
                            alert(
                                "건물 내 이동인 경우 주소가 동일해야 합니다."
                            );
                            return false;
                        }
                    }

                    //회수 + 재설치의 경우(포장운반) (s)
                    if (data.orderTypeSub == "1") {
                        if (Validation.isEmpty(data.location)) {
                            alert(
                                `${data.installTxt} 이동 구분을 선택 후 희망일자를 다시 확인해 주세요.`
                            );
                            return false;
                        }
                    }

                    break;
                case 3:
                    //결제 방법체크
                    if (Validation.isEmpty(data.payMethod)) {
                        alert("결제 방법을 선택하세요.");
                        return false;
                    }

                    //계좌이체, 가상계좌시 환불데이터 입력
                    if (data.payMethod !== "CARD") {
                        if (
                            Validation.isEmpty(data.bankSelect) ||
                            Validation.isEmpty(data.bankAccHolder) ||
                            Validation.isEmpty(data.bankNo)
                        ) {
                            alert("환불 계좌 정보를 모두 입력해주세요.");
                            return false;
                        }
                    }
                    //개인정보 수집 동의 체크
                    if (!Validation.isChk(data.policy)) {
                        alert("개인정보 수집 · 이용 동의 안내에 체크해주세요.");
                        return false;
                    }
                    break;
                default:
                    break;
            }
            return data;
        },
        [formData]
    );

    const handleSubmit = useCallback(
        (str) => {
            let result;
            switch (str) {
                case "PREV":
                    setProgress((prevProgress) => prevProgress - 1);
                    break;
                case "NEXT":
                case "SUBMIT":
                    result = checkValidation(progress, formData);
                    break;
                default:
                    break;
            }
            if (result) {
                setProgress((prevProgress) => prevProgress + 1);
                handleFormChange(formData);
            }
        },
        [progress, formData]
    );

    console.log("formData", formData);

    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service2" />
            {(loginDone || certifyState) && (
                <Container>
                    <PageName title="이전/설치 접수" />

                    <ServiceProgress step="3" progress={progress} />

                    {/* 이전설치접수 */}
                    {CurrentForm && (
                        <CurrentForm
                            formData={formData}
                            onFormChange={handleFormChange}
                        />
                    )}

                    {process == 2 && <NoticeService noticeName="svcTrans1" />}

                    {progress == 3 && (
                        <>
                            {/* 약관동의 */}
                            <Term
                                allChk="Y"
                                termslist={termslist}
                                onFormChange={handleFormChange}
                                type="allChkDiv"
                            />

                            <NoticeService noticeName="svcTrans2" />
                        </>
                    )}

                    {/* 버튼 */}
                    {progress == 1 && (
                        <>
                            <div
                                className="type1 btn-wrap"
                                style={{ textAlign: "center" }}
                            >
                                <button onClick={() => handleSubmit("NEXT")}>
                                    다음
                                </button>
                            </div>
                        </>
                    )}

                    {progress == 2 && (
                        <>
                            <div
                                className="type2 btn-wrap"
                                style={{ textAlign: "center" }}
                            >
                                <button
                                    className="btn-white"
                                    onClick={() => handleSubmit("PREV")}
                                >
                                    취소
                                </button>
                                <button
                                    className="btn-primary"
                                    onClick={() => handleSubmit("NEXT")}
                                >
                                    다음
                                </button>
                            </div>
                        </>
                    )}

                    {progress == 3 && (
                        <div
                            className="type2 btn-wrap"
                            style={{ textAlign: "center" }}
                        >
                            <button
                                className="btn-white"
                                onClick={() => handleSubmit("PREV")}
                            >
                                취소
                            </button>
                            <button
                                className="btn-pay"
                                onClick={() => handleSubmit("SUBMIT")}
                            >
                                {formData.totalPrice
                                    ? formData.totalPrice
                                    : "0"}
                                원 결제하기
                            </button>
                        </div>
                    )}
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

export default ServiceTransfer;
