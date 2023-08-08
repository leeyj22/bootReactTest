import React, { useCallback, useEffect, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import { useDispatch, useSelector } from "react-redux";
import { common } from "../../func/common";
import { GET_MY_RENTAL_LIST_REQUEST } from "../../reducers/service";
import { FormInfoViewStyle } from "../../style/FormStyle";
const ServiceTransferForm1 = ({ formData, onFormChange }) => {
    const dispatch = useDispatch();
    const { certifyState } = useSelector((state) => state.user);
    const { myLentalList } = useSelector((state) => state.service);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [serviceTransferFormData, setServiceTransferFormData] = useState({
        //이전설치접수1 초기값 설정 : 제품선택, 접수 유형 선택
        productSelector: "none", //제품
        prdtCate: "0", // 제품없는경우 - 제품 카테고리. 안마의자 M, 라클라우드 L, 정수기 W
        prdtName: "", // 제품없는경우 - 제품명
        orderType: "", //주문타입(구매/렌탈 여부)
        orderTypeSub: "", //주문타입 sub(구매/렌탈 여부 서브)

        bodyNo: "", //ERP CUSTCODE(서비스배송 bodyNo)
        modelCode: "", //제품코드
        serviceMonth: "",
        qty: "1",
        goodsIdx: "1", //상품 Index,
    });

    useEffect(() => {
        onFormChange({
            ...formData,
            ...serviceTransferFormData,
        });
    }, [serviceTransferFormData]);

    useEffect(() => {
        if (certifyState) {
            const userCertData = common.getSesstionStorageCertUser();
            dispatch({
                type: GET_MY_RENTAL_LIST_REQUEST,
                data: {
                    name: userCertData.CertUserName,
                    phone: userCertData.CertPhoneNo,
                },
            });
        }
    }, [certifyState]);

    useEffect(() => {
        if (
            myLentalList !== null &&
            myLentalList !== undefined &&
            myLentalList.length > 1
        ) {
            // alert(
            //     "사용중인 제품이 조회되지 않습니다.\n제품명을 직접 입력하여 접수해주세요."
            // );
        }
    }, [myLentalList]);

    useEffect(() => {
        const foundProduct = myLentalList?.find(
            (list) => list.custCode === serviceTransferFormData.custCode
        );
        setSelectedProduct(foundProduct);
    }, [serviceTransferFormData.productSelector, myLentalList]);

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            const newValue = type === "checkbox" ? checked : value;

            switch (name) {
                case "productSelector":
                    const selectedOption = e.target.selectedOptions[0];
                    serviceTransferFormData.modelCode =
                        selectedOption.getAttribute("data-modelcode");
                    serviceTransferFormData.bodyNo = newValue;
                    serviceTransferFormData.prdtCate =
                        selectedOption.getAttribute("data-grpcode");
                    serviceTransferFormData.serviceMonth =
                        selectedOption.getAttribute("data-servicemonth");
                    serviceTransferFormData.prdtName =
                        selectedOption.getAttribute("data-modelname");
                    serviceTransferFormData.custCode = newValue;

                    break;
                case "prdtCate":
                    serviceTransferFormData.orderType = "";
                    serviceTransferFormData.orderTypeSub = "";
                    if (newValue !== "L") {
                        serviceTransferFormData.prdtName = "";
                    } else {
                        serviceTransferFormData.prdtName = "모션베드";
                    }
                    break;
                case "orderType":
                    serviceTransferFormData.orderTypeTxt =
                        e.target.getAttribute("data-txt");
                    serviceTransferFormData.orderTypeSub =
                        e.target.getAttribute("data-subordertype");

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
                                    value={
                                        serviceTransferFormData.productSelector
                                    }
                                    onChange={handleChange}
                                >
                                    <option value="none">
                                        신청하실 제품을 선택하세요.
                                    </option>

                                    {myLentalList?.map((list) => (
                                        <option
                                            key={list.custCode}
                                            value={list.custCode}
                                            data-modelname={list.erpModelName}
                                            data-modelcode={list.modelCode}
                                            data-grpcode={list.grpCode}
                                            data-servicemonth={list.asPeriod}
                                        >
                                            {list.erpModelName}
                                        </option>
                                    ))}
                                    <option value="">직접입력</option>
                                </select>
                            </div>
                            <p className="txt color-grey-b4">
                                ※ 라클라우드의 경우 모션베드(전동침대)만
                                이전/설치 가능합니다.
                            </p>
                        </div>
                    </div>

                    {/* 제품이 없는 경우 노출 : 직접입력 */}
                    {serviceTransferFormData.productSelector == "" ? (
                        <div className="form-item col-2">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="prdtCate">
                                        제품 카테고리
                                    </label>
                                    <select
                                        name="prdtCate"
                                        value={serviceTransferFormData.prdtCate}
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
                                        name="prdtName"
                                        value={
                                            serviceTransferFormData.prdtCate ==
                                            "L"
                                                ? "모션베드"
                                                : serviceTransferFormData.prdtName
                                        }
                                        onChange={handleChange}
                                        readOnly={
                                            serviceTransferFormData.prdtCate ==
                                            "L"
                                                ? true
                                                : false
                                        }
                                    />
                                </div>
                            </div>
                        </div>
                    ) : serviceTransferFormData.productSelector !== "none" ? (
                        <>
                            <FormInfoViewStyle>
                                <div className="form-viewer-item form-info">
                                    <div className="form-item col-2">
                                        {selectedProduct && (
                                            <>
                                                <div className="col">
                                                    <span className="form-title">
                                                        제품명
                                                    </span>
                                                    <p>
                                                        {
                                                            selectedProduct.erpModelName
                                                        }
                                                    </p>
                                                </div>
                                                <div className="col">
                                                    <span className="form-title">
                                                        사용자명
                                                    </span>
                                                    <p>
                                                        {
                                                            selectedProduct.instCustName
                                                        }
                                                    </p>
                                                </div>
                                                <div className="col">
                                                    <span className="form-title">
                                                        연락처
                                                    </span>
                                                    <p>
                                                        {
                                                            selectedProduct.custMobile
                                                        }
                                                    </p>
                                                </div>
                                                <div className="col">
                                                    <span className="form-title">
                                                        설치주소
                                                    </span>
                                                    <p>
                                                        {selectedProduct.instAddr1 +
                                                            selectedProduct.instAddr2}
                                                    </p>
                                                </div>
                                            </>
                                        )}
                                    </div>
                                </div>
                            </FormInfoViewStyle>
                        </>
                    ) : (
                        ""
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
                            {serviceTransferFormData.prdtCate !== "W" && (
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option1"
                                            name="orderType"
                                            value="3"
                                            data-subordertype="1"
                                            data-txt="지역 이동(포장 운반)"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "1"
                                            }
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
                                            name="orderType"
                                            value="3"
                                            data-subordertype="3"
                                            data-txt="건물 내 층간 이동"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "3"
                                            }
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
                                            name="orderType"
                                            value="4"
                                            data-subordertype="4"
                                            data-txt="건물 내 집안 이동"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "4"
                                            }
                                        />
                                        <label htmlFor="option3">
                                            <span>건물 내 집안 이동</span>
                                            <span>
                                                집안 내에서 이동하는 서비스
                                            </span>
                                        </label>
                                    </div>
                                </div>
                            )}

                            {/* 접수 유형 (정수기) : 철거+재설치, 철거, 재설치 */}
                            {serviceTransferFormData.prdtCate === "W" && (
                                <div className="form-radio-btns col-3">
                                    <div className="radio-btn col">
                                        <input
                                            type="radio"
                                            id="option4"
                                            name="orderType"
                                            value="5"
                                            data-subordertype="5"
                                            data-txt="철거+재설치"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "5"
                                            }
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
                                            name="orderType"
                                            value="5"
                                            data-subordertype="6"
                                            data-txt="제품 철거 서비스(포장+운반
                                                미포함)"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "6"
                                            }
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
                                            name="orderType"
                                            value="5"
                                            data-subordertype="7"
                                            data-txt="재설치"
                                            onChange={handleChange}
                                            checked={
                                                serviceTransferFormData.orderTypeSub ===
                                                "7"
                                            }
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
