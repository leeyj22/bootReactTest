// useParseQueryString.js 파일의 내용
import { useMemo } from "react";

export const useParseQueryString = (queryString) => {
    return useMemo(() => {
        const params = new URLSearchParams(queryString);
        const result = {};

        for (const [key, value] of params.entries()) {
            result[key] = value;
        }

        return result;
    }, [queryString]);
};

export const useParseQueryString2 = (queryString) => {
    const params = new URLSearchParams(queryString);
    const result = {};

    for (const [key, value] of params.entries()) {
        result[key] = value;
    }

    return result;
};
