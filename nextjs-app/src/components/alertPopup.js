import React from "react";

const AlertPopup = ({ message, onClose }) => {
    return (
        <div className="alert-popup">
            <div className="alert-content">
                <p>{message}</p>
                <button onClick={onClose}>닫기</button>
            </div>
        </div>
    );
};

export default AlertPopup;
