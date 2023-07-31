import React, { useEffect, useState } from "react";
import useFileInput from "../../hooks/useFileInput";

const FileMulti = ({ name, maxSize, maxLen, fileList, onFormChange }) => {
    const { inputRef, selectedFiles, handleFileChange, openFileDialog } =
        useFileInput();

    const handleChangeSelectedFiles = (files) => {
        handleFileChange(files); // 파일 선택 상태 업데이트
        onFormChange(files); // 상위 컴포넌트로 선택한 파일들 전달
    };
    const handleRemoveFile = (index) => {
        const updateFile = selectedFiles.filter((file, i) => i !== index);
        handleChangeSelectedFiles(updateFile);
    };

    return (
        <div className="form-file">
            <div className="form-file-inner">
                {fileList == "Y" && (
                    <div className="file-list">
                        {selectedFiles.map((file, index) => {
                            return (
                                <div className="file-item" key={index}>
                                    <span>{file.name}</span>
                                    <button
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
                    onChange={(e) => handleChangeSelectedFiles(e.target.files)}
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
