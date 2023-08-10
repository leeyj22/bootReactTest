import React, { useEffect, useState } from "react";
import { api } from "./api";
import { common } from "./common";

export const service = {
    setPrice: (formData) => {
        //---안마의자,라클라우드
        //포장운반 : 회수 + 재설치 							 			 orderTypeSub 1
        //다른층 이동  : 회수 + 재설치 ,회수 희망일 뺌, 재설치 희망일 있음 			 orderTypeSub 3
        //같은층 : 회수 정보X , 진행만					 			 		 orderTypeSub 4

        //---W정수기
        //W정수기 철거,재설치 : 철거 + 재설치								 orderTypeSub 5
        //W정수기 철거 -> 철거만											 orderTypeSub 6
        //W정수기 재설치 -> 재설치만										 orderTypeSub 7
        let oriPrice = 0;
        let oriPrice_L = 0;
        let addPrice = 0;
        let totalPrice = 0;
        let serviceName = ""; //결제 서비스 유형 설정

        switch (formData.orderTypeSub) {
            case "1":
                oriPrice = 200000;
                oriPrice_L = 300000;
                serviceName = "포장 운반(회수 + 재설치)";

                const oldArea = formData.orderAddr1.split(" ");
                const recArea = formData.receiveAddr1.split(" ");
                //console.log("oldArea : ", oldArea)
                //console.log("recArea : ", recArea)

                if (
                    common.areaChange(oldArea[0]) == "50" ||
                    common.areaChange(recArea[0]) == "50"
                ) {
                    if (
                        common.areaChange(oldArea[0]).length > 0 &&
                        common.areaChange(recArea[0]).length > 0
                    ) {
                        // 회수, 재설치 주소가 있을때
                        if (
                            common.areaChange(oldArea[0]) !=
                            common.areaChange(recArea[0])
                        ) {
                            // 회수와 재설치 권역이 다르면 부과
                            if (formData.prdtCate == "L") {
                                addPrice = 100000;
                            } else {
                                addPrice = 80000;
                            }
                            //addPrice = 80000;
                        }
                    }
                }
                break;
            case "2":
                oriPrice = 200000;
                oriPrice_L = 300000;
                addPrice = 80000;
                break;
            case "3":
                oriPrice = 60000;
                oriPrice_L = 150000;
                serviceName = "다른층 이동";
                break;

            case "4":
                oriPrice = 50000;
                oriPrice_L = 80000;
                serviceName = "같은층 이동";
                break;

            case "5":
                oriPrice = 50000;
                oriPrice_L = 100000;
                //결제 서비스 유형 설정
                serviceName = "철거+재설치";

                break;

            case "6":
                oriPrice = 30000;
                serviceName = "철거";
                break;

            case "7":
                oriPrice = 30000;
                serviceName = "재설치";
                break;

            default:
                break;
        }
    },
};

export const dateData = {
    disabledDate: null,
    today: null,
    startDate: null,
    endDate: null,
    nowDay: null,
    grpCode: null,
    calName: "", //캘린더 이름 (unInsDate회수|철거|분해 , insDate 설치|조립, moveDate 이사)
    param: "T", //파라미터 이전설치 T, 분해조립 A
    init: (data) => {
        // 날짜 셋팅(+7일뒤 최대 60일까지만 선택 가능)
        const self = dateData;
        self.disabledDate = [];
        self.today = new Date(); //오늘 날짜
        self.startDate = new Date();
        self.endDate = new Date();
        self.nowDay = self.today.toISOString().slice(0, 10); //YYYY-MM-DD 형식으로 저장
        self.calName = data.name;

        self.grpCode = data.grpCode; //카테고리 코드 M,L,W

        //기본 신청일~최대가능날짜 셋팅
        // 이사날짜 : 주말 상관없이 일단 체크 가능.
        if (self.calName !== "moveDate") {
            //회수|철거, 설치
            self.setStartEndDate();
            self.setFilterWeekend();
        } else {
            //이사
            self.setStartEndDateMoveDate();
        }

        //특정날짜 휴무일 셋팅
        self.setCompanyDisabledDate();

        //기본 휴무일 제외
        self.getHoliday(data.holidayData);

        return {
            minDate: self.startDate,
            maxDate: self.endDate,
            excludesDates: [...new Set(self.disabledDate)],
        };
    },
    dateStringToNewDate: (dateString) => {
        return dateString.map((dateString) => new Date(dateString));
    },
    setStartEndDate: () => {
        const self = dateData;
        // 날짜 셋팅(+7일뒤 최대 60일까지만 선택 가능)

        self.startDate.setDate(self.startDate.getDate() + 7); //신청일+7일
        self.endDate.setDate(self.endDate.getDate() + 60); //신청일로 부터 최대 60일
    },
    setStartEndDateMoveDate: () => {
        const self = dateData;
        // 날짜 셋팅(+8일뒤 최대 60일까지만 선택 가능)

        self.startDate.setDate(self.startDate.getDate() + 8); //신청일+8일
        self.endDate.setMonth(self.endDate.getMonth() + 6); //신청일로 부터 6개월까지 보임
    },
    getHoliday: (holidays) => {
        //휴무일 셋팅
        const self = dateData;

        //휴일 제외
        for (const holiday of holidays) {
            if (
                (self.grpCode === "M" && holiday.chair === "Y") ||
                (self.grpCode === "L" && holiday.lacloud === "Y") ||
                (self.grpCode === "W" && holiday.water === "Y")
            ) {
                self.disabledDate.push(holiday.limitDate);
            }
        }
    },

    setCompanyDisabledDate: () => {
        //특수날짜 하드코딩 추가
        //성수기 안마의자 배송집중을 위한 이전설치 접수 제한 건으로 불가일 추가 셋팅
        const self = dateData;
        let companyDisabledDate = [],
            _startDate = new Date("2022-01-18"),
            _endDate = new Date("2022-01-23"),
            _checkDate = _startDate;

        while (_checkDate < _endDate) {
            companyDisabledDate.push(_checkDate.toISOString().slice(0, 10)); //YYYY-MM-DD 형식으로 저장

            _checkDate.setDate(_checkDate.getDate() + 1);
        }

        self.disabledDate = self.disabledDate.concat(companyDisabledDate);
    },
    setFilterWeekend: () => {
        //주말제외
        const self = dateData;

        const weekdayArray = []; //평일
        const weekendArray = []; //주말
        const startDate = self.startDate;
        const endDate = self.endDate;
        const currentDate = new Date(startDate);

        while (currentDate <= endDate) {
            const dayOfWeek = currentDate.getDay();
            // dayOfWeek 0 토요일 , dayOfWeek 6 일요일
            if (dayOfWeek !== 0 && dayOfWeek !== 6) {
                weekdayArray.push(currentDate.toISOString().slice(0, 10));
            } else {
                weekendArray.push(currentDate.toISOString().slice(0, 10)); // YYYY-MM-DD 형식으로 저장
            }
            currentDate.setDate(currentDate.getDate() + 1);
        }

        self.disabledDate = self.disabledDate.concat(weekendArray);
    },
    checkLocationMoveDate: (date, location) => {
        const self = dateData;
        //회수, 철거, 설치 변경 -> 이사일
        if (date == void 0 || date == "") {
            return;
        }
        const diffDate = location == "1" ? 14 : 7;
        const timemilli = date.getTime() + 1000 * 60 * 60 * 24 * diffDate;
        let minDate = new Date(timemilli);

        while (self.checkHolidays(minDate) !== true) {
            let newDate = minDate.getTime() + 1000 * 60 * 60 * 24;

            minDate = new Date(newDate);
        }

        return minDate;
    },
    checkLocation: (date, location) => {
        const self = dateData;
        // location 의 값 = 1  	외륙(제주도 등) ↔ 내륙 = 내·외륙간의 재설치 	희망일로부터 	2주 뒤
        // location 의 값 = 2	내륙간 이동 = 타 지역간의 재설치 				희망일로부터 	1주 뒤

        // 회수,철거일 변경 -> 설치일 셋팅
        if (date == void 0 || date == "") {
            return;
        }
        const diffDate = location == "1" ? 14 : 7;
        const timemilli = date.getTime() + 1000 * 60 * 60 * 24 * diffDate;
        let minDate = new Date(timemilli);

        while (self.checkHolidays(minDate) !== true) {
            let newDate = minDate.getTime() + 1000 * 60 * 60 * 24;

            minDate = new Date(newDate);
        }

        return minDate;
    },
    checkHolidays: (minDate) => {
        const self = dateData;
        // 휴일 및 안되는 날짜 계산.
        // 가장 가까운 날짜로 지정.

        if (!Array.isArray(self.disabledDate)) {
            // self.disabledDate가 배열이 아닌 경우에 대한 처리
            return false;
        }

        const isDateDisabled = self.disabledDate.some((disabledDate) => {
            return new Date(disabledDate) === minDate;
        });

        const isWeekend = [0, 6].includes(minDate.getDay());

        return !isDateDisabled && !isWeekend;
    },
};
