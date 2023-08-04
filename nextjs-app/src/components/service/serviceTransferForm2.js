import React, { useCallback, useState, useEffect } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import AddrForm from "../form/addrForm";
import Calendar from "../form/calendar";

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

    const getPostcode = () => {
        searchPostcode(true);
    };

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
                        addr1="orderAddr1"
                        addr2="orderAddr2"
                        formData={formData}
                        setFormData={setFormData}
                        onFormChange={onFormChange}
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

                {/* 설치자 이름 */}
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
                                    name="receiver"
                                    value={formData.receiver}
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
                                        name="telNumb02"
                                        value={formData.telNumb02}
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
                                        name="telNumb02_01"
                                        value={formData.telNumb02_01}
                                        onChange={handleChange}
                                    />
                                </div>
                                <span className="call-txt">-</span>
                                <div>
                                    <input
                                        type="text"
                                        className="call-num-box"
                                        maxLength="4"
                                        name="telNumb02_02"
                                        value={formData.telNumb02_02}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 설치 주소 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">설치 주소</p>
                    </div>
                    <AddrForm
                        zipcode="zipCode2"
                        addr1="receiveAddr1"
                        addr2="receiverAddr2"
                        formData={formData}
                        setFormData={setFormData}
                        onFormChange={onFormChange}
                    />
                </div>

                {/* 설치 희망일 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">설치 희망일</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <Calendar />
                            </div>
                            <p className="txt color-grey-b4">
                                ※ 희망일은 접수일 기준 7일 이후로 선택해 주세요.
                            </p>
                        </div>
                    </div>
                </div>
                {/* 이사 예정일 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="">이사 예정일</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <Calendar />
                            </div>
                        </div>
                    </div>
                </div>
                {/* 요청사항 */}
                <div className="form-write-item">
                    <div className="form-title">
                        <p className="necessary">요청 사항</p>
                    </div>
                    <div className="form-item col-1">
                        <div className="col">
                            <div className="form-input">
                                <textarea
                                    name="comment"
                                    placeholder="요청 사항을 입력하세요.(최대 100자)"
                                    maxLength="100"
                                ></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </>
    );
};

export default ServiceTransferForm2;
