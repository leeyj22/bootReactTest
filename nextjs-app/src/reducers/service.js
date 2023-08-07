import produce from "../util/produce";

export const initalState = {
    formData: {
        getMyRentalListLoading: true,
        getMyRentalListDone: false,
        getMyRentalListError: null,
        myLentalList: null,
        getTermLoading: true,
        getTermDone: false,
        getTermError: null,
        termTxt: null,
        submitServiceLoading: true,
        submitServiceDone: false,
        submitServiceError: null,
    },
};
//내 렌탈 제품 리스트 가져오기
//(서비스접수 : 제품 불러오기)
export const GET_MY_RENTAL_LIST_REQUEST = "GET_MY_RENTAL_LIST_REQUEST";
export const GET_MY_RENTAL_LIST_SUCCESS = "GET_MY_RENTAL_LIST_SUCCESS";
export const GET_MY_RENTAL_LIST_FAILURE = "GET_MY_RENTAL_LIST_FAILURE";

//약관 데이터 가져오기
export const GET_TERM_REQUEST = "GET_TERM_REQUEST";
export const GET_TERM_SUCCESS = "GET_TERM_SUCCESS";
export const GET_TERM_FAILURE = "GET_TERM_FAILURE";

//서비스 접수
export const SUBMIT_SERVICE_REQUEST = "SUBMIT_SERVICE_REQUEST";
export const SUBMIT_SERVICE_SUCCESS = "SUBMIT_SERVICE_SUCCESST";
export const SUBMIT_SERVICE_FAILURE = "SUBMIT_SERVICE_FAILURE";

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
            //약관 데이터 가져오기
            case GET_TERM_REQUEST:
                draft.getTermLoading = true;
                draft.getTermDone = false;
                draft.getTermError = null;
                break;
            case GET_TERM_SUCCESS:
                draft.getTermLoading = false;
                draft.getTermDone = true;
                draft.termTxt = action.data;
                break;
            case GET_TERM_FAILURE:
                draft.getTermLoading = false;
                draft.getTermError = action.error;
                break;
            //서비스 접수
            case SUBMIT_SERVICE_REQUEST:
                draft.submitServiceLoading = true;
                draft.submitServiceDone = false;
                draft.submitServiceError = null;
                break;
            case SUBMIT_SERVICE_SUCCESS:
                draft.submitServiceLoading = false;
                draft.submitServiceDone = true;
                break;
            case SUBMIT_SERVICE_FAILURE:
                draft.submitServiceLoading = false;
                draft.submitServiceError = action.error;
                break;
        }
    });
};

export default reducer;
