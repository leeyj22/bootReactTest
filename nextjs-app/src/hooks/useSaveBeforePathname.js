import { useEffect, useState } from "react";

export const useSaveBeforePathname = () => {
    useEffect(() => {
        const pathname = window.location.pathname;
        sessionStorage.setItem("beforeUrl", pathname);
    }, []);
};
