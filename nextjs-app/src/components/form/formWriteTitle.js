import React from "react";

const FormWriteTitle = ({ title, service }) => {
    // service
    // afterSErvice : 서비스 접수
    return (
        <div className="form-write-title">
            <p>{title}</p>

            <button className="info">비용안내</button>
        </div>
    );
};

export default FormWriteTitle;
