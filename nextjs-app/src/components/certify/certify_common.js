import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { pageTypeMap } from "../../data/pageType";
import { CERTIFY_PAGETYPE_REQUEST, CERTIFY_REQUEST } from "../../reducers/user";
import { Certify } from "../../hooks/certify";

//본인인증 컴포넌트
const CertifyCommon = () => {
    const dispatch = useDispatch();
    const {
        certifyPagetypeDone,
        certifyPageType,
        certifyDone,
        certifyFormData,
    } = useSelector((state) => state.user);

    //본인인증 1
    useEffect(() => {
        const pageTypeSession = sessionStorage.getItem("beforeUrl");
        if (pageTypeSession !== "" && pageTypeSession !== undefined) {
            if (pageTypeSession == null) {
                sessionStorage.setItem("beforeUrl", "/login");
            }
            // Certify.openCert(pageTypeMap[pageType]);
            const param =
                pageTypeMap[pageTypeSession] !== undefined
                    ? pageTypeMap[pageTypeSession]
                    : pageTypeMap["/"];
            // API
            dispatch({
                type: CERTIFY_PAGETYPE_REQUEST,
                data: param,
            });
        }
    }, []);

    //본인인증 2 : 본인인증 정보 요청 /certify/requestCertification
    useEffect(() => {
        if (certifyPagetypeDone && certifyPageType !== null) {
            const params = {
                urlCode: certifyPageType.urlCode,
                callbackUrl: "/certify/certifyResult",
                plusInfo: {
                    movePageUrl: encodeURIComponent(
                        pageTypeMap[certifyPageType.pageType]
                    ),
                    pageType: encodeURIComponent(certifyPageType.pageType),
                },
            };

            // API
            dispatch({
                type: CERTIFY_REQUEST,
                data: params,
            });
        }
    }, [certifyPagetypeDone == true, certifyPageType !== null]);

    //본인인증 3 : 인증팝업 열기
    useEffect(() => {
        if (certifyDone && certifyFormData !== null) {
            console.log("333");
            Certify.openKMCISWindow("reqKMCISForm", certifyFormData);
        }
    }, [certifyDone, certifyFormData]);

    return (
        <>
            {/* <iframe
                id="iframeEle"
                name="iframeEle"
                style={{ overflow: "hidden" }}
            ></iframe> */}
            {certifyDone && certifyFormData !== null && (
                <form name="reqKMCISForm" className="hdtxt">
                    <input type="hidden" name="tr_cert" />
                    <input type="hidden" name="tr_url" />
                    <input type="hidden" name="tr_add" />
                </form>
            )}
        </>
    );
};

export default CertifyCommon;
