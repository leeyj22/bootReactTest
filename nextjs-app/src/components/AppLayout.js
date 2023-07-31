import React from "react";
import PropTypes from "prop-types";
import Headers from "./headers";
import Footer from "./footer";
import FooterNotice from "./footerNotice";

const AppLayout = ({ children }) => {
    return (
        <>
            <Headers />

            {children}

            <FooterNotice />
            <Footer />
        </>
    );
};

AppLayout.propTypes = {
    children: PropTypes.node.isRequired,
};

export default AppLayout;
