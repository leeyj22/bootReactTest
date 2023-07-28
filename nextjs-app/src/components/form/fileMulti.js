import React, { useState } from "react";
import useFileInput from "../../hooks/useFileInput";

const FileMulti = ({ name, maxSize, maxLen, fileList, onFormChange }) => {
    const { inputRef, selectedFiles, handleFileChange, openFileDialog } =
        useFileInput();

    // const handleChange = (e) => {
    //     const { name, value } = e.target;

    //     console.log("file value", value);

    //     setFileData({
    //         ...fileData,
    //         [name]: value,
    //     });

    //     onFormChange({
    //         ...fileData,
    //         [name]: value,
    //     });
    // };

    return (
        <div className="form-file">
            <div className="form-file-inner">
                {fileList == "Y" && (
                    <div className="file-list">
                        {selectedFiles.map((file, index) => {
                            <div className="file-item" key={index}>
                                <span>{file.name}</span>
                                <button
                                    className="btn-remove-file"
                                    key={index}
                                ></button>
                            </div>;
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
                    onChange={handleFileChange}
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
