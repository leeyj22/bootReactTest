import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { ko } from "date-fns/locale";
import moment from "moment";
const Calendar = ({ name, formData, setFormData }) => {
    const [selectedDate, setSelectedDate] = useState(formData);
    useEffect(() => {
        if (formData == "") {
            setSelectedDate(null);
        }
    }, [formData]);
    const handleChange = (data) => {
        setSelectedDate(data);
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
            selected={selectedDate}
            onChange={(data) => handleChange(data)}
            placeholderText="YYYY-MM-DD"
            className="input-form-datepicker"
        />
    );
};

export default Calendar;
