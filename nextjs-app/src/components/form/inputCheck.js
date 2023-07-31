import React from "react";
import { InputChkbox } from "../../style/FormStyle";

const InputCheck = ({ id, name }) => {
    return (
        <InputChkbox className="input-checkbox">
            <input type="checkbox" id={id} checked />
            <label htmlFor={id}>{name}</label>
        </InputChkbox>
    );
};

export default InputCheck;
