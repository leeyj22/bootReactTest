import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import AppLayout from "../../components/AppLayout";
import { pageTypeMap } from "../../data/pageType";
import { CERTIFY_PAGETYPE_REQUEST } from "../../reducers/user";
import { Certify } from "../../hooks/certify";

//본인인증 Step1
const certify = () => {
    const dispatch = useDispatch();
    const { certifyPagetypeDone, certifyPageType } = useSelector(
        (state) => state.user
    );

    const [pageType, setPageType] = useState(null);
    useEffect(() => {
        setPageType(sessionStorage.getItem("beforeUrl"));
        if (pageType !== "" && pageType !== undefined) {
            // Certify.openCert(pageTypeMap[pageType]);
            dispatch({
                type: CERTIFY_PAGETYPE_REQUEST,
                data: pageTypeMap[pageType],
            });
        }
    }, []);
    useEffect(() => {
        if (certifyPagetypeDone && pageType !== undefined && pageType !== "") {
            const params = {
                urlCode: certifyPageType.urlCode,
                callbackUrl: "/certify/certifyResult",
                plusInfo: {
                    movePageUrl: encodeURIComponent(pageTypeMap[pageType]),
                    pageType: encodeURIComponent(certifyPageType.pageType),
                },
            };
        }
    }, [pageType, certifyPagetypeDone]);

    return <AppLayout>본인인증 1</AppLayout>;
};

// export const getServerSideProps = wrapper.getServerSideProps(
//     (store) =>
//         async ({ req, res, ...etc }) => {
//             const pageType = await useGetBeforePathname();
//             console.log("pageType", pageType);

//             store.dispatch({
//                 type: GET_TEST_APT_REQUEST,
//                 data: "jm91",
//             });

//             store.dispatch(END);

//             await store.sagaTask.toPromise();
//         }
// );

export default certify;
