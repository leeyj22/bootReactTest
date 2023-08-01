//초기화
import { HYDRATE } from "next-redux-wrapper";
import { combineReducers } from "redux";
import user from "./user";
import page from "./page";
const initalState = {
    user: {
        isLoggedIn: false,
        user: null,
        signUpdata: {},
        loginData: {},
    },
};

//reducer
const rootReducer = (state = initalState, action) => {
    switch (action.type) {
        case HYDRATE:
            console.log("HYDRATE", HYDRATE);
            return action.payload;
        default: {
            const combinedReducer = combineReducers({
                user,
                page,
            });
            return combinedReducer(state, action);
        }
    }
};

export default rootReducer;
