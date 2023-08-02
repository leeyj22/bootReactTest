import React, { useEffect } from "react";
import { END } from "redux-saga";
import axios from "axios";
import wrapper from "../../store/configureStore";
import { CERTIFY_SAVE_SSR_REQUEST } from "../../reducers/user";
import { useSelector } from "react-redux";

const certify_result = () => {
    const { certifyInfo } = useSelector((state) => state.user);
    useEffect(() => {
        if (certifyInfo !== null && certifyInfo !== undefined) {
            const certifyInfoObj = JSON.parse(certifyInfo);
            certifyInfoObj.certifyInfoState = true;
            window.opener.postMessage(certifyInfoObj);
            const closePop = setTimeout(() => {
                const UserAgent = navigator.userAgent;
                if (
                    UserAgent.match(
                        /iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i
                    ) != null ||
                    UserAgent.match(/LG|SAMSUNG|Samsung/) != null
                ) {
                    // window.close();
                    window.opener.postMessage(certifyInfo);
                } else if (window.opener.parent != null) {
                    // window.close();
                    window.opener.parent.postMessage(certifyInfo);
                } else {
                    // window.close();
                    window.opener.postMessage(certifyInfo);
                }
            }, 1000);

            return () => {
                clearTimeout(closePop);
            };
        }
    }, [certifyInfo !== null && certifyInfo !== undefined]);
    return (
        <div>
            {certifyInfo !== null && certifyInfo !== undefined
                ? "본인인증에 성공하였습니다"
                : "본인인증에 실패하였습니다."}
        </div>
    );
};

export const getServerSideProps = wrapper.getServerSideProps(
    (store) =>
        async ({ req, res, ...data }) => {
            store.dispatch(END);
            console.log(
                "data==================",
                data.query.data,
                typeof data.query.data
            );
            // console.log("req=========================", req);
            // console.log("res=========================", res);

            const certifyData = data.query.data;
            store.dispatch({
                type: CERTIFY_SAVE_SSR_REQUEST,
                data: certifyData,
            });
            await store.sagaTask.toPromise();
        }
);

export default certify_result;
