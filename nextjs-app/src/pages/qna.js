import React from "react";
import AppLayout from "../components/AppLayout";
import Breadcrumb from "../components/breadcrumb";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import TabMenu from "../components/form/tabMenu";
import { SearchInputStyle } from "../style/FormStyle";
//자주 묻는 질문
const qna = () => {
    return (
        <AppLayout>
            <Breadcrumb pageId="guide" pageSubId="guide1" />
            <Container>
                <PageName title="자주 묻는 질문" />

                <SearchInputStyle pos="right">
                    <input type="text" placeholder="검색어를 입력해 보세요." />
                </SearchInputStyle>

                <TabMenu />

                <ul>
                    <li>
                        <button></button>
                    </li>
                </ul>
            </Container>
        </AppLayout>
    );
};

export default qna;
