import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {
    GET_HOLIDAY_FAILURE,
    GET_HOLIDAY_REQUEST,
    GET_HOLIDAY_SUCCESS,
    GET_TRANSFER_PRICE_FAILURE,
    GET_TRANSFER_PRICE_REQUEST,
    GET_TRANSFER_PRICE_SUCCESS,
} from "../reducers/serviceTransfer";

//약관 데이터 가져오기
function getTransferPriceAPI(data) {
    return axios.post("/customer/getTransferPrice", data);
}

function* getTransferPrice(action) {
    try {
        const result = yield call(getTransferPriceAPI, action.data);
        console.log("getTransferPriceAPI result", result);

        if (result.data.status.code === "200") {
            yield put({
                type: GET_TRANSFER_PRICE_SUCCESS,
                data: result.data.data,
            });
        }
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_TRANSFER_PRICE_FAILURE,
            error: err.response.data,
        });
    }
}

//휴일 데이터 가져오기
function getHolidaysAPI(data) {
    return axios.post(`/customerHelp/findErpHoliday?holidayType=${data}`);
}

function* getHolidays(action) {
    try {
        const result = yield call(getHolidaysAPI, action.data);
        console.log("getHolidaysAPI result", result);

        yield put({
            type: GET_HOLIDAY_SUCCESS,
            data: result.data.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_HOLIDAY_FAILURE,
            error: err.response.data,
        });
    }
}

function* watchGetMyRentalList() {
    yield takeLatest(GET_TRANSFER_PRICE_REQUEST, getTransferPrice);
}
function* watchGetHolidays() {
    yield takeLatest(GET_HOLIDAY_REQUEST, getHolidays);
}

export default function* userSaga() {
    yield all([fork(watchGetMyRentalList)]);
    yield all([fork(watchGetHolidays)]);
}
