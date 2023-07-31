// useParseQueryString.js 파일의 내용
import { useMemo } from "react";

const useParseQueryString = (queryString) => {
    return useMemo(() => {
        console.log("queryString", queryString);
        const params = new URLSearchParams(queryString);
        const result = {};

        for (const [key, value] of params.entries()) {
            result[key] = value;
        }

        return result;
    }, [queryString]);
};

export default useParseQueryString;
