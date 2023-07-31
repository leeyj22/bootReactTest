import { useState } from "react";

// 팝업 컴포넌트를 show와 hide 시킬 수 있는 custom hook
const usePopup = () => {
    const [isPopupVisible, setPopupVisible] = useState(false);

    // 팝업을 보이게 하는 함수
    const showPopup = () => {
        setPopupVisible(true);
    };

    // 팝업을 숨기게 하는 함수
    const hidePopup = () => {
        setPopupVisible(false);
    };

    // 팝업 상태와 보이기/숨기기 함수를 반환
    return {
        isPopupVisible,
        showPopup,
        hidePopup,
    };
};

export default usePopup;
