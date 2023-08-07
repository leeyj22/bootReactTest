export const common = {
    getSesstionStorageCertUser: () => {
        const CertUserName = sessionStorage.getItem("CertUserName");
        const CertPhoneNo = sessionStorage.getItem("CertPhoneNo");
        const CertGender = sessionStorage.getItem("CertGender");
        const CertBirthDay = sessionStorage.getItem("CertBirthDay");

        return {
            CertUserName,
            CertPhoneNo,
            CertGender,
            CertBirthDay,
        };
    },
};
