import React from "react";
import { ButtonStyle } from "../../style/FormStyle";

const Button = ({ btnName, position, formData, checkValidation }) => {
    const handleSubmit = () => {
        const resultFormData = checkValidation({
            ...formData,
        });
        //데이터 전달
        console.log("Button 클릭! resultFormData 전달 : ", resultFormData);
    };
    return (
        <div className={`${btnName} btn-wrap`}>
            <ButtonStyle position={position} onClick={handleSubmit}>
                접수하기
            </ButtonStyle>
        </div>
    );
};

export default Button;
