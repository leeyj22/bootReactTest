import React, { useEffect, useState } from "react";
import useFileInput from "../../hooks/useFileInput";
import { fileToSize } from "../../hooks/fileToSize";

const FileMulti = ({ name, maxSize, maxLen, fileList, onFormChange }) => {
    const {
        inputRef,
        selectedFiles,
        handleFileChange,
        handleRemoveFile,
        openFileDialog,
        isFileAddFailed,
    } = useFileInput(3, 10);

    useEffect(() => {
        if (selectedFiles.length > 1) {
            onFormChange(selectedFiles); // 상위 컴포넌트로 선택한 파일들 전달
        }
    }, [selectedFiles]);

    return (
        <div className="form-file">
            <div className="form-file-inner">
                {fileList == "Y" && (
                    <div className="file-list">
                        {selectedFiles.map((file, index) => {
                            return (
                                <div className="file-item" key={index}>
                                    <span title={file.name}>
                                        {file.name}({fileToSize(file.size)})
                                    </span>
                                    <button
                                        title="파일 빼기"
                                        className="btn-remove-file"
                                        key={index}
                                        onClick={() => handleRemoveFile(index)}
                                    ></button>
                                </div>
                            );
                        })}
                    </div>
                )}
                <input
                    type="file"
                    name={name}
                    className="get-file"
                    accept="image/*,video/*"
                    multiple
                    ref={inputRef}
                    onChange={(e) => handleFileChange(e.target.files)}
                />
                <button className="btn-file" onClick={openFileDialog}>
                    파일찾기
                </button>
            </div>
            <div className="notice">
                <p>
                    ※ 첨부파일은 최대 {maxLen}개, {maxSize} 이내 이미지, 동영상
                    파일만 첨부 가능합니다.
                </p>
                {/* <div className="can-file-extension">
                    * 등록 가능 확장자 : <em>.jpeg</em> <em>.gif</em>
                    <em>.png</em> <em>.jpg</em>
                </div> */}
            </div>
        </div>
    );
};

export default FileMulti;
