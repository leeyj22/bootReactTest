import React, { useEffect, useState } from "react";
import { PostCode } from "../../lib/postcode";

const AddrForm = ({
    zipcode,
    addr1,
    addr2,
    formData,
    setFormData,
    handleChange,
}) => {
    const { postData, searchPostcode } = PostCode();

    useEffect(() => {
        console.log(zipcode, addr1, addr2);
        console.log("postData", postData);
        if (Object.keys(postData).length !== 0) {
            // onFormChange({
            //     ...formData,
            //     formData[zipcode]: postData.zipNo, //우편번호
            //     formData[addr1]: postData.roadAddrPart1, //주소
            //     formData[addr2]: postData.addrDetail, //상세 주소
            // });
            setFormData((prevFormData) => ({
                ...prevFormData,
                [zipcode]: postData.zipNo,
                [addr1]: postData.roadAddrPart1,
                [addr2]: postData.addrDetail,
            }));
        }
    }, [postData]);
    console.log("formData", formData);

    const getPostcode = () => {
        searchPostcode(true);
    };
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
                    />
                    <button
                        id="searchAddr"
                        className="btn-zip"
                        onClick={getPostcode}
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
