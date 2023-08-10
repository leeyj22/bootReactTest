import React, { useCallback, useEffect, useState } from "react";

import FormViewTitle from "../form/formViewTitle";
import { FormInfoViewStyle } from "../../style/FormStyle";
import { common } from "../../func/common";
import { Validation } from "../../func/validation";
import { bankData } from "../../data/bank";

const ServiceTransferForm3 = ({ formData, onFormChange }) => {
    const [serviceTransferFormData, setServiceTransferFormData] = useState({
        //이전설치접수3 초기값 설정 : 결제방법, 비용
        payMethod: "CARD", //결제수단 BANK 실시간계좌이체| VBANK 가상계좌| CARD 신용카드
        bankSelect: "none", //환불 은행코드
        bankName: "", //환불 은행명
        bankAccHolder: "", //환불 예금주명
        bankNo: "", //환불 계좌번호
    });

    useEffect(() => {
        const delay = 300; // 디바운싱 딜레이 (300ms)
        let timerId;
        const updateFormData = () => {
            onFormChange({
                ...formData,
                ...serviceTransferFormData,
            });
        };
        if (timerId) {
            clearTimeout(timerId); // 타이머 리셋
        }

        timerId = setTimeout(updateFormData, delay);

        return () => {
            clearTimeout(timerId); // 컴포넌트가 unmount 되거나 업데이트 되기 전에 타이머 클리어
        };
    }, [serviceTransferFormData]);

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            let newValue = type === "checkbox" ? checked : value;

            switch (name) {
                case "bankNo":
                    newValue = Validation.inputOnlyNum(newValue);
                    break;
                case "bankSelect":
                    const selectedOption = e.target.selectedOptions[0];
                    serviceTransferFormData.bankName =
                        selectedOption.getAttribute("data-bankname");
                    break;
                default:
                    break;
            }

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
                        {formData.showUnInstall && (
                            <div className="form-item col-1">
                                <div className="col">
                                    <span className="form-title">
                                        {formData.unInstallTxt} 정보
                                    </span>
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
                        )}

                        {formData.showInstall && (
                            <div className="form-item col-1">
                                <div className="col">
                                    <span className="form-title">
                                        {formData.installTxt} 정보
                                    </span>
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
                                        <em>희망일</em>{" "}
                                        <em>{formData.insDate}</em>
                                    </p>
                                    <p>
                                        <em>이사예정일</em>
                                        <em>{formData.moveDate}</em>
                                    </p>
                                </div>
                            </div>
                        )}
                    </div>
                </FormInfoViewStyle>
                {/* 이전/설치 비용 */}
                <div className="form-viewer-price">
                    <span>이전/설치 비용</span>
                    <strong>
                        <em>{common.toDotNumber(formData.totalPrice)}</em>원
                    </strong>
                </div>
            </article>
            <article className="form-write-basic">
                <FormViewTitle title="결제 방법 선택" />

                <FormInfoViewStyle type="pay">
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary grey">결제 방법</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="payMethod1"
                                            name="payMethod"
                                            value="CARD"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.payMethod ===
                                                "CARD"
                                            }
                                        />
                                        <label htmlFor="payMethod1">
                                            <span>신용카드</span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="payMethod2"
                                            name="payMethod"
                                            value="BANK"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.payMethod ===
                                                "BANK"
                                            }
                                        />
                                        <label htmlFor="payMethod2">
                                            <span>계좌이체</span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="payMethod3"
                                            name="payMethod"
                                            value="VBANK"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.payMethod ===
                                                "VBANK"
                                            }
                                        />
                                        <label htmlFor="payMethod3">
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
                    {serviceTransferFormData.payMethod !== "CARD" && (
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary grey">환불 계좌 입력</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="col-3">
                                        <div className="form-selectbox col">
                                            <label htmlFor="bankSelect">
                                                입금 은행을 선택하세요.
                                            </label>
                                            <select
                                                name="bankSelect"
                                                value={
                                                    serviceTransferFormData.bankSelect
                                                }
                                                onChange={handleChange}
                                            >
                                                <option value="none">
                                                    입금 은행을 선택하세요.
                                                </option>
                                                {bankData?.map((bank) => (
                                                    <option
                                                        key={bank.bankCode}
                                                        data-bankname={
                                                            bank.name
                                                        }
                                                        value={bank.bankCode}
                                                    >
                                                        {bank.name}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                        <div className="form-input col">
                                            <input
                                                type="text"
                                                name="bankAccHolder"
                                                placeholder="예금주를 입력하세요."
                                                value={
                                                    serviceTransferFormData.bankAccHolder
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                        <div className="form-input col">
                                            <input
                                                type="text"
                                                name="bankNo"
                                                placeholder="계좌번호를 입력하세요. (하이픈 - 생략)"
                                                value={
                                                    serviceTransferFormData.bankNo
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}
                </FormInfoViewStyle>
            </article>
        </>
    );
};

export default ServiceTransferForm3;
