import React, { useEffect, useMemo, useRef } from "react";
import Link from "next/link";
import { ImgUrl } from "../hooks/imgurl";
import { useDispatch, useSelector } from "react-redux";
import { LOGIN_URL_REQUEST } from "../reducers/user";
import { useRouter } from "next/router";

const headerUi = (otherLinkRef) => {
    const headerEl = document.querySelector("#header");
    const depth1Items = headerEl.querySelectorAll(".depth1 > li > a");
    const depth2Menu = headerEl.querySelectorAll(".depth2");
    const logoLink = headerEl.querySelector(".logo");
    const joinMenu = headerEl.querySelector(".join");
    const otherLinkPop = document.querySelector(".other-link-pop");
    const otherLinks = document.querySelector(".other-links");

    const addDepth2Bg = () => {
        const bgDepth2 = document.createElement("span");
        bgDepth2.classList.add("bg-depth2");
        headerEl.appendChild(bgDepth2);
    };
    const removeDepth2Bg = () => {
        const bgDepthEL = headerEl.getElementsByClassName("bg-depth2");
        if (bgDepthEL.length > 0) {
            bgDepthEL[0].remove();
        }
    };
    const removeOtherPop = () => {
        otherLinkPop.classList.remove("active");
        otherLinks.classList.remove("active");
    };

    // gnb mouse enter
    const handleHoverDepth1 = (event) => {
        const depth1Item = event.currentTarget;
        if (!depth1Item.classList.contains("other-links")) {
            removeDepth2Bg();
            addDepth2Bg();
            removeOtherPop();
            const depth2 = depth1Item.parentNode.querySelector(".depth2");
            depth2Menu.forEach((item) => {
                item.classList.remove("active");
                item.style.display = "block";
            });
            if (depth2 !== null) {
                depth2.classList.add("active");
            }
        }
    };

    // gnb mouse leaves
    const handleMouseLeaveDepth2 = () => {
        removeDepth2Bg();
        depth2Menu.forEach((item) => {
            item.style.display = "none";
        });
    };

    const handleClickOtherLink = () => {
        otherLinks.classList.toggle("active");
        otherLinkPop.classList.toggle("active");
    };

    const handleClickWindow = (event) => {
        const targetItem = event.target;
        const otherLinkWrap = otherLinkRef.current;
        if (otherLinkWrap && !otherLinkWrap.contains(targetItem.parentNode)) {
            removeOtherPop();
        }
    };

    depth2Menu.forEach((item) => {
        item.addEventListener("mouseenter", handleHoverDepth1);
        item.addEventListener("mouseleave", handleMouseLeaveDepth2);
    });

    depth1Items.forEach((item) => {
        item.addEventListener("mouseenter", handleHoverDepth1);
        item.addEventListener("focus", handleHoverDepth1);
        item.addEventListener("mouseleave", handleMouseLeaveDepth2);
    });

    logoLink?.addEventListener("focus", handleMouseLeaveDepth2);
    joinMenu?.addEventListener("focus", handleMouseLeaveDepth2);

    otherLinks.addEventListener("click", handleClickOtherLink);
    window.addEventListener("mousedown", handleClickWindow);

    // Clean up event listeners when the component unmounts
    return () => {
        depth1Items.forEach((item) => {
            item.removeEventListener("mouseenter", handleHoverDepth1);
            item.removeEventListener("focus", handleHoverDepth1);
            item.removeEventListener("mouseleave", handleMouseLeaveDepth2);
        });
        depth2Menu.forEach((item) => {
            item.removeEventListener("mouseenter", handleHoverDepth1);
            item.removeEventListener("mouseleave", handleMouseLeaveDepth2);
        });
        logoLink?.removeEventListener("focus", handleMouseLeaveDepth2);
        joinMenu?.removeEventListener("focus", handleMouseLeaveDepth2);
        otherLinks.removeEventListener("click", handleClickOtherLink);
        window.removeEventListener("mousedown", handleClickWindow);
    };
};

const Headers = () => {
    const dispatch = useDispatch();
    const otherLinkRef = useRef(null);
    const loginstate = false;
    const router = useRouter();

    const { loginUrlDone, loginUrl } = useSelector((state) => state.user);

    useEffect(() => {
        if (loginUrlDone) {
            // location.href = loginUrl;
            location.href =
                "https://tauth.bodyfriend.com/auth/common/login?client_id=tbodyfriend&redirect_uri=http://localhost:3000/login/login_return";
        }
    }, [loginUrlDone]);

    useEffect(() => {
        //header UI
        headerUi(otherLinkRef);
    }, [otherLinkRef]);

    const login = () => {
        dispatch({
            type: LOGIN_URL_REQUEST,
        });
    };

    return (
        <header id="header">
            <div className="inner">
                <Link href="/" className="logo">
                    <h1>
                        <img
                            src={ImgUrl + "/bodyfriend/common/logo/logo_bf.svg"}
                            alt="바디프랜드"
                        />
                        <span className="hdtxt">바디프랜드</span>
                    </h1>
                </Link>
                <nav className="gnb">
                    <ul className="depth1">
                        <li ref={otherLinkRef}>
                            <button className="other-links">
                                고객센터<i></i>
                            </button>
                            <div className="other-link-pop">
                                <ul>
                                    <li>
                                        <Link href="/">바디프랜드</Link>
                                    </li>
                                    <li>
                                        <Link href="/">바디프랜드 쇼핑몰</Link>
                                    </li>
                                    <li>
                                        <Link href="/">
                                            바디프랜드 고객센터
                                        </Link>
                                    </li>
                                    <li>
                                        <Link href="/">
                                            바디프랜드 비즈니스몰
                                        </Link>
                                    </li>
                                    <li>
                                        <Link href="/">바디프랜드 채용</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <Link
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                문제해결
                            </Link>
                            <div className="depth2 item0">
                                <label>문제해결</label>
                                <ul>
                                    <li>
                                        <Link href="/">자주 묻는 질문</Link>
                                    </li>
                                    <li>
                                        <Link href="/">동영상 가이드</Link>
                                    </li>
                                    <li>
                                        <Link href="/">제품 설명서</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <Link
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                서비스 신청
                            </Link>
                            <div className="depth2 item1">
                                <label>서비스 신청</label>
                                <ul>
                                    <li>
                                        <Link href="/">서비스 접수</Link>
                                    </li>
                                    <li>
                                        <Link href="/">이전/설치 접수</Link>
                                    </li>
                                    <li>
                                        <Link href="/">분해/조립 접수</Link>
                                    </li>
                                    <li>
                                        <Link href="/">서비스 요금 안내</Link>
                                    </li>
                                    <li>
                                        <Link href="/">서비스 진행 현황</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <Link
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                제품관리
                            </Link>
                            <div className="depth2 item2">
                                <label>제품관리</label>
                                <ul>
                                    <li>
                                        <Link href="/">주문/배송 조회</Link>
                                    </li>
                                    <li>
                                        <Link href="/">정품 등록</Link>
                                    </li>
                                    <li>
                                        <Link href="/">사용 정보 관리</Link>
                                    </li>
                                    <li>
                                        <Link href="/">결제 정보 관리</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <Link
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                고객지원
                            </Link>
                            <div className="depth2 item3">
                                <label>고객지원</label>
                                <ul>
                                    <li>
                                        <Link href="/">주문/배송 조회</Link>
                                    </li>
                                    <li>
                                        <Link href="/">정품 등록</Link>
                                    </li>
                                    <li>
                                        <Link href="/">사용 정보 관리</Link>
                                    </li>
                                    <li>
                                        <Link href="/">결제 정보 관리</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <Link
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                고객의소리
                            </Link>
                            <div className="depth2 item4">
                                <label>고객의 소리</label>
                                <ul>
                                    <li>
                                        <Link href="/">칭찬합니다</Link>
                                    </li>
                                    <li>
                                        <Link href="/">불편합니다</Link>
                                    </li>
                                    <li>
                                        <Link href="/">건의합니다</Link>
                                    </li>
                                    <li>
                                        <Link href="/">대표이사 신문고</Link>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </nav>

                <div className="util">
                    {loginstate ? (
                        <>
                            <Link href="/" className="">
                                마이페이지
                            </Link>
                            <Link href="/" className="" onClick={() => login()}>
                                로그아웃
                            </Link>
                        </>
                    ) : (
                        <>
                            <Link href="/" className="join">
                                회원가입
                            </Link>
                            <Link
                                href="/"
                                className="login"
                                onClick={() => login()}
                            >
                                로그인
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
};

export default React.memo(Headers);
