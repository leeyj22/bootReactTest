import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {
    LOGIN_URL_FAILURE,
    LOGIN_URL_REQUEST,
    LOGIN_URL_SUCCESS,
    GET_TEST_APT_FAILURE,
    GET_TEST_APT_REQUEST,
    GET_TEST_APT_SUCCESS,
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    CERTIFY_PAGETYPE_SUCCESS,
    CERTIFY_PAGETYPE_FAILURE,
    CERTIFY_PAGETYPE_REQUEST,
    CERTIFY_REQUEST,
    CERTIFY_SUCCESS,
    CERTIFY_FAILURE,
    LOAD_USER_INFO_REQUEST,
    LOAD_USER_INFO_SUCCESS,
    LOAD_USER_INFO_FAILURE,
    CERTIFY_USER_INFO_SUCCESS,
    CERTIFY_USER_INFO_FAILURE,
    CERTIFY_USER_INFO_REQUEST,
} from "../reducers/user";

//test
function getTestAPI(data) {
    console.log("data???", data);
    return axios.get(`/api/userdata?userid=${data}`);
}

function* getTest(action) {
    try {
        const result = yield call(getTestAPI, action.data);
        console.log("getTestAPI result", result);

        yield put({
            type: GET_TEST_APT_SUCCESS,
            data: result.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_TEST_APT_FAILURE,
            data: err.response.data,
        });
    }
}

// login request url
function loginRequestUrlAPI() {
    return axios.get("member/login");
}

function* loginRequestUrl(action) {
    try {
        const result = yield call(loginRequestUrlAPI);
        console.log("loginAPI result", result);

        yield put({
            type: LOGIN_URL_SUCCESS,
            data: result.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: LOGIN_URL_FAILURE,
            data: err.response.data,
        });
    }
}

//login
function loginRequestAPI(data) {
    console.log("data", data);
    return axios.post("member/login_return", data);
}

function* loginRequest(action) {
    try {
        const result = yield call(loginRequestAPI, action.data);
        console.log("loginAPI result", result);

        yield put({
            type: LOGIN_SUCCESS,
            data: result.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: LOGIN_FAILURE,
            data: err.response.data,
        });
    }
}
//로그인 상태 여부 체크(ssr)
// function loadUserInfoAPI() {
//     return axios.get(`/certify/certifyCheck`);
// }

// function* loadUserInfo() {
//     try {
//         const result = yield call(loadUserInfoAPI);
//         console.log("loadUserInfoAPI result", result);

//         yield put({
//             type: LOAD_USER_INFO_SUCCESS,
//             data: result.data.data,
//         });
//     } catch (err) {
//         console.error(err);
//         yield put({
//             type: LOAD_USER_INFO_FAILURE,
//             data: err.response.data,
//         });
//     }
// }
//본인인증1
function certifyPageTypeAPI(data) {
    return axios.post(`/certify/${data}`);
}

function* certifyPageType(action) {
    try {
        const result = yield call(certifyPageTypeAPI, action.data);
        console.log("certifyPageTypeAPI result", result);

        yield put({
            type: CERTIFY_PAGETYPE_SUCCESS,
            data: result.data.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: CERTIFY_PAGETYPE_FAILURE,
            data: err.response.data,
        });
    }
}

//본인인증2 - 본인인증 화면 호출.
function certifyRequestAPI(data) {
    return axios.post(`/certify/requestCertification`, data);
}

function* certifyRequest(action) {
    try {
        const result = yield call(certifyRequestAPI, action.data);
        console.log("certifyRequestAPI result", result);

        yield put({
            type: CERTIFY_SUCCESS,
            data: result.data.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: CERTIFY_FAILURE,
            data: err.response.data,
        });
    }
}

//본인인증 상태 여부 체크(ssr)
function certifyUserInfoAPI() {
    return axios.get(`/certify/certifyCheck`);
}

function* certifyUserInfo() {
    try {
        const result = yield call(certifyUserInfoAPI);
        console.log("certifyUserInfoAPI result", result);

        yield put({
            type: CERTIFY_USER_INFO_SUCCESS,
            data: result.data.data.isCertify,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: CERTIFY_USER_INFO_FAILURE,
            data: err.response.data,
        });
    }
}

function* watchLoadTest() {
    yield takeLatest(GET_TEST_APT_REQUEST, getTest);
}
function* watchLoginUrl() {
    yield takeLatest(LOGIN_URL_REQUEST, loginRequestUrl);
}
function* watchLogin() {
    yield takeLatest(LOGIN_REQUEST, loginRequest);
}
function* watchCertifyPageType() {
    yield takeLatest(CERTIFY_PAGETYPE_REQUEST, certifyPageType);
}
function* watchCertify() {
    yield takeLatest(CERTIFY_REQUEST, certifyRequest);
}
function* watchCertifyUserInfo() {
    yield takeLatest(CERTIFY_USER_INFO_REQUEST, certifyUserInfo);
}
// function* watchLoadUserInfo() {
//     yield takeLatest(LOAD_USER_INFO_REQUEST, loadUserInfo);
// }

export default function* userSaga() {
    yield all([fork(watchLoadTest)]);
    yield all([fork(watchLoginUrl)]);
    yield all([fork(watchLogin)]);
    yield all([fork(watchCertifyPageType)]);
    yield all([fork(watchCertify)]);
    yield all([fork(watchCertifyUserInfo)]);
    // yield all([fork(watchLoadUserInfo)]);
}
