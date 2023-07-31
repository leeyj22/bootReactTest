import React, { useEffect } from "react";
import { LOGIN_REQUEST } from "../../reducers/user";
import { useDispatch } from "react-redux";

const parseQueryString = (queryString) => {
    const params = new URLSearchParams(queryString);
    const result = {};

    for (const [key, value] of params.entries()) {
        result[key] = value;
    }

    return result;
};

const LoginReturn = () => {
    const dispatch = useDispatch();
    useEffect(() => {
        const schUrl = window.location.search;
        const schObj = parseQueryString(schUrl);
        console.log("schObj", schObj);

        dispatch({
            type: LOGIN_REQUEST,
            data: schObj,
        });
    }, []);

    return <div>return url</div>;
};

export default LoginReturn;
