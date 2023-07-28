import React from "react";
import { terms } from "../../data/terms";

// const termslist = ['policy','mareting'];

console.log("terms", terms);

const Term = ({ allChk, termslist }) => {
    console.log("termslist", termslist);
    return (
        <article className="term-agree-wrap" data-allchk={allChk}>
            {allChk == "Y" && (
                <dl className="terms-list">
                    <dt className="temp-title">
                        <input type="checkbox" id="chkAll" />
                        <label htmlFor="chkAll">전체 동의</label>
                    </dt>
                </dl>
            )}
            {terms.map((term) => {
                if (termslist.includes(term.id)) {
                    return (
                        <dl className="terms-list" key={term.id}>
                            <dt className="temp-title">
                                <input type="checkbox" id={term.param} />
                                <label htmlFor={term.param}>{term.name}</label>
                                <button>
                                    <span className="hdtxt">
                                        개인정보 수집 · 이용 동의 안내 팝업 열기
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
