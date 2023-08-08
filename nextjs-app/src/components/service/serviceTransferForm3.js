import React, { useCallback, useEffect, useState } from "react";

import FormViewTitle from "../form/formViewTitle";
import { FormInfoViewStyle } from "../../style/FormStyle";

const ServiceTransferForm3 = ({ formData, onFormChange }) => {
    const [serviceTransferFormData, setServiceTransferFormData] = useState({
        //이전설치접수3 초기값 설정 : 결제방법, 비용
    });

    useEffect(() => {
        onFormChange({
            ...formData,
            ...serviceTransferFormData,
        });
    }, [serviceTransferFormData]);

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            const newValue = type === "checkbox" ? checked : value;

            setServiceTransferFormData((prevFormData) => ({
                ...prevFormData,
                [name]: newValue,
            }));
        },
        [serviceTransferFormData]
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
                                <p>{formData.prdtName}</p>
                            </div>
                            <div className="col">
                                <span className="form-title">접수 유형</span>
                                <p>{formData.orderTypeTxt}</p>
                            </div>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <span className="form-title">회수 정보</span>
                                <p>
                                    <em>{formData.orderPerson}</em>
                                    <em>
                                        {formData.telNumb01 +
                                            "-" +
                                            formData.telNumb01_01 +
                                            "-" +
                                            formData.telNumb01_02}
                                    </em>
                                </p>
                                <p>
                                    <em>({formData.zipCode1})</em>
                                    <em>
                                        {formData.orderAddr1}
                                        {formData.orderAddr2}
                                    </em>
                                </p>
                                <p>
                                    <em>희망일</em>
                                    <em>{formData.unInsDate}</em>
                                </p>
                                <p>
                                    <em>이사예정일</em>
                                    <em>{formData.moveDate}</em>
                                </p>
                            </div>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <span className="form-title">설치 정보</span>
                                <p>
                                    <em>{formData.receiver}</em>
                                    <em>
                                        {formData.telNumb02 +
                                            "-" +
                                            formData.telNumb02_01 +
                                            "-" +
                                            formData.telNumb02_02}
                                    </em>
                                </p>
                                <p>
                                    <em>({formData.zipCode2})</em>
                                    <em>
                                        {formData.receiveAddr1}
                                        {formData.receiveAddr2}
                                    </em>
                                </p>
                                <p>
                                    <em>희망일</em> <em>{formData.insDate}</em>
                                </p>
                                <p>
                                    <em>이사예정일</em>
                                    <em>{formData.moveDate}</em>
                                </p>
                            </div>
                        </div>
                    </div>
                </FormInfoViewStyle>
                {/* 이전/설치 비용 */}
                <div className="form-viewer-price">
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
                                <p className="txt color-red-f0">
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

export default ServiceTransferForm3;
