import Link from "next/link";
import React from "react";

const FooterNotice = () => {
    return (
        <section className="footer-notice">
            <div className="inner">
                <div className="board-wrap">
                    <span className="board-name">공지사항</span>
                    <span className="board-title">
                        <Link href="/" title="" className="new">
                            서비스 이용약관 및 개인정보 처리방침 개정 안내 test
                        </Link>
                    </span>
                </div>
                <Link href="/" title="더보기" className="board-more">
                    더보기
                </Link>
            </div>
        </section>
    );
};

export default FooterNotice;
