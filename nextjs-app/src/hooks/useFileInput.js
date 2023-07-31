import React, { useRef, useState } from "react";

const useFileInput = (maxFiles) => {
    const inputRef = useRef(null);
    const [selectedFiles, setSelectedFiles] = useState([]);

    const handleFileChange = (files) => {
        // 기존 선택된 파일들과 새로 추가된 파일들을 합칩니다.
        const allFiles = [...selectedFiles, ...Array.from(files)];

        // 최대 파일 개수를 확인하여 제한
        if (allFiles.length <= maxFiles) {
            setSelectedFiles(allFiles);
        } else {
            alert(`최대 ${maxFiles}개의 파일만 선택할 수 있습니다.`);
        }
    };

    const openFileDialog = () => {
        if (inputRef.current) {
            inputRef.current.click();
        }
    };

    const handleRemoveFile = (index) => {
        const updatedFiles = selectedFiles.filter((file, i) => i !== index);
        setSelectedFiles(updatedFiles);
    };

    return {
        inputRef,
        selectedFiles,
        handleFileChange,
        handleRemoveFile,
        openFileDialog,
    };
};

export default useFileInput;
