import React from "react";
import PropTypes from "prop-types";
import Headers from "./headers";
import Footer from "./footer";
import FooterNotice from "./footerNotice";
import { GlobalStyle } from "../style/CommonStyle";
import LoadingComponent from "./loadingComponent";

const AppLayout = ({ children }) => {
    return (
        <>
            <GlobalStyle />
            <Headers />

            {children}

            <FooterNotice />
            <Footer />
            <LoadingComponent />
        </>
    );
};

AppLayout.propTypes = {
    children: PropTypes.node.isRequired,
};

export default AppLayout;
