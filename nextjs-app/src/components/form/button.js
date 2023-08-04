import React from "react";

const Button = ({ btnType, name, pos, formData, checkValidation }) => {
    const handleSubmit = () => {
        const resultFormData = checkValidation({
            ...formData,
        });
        //데이터 전달
        console.log("Button 클릭! resultFormData 전달 : ", resultFormData);
    };
    return (
        <div className={`${btnType} btn-wrap`} style={{ textAlign: pos }}>
            <button onClick={handleSubmit}>{name}</button>
        </div>
    );
};

export default Button;
