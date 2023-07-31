import React, { useRef, useState } from "react";

const useFileInput = () => {
    const inputRef = useRef(null);
    const [selectedFiles, setSelectedFiles] = useState([]);

    const handleFileChange = (e) => {
        const files = Array.from(e.target.files);
        setSelectedFiles(files);
    };

    const openFileDialog = () => {
        if (inputRef.current) {
            inputRef.current.click();
        }
    };

    return {
        inputRef,
        selectedFiles,
        handleFileChange,
        openFileDialog,
    };
};

export default useFileInput;
