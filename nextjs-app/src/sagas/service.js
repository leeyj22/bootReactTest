import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {} from "../reducers/user";
import {
    GET_MY_RENTAL_LIST_FAILURE,
    GET_MY_RENTAL_LIST_REQUEST,
    GET_MY_RENTAL_LIST_SUCCESS,
    GET_TERM_FAILURE,
    GET_TERM_REQUEST,
    GET_TERM_SUCCESS,
    SUBMIT_SERVICE_REQUEST,
    SUBMIT_SERVICE_SUCCESS,
} from "../reducers/service";

//약관 데이터 가져오기
function getTermAPI(data) {
    return axios.get(`/customer/getTerms?term_index=${data}`);
}

function* getTerm(action) {
    try {
        const result = yield call(getTermAPI, action.data);
        console.log("getTermAPI result", result);

        yield put({
            type: GET_TERM_SUCCESS,
            data: result.data.data.serviceTerms,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_TERM_FAILURE,
            error: err.response.data,
        });
    }
}
//서비스접수 > 나의 제품 리스트 가져오기
function getMyRentalListAPI(data) {
    return axios.get(
        `/api/myinfo/getMyAllProductList?name=${data.name}&phone=${data.phone}&useImg=N`
    );
}

function* getMyRentalList(action) {
    try {
        const result = yield call(getMyRentalListAPI, action.data);
        console.log("getMyRentalList result", result);

        yield put({
            type: GET_MY_RENTAL_LIST_SUCCESS,
            data: result.data.data.productList,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_MY_RENTAL_LIST_FAILURE,
            error: err.response.data,
        });
    }
}
//서비스 접수하기
function submitServiceAPI(data) {
    return axios.post("/api/customer/saveAfterService", data);
}

function* submitService(action) {
    try {
        const result = yield call(submitServiceAPI, action.data);
        console.log("submitServiceAPI result", result);

        if (result.data.status.code == 200) {
            yield put({
                type: SUBMIT_SERVICE_SUCCESS,
            });
        }
    } catch (err) {
        console.error(err);
        yield put({
            type: SUBMIT_SERVICE_FAILURE,
            error: err.response.data,
        });
    }
}

function* watchGetMyRentalList() {
    yield takeLatest(GET_MY_RENTAL_LIST_REQUEST, getMyRentalList);
}
function* watchGetTerm() {
    yield takeLatest(GET_TERM_REQUEST, getTerm);
}
function* watchSubmitService() {
    yield takeLatest(SUBMIT_SERVICE_REQUEST, submitService);
}

export default function* userSaga() {
    yield all([fork(watchGetMyRentalList)]);
    yield all([fork(watchGetTerm)]);
    yield all([fork(watchSubmitService)]);
}
