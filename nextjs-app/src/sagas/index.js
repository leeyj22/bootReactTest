import axios from "axios";
import { all, fork } from "redux-saga/effects";
import userSaga from "./user";

export default function* rootSaga() {
  axios.defaults.baseURL = "http://localhost:4343";
  axios.defaults.withCredentials = true;
  yield all([fork(userSaga)]);
}
