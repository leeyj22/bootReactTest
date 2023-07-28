import Link from "next/link";
import React from "react";
import { ImgUrl } from "../hooks/imgurl";

const Footer = () => {
    return (
        <footer>
            <div className="top">
                <div className="inner">
                    <Link href="/">회사소개</Link>
                    <Link href="/">이용약관</Link>
                    <Link href="/">
                        <strong>개인정보처리방침</strong>
                    </Link>
                    <Link href="/">사업자정보확인</Link>
                    <Link href="/">에스크로서비스 가입확인</Link>
                    <Link href="/">사이트맵</Link>
                </div>
            </div>
            <div className="bottom">
                <div className="inner">
                    <div>
                        <h2>(주)바디프랜드</h2>
                        <span>대표이사 : 박상민</span>
                        <span>
                            서울특별시 강남구 양재천로 163 바디프랜드 도곡타워
                        </span>
                    </div>
                    <div>
                        <span>사업자등록번호 : 106-86-49737</span>
                        <span>통신판매업신고번호 : 제 강남 15396</span>
                        <span>개인정보책임자 : 홍성진</span>
                    </div>
                    <div>
                        <p className="copy color-grey-b0">
                            Copyright © BODYFRIEND, Inc All Rights Reserved.
                        </p>
                    </div>
                    <div>
                        <span>바디프랜드 공식 SNS</span>
                        <span className="sns">
                            <Link href="/" title="유튜브">
                                <img
                                    src={
                                        ImgUrl + "cs/icon/app_logo_youtube.svg"
                                    }
                                    alt=""
                                />
                            </Link>
                            <Link href="/" title="페이스북">
                                <img
                                    src={
                                        ImgUrl + "cs/icon/app_logo_facebook.svg"
                                    }
                                    alt=""
                                />
                            </Link>
                            <Link href="/" title="인스타그램">
                                {" "}
                                <img
                                    src={
                                        ImgUrl +
                                        "cs/icon/app_logo_Instagram.svg"
                                    }
                                    alt=""
                                />
                            </Link>
                            <Link href="/" title="블로그">
                                {" "}
                                <img
                                    src={ImgUrl + "cs/icon/app_logo_blog.svg"}
                                    alt=""
                                />
                            </Link>
                            <Link href="/" title="네이버포스트">
                                {" "}
                                <img
                                    src={ImgUrl + "cs/icon/app_logo_naver.svg"}
                                    alt=""
                                />
                            </Link>
                        </span>
                    </div>
                    <div>
                        <span>멤버십 앱 다운로드</span>
                        <span className="app">
                            <Link href="/" title="앱스토어">
                                {" "}
                                <img
                                    src={
                                        ImgUrl + "cs/icon/app_logo_appstore.svg"
                                    }
                                    alt=""
                                />
                            </Link>
                            <Link href="/" title="구글플레이">
                                {" "}
                                <img
                                    src={
                                        ImgUrl +
                                        "cs/icon/app_logo_googleplay.svg"
                                    }
                                    alt=""
                                />
                            </Link>
                        </span>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default React.memo(Footer);
