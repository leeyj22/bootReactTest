import React, { useCallback, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import AddrForm from "../form/addrForm";
import Calendar from "../form/calendar";

import FormViewTitle from "../form/formViewTitle";
import { FormInfoViewStyle } from "../../style/FormStyle";
import NoticeService from "./noticeService";

const ServiceTransferForm2 = ({ onFormChange }) => {
    const [formData, setFormData] = useState({
        //이전설치접수2 초기값 설정 : 회수 정보, 설치 정보
        orderPerson: "", // 회수자 이름
        telNumb01: "", //회수자 연락처1
        telNumb01_01: "", //회수자 연락처2
        telNumb01_02: "", //회수자 연락처3
        zipCode1: "", //회수 우편번호
        orderAddr1: "", //회수 주소
        orderAddr2: "", //회수 상세 주소
        unInsDate: "", //회수 희망일
        receiver: "", //설치자 이름
        telNumb02: "", //설치자 연락처1
        telNumb02_01: "", //설치자 연락처2
        telNumb02_02: "", //설치자 연락처3
        zipCode2: "", //설치 우편번호
        receiveAddr1: "", //설치 주소
        receiverAddr2: "", //설치 상세 주소
    });

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            const newValue = type === "checkbox" ? checked : value;

            setFormData((prevFormData) => ({
                ...prevFormData,
                [name]: newValue,
            }));

            onFormChange({
                ...formData,
                [name]: newValue,
            });
        },
        [onFormChange, formData]
    );

    return (
        <>
            <article className="form-viewer-basic">
                {/* 이전/설치 신청 정보 */}
                <FormViewTitle title="이전/설치 신청 정보" />
                <FormInfoViewStyle>
                    <div className="form-viewer-item form-info">
                        <div className="form-item col-2">
                            <div className="col">
                                <span className="form-title">제품</span>
                                <p>더 파라오</p>
                            </div>
                            <div className="col">
                                <span className="form-title">접수 유형</span>
                                <p>지역 이동(포장 운반)</p>
                            </div>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <span className="form-title">회수 정보</span>
                                <p>
                                    <em>김바디</em> <em>010-1234-5678</em>
                                </p>
                                <p>
                                    <em>(06302)</em>{" "}
                                    <em>부산 부산진구 가야대로 772 6층</em>
                                </p>
                                <p>
                                    <em>희망일</em> <em>2022-01-07(월)</em>
                                </p>
                                <p>
                                    <em>이사예정일</em> <em>2022-01-06(일)</em>
                                </p>
                            </div>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <span className="form-title">설치 정보</span>
                                <p>
                                    <em>김바디</em> <em>010-1234-5678</em>
                                </p>
                                <p>
                                    <em>(06302)</em>{" "}
                                    <em>부산 부산진구 가야대로 772 6층</em>
                                </p>
                                <p>
                                    <em>희망일</em> <em>2022-01-07(월)</em>
                                </p>
                                <p>
                                    <em>이사예정일</em> <em>2022-01-06(일)</em>
                                </p>
                            </div>
                        </div>
                    </div>
                </FormInfoViewStyle>
                {/* 이전/설치 비용 */}
                <div class="form-viewer-price">
                    <span>이전/설치 비용</span>
                    <strong>
                        <em>300,000</em>원
                    </strong>
                </div>

                <FormViewTitle title="결제 방법 선택" />

                <FormInfoViewStyle type="pay">
                    <div className="form-viewer-item">
                        <div className="form-title">
                            <p className="necessary">결제 방법</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option1"
                                            name="option"
                                        />
                                        <label htmlFor="option1">
                                            <span>신용카드</span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option1"
                                            name="option"
                                        />
                                        <label htmlFor="option1">
                                            <span>계좌이체</span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option1"
                                            name="option"
                                        />
                                        <label htmlFor="option1">
                                            <span>가상계좌</span>
                                        </label>
                                    </div>
                                </div>
                                <p className="txt color-redf-0">
                                    ※ 50,000원 이상 결제 시 할부 가능합니다.
                                </p>
                            </div>
                        </div>
                    </div>
                </FormInfoViewStyle>
            </article>
        </>
    );
};

export default ServiceTransferForm2;
