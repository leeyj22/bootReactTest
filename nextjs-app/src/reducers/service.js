import produce from "../util/produce";

export const initalState = {
    formData: {
        getMyRentalListLoading: true,
        getMyRentalListDone: false,
        getMyRentalListError: null,
        myLentalList: null,
    },
};

export const GET_MY_RENTAL_LIST_REQUEST = "GET_MY_RENTAL_LIST_REQUEST";
export const GET_MY_RENTAL_LIST_SUCCESS = "GET_MY_RENTAL_LIST_SUCCESS";
export const GET_MY_RENTAL_LIST_FAILURE = "GET_MY_RENTAL_LIST_FAILURE";

export const reducer = (state = initalState, action) => {
    return produce(state, (d) => {
        const draft = d;
        switch (action.type) {
            case GET_MY_RENTAL_LIST_REQUEST:
                draft.getMyRentalListLoading = true;
                draft.getMyRentalListDone = false;
                draft.getMyRentalListError = null;
                break;
            case GET_MY_RENTAL_LIST_SUCCESS:
                draft.getMyRentalListLoading = false;
                draft.getMyRentalListDone = true;
                draft.myLentalList = action.data;
                break;
            case GET_MY_RENTAL_LIST_FAILURE:
                draft.getMyRentalListLoading = false;
                draft.getMyRentalListError = action.error;
                break;
        }
    });
};

export default reducer;
