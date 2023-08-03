import React, { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { ko } from "date-fns/locale";
const Calendar = () => {
    const [selectedDate, setSelectedDate] = useState(null);
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
            onChange={(date) => setSelectedDate(date)}
            placeholderText="YYYY-MM-DD"
            className="input-form-datepicker"
        />
    );
};

export default Calendar;
