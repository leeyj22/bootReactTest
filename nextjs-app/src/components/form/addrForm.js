import React, { useCallback } from "react";
import { PostCode } from "../../lib/postcode";

const AddrForm = ({
    zipcode,
    addr1,
    addr2,
    postData,
    formData,
    setFormData,
}) => {
    const handleGetPostCode = () => {
        PostCode.init((data) => {
            setFormData({
                ...formData,
                [zipcode]: data.zipNo,
                [addr1]: data.roadAddrPart1,
                [addr2]: data.addrDetail,
                [postData]: data,
            });
        });
    };

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            const newValue = type === "checkbox" ? checked : value;

            setFormData({
                ...formData,
                [name]: newValue,
            });
        },
        [formData]
    );

    return (
        <>
            <div className="form-item form-addr col-1">
                <div className="col">
                    <input
                        type="text"
                        readOnly="readOnly"
                        className="zipcode"
                        name={zipcode}
                        id={zipcode}
                        value={formData[zipcode] || ""}
                        onClick={handleGetPostCode}
                    />
                    <button
                        id="searchAddr"
                        className="btn-zip"
                        onClick={handleGetPostCode}
                    >
                        우편번호찾기
                    </button>
                </div>
            </div>
            <div className="form-item form-addr col-1">
                <div className="col">
                    <input
                        type="text"
                        readOnly="readOnly"
                        maxLength="100"
                        name={addr1}
                        id={addr1}
                        value={formData[addr1] || ""}
                        onClick={handleGetPostCode}
                    />
                </div>
            </div>
            <div className="form-item form-addr col-1">
                <div className="col">
                    <input
                        type="text"
                        maxLength="100"
                        name={addr2}
                        id={addr2}
                        value={formData[addr2] || ""}
                        onChange={handleChange}
                    />
                </div>
            </div>
        </>
    );
};

export default AddrForm;
