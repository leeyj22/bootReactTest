import Breadcrumb from "../components/breadcrumb";
import AppLayout from "../components/AppLayout";
import React from "react";
import { Container } from "../style/AppCommonStyle";
import PageName from "../components/pagename";
import FormWriteTitle from "../components/form/formWriteTitle";
import FileMulti from "../components/form/fileMulti";
import Term from "../components/term/term";

//서비스 접수 service1

const Service = () => {
    return (
        <AppLayout>
            <Breadcrumb pageId="service" pageSubId="service1" />
            <Container>
                <PageName title="서비스 접수" />
                <article className="form-write-basic">
                    <FormWriteTitle
                        title="1.제품 선택/증상 입력"
                        service="afterService"
                    />

                    {/* 제품 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">제품</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="productSelector">
                                        사용 제품 선택
                                    </label>
                                    <select id="productSelector">
                                        <option value="O20170500727">
                                            NEW팬텀블랙에디션(와인)
                                        </option>
                                        <option value="">직접입력</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        {/* 제품이 없는 경우 노출 : 직접입력 */}
                        <div className="form-item col-2">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="productSelector">
                                        제품 카테고리
                                    </label>
                                    <select id="productSelector">
                                        <option value="0">
                                            제품카테고리 선택
                                        </option>
                                        <option value="M">안마의자</option>
                                        <option value="L">라클라우드</option>
                                        <option value="W">W정수기</option>
                                        <option value="E">기타</option>
                                    </select>
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-input">
                                    <input
                                        type="text"
                                        placeholder="제품명을 입력하세요."
                                        name="model_nm"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 서비스 유형 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">서비스 유형</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="chkCs">
                                        서비스 유형을 선택하세요
                                    </label>
                                    <select id="chkCs" name="chkCs">
                                        <option value="">
                                            서비스 유형을 선택하세요
                                        </option>
                                        <option value="1">소음</option>
                                        <option value="2">파손</option>
                                        <option value="3">작동이상</option>
                                        <option value="4">설치불량</option>
                                        <option value="5">소모품 교체</option>
                                        <option value="8">긴급누수</option>
                                        <option value="6">기타</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 제목 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">제목</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-input">
                                    <input
                                        type="text"
                                        placeholder="제목을 입력하세요."
                                        name="title"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 내용 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">내용</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-input">
                                    <textarea
                                        placeholder="내용을 입력하세요."
                                        name="content"
                                        rows="10"
                                    ></textarea>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 첨부파일 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="">첨부파일</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <FileMulti
                                    id="ex_filename"
                                    name="ex_filename"
                                    maxLen="3"
                                    maxSize="300MB"
                                    fileList="Y"
                                />
                            </div>
                        </div>
                    </div>

                    {/* 제품 구매처 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">제품 구매처</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-selectbox">
                                    <label htmlFor="market">
                                        구매처를 선택하세요
                                    </label>
                                    <select id="market" name="market">
                                        <option value="">
                                            구매처를 선택하세요
                                        </option>
                                        <option value="1">온라인쇼핑몰</option>
                                        <option value="2">직영전시장</option>
                                        <option value="3">홈쇼핑</option>
                                        <option value="4">하이마트</option>
                                        <option value="6">
                                            삼성디지털프라자
                                        </option>
                                        <option value="7">전자랜드</option>
                                        <option value="5">기타</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <FormWriteTitle
                        title="2.고객 정보 입력"
                        service="afterService"
                    />

                    {/* 이름 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">이름</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-input">
                                    <input
                                        type="text"
                                        placeholder="이름"
                                        name="name"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 연락처 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">연락처</p>
                        </div>
                        <div className="form-item col-1">
                            <div className="col">
                                <div className="form-selectbox form-call">
                                    <div>
                                        <label htmlFor="cell1">010</label>
                                        <select id="cell1">
                                            <option value="010">010</option>
                                            <option value="011">011</option>
                                            <option value="016">016</option>
                                            <option value="017">017</option>
                                            <option value="018">018</option>
                                            <option value="019">019</option>
                                        </select>
                                    </div>
                                    <span className="call-txt">-</span>
                                    <div>
                                        <input
                                            type="text"
                                            id="cell2"
                                            className="call-num-box"
                                            name="cell2"
                                            maxLength="4"
                                        />
                                    </div>
                                    <span className="call-txt">-</span>
                                    <div>
                                        <input
                                            type="text"
                                            id="cell3"
                                            className="call-num-box"
                                            name="cell3"
                                            maxLength="4"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* 주소 */}
                    <div className="form-write-item">
                        <div className="form-title">
                            <p className="necessary">주소</p>
                        </div>
                        <div className="form-item form-addr col-1">
                            <div className="col">
                                <input
                                    type="text"
                                    id="zipCode"
                                    name="zipCode"
                                    readOnly="readOnly"
                                    className="zipcode"
                                />
                                <button id="searchAddr" className="btn-zip">
                                    우편번호찾기
                                </button>
                            </div>
                        </div>
                        <div className="form-item form-addr col-1">
                            <div className="col">
                                <input
                                    type="text"
                                    name="add"
                                    id="addr"
                                    readOnly="readOnly"
                                    defaultValue=""
                                    maxLength="100"
                                />
                            </div>
                        </div>
                        <div className="form-item form-addr col-1">
                            <div className="col">
                                <input
                                    type="text"
                                    name="addrDetail"
                                    id="addrDetail"
                                    defaultValue=""
                                    maxLength="100"
                                />
                            </div>
                        </div>
                    </div>
                </article>

                {/* 약관동의 */}
                <Term allChk="Y" termslist={["policy", "marketing"]} />
            </Container>
        </AppLayout>
    );
};

export default Service;
