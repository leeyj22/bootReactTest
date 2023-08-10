//초기화
import { HYDRATE } from "next-redux-wrapper";
import { combineReducers } from "redux";
import user from "./user";
import page from "./page";
import service from "./service";
import setviceTransfer from "./serviceTransfer";

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
                service,
                setviceTransfer,
            });
            return combinedReducer(state, action);
        }
    }
};

export default rootReducer;
