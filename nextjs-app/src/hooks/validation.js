import React, { useEffect } from "react";
export const Validation = {
    isEmpty: (targetStr) => {
        if (
            targetStr === undefined ||
            targetStr.toString().replace(/\s/g, "") === ""
        ) {
            return true;
        }
        return false;
    },
};
