import React, { useState } from "react";
import { terms } from "../../data/terms";
import InputCheck from "../form/inputCheck";

// const termslist = ['policy','mareting'];

const Term = ({ allChk, termslist, onFormChange }) => {
    //termslist 에 따라 formData 초기화.
    const initialFormData = termslist.reduce((acc, termId) => {
        return {
            ...acc,
            [termId]: false,
        };
    }, {});

    const [formData, setFormData] = useState(initialFormData);

    const handleChange = (e) => {
        const { id, checked } = e.target;

        setFormData((currentFormData) => {
            let updatedFormData;

            if (id === "chkAll") {
                // "모두 동의" 체크박스가 선택될 때, 나머지 항목들을 checked 값에 따라 업데이트
                updatedFormData = {
                    ...currentFormData,
                    [id]: checked,
                };

                for (const termId of termslist) {
                    updatedFormData[termId] = checked;
                }
            } else {
                // "모두 동의" 이외의 체크박스가 선택될 때, 해당 항목의 checked 값을 업데이트
                updatedFormData = {
                    ...currentFormData,
                    [id]: checked,
                };

                // 모든 "나머지 항목" 체크박스가 선택되었는지 확인하는 변수
                let isAllChecked = true;
                for (const termId of termslist) {
                    if (termId !== "chkAll" && !updatedFormData[termId]) {
                        isAllChecked = false;
                        break;
                    }
                }

                // 모든 "나머지 항목" 체크박스가 선택되었다면 "모두 동의" 체크박스도 선택하기
                updatedFormData.chkAll = isAllChecked;
            }

            onFormChange(updatedFormData);

            return updatedFormData;
        });
    };

    return (
        <article className="term-agree-wrap" data-allchk={allChk}>
            {allChk == "Y" && (
                <dl className="terms-list">
                    <dt className="temp-title">
                        <InputCheck
                            id="chkAll"
                            name="모두 동의"
                            checked={formData["chkAll"] || false}
                            onChange={handleChange}
                        />
                    </dt>
                </dl>
            )}
            {terms.map((term) => {
                if (termslist.includes(term.id)) {
                    return (
                        <dl className="terms-list" key={term.id}>
                            <dt className="temp-title">
                                <InputCheck
                                    id={term.id}
                                    name={term.name}
                                    checked={formData[term.id] || false}
                                    onChange={handleChange}
                                />
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

export default Term;
