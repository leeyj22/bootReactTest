import produce from "../util/produce";

export const initalState = {
    getTransferPriceLoading: false,
    getTransferPriceDone: false,
    getTransferPriceError: null,
    transferPrice: null,
    getHolidayLoading: false,
    getHolidayDone: false,
    getHolidayError: null,
    holidayData: null,
};

export const SET_SERVICE_TRANSFER_DATA = "SET_SERVICE_TRANSFER_DATA";

//이전설치 가격계산
export const GET_TRANSFER_PRICE_REQUEST = "GET_TRANSFER_PRICE_REQUEST";
export const GET_TRANSFER_PRICE_SUCCESS = "GET_TRANSFER_PRICE_SUCCESS";
export const GET_TRANSFER_PRICE_FAILURE = "GET_TRANSFER_PRICE_FAILURE";

//휴무일 데이터
export const GET_HOLIDAY_REQUEST = "GET_HOLIDAY_REQUEST";
export const GET_HOLIDAY_SUCCESS = "GET_HOLIDAY_SUCCESS";
export const GET_HOLIDAY_FAILURE = "GET_HOLIDAY_FAILURE";

export const reducer = (state = initalState, action) => {
    return produce(state, (d) => {
        const draft = d;
        switch (action.type) {
            case GET_TRANSFER_PRICE_REQUEST:
                draft.getTransferPriceLoading = true;
                draft.getTransferPriceDone = false;
                draft.getTransferPriceError = true;
                break;
            case GET_TRANSFER_PRICE_SUCCESS:
                draft.getTransferPriceLoading = false;
                draft.getTransferPriceDone = true;
                draft.transferPrice = action.data;
                break;
            case GET_TRANSFER_PRICE_FAILURE:
                draft.getTransferPriceLoading = false;
                draft.getTransferPriceError = action.error;
                break;
            case GET_HOLIDAY_REQUEST:
                draft.getHolidayLoading = true;
                draft.getHolidayDone = false;
                draft.getHolidayError = true;
                break;
            case GET_HOLIDAY_SUCCESS:
                draft.getHolidayLoading = false;
                draft.getHolidayDone = true;
                draft.holidayData = action.data;
                break;
            case GET_HOLIDAY_FAILURE:
                draft.getHolidayLoading = false;
                draft.getHolidayError = action.error;
                break;
        }
    });
};

export default reducer;
