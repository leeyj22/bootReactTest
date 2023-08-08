import React, { useState, useCallback } from "react";
import { useDispatch } from "react-redux";
import { LOGIN_OLD_REQUEST } from "../../reducers/user";
import AppLayout from "../../components/AppLayout";
import { Container } from "../../style/AppCommonStyle";

const login_test = () => {
    const dispatch = useDispatch();
    const [formData, setFormData] = useState({
        userId: "",
        pwd: "",
    });
    const handleChange = useCallback((e) => {
        const { name, value, type, checked } = e.target;
        const newValue = type === "checkbox" ? checked : value;

        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: newValue,
        }));
    }, []);

    const handleSubmit = () => {
        if (formData.userId == "") {
            alert("아이디를 입력하세요");
            return false;
        }
        if (formData.pwd == "") {
            alert("비밀번호를 입력하세요");
            return false;
        }
        dispatch({
            type: LOGIN_OLD_REQUEST,
            data: formData,
        });
    };
    return (
        <AppLayout>
            <Container>
                <input
                    type="text"
                    name="id"
                    value={formData.id}
                    onChange={handleChange}
                />
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                />
                <button onClick={handleSubmit}>로그인</button>
            </Container>
        </AppLayout>
    );
};

export default login_test;
