import produce from "../util/produce";

export const initalState = {
    formData: {},
};

export const SET_SERVICE_TRANSFER_DATA = "SET_SERVICE_TRANSFER_DATA";

export const reducer = (state = initalState, action) => {
    return produce(state, (d) => {
        const draft = d;
        switch (action.type) {
            case SET_SERVICE_TRANSFER_DATA:
                draft.formData[action.name] = action.data;
                break;
        }
    });
};

export default reducer;
