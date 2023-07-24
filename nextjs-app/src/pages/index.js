import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import AppLayout from "../components/AppLayout";
import { GET_TEST_APT_REQUEST } from "../reducers/user";

const index = () => {
  const dispatch = useDispatch();

  const plz = () => {
  console.log('asdsadasd');
     dispatch({
          type: GET_TEST_APT_REQUEST,
          data: "jm91",
        });
  }
  return (
    <AppLayout>
      <button onClick={() => plz()}>버튼</button>
      <h3>리스트</h3>sfdgsdgsdgsdg
    </AppLayout>
  );
};

export default index;
