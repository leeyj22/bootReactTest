import React, { useCallback, useEffect, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import FileMulti from "../form/fileMulti";
import { PostCode } from "../../lib/postcode";
import AddrForm from "../form/addrForm";

const ServiceForm = ({ onFormChange }) => {
    const { postData, searchPostcode } = PostCode();
    const [formData, setFormData] = useState({
        //서비스접수 초기값 설정
        productSelector: "", //제품
        prdtCate: "", // 제품없는경우 - 제품 카테고리
        prdtName: "", // 제품없는경우 - 제품명
        serviceGroup: "", //서비스 유형
        title: "", //제목
        content: "", //내용
        ex_filename: [], //첨부파일
        prdtShop: "", //제품 구매처
        name: "", //고객 이름
        cell1: "", //연락처 1번째 자리
        cell2: "", //연락처 2번째 자리
        cell3: "", //연락처 3번째 자리
        contact: "", //고객 연락처 : cell1 + cell2 + cell3
        zip: "", //우편번호
        addr1: "", //주소
        addr2: "", //상세 주소
    });

    useEffect(() => {
        // 스크립트를 동적으로 생성하여 <head> 태그의 자식으로 추가
        const script = document.createElement("script");
        script.src = "https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js";
        document.head.appendChild(script);

        if (Object.keys(postData).length !== 0) {
            onFormChange({
                ...formData,
                zip: postData.zipNo, //우편번호
                addr1: postData.roadAddrPart1, //주소
                addr2: postData.addrDetail, //상세 주소
            });
            setFormData({
                ...formData,
                zip: postData.zipNo, //우편번호
                addr1: postData.roadAddrPart1, //주소
                addr2: postData.addrDetail, //상세 주소
            });
        }
        return () => {
            // 컴포넌트가 언마운트될 때 스크립트 요소를 제거하여 메모리 누수 방지
            document.head.removeChild(script);
        };
    }, [postData]);

    const getPostcode = () => {
        searchPostcode(true);
    };

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

    const handleFileChange = (files) => {
        onFormChange({
            ...formData,
            ex_filename: files,
        });
    };

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
                                    value={formData.productSelector}
                                    onChange={handleChange}
                                >
                                    <option value="none">
                                        신청하실 제품을 선택하세요.
                                    </option>
                                    <option value="">직접입력</option>
                                </select>
                            </div>
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
                                        name="prdtName"
                                        value={formData.prdtName}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
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
                                    value={formData.serviceGroup}
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
                                    value={formData.title}
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
                                    value={formData.cotent}
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
                                onFormChange={handleFileChange}
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
                                    value={formData.prdtShop}
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
                                    value={formData.name}
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
                                        value={formData.cell1}
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
                                        value={formData.cell2}
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
                                        value={formData.cell3}
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
                        handleChange={handleChange}
                        onFormChange={onFormChange}
                        formData={formData}
                        setFormData={setFormData}
                    />
                </div>
            </article>
        </>
    );
};

export default ServiceForm;
