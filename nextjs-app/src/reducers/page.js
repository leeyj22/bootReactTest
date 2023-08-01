import produce from "../util/produce";

export const initalState = {
    qnaLoading: true,
    qnaDone: false,
    qetQnaError: null,
    qna: null,
};

export const QNA_REQUEST = "QNA_REQUEST";
export const QNA_SUCCESS = "QNA_SUCCESS";
export const QNA_FAILURE = "QNA_FAILURE";

export const reducer = (state = initalState, action) => {
    return produce(state, (d) => {
        const draft = d;
        switch (action.type) {
            case QNA_REQUEST:
                draft.qnaLoading = true;
                draft.qnaDone = false;
                draft.qetQnaError = null;
                break;
            case QNA_SUCCESS:
                draft.qnaLoading = false;
                draft.qnaDone = true;
                draft.qna = action.data;
                break;
            case QNA_FAILURE:
                draft.qnaLoading = false;
                draft.qetQnaError = action.error;
                break;
        }
    });
};

export default reducer;
