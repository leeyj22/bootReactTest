import React from "react";

const ServiceAssembleDisassemble2 = () => {
    return (
        <>
            <article className="form-write-basic">
                <FormWriteTitle
                    title="1. 분해 정보"
                    service="serviceTransfer"
                />

                {/* 분해 이름 */}
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
            </article>
        </>
    );
};

export default ServiceAssembleDisassemble2;
