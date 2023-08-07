import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {} from "../reducers/user";
import {
    GET_MY_RENTAL_LIST_FAILURE,
    GET_MY_RENTAL_LIST_REQUEST,
    GET_MY_RENTAL_LIST_SUCCESS,
} from "../reducers/service";

//test
function getMyRentalListAPI(data) {
    return axios.get(
        `/customerHelp/selectMyRentalList_Inst?name=${data.name}&phone=${data.phone}`
    );
}

function* getMyRentalList(action) {
    try {
        const result = yield call(getMyRentalListAPI, action.data);
        console.log("getMyRentalList result", result);

        yield put({
            type: GET_MY_RENTAL_LIST_SUCCESS,
            data: result.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: GET_MY_RENTAL_LIST_FAILURE,
            data: err.response.data,
        });
    }
}

function* watchGetMyRentalList() {
    yield takeLatest(GET_MY_RENTAL_LIST_REQUEST, getMyRentalList);
}

export default function* userSaga() {
    yield all([fork(watchGetMyRentalList)]);
}
