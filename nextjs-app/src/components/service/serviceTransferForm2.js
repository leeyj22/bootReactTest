import React, { useCallback, useState, useEffect } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import AddrForm from "../form/addrForm";
import Calendar from "../form/calendar";

const ServiceTransferForm2 = ({ onFormChange }) => {
    const [formData, setFormData] = useState({
        //이전설치접수2 초기값 설정 : 회수 정보, 설치 정보
        orderPerson: "", // 회수자 이름
        telNumb01: "", //회수자 연락처1
        telNumb01_01: "", //회수자 연락처1
        telNumb01_02: "", //회수자 연락처1
        zipCode1: "", //회수 우편번호
        addr1: "", //회수 주소
        addr2: "", //회수 상세 주소
        unInsDate: "", //회수 희망일
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
            <article className="form-write-basic">
                <FormWriteTitle
                    title="1. 회수 정보"
                    service="serviceTransfer"
                />

                {/* 회수 이름 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">이름</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <input
                                    type="text"
                                    placeholder="이름을 입력하세요"
                                    name="orderPerson"
                                    value={formData.orderPerson}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                {/* 회수 연락처 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">연락처</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-selectbox form-call">
                                <div>
                                    <label htmlFor="telNumb01">010</label>
                                    <select
                                        name="telNumb01"
                                        value={formData.telNumb01}
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
                                        name="telNumb01_01"
                                        value={formData.telNumb01_01}
                                        onChange={handleChange}
                                    />
                                </div>
                                <span className="call-txt">-</span>
                                <div>
                                    <input
                                        type="text"
                                        className="call-num-box"
                                        maxLength="4"
                                        name="telNumb01_02"
                                        value={formData.telNumb01_02}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 회수 주소 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">회수 주소</p>
                    </div>
                    <AddrForm
                        zipcode="zipCode1"
                        addr1="addr1"
                        addr2="addr2"
                        handleChange={handleChange}
                        onFormChange={onFormChange}
                        formData={formData}
                        setFormData={setFormData}
                    />
                </div>

                {/* 회수 희망일 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">회수 희망일</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                {/* <input
                                    type="text"
                                    placeholder="YYYY-MM-DD"
                                    name="unInsDate"
                                    value={formData.unInsDate}
                                    onChange={handleChange}
                                /> */}
                                <Calendar />
                            </div>
                        </div>
                    </div>
                </div>

                <FormWriteTitle
                    title="2. 설치 정보"
                    service="serviceTransfer"
                />

                <div className="form-write-item">
                    <div className="form-write-same-chk">
                        <input type="checkbox" name="" id="sameInfo" />
                        <label htmlFor="sameInfo">회수자 정보와 동일</label>
                    </div>
                </div>
            </article>
        </>
    );
};

export default ServiceTransferForm2;
