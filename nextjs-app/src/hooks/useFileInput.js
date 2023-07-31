import React, { useRef, useState } from "react";

const useFileInput = (maxFiles, maxSizeInMB) => {
    const inputRef = useRef(null);
    const [selectedFiles, setSelectedFiles] = useState([]);
    const [isFileAddFailed, setIsFileAddFailed] = useState(false); // 실패 여부 변수 추가
    const maxFileSize = maxSizeInMB * 1024 * 1024; // MB를 bytes로 변환

    const handleFileChange = (files) => {
        // 기존 선택된 파일들과 새로 추가된 파일들을 합칩니다.
        const allFiles = [...selectedFiles, ...Array.from(files)];

        // 중복 파일을 제외합니다.
        const uniqueFiles = allFiles.reduce((acc, file) => {
            const existingFile = acc.find((f) => f.name === file.name);
            if (!existingFile) {
                acc.push(file);
            } else {
                // 중복 파일이 있을 경우 알림창을 띄워줍니다.
                alert(`${file.name} 파일명은 이미 추가하였습니다.`);
            }
            return acc;
        }, []);

        // 파일 용량을 계산하여 최대 허용 용량을 초과하는지 확인합니다.
        let totalSize = 0;
        for (const file of uniqueFiles) {
            totalSize += file.size;
        }

        if (totalSize <= maxFileSize && uniqueFiles.length <= maxFiles) {
            setSelectedFiles(uniqueFiles);
            setIsFileAddFailed(false); // 파일 추가 성공
        } else {
            if (totalSize > maxFileSize) {
                alert(
                    `파일 최대 허용 용량을 초과하였습니다. (최대 ${maxSizeInMB}MB)`
                );
                setIsFileAddFailed(true);
            } else {
                alert(`최대 ${maxFiles}개의 파일만 선택할 수 있습니다.`);
                setIsFileAddFailed(true);
            }
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
        isFileAddFailed,
    };
};

export default useFileInput;
