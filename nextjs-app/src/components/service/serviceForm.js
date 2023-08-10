import React, { useCallback, useEffect, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import FileMulti from "../form/fileMulti";
import AddrForm from "../form/addrForm";
import { useDispatch, useSelector } from "react-redux";
import { common } from "../../func/common";
import { GET_MY_RENTAL_LIST_REQUEST } from "../../reducers/service";
import { FormInfoViewStyle } from "../../style/FormStyle";
import { Validation } from "../../func/validation";

const ServiceForm = ({ formData, onFormChange }) => {
    const dispatch = useDispatch();
    const { certifyState } = useSelector((state) => state.user);
    const { myLentalList } = useSelector((state) => state.service);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [serviceData, setServiceData] = useState({
        //서비스접수 초기값 설정
        productSelector: "none", //제품
        prdtCate: "0", // 제품없는경우 - 제품 카테고리
        prdtName: "", // 제품없는경우 - 제품명
        serviceGroup: "", //서비스 유형
        title: "", //제목
        content: "", //내용
        ex_filename: [], //첨부파일
        prdtShop: "", //제품 구매처
        name: "", //고객 이름
        cell1: "010", //연락처 1번째 자리
        cell2: "", //연락처 2번째 자리
        cell3: "", //연락처 3번째 자리
        contact: "", //고객 연락처 : cell1 + cell2 + cell3
        zip: "", //우편번호
        addr1: "", //주소
        addr2: "", //상세 주소
    });

    useEffect(() => {
        const delay = 300; // 디바운싱 딜레이 (300ms)
        let timerId;
        const updateFormData = () => {
            onFormChange({
                ...formData,
                ...serviceData,
            });
        };
        if (timerId) {
            clearTimeout(timerId); // 타이머 리셋
        }

        timerId = setTimeout(updateFormData, delay);

        return () => {
            clearTimeout(timerId); // 컴포넌트가 unmount 되거나 업데이트 되기 전에 타이머 클리어
        };
    }, [serviceData]);

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
            (list) => list.custCode === serviceData.custCode
        );
        setSelectedProduct(foundProduct);
    }, [serviceData.productSelector, myLentalList]);

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            let newValue = type === "checkbox" ? checked : value;

            switch (name) {
                case "cell2":
                case "cell3":
                    newValue = Validation.inputOnlyNum(newValue);
                    break;
                case "productSelector":
                    const selectedOption = e.target.selectedOptions[0];
                    const grpCode = selectedOption.getAttribute("data-grpcode");
                    const modelname =
                        selectedOption.getAttribute("data-modelname");
                    serviceData.prdtCate = grpCode;
                    serviceData.prdtName = modelname;
                    serviceData.custCode = newValue;
                    break;
                case "prdtCate":
                    if (newValue !== "L") {
                        serviceData.prdtName = "";
                    } else {
                        serviceData.prdtName = "모션베드";
                    }
                    break;
                default:
                    break;
            }

            setServiceData((prevFormData) => ({
                ...prevFormData,
                [name]: newValue,
            }));
        },
        [serviceData]
    );

    return (
        <>
            <article className="form-write-basic">
                <FormWriteTitle
                    title="1.제품 선택/증상 입력"
                    service="afterService"
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
                                    value={serviceData.productSelector}
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
                                            data-grpcode={list.grpCode}
                                        >
                                            {list.erpModelName}
                                        </option>
                                    ))}
                                    <option value="">직접입력</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* 제품이 없는 경우 노출 : 직접입력 */}
                    {serviceData.productSelector == "" ? (
                        <div className="form-item col-2">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="prdtCate">
                                        제품 카테고리
                                    </label>
                                    <select
                                        name="prdtCate"
                                        value={serviceData.prdtCate}
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
                                            serviceData.prdtCate == "L"
                                                ? "모션베드"
                                                : serviceData.prdtName
                                        }
                                        onChange={handleChange}
                                        readOnly={
                                            serviceData.prdtCate == "L"
                                                ? true
                                                : false
                                        }
                                    />
                                </div>
                            </div>
                        </div>
                    ) : serviceData.productSelector !== "none" ? (
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

                {/* 서비스 유형 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">서비스 유형</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-selectbox">
                                <label htmlFor="serviceGroup">
                                    서비스 유형을 선택하세요
                                </label>
                                <select
                                    name="serviceGroup"
                                    value={serviceData.serviceGroup}
                                    onChange={handleChange}
                                >
                                    <option value="">
                                        서비스 유형을 선택하세요
                                    </option>
                                    <option value="1">소음</option>
                                    <option value="2">파손</option>
                                    <option value="3">작동이상</option>
                                    <option value="4">설치불량</option>
                                    <option value="5">소모품 교체</option>
                                    <option value="8">긴급누수</option>
                                    <option value="6">기타</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 제목 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">제목</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <input
                                    type="text"
                                    placeholder="제목을 입력하세요."
                                    name="title"
                                    value={serviceData.title}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                {/* 내용 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">내용</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <textarea
                                    placeholder="내용을 입력하세요."
                                    rows="10"
                                    name="content"
                                    value={serviceData.cotent}
                                    onChange={handleChange}
                                ></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 첨부파일 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="">첨부파일</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <FileMulti
                                name="ex_filename"
                                maxLen="3"
                                maxSize="300MB"
                                id="ex_filename"
                                fileList="Y"
                                formData={serviceData}
                                setFormData={setServiceData}
                            />
                        </div>
                    </div>
                </div>

                {/* 제품 구매처 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">제품 구매처</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-selectbox">
                                <label htmlFor="prdtShop">
                                    구매처를 선택하세요
                                </label>
                                <select
                                    name="prdtShop"
                                    value={serviceData.prdtShop}
                                    onChange={handleChange}
                                >
                                    <option value="">
                                        구매처를 선택하세요
                                    </option>
                                    <option value="1">온라인쇼핑몰</option>
                                    <option value="2">직영전시장</option>
                                    <option value="3">홈쇼핑</option>
                                    <option value="4">하이마트</option>
                                    <option value="6">삼성디지털프라자</option>
                                    <option value="7">전자랜드</option>
                                    <option value="5">기타</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <FormWriteTitle
                    title="2.고객 정보 입력"
                    service="afterService"
                />

                {/* 이름 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">이름</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <input
                                    type="text"
                                    placeholder="이름"
                                    name="name"
                                    value={serviceData.name}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                {/* 연락처 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">연락처</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-selectbox form-call">
                                <div>
                                    <label htmlFor="cell1">010</label>
                                    <select
                                        name="cell1"
                                        value={serviceData.cell1}
                                        onChange={handleChange}
                                    >
                                        <option value="010">010</option>
                                        <option value="011">011</option>
                                        <option value="016">016</option>
                                        <option value="017">017</option>
                                        <option value="018">018</option>
                                        <option value="019">019</option>
                                    </select>
                                </div>
                                <span className="call-txt">-</span>
                                <div>
                                    <input
                                        type="text"
                                        className="call-num-box"
                                        maxLength="4"
                                        name="cell2"
                                        value={serviceData.cell2}
                                        onChange={handleChange}
                                    />
                                </div>
                                <span className="call-txt">-</span>
                                <div>
                                    <input
                                        type="text"
                                        className="call-num-box"
                                        maxLength="4"
                                        name="cell3"
                                        value={serviceData.cell3}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 주소 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">주소</p>
                    </div>
                    <AddrForm
                        zipcode="zip"
                        addr1="addr1"
                        addr2="addr2"
                        formData={serviceData}
                        setFormData={setServiceData}
                        // onFormChange={onFormChange}
                    />
                </div>
            </article>
        </>
    );
};

export default ServiceForm;
