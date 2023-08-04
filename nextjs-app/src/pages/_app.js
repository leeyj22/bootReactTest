import React from "react";
import PropTypes from "prop-types";
import Head from "next/head";
import wrapper from "../store/configureStore";
import "../../public/css/index.min.css";

const Cs = ({ Component, ...rest }) => {
    return (
        <>
            <Head>
                <meta charSet="utf-8" />
                <title>CS</title>
            </Head>
            <Component />
        </>
    );
};

Cs.propTypes = {
    Component: PropTypes.elementType.isRequired,
};

export default wrapper.withRedux(Cs);
