import React from "react";
import DaumPostcodeEmbed from "react-daum-postcode";
import { ModalPost } from "../style/AppCommonStyle";

const Post = ({ hidePopup, formData, setFormData }) => {
    const handleComplete = (data) => {
        let zipcode = data.zonecode; //우편주소
        let fullAddress = data.address; //최종 주소 변수
        let extraAddress = ""; //조합형 주소 변수
        // 도로명
        if (data.userSelectedType === "R") {
            // 법정동명 추가
            if (data.bname !== "") {
                extraAddress += data.bname;
            }
            //건물명 추가
            if (data.buildingName !== "") {
                extraAddress +=
                    extraAddress !== ""
                        ? `, ${data.buildingName}`
                        : data.buildingName;
            }
            // 전체 주소
            fullAddress += extraAddress !== "" ? ` (${extraAddress})` : "";
        } else {
            fullAddress = data.jibunAddress;
        }

        // console.log(fullAddress); // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
        setFormData({
            ...formData,
            zipcode: zipcode,
            addr1: fullAddress,
        });
    };

    const handleClose = () => {
        hidePopup();
    };

    return (
        <ModalPost>
            <div className="top">
                <h3>주소찾기</h3>
                <button onClick={handleClose}>닫기</button>
            </div>
            <DaumPostcodeEmbed
                onComplete={handleComplete}
                onClose={handleClose}
            />
        </ModalPost>
    );
};

export default Post;
