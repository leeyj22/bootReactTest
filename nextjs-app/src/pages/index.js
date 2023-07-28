import React from "react";
import { useDispatch } from "react-redux";
import { END } from "redux-saga";
import axios from "axios";
import AppLayout from "../components/AppLayout";
import { GET_TEST_APT_REQUEST } from "../reducers/user";
import PageName from "../components/pagename";
import { Container } from "../style/AppCommonStyle";
import Link from "next/link";
import wrapper from "../store/configureStore";

const index = () => {
    const dispatch = useDispatch();

    const plz = () => {
        // dispatch({
        //     type: GET_TEST_APT_REQUEST,
        //     data: "jm91",
        // });
    };
    return (
        <AppLayout>
            <Container>
                <PageName />
                <button onClick={() => plz()}>버튼</button>
                <br />
                <Link href="/service">서비스페이지 이동</Link>
            </Container>
        </AppLayout>
    );
};

export const getServerSideProps = wrapper.getServerSideProps(
    (store) =>
        async ({ req, res, ...etc }) => {
            store.dispatch({
                type: GET_TEST_APT_REQUEST,
                data: "jm91",
            });

            store.dispatch(END);

            await store.sagaTask.toPromise();
        }
);
export default index;
