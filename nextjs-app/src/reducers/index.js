//초기화
import { HYDRATE } from "next-redux-wrapper";
import { combineReducers } from "redux";
import user from "./user";
import page from "./page";

//reducer
const rootReducer = (state, action) => {
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
