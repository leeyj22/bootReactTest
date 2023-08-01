import { all, delay, fork, put, takeLatest, call } from "redux-saga/effects";
import axios from "axios";
import {} from "../reducers/user";
import { QNA_FAILURE, QNA_REQUEST, QNA_SUCCESS } from "../reducers/page";

//QNA 자주 묻는 질문
function getQnaAPI(data) {
    return axios.post(`/customer/selectFaqList`, {
        classification: "others",
        classification_sub: "all",
        search: "",
    });
}
function* getQna(action) {
    try {
        const result = yield call(getQnaAPI);
        console.log("getQnaAPI result", result);

        yield put({
            type: QNA_SUCCESS,
            data: result.data,
        });
    } catch (err) {
        console.error(err);
        yield put({
            type: QNA_FAILURE,
            data: err.response.data,
        });
    }
}

function* watchQna() {
    yield takeLatest(QNA_REQUEST, getQna);
}

export default function* userSaga() {
    yield all([fork(watchQna)]);
}
