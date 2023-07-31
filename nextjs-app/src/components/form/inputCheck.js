import React from "react";
import { InputChkbox } from "../../style/FormStyle";

const InputCheck = ({ id, name }) => {
    return (
        <InputChkbox>
            <input type="checkbox" id={id} />
            <label htmlFor={id}>{name}</label>
        </InputChkbox>
    );
};

export default InputCheck;
