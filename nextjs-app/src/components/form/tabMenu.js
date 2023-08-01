import React from "react";
import { TabStyle1 } from "../../style/FormStyle";

const TabMenu = () => {
    return (
        <TabStyle1>
            <ul>
                <li>
                    <button className="active">바디프랜드</button>
                </li>
                <li>
                    <button>라클라우드</button>
                </li>
                <li>
                    <button>W정수기</button>
                </li>
                <li>
                    <button>기타제품</button>
                </li>
            </ul>
        </TabStyle1>
    );
};

export default TabMenu;
