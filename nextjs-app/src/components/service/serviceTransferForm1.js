import React, { useCallback, useEffect, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";

const ServiceTransferForm1 = ({ onFormChange }) => {
    const [formData, setFormData] = useState({
        //이전설치접수1 초기값 설정 : 제품선택, 접수 유형 선택
        bodyNo: "", //ERP CUSTCODE(서비스배송 bodyNo)
        modelCode: "", //제품코드
        goodsName: "", //주문 상품명
        goodsIdx: "", //상품 Index,
        goodsQty: "", //상품 수량
        grpcode: "", // 안마의자 M, 라클라우드 L, 정수기 W
        // goodsPrice: "", //상품 가격
        orderType: "", //주문타입(구매/렌탈 여부)
        orderTypeSub: "", //주문타입 sub(구매/렌탈 여부 서브)
    });

    useEffect(() => {}, []);

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
            <article className="form-write-basic">
                <FormWriteTitle
                    title="1. 제품 선택"
                    service="serviceTransfer"
                />

                {/* 제품 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">제품</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-selectbox">
                                <label htmlFor="productSelector">
                                    사용 제품 선택
                                </label>
                                <select
                                    name="productSelector"
                                    value={formData.productSelector}
                                    onChange={handleChange}
                                >
                                    <option value="none">
                                        신청하실 제품을 선택하세요.
                                    </option>
                                    <option value="">직접입력</option>
                                    {/* <option value="test" modelCode="" bodyNo="">
                                        ordGoods
                                    </option> */}
                                </select>
                            </div>
                            <p className="txt color-grey-b4">
                                ※ 라클라우드의 경우 모션베드(전동침대)만
                                이전/설치 가능합니다.
                            </p>
                        </div>
                    </div>

                    {/* 제품이 없는 경우 노출 : 직접입력 */}
                    {(formData.productSelector == "" ||
                        formData.productSelector == "DI") && (
                        <div className="form-item col-2">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="prdtCate">
                                        제품 카테고리
                                    </label>
                                    <select
                                        name="prdtCate"
                                        value={formData.prdtCate}
                                        onChange={handleChange}
                                    >
                                        <option value="0">
                                            제품카테고리 선택
                                        </option>
                                        <option value="M">안마의자</option>
                                        <option value="L">라클라우드</option>
                                        <option value="W">W정수기</option>
                                        <option value="E">기타</option>
                                    </select>
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-input">
                                    <input
                                        type="text"
                                        placeholder="제품명을 입력하세요."
                                        name="ordGoods"
                                        value={formData.ordGoods}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                    )}
                </div>

                <FormWriteTitle
                    title="2. 접수 유형 선택"
                    service="serviceTransfer"
                />

                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">접수 유형</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            {/* 접수 유형 (안마의자, 라클라우드) : 포장운반, 다른층이동, 같은층 이동*/}
                            {formData.grpcode != "W" && (
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option1"
                                            name="option"
                                        />
                                        <label htmlFor="option1">
                                            <span>지역 이동(포장 운반)</span>
                                            <span>
                                                제품을 분해+포장+이동 재설치하는
                                                서비스
                                            </span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option2"
                                            name="option"
                                        />
                                        <label htmlFor="option2">
                                            <span>건물 내 층간 이동</span>
                                            <span>
                                                동일한 건물 내에서 다른 층으로
                                                이동하는 서비스
                                            </span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option3"
                                            name="option"
                                        />
                                        <label htmlFor="option3">
                                            <span>건물 내 층간 이동</span>
                                            <span>
                                                집안 내에서 이동하는 서비스
                                            </span>
                                        </label>
                                    </div>
                                </div>
                            )}

                            {/* 접수 유형 (정수기) : 철거+재설치, 철거, 재설치 */}
                            {formData.grpcode == "W" && (
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option4"
                                            name="option"
                                        />
                                        <label htmlFor="option4">
                                            <span>철거+재설치</span>
                                            <span>
                                                제품 철거 + 재설치
                                                서비스(포장+운반 미포함)
                                            </span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option5"
                                            name="option"
                                        />
                                        <label htmlFor="option5">
                                            <span>철거</span>
                                            <span>
                                                제품 철거 서비스(포장+운반
                                                미포함)
                                            </span>
                                        </label>
                                    </div>
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option6"
                                            name="option"
                                        />
                                        <label htmlFor="option6">
                                            <span>재설치</span>
                                            <span>
                                                제품 재설치 서비스(포장+운반
                                                미포함)
                                            </span>
                                        </label>
                                    </div>
                                </div>
                            )}

                            <p className="txt color-red-f0">
                                ※ 제품 분해/조립만 필요한 경우 분해/조립 접수
                                메뉴를 이용해 주세요. (포장/이동 제외)
                            </p>
                        </div>
                    </div>
                </div>
            </article>
        </>
    );
};

export default ServiceTransferForm1;
