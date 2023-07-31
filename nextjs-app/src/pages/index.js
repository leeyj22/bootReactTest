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
                <button onClick={() => plz()}>버튼</button>
                <Link href="/service">서비스접수페이지 이동</Link>
                <h3>리스트</h3>
            </Container>
        </AppLayout>
    );
};

export default index;
