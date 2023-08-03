import React from "react";
import { useDispatch } from "react-redux";
import AppLayout from "../components/AppLayout";
import { GET_TEST_APT_REQUEST } from "../reducers/user";
import PageName from "../components/pagename";
import { Container } from "../style/AppCommonStyle";
import Link from "next/link";

const index = () => {
    const dispatch = useDispatch();

    const plz = () => {
        dispatch({
            type: GET_TEST_APT_REQUEST,
            data: "jm91",
        });
    };
    return (
        <AppLayout>
            <Container>
                <PageName />
                <button onClick={() => plz()}> API연결확인 테스트 버튼</button>
                <br />
                <br />
                <br />
                <Link href="/service">서비스접수이동</Link>
                <br />
                <br />
                <br />
                <Link href="/service_transfer">이전/설치 접수 이동</Link>
                <br />
                <br />
                <br />
                <Link href="/qna">자주 묻는 질문 qna</Link>
                <br />
                <br />
                <br />
            </Container>
        </AppLayout>
    );
};

export default index;
