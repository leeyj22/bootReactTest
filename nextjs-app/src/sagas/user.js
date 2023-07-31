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
} from "../reducers/user";

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
    return axios.post("member/login", data);
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

function* watchLoadTest() {
    yield takeLatest(GET_TEST_APT_REQUEST, getTest);
}
function* watchLoginUrl() {
    yield takeLatest(LOGIN_URL_REQUEST, loginRequestUrl);
}
function* watchLogin() {
    yield takeLatest(LOGIN_REQUEST, loginRequest);
}

export default function* userSaga() {
    yield all([fork(watchLoadTest)]);
    yield all([fork(watchLoginUrl)]);
    yield all([fork(watchLogin)]);
}
