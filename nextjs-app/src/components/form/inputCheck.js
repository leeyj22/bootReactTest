import React from "react";
import { InputChkbox } from "../../style/FormStyle";

const InputCheck = ({ id, name, checked, onChange }) => {
    return (
        <InputChkbox>
            <input
                type="checkbox"
                id={id}
                name={id}
                checked={checked}
                onChange={onChange}
            />
            <label htmlFor={id}>{name}</label>
        </InputChkbox>
    );
};

export default InputCheck;
