import React,{useEffect} from "react";
import AppLayout from "../components/AppLayout";
import { Container } from "../style/AppCommonStyle";
const error = () => {

    useEffect(() => {
//        alert('');
    },[]);

    return (
        <AppLayout>
            <Container>
                error
            </Container>
        </AppLayout>
    );
};

export default error;
