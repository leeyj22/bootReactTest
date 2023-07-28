import Link from "next/link";
import React, { useEffect, useRef } from "react";
import { menu } from "../data/menu";

const bradbrumbUi = (bradbrumbRef) => {
    const bradbrumbEl = document.querySelector(".bradbrumb_wrap");
    const bradbrumbList = bradbrumbEl.querySelectorAll("li");

    const closePop = (event) => {
        const targetItem = event.currentTarget;
        bradbrumbList.forEach((sibling) => {
            if (sibling !== targetItem) {
                sibling.classList.remove("active");
            }
        });
    };
    const handleClickToggle = (event) => {
        const targetItem = event.currentTarget;

        closePop(event);
        targetItem.classList.toggle("active");
    };

    const handleClickRemove = (event) => {
        const targetItem = event.target;
        const bradbrumbWrap = bradbrumbRef.current;
        if (bradbrumbWrap && !bradbrumbWrap.contains(targetItem.parentNode)) {
            closePop(event);
        }
    };

    bradbrumbList.forEach((item) => {
        item.addEventListener("click", handleClickToggle);
    });
    window.addEventListener("mousedown", handleClickRemove);

    // Clean up event listeners when the component unmounts
    return () => {
        bradbrumbList.forEach((item) => {
            item.removeEventListener("click", handleClickToggle);
        });
        window.removeEventListener("mousedown", handleClickRemove);
    };
};

const Breadcrumb = ({ pageId, pageSubId }) => {
    const bradbrumbRef = useRef(null);
    useEffect(() => {
        //bradbrumb UI
        bradbrumbUi(bradbrumbRef);
    }, [bradbrumbRef]);
    return (
        <section className="bradbrumb_wrap" ref={bradbrumbRef}>
            <ul className="inner">
                {menu.depth1.map((menu1) => {
                    return (
                        menu1.id === pageId && (
                            <li key={menu1.id}>
                                <Link href={menu1.link}>{menu1.name}</Link>
                                <div className="pop">
                                    {menu.depth1.map(({ id, link, name }) => {
                                        return (
                                            <Link href={link} key={id}>
                                                {name}
                                            </Link>
                                        );
                                    })}
                                </div>
                            </li>
                        )
                    );
                })}

                {menu[pageId].map((page) => {
                    return (
                        page.id == pageSubId && (
                            <li key={page.id}>
                                <Link href={page.link}>{page.name}</Link>
                                <div className="pop">
                                    {menu[pageId].map(({ id, link, name }) => {
                                        return (
                                            <Link href={link} key={id}>
                                                {name}
                                            </Link>
                                        );
                                    })}
                                </div>
                            </li>
                        )
                    );
                })}
            </ul>

            {/* <ul>
                <li>
                    <Link>서비스 신청</Link>
                    <div className="pop">
                        <Link>문제해결</Link>
                        <Link>서비스 신청</Link>
                        <Link>제품관리</Link>
                        <Link>고객지원</Link>
                        <Link>고객의 소리</Link>
                    </div>
                </li>
                <li>
                    <Link>서비스 접수</Link>
                    <div className="pop">
                        <Link>서비스 접수</Link>
                        <Link>이전/설치 접수</Link>
                        <Link>분해/조립 접수</Link>
                        <Link>서비스 요금 안내</Link>
                        <Link>서비스 진행 현황</Link>
                    </div>
                </li>
            </ul> */}
        </section>
    );
};

export default React.memo(Breadcrumb);
