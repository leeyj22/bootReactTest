import React, { useEffect } from "react";
import { PostCode } from "../../lib/postcode";

const AddrForm = ({
    zipcode,
    addr1,
    addr2,
    handleChange,
    onFormChange,
    formData,
    setFormData,
}) => {
    const { postData, searchPostcode } = PostCode();
    useEffect(() => {
        // 스크립트를 동적으로 생성하여 <head> 태그의 자식으로 추가
        const script = document.createElement("script");
        script.src = "https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js";
        document.head.appendChild(script);

        if (Object.keys(postData).length !== 0) {
            onFormChange({
                ...formData,
                [zipcode]: postData.zipNo, //우편번호
                [addr1]: postData.roadAddrPart1, //주소
                [addr2]: postData.addrDetail, //상세 주소
            });
            setFormData({
                ...formData,
                [zipcode]: postData.zipNo, //우편번호
                [addr1]: postData.roadAddrPart1, //주소
                [addr2]: postData.addrDetail, //상세 주소
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
                        value={formData[zipcode]}
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
                        value={formData[addr1]}
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
                        value={formData[addr2]}
                        onChange={handleChange}
                    />
                </div>
            </div>
        </>
    );
};

export default AddrForm;
