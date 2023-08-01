import React, { useEffect } from "react";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../../store/configureStore";

const certify_result = () => {
    useEffect(() => {
        //로컬스토리지 저장 + 데이터 저장.
        // setTimeout(() => {
        //     window.close();
        // }, 1000);
    }, []);
    return <div>본인인증 완료</div>;
};

export const getServerSideProps = wrapper.getServerSideProps(
    (store) =>
        async ({ req, res, ...etc }) => {
            store.dispatch(END);

            console.log("req=========================", req);
            console.log("res=========================", res);
            await store.sagaTask.toPromise();
        }
);

export default certify_result;
