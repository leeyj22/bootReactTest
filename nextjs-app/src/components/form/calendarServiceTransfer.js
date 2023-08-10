import React, { useCallback, useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { ko } from "date-fns/locale";
import moment from "moment";
import { dateData } from "../../func/service";
import { useSelector } from "react-redux";

const CalendarServiceTransfer = ({
    name,
    grpCode,
    formData,
    setFormData,
    orderTypeSub,
}) => {
    const { holidayData } = useSelector((state) => state.setviceTransfer);
    const [calendarDate, setCalendarDate] = useState({
        selectedDate: formData[name], //선택날짜
        minDate: "", //시작날짜
        maxDate: "", //마지막 날짜
        excludedDates: [], //제외날짜
    });

    const chkMinDate = (name) => {
        const date =
            formData[name] !== "" ? new Date(formData[name]) : new Date();

        if (name === "unInsDate") {
            const newMinDate = dateData.checkLocation(date, formData.location);
            setFormData((prevFormDate) => ({
                ...prevFormDate,
                minDate: newMinDate,
            }));
        }
    };
    useEffect(() => {
        //기본 셋팅(휴일/주말 제외, 시작일, 마지막일 셋팅)
        if (holidayData !== null) {
            const settingData = dateData.init({ name, grpCode, holidayData });
            setCalendarDate({
                ...calendarDate,
                minDate: settingData.minDate,
                maxDate: settingData.maxDate,
                excludedDates: dateData.dateStringToNewDate(
                    settingData.excludesDates
                ),
            });
        }
    }, [holidayData]);

    useEffect(() => {
        if (
            (orderTypeSub == "1" || orderTypeSub == "5") &&
            formData[name] !== "" &&
            formData[name] !== undefined
        ) {
            chkMinDate(name);
        }
    }, [formData[name]]);

    useEffect(() => {
        let minDate = formData.minDate;
        if (name === "insDate") {
            setCalendarDate({
                ...calendarDate,
                selectedDate: minDate,
                minDate: minDate,
            });
            setFormData((prevFormDate) => ({
                ...prevFormDate,
                [name]: moment(minDate).format("YYYY-MM-DD"),
            }));
        }
    }, [formData.minDate]);

    useEffect(() => {
        if (
            (orderTypeSub == "1" || orderTypeSub == "5") &&
            formData[name] !== "" &&
            formData[name] !== undefined
        ) {
            chkMinDate(name);
        }
    }, [formData.location]);

    //날짜 선택
    const handleChange = (data) => {
        setCalendarDate({
            ...calendarDate,
            selectedDate: data,
        });

        if (data) {
            const date = moment(data).format("YYYY-MM-DD");
            setFormData((prevFormDate) => ({
                ...prevFormDate,
                [name]: date,
            }));
        }
    };

    return (
        <DatePicker
            dateFormat="yyyy-MM-dd"
            dateFormatCalendar="yyyy년 MM월"
            // selectsRange={false}
            locale={ko}
            shouldCloseOnSelect
            showMonthDropdown
            showYearDropdown
            dropdownMode="select"
            // monthsShown={2}
            selected={calendarDate.selectedDate}
            onChange={(data) => handleChange(data)}
            placeholderText="YYYY-MM-DD"
            className="input-form-datepicker"
            excludeDates={calendarDate.excludedDates}
            minDate={calendarDate.minDate}
            maxDate={calendarDate.maxDate}
        />
    );
};

export default CalendarServiceTransfer;
