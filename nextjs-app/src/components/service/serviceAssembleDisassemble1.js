import React, { useCallback, useState } from "react";
import FormWriteTitle from "../form/formWriteTitle";

const ServiceAssembleDisassemble1 = ({ onFormChange }) => {
    const [formData, setFormData] = useState({
        //분해조립 초기값 설정 : 제품선택, 접수 유형 선택
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
                    title="1. 제품 선택"
                    service="serviceAssembleDisassemble"
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
                                    <option value="test" modelCode="" bodyNo="">
                                        ordGoods
                                    </option>
                                </select>
                            </div>
                            <p className="txt color-grey-b4">
                                ※{" "}
                                <em className="color-red-f0">폭이 좁은 모델</em>
                                은 분해 조립 없이도 충분히 이동 가능합니다.
                                <br />
                                분해/조립 신청 시{" "}
                                <em className="color-red-f0">
                                    다리만 분해 가능
                                </em>
                                하고{" "}
                                <em className="color-red-f0">
                                    비용도 전액 발생
                                </em>
                                되는 점 유의하시기 바랍니다. (ex. 엘리자베스,
                                엘리자베스플러스, 엘리제, 아제라, 레그넘,
                                아이로보)
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
                    service="serviceAssembleDisassemble"
                />
            </article>
        </>
    );
};

export default ServiceAssembleDisassemble1;
