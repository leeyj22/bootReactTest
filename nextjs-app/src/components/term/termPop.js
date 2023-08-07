import React, { useCallback } from "react";

const TermPop = ({ termData, setShowTerm, termHtml }) => {
    const onClose = useCallback(() => {
        setShowTerm(false);
    }, []);
    console.log("termHtml", termHtml);
    return (
        <section className="section-popup">
            <div className="section-popup--top">
                <h1>{termData.name}</h1>
                <button onClick={onClose}>
                    <span className="hdtxt">팝업닫기</span>
                </button>
            </div>
            <div className="section-popup--con">
                <div
                    className="inner"
                    dangerouslySetInnerHTML={{ __html: termHtml }}
                ></div>
            </div>
            <div className="section-popup--bottom">
                <button
                    className="btn-section-popup-close"
                    type="button"
                    onClick={onClose}
                >
                    닫기
                </button>
            </div>
        </section>
    );
};

export default TermPop;
