import React, { useEffect, useState } from "react";
import { terms } from "../../data/terms";
import InputCheck from "../form/inputCheck";
import TermPop from "./termPop";
import { useDispatch, useSelector } from "react-redux";
import { GET_TERM_REQUEST } from "../../reducers/service";

// const termslist = ['policy','mareting'];

const Term = ({ allChk, termslist, onFormChange, type }) => {
    const dispatch = useDispatch();

    const { termTxt } = useSelector((state) => state.service);
    //termslist 에 따라 termsData 초기화.
    const initialFormData = termslist.reduce((acc, termId) => {
        return {
            ...acc,
            [termId]: false,
        };
    }, {});

    const [termsData, setTermsData] = useState(initialFormData);
    const [termData, setTermData] = useState({});
    const [showTerm, setShowTerm] = useState(false);

    useEffect(() => {
        onFormChange((prevFormData) => ({
            ...prevFormData,
            ...termsData,
        }));
    }, [termsData]);

    const handleChange = (e) => {
        const { id, checked } = e.target;

        setTermsData((currentFormData) => {
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

            return updatedFormData;
        });
    };

    const onTermPop = (data) => {
        if (data.pop == "Y") {
            setTermData(data);
            setShowTerm(true);
            dispatch({
                type: GET_TERM_REQUEST,
                data: data.index,
            });
        }
    };

    return (
        <article className="term-agree-wrap" data-allchk={allChk}>
            {allChk == "Y" && (
                <dl className={`${type} terms-list`}>
                    <dt className="temp-title">
                        <InputCheck
                            id="chkAll"
                            name="모두 동의"
                            checked={termsData["chkAll"] || false}
                            onChange={handleChange}
                            necessary="chkAll"
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
                                    checked={termsData[term.id] || false}
                                    necessary={term.necessary}
                                    onChange={handleChange}
                                />
                                {term.pop == "Y" && (
                                    <button
                                        className="btn-temp-pop"
                                        onClick={() => onTermPop(term)}
                                    >
                                        <span className="hdtxt">
                                            {term.name} 팝업 열기
                                        </span>
                                    </button>
                                )}
                            </dt>
                        </dl>
                    );
                }
                return null;
            })}

            {showTerm && termTxt !== undefined && (
                <TermPop
                    termData={termData}
                    setShowTerm={setShowTerm}
                    termHtml={termTxt}
                />
            )}
        </article>
    );
};

export default Term;
