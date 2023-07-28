import React from "react";

const FileMulti = ({ id, name, maxSize, maxLen, fileList }) => {
    return (
        <div className="form-file">
            <div className="form-file-inner">
                {fileList == "Y" && (
                    <div className="file-list">
                        <div className="file-item">
                            <span>파일명(용량)</span>
                            <button className="btn-remove-file"></button>
                        </div>
                    </div>
                )}
                <label htmlFor={id} className="btn-file">
                    <input
                        type="file"
                        name={name}
                        id={id}
                        className="get-file"
                        accept="image/*,video/*"
                        multiple
                    />
                    파일찾기
                </label>
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
