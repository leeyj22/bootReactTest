import React from "react";
import { InputChkbox } from "../../style/FormStyle";

const InputCheck = ({ id, name, necessary, checked, onChange }) => {
    return (
        <InputChkbox>
            <input
                type="checkbox"
                id={id}
                name={id}
                checked={checked}
                onChange={onChange}
            />
            <label htmlFor={id}>
                <em>
                    {necessary === "chkAll"
                        ? ""
                        : necessary == "Y"
                        ? "[필수]"
                        : "[선택]"}
                </em>
                <span dangerouslySetInnerHTML={{ __html: name }}></span>
            </label>
        </InputChkbox>
    );
};

export default InputCheck;
