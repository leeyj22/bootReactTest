import React from "react";
import PropTypes from "prop-types";
import Head from "next/head";
import { Provider } from "react-redux";
import wrapper from "../store/configureStore";
import "../../public/css/index.min.css";

const Cs = ({ Component, ...rest }) => {
    const { store, props } = wrapper.useWrappedStore(rest);
    return (
        <>
            <Provider store={store}>
                <Head>
                    <meta charSet="utf-8" />
                    <title>CS</title>
                </Head>
                <Component />
            </Provider>
        </>
    );
};

Cs.propTypes = {
    Component: PropTypes.elementType.isRequired,
};

export default Cs;
