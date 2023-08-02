import produce from "../util/produce";

export const initalState = {
    getTestLoading: true,
    getTestDone: false,
    getTestError: null,
    test: null,
    //로그인
    loginUrlLoading: true,
    loginUrlDone: false,
    loginUrlError: null,
    loginUrl: null,
    loginLoading: true,
    loginDone: false, //로그인상태. 본인인증했을 경우 true
    loginError: null,
    //본인인증
    certifyPagetypeLoading: true,
    certifyPagetypeDone: false,
    certifyPagetypeError: null,
    certifyPageType: null,
    certifyLoading: true,
    certifyDone: false,
    certifyError: null,
    certifyFormData: null,
    certifyInfo: null,
    certifyState: false, //본인확인여부
    user: null,
};

export const GET_TEST_APT_REQUEST = "GET_TEST_APT_REQUEST";
export const GET_TEST_APT_SUCCESS = "GET_TEST_APT_SUCCESS";
export const GET_TEST_APT_FAILURE = "GET_TEST_APT_FAILURE";

//로그인
export const LOGIN_URL_REQUEST = "LOGIN_URL_REQUEST";
export const LOGIN_URL_SUCCESS = "LOGIN_URL_SUCCESS";
export const LOGIN_URL_FAILURE = "LOGIN_URL_FAILURE";

export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN_FAILURE";

// 본인인증
export const CERTIFY_PAGETYPE_REQUEST = "CERTIFY_PAGETYPE_REQUEST";
export const CERTIFY_PAGETYPE_SUCCESS = "CERTIFY_PAGETYPE_SUCCESS";
export const CERTIFY_PAGETYPE_FAILURE = "CERTIFY_PAGETYPE_FAILURE";
export const CERTIFY_REQUEST = "CERTIFY_REQUEST";
export const CERTIFY_SUCCESS = "CERTIFY_SUCCESS";
export const CERTIFY_FAILURE = "CERTIFY_FAILURE";
export const CERTIFY_SAVE_LOCALSTOREGE_REQUEST =
    "CERTIFY_SAVE_LOCALSTOREGE_REQUEST";
export const CERTIFY_SAVE_SSR_REQUEST = "CERTIFY_SAVE_SSR_REQUEST";

//로그인(본인인증상태) 여부 체크
export const LOAD_USER_INFO_REQUEST = "LOAD_USER_INFO_REQUEST";
export const LOAD_USER_INFO_SUCCESS = "LOAD_USER_INFO_SUCCESS";
export const LOAD_USER_INFO_FAILURE = "LOAD_USER_INFO_FAILURE";

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
                draft.loginDone = true;
                draft.user = action.data;
                break;
            case LOGIN_FAILURE:
                draft.loginLoading = false;
                draft.loginError = action.error;
                break;
            //본인인증1
            case CERTIFY_PAGETYPE_REQUEST:
                draft.certifyPagetypeLoading = true;
                draft.certifyPagetypeDone = false;
                draft.certifyPagetypeError = null;
                break;
            case CERTIFY_PAGETYPE_SUCCESS:
                draft.certifyPagetypeLoading = false;
                draft.certifyPagetypeDone = true;
                draft.certifyPageType = action.data;
                break;
            case CERTIFY_PAGETYPE_FAILURE:
                draft.certifyPagetypeLoading = false;
                draft.certifyPagetypeError = action.error;
                break;
            //본인인증2
            case CERTIFY_REQUEST:
                draft.certifyLoading = true;
                draft.certifyDone = false;
                draft.certifyError = null;
                break;
            case CERTIFY_SUCCESS:
                draft.certifyLoading = false;
                draft.certifyDone = true;
                draft.certifyFormData = action.data;
                break;
            case CERTIFY_FAILURE:
                draft.certifyLoading = false;
                draft.certifyError = action.error;
                break;
            // 본인인증3
            case CERTIFY_SAVE_SSR_REQUEST:
                draft.certifyInfo = action.data;
                draft.certifyState = true;
                break;
            case CERTIFY_SAVE_LOCALSTOREGE_REQUEST:
                draft.certifyInfo = action.data;
                draft.loginDone = true;
                break;
            //로그인, 본인인증 상태 여부 체크(ssr)
            case LOAD_USER_INFO_REQUEST:
                draft.loginLoading = true;
                draft.loginDone = false;
                draft.loginError = null;
                break;
            case LOAD_USER_INFO_SUCCESS:
                draft.loginLoading = false;
                draft.loginDone = true;
                break;
            case LOAD_USER_INFO_FAILURE:
                draft.loginLoading = false;
                draft.loginError = action.error;
                break;
        }
    });
};

export default reducer;
