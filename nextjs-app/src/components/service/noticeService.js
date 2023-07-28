import React from "react";
import { NoticeServiceStyle } from "../../style/ServiceStyle";
import { notice } from "../../data/notice";

const NoticeService = ({ noticeName }) => {
    return (
        <NoticeServiceStyle className="notice-wrap">
            <h4>유의사항</h4>
            <div>
                <ul>
                    {notice[noticeName].map((txt, idx) => {
                        return (
                            <li
                                key={idx}
                                dangerouslySetInnerHTML={{ __html: txt }}
                            ></li>
                        );
                    })}
                </ul>
            </div>
        </NoticeServiceStyle>
    );
};

export default NoticeService;
