import axios from "axios";
import { backUrl } from "../config/config";
import { all, fork } from "redux-saga/effects";
import userSaga from "./user";

export default function* rootSaga() {
    axios.defaults.baseURL = backUrl;
    axios.defaults.withCredentials = true;
    yield all([fork(userSaga)]);
}
