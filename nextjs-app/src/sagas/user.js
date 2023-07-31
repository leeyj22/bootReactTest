import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {
    LOGIN_FAILURE,
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    GET_TEST_APT_FAILURE,
    GET_TEST_APT_REQUEST,
    GET_TEST_APT_SUCCESS,
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

// login
function loginAPI() {
    return axios.get("member/login");
}

function* login(action) {
    try {
        const result = yield call(loginAPI);
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
function* watchLogin() {
    yield takeLatest(LOGIN_REQUEST, login);
}

export default function* userSaga() {
    yield all([fork(watchLoadTest)]);
    yield all([fork(watchLogin)]);
}
