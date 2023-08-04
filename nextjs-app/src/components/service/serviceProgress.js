import React from "react";
import { ServiceProgressStyle } from "../../style/ServiceStyle";

const progressStep = [
    { id: 1, step: "1", name: "제품/접수 유형 선택", now: true, finish: false },
    { id: 2, step: "2", name: "신청 정보 입력", now: false, finish: false },
    { id: 3, step: "3", name: "결제 정보 입력", now: false, finish: false },
];

const ServiceProgress = ({ step, progress }) => {
    return (
        <ServiceProgressStyle step={step} progress={progress}>
            <ul>
                {progressStep.map((item) => {
                    return (
                        <li
                            key={item.id}
                            className={item.step < progress ? "active" : ""}
                        >
                            <span>{item.step}</span>
                            <em>{item.name}</em>
                        </li>
                    );
                })}
            </ul>
        </ServiceProgressStyle>
    );
};

export default ServiceProgress;
