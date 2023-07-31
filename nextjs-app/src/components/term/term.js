import React, { useState } from "react";
import { terms } from "../../data/terms";
import InputCheck from "../form/inputCheck";

// const termslist = ['policy','mareting'];

const Term = ({ allChk, termslist, onFormChange }) => {
    const [formData, setFormData] = useState({
        // 약관 항목
    });

    const handleChange = (e) => {
        setFormData((prevFormData) => ({
            ...prevFormData,
        }));

        onFormChange({
            ...formData,
        });
    };
    return (
        <article className="term-agree-wrap" data-allchk={allChk}>
            {allChk == "Y" && (
                <dl className="terms-list">
                    <dt className="temp-title">
                        <InputCheck id="chkAll" name="모두 동의" />
                    </dt>
                </dl>
            )}
            {terms.map((term) => {
                if (termslist.includes(term.id)) {
                    return (
                        <dl className="terms-list" key={term.id}>
                            <dt className="temp-title">
                                <InputCheck id={term.id} name={term.name} />
                                <button className="btn-temp-pop">
                                    <span className="hdtxt">
                                        {term.name} 팝업 열기
                                    </span>
                                </button>
                            </dt>
                        </dl>
                    );
                }
                return null;
            })}
        </article>
    );
};

export default React.memo(Term);
