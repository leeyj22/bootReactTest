import React, { useEffect } from "react";
import AppLayout from "../../components/AppLayout";
import { useSelector } from "react-redux";
import { Validation } from "../../hooks/validation";

const CertifyCommon = () => {
    const { certifyDone, cerfifyFormData } = useSelector((state) => state.user);
    console.log("certifyDone", certifyDone);
    useEffect(() => {
        if (certifyDone) {
            console.log("cerfifyFormData", cerfifyFormData);
            if (Validation.isEmpty(target)) {
                console.log("모바일 인증 타겟 없음");
                return;
            }

            if (result == void 0 || result.data == void 0) {
                console.log("모바일 인증 오류");
                alert("잠시뒤 다시 이용하시기 바랍니다.");
                return;
            }
        }
    }, [certifyDone, cerfifyFormData]);
    // const handleSubmit = (event) => {
    //     event.preventDefault(); // 기본 폼 제출 동작을 막습니다.

    //     // 폼 데이터를 처리하는 로직을 구현합니다.
    //     // 폼 데이터를 이용하여 API 호출 등을 할 수 있습니다.
    //     const formData = new FormData(event.target);
    //     const trCertValue = formData.get("tr_cert");
    //     const trUrlValue = formData.get("tr_url");
    //     const trAddValue = formData.get("tr_add");

    //     // ...폼 데이터를 활용한 추가 로직...
    // };

    return (
        <AppLayout>
            본인인증2
            <iframe
                id="iframeEle"
                name="iframeEle"
                style={{ overflow: "hidden" }}
            ></iframe>
            <form name="reqKMCISForm">
                <input type="hidden" name="tr_cert" value="" />
                <input type="hidden" name="tr_url" value="" />
                <input type="hidden" name="tr_add" value="" />

                <button type="submit">Submit</button>
            </form>
        </AppLayout>
    );
};

export default CertifyCommon;
