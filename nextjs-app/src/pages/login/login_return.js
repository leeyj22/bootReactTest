import React, { useEffect } from "react";
import { LOGIN_REQUEST } from "../../reducers/user";
import { useDispatch } from "react-redux";
import { parseQueryString } from "../../hooks/parseQueryString";
const LoginReturn = () => {
    const dispatch = useDispatch();
    useEffect(() => {
        const schUrl = window.location.search;
        const schObj = parseQueryString(schUrl);

        dispatch({
            type: LOGIN_REQUEST,
            data: schObj,
        });
    }, []);

    return <div>return url</div>;
};

export default LoginReturn;
