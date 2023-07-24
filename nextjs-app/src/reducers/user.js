import produce from "../util/produce";

export const initalState = {
  getTestLoading: true,
  getTestDone: false,
  getTestError: null,
  test: null,
};

export const GET_TEST_APT_REQUEST = "GET_TEST_APT_REQUEST";
export const GET_TEST_APT_SUCCESS = "GET_TEST_APT_SUCCESS";
export const GET_TEST_APT_FAILURE = "GET_TEST_APT_FAILURE";

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
    }
  });
};

export default reducer;
