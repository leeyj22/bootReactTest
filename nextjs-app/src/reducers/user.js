import produce from "../util/produce";

export const initalState = {
    getTestLoading: true,
    getTestDone: false,
    getTestError: null,
    test: null,
    loginUrlLoading: true,
    loginUrlDone: false,
    loginUrlError: null,
    loginUrl: null,
    loginLoading: true,
    loginDone: false,
    loginError: null,
    user: null,
};

export const GET_TEST_APT_REQUEST = "GET_TEST_APT_REQUEST";
export const GET_TEST_APT_SUCCESS = "GET_TEST_APT_SUCCESS";
export const GET_TEST_APT_FAILURE = "GET_TEST_APT_FAILURE";

export const LOGIN_URL_REQUEST = "LOGIN_URL_REQUEST";
export const LOGIN_URL_SUCCESS = "LOGIN_URL_SUCCESS";
export const LOGIN_URL_FAILURE = "LOGIN_URL_FAILURE";

export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN_FAILURE";

export const reducer = (state = initalState, action) => {
    return produce(state, (d) => {
        const draft = d;
        switch (action.type) {
            case GET_TEST_APT_REQUEST:
                draft.getTestLoading = true;
                draft.getTestDone = false;
                draft.getTestError = null;
                break;
            case GET_TEST_APT_SUCCESS:
                draft.getTestLoading = false;
                draft.getTestDone = true;
                draft.test = action.data;
                break;
            case GET_TEST_APT_FAILURE:
                draft.getTestLoading = false;
                draft.getTestError = action.error;
                break;
            // login
            case LOGIN_URL_REQUEST:
                draft.loginUrlLoading = true;
                draft.loginUrlDone = false;
                draft.loginUrlError = null;
                break;
            case LOGIN_URL_SUCCESS:
                draft.loginUrlLoading = false;
                draft.loginUrlDone = true;
                draft.loginUrl = action.data;
                break;
            case LOGIN_URL_FAILURE:
                draft.loginUrlLoading = false;
                draft.loginUrlError = action.error;
                break;
            case LOGIN_REQUEST:
                draft.loginLoading = true;
                draft.loginDone = false;
                draft.loginError = null;
                break;
            case LOGIN_SUCCESS:
                draft.loginLoading = false;
                draft.loginUrlDone = true;
                draft.user = action.data;
                break;
            case LOGIN_FAILURE:
                draft.loginLoading = false;
                draft.loginError = action.error;
                break;
        }
    });
};

export default reducer;
