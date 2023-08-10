import React, { useCallback, useState, useEffect } from "react";
import FormWriteTitle from "../form/formWriteTitle";
import AddrForm from "../form/addrForm";
import Calendar from "../form/calendar";
import { Validation } from "../../func/validation";
import { service } from "../../func/service";
import { useDispatch, useSelector } from "react-redux";
import {
    GET_HOLIDAY_REQUEST,
    GET_TRANSFER_PRICE_REQUEST,
} from "../../reducers/serviceTransfer";

const ServiceTransferForm2 = ({ formData, onFormChange }) => {
    const dispatch = useDispatch();
    const { getTransferPriceDone, transferPrice } = useSelector(
        (state) => state.setviceTransfer
    );
    const [serviceTransferFormData, setServiceTransferFormData] = useState({
        //이전설치접수2 초기값 설정 : 회수 정보, 설치 정보
        orderPerson: "", // 회수자 이름
        telNumb01: "010", //회수자 연락처1
        telNumb01_01: "", //회수자 연락처2
        telNumb01_02: "", //회수자 연락처3
        zipCode1: "", //회수 우편번호
        orderAddr1: "", //회수 주소
        orderAddr2: "", //회수 상세 주소
        orderPostData: "", //회수 주소.(가격 계산용)
        unInsDate: "", //회수 희망일
        receiver: "", //설치자 이름
        telNumb02: "010", //설치자 연락처1
        telNumb02_01: "", //설치자 연락처2
        telNumb02_02: "", //설치자 연락처3
        zipCode2: "", //설치 우편번호
        receiveAddr1: "", //설치 주소
        receiverAddr2: "", //설치 상세 주소
        receivePostData: "", //설치 주소.(가격 계산용)
        insDate: "", //설치 희망일
        moveDate: "", //이사 예정일
        location: "", //이동 구분 1:외륙(제주도 등) 2:내륙간 이동
        comment: "", //요청사항
        installTxt: "회수", //회수 또는 철거 텍스트
        unInstallTxt: "설치", //설치 또는 진행 텍스트
        originalPrice: "", //가격
        addPrice: "",
        totalPrice: "", //총 가격 , goodsPrice
    });
    const [orderTypeInit, setOrderTypeInit] = useState({
        installTxt: "설치",
        unInstallTxt: "회수",
        showInstall: true, //설치 정보 숨김처리 true보임|false안보임
        showUnInstall: true,
        showInstallDate: true, //회수 희망일
        showLocation: false, //이동 구분
    });

    useEffect(() => {
        dispatch({
            type: GET_HOLIDAY_REQUEST,
            data: "T",
            //이전설치 T, 분해조립 A
        });
    }, []);
    useEffect(() => {
        if (formData.orderTypeSub !== "") {
            let installTxt = orderTypeInit.installTxt;
            let unInstallTxt = orderTypeInit.unInstallTxt;
            let showInstall = orderTypeInit.showInstall;
            let showUnInstall = orderTypeInit.showUnInstall;
            let showInstallDate = orderTypeInit.showInstallDate;
            let showLocation = orderTypeInit.showLocation;
            switch (formData.orderTypeSub) {
                case "1": //포장(회수+설치)
                    showLocation = true;
                    break;
                case "3": //다른층 이동(회수 + 설치)
                    showInstallDate = false; //회수 희망일 숨김
                    installTxt = "진행";
                    break;
                case "4": //같은층 이동(진행) - 설치만
                    showUnInstall = false;
                    installTxt = "진행";
                    break;
                case "5": //포장(철거 + 설치) - 회수+설치
                    unInstallTxt = "철거";
                    break;
                case "6": //철거(회수)만
                    showInstall = false;
                    unInstallTxt = "철거";
                    break;
                case "7": //설치만
                    showUnInstall = false;
                    break;
                default:
                    unInstallTxt = "회수";
                    installTxt = "설치";
                    break;
            }
            setOrderTypeInit({
                installTxt,
                unInstallTxt,
                showInstall,
                showUnInstall,
                showInstallDate,
                showLocation,
            });
            setServiceTransferFormData((prevFormData) => ({
                ...prevFormData,
                installTxt,
                unInstallTxt,
                showInstall,
                showUnInstall,
            }));
        }
    }, [formData.orderTypeSub !== ""]);

    useEffect(() => {
        onFormChange({
            ...formData,
            ...serviceTransferFormData,
        });
    }, [serviceTransferFormData]);

    useEffect(() => {
        if (
            serviceTransferFormData.orderAddr1 !== "" ||
            serviceTransferFormData.receiveAddr1 !== ""
        ) {
            service.setPrice(serviceTransferFormData);
            getTransferPriceAPI();
        }
    }, [
        serviceTransferFormData.orderAddr1,
        serviceTransferFormData.receiveAddr1,
    ]);

    useEffect(() => {
        if (getTransferPriceDone) {
            const { originalPrice, addPrice } = transferPrice;
            setServiceTransferFormData((prevFormData) => ({
                ...prevFormData,
                originalPrice: parseInt(originalPrice),
                addPrice: parseInt(addPrice),
                totalPrice: parseInt(originalPrice) + parseInt(addPrice),
            }));
        }
    }, [getTransferPriceDone]);

    const getTransferPriceAPI = () => {
        const orderRoadAddr = serviceTransferFormData.orderPostData
            .roadAddrPart1
            ? serviceTransferFormData.orderPostData.roadAddrPart1
            : serviceTransferFormData.receivePostData.roadAddrPart1;
        const recRoadAddress = serviceTransferFormData.receivePostData
            .roadAddrPart1
            ? serviceTransferFormData.receivePostData.roadAddrPart1
            : serviceTransferFormData.orderPostData.roadAddrPart1;
        const params = {
            custKind: formData.prdtCate, //제품구분코드(필)
            shippingType: formData.orderTypeSub, //서비스타입(필)
            oldRoadAddress: orderRoadAddr, //회수 (이전설치유형에 따라 없을수 있음)
            recRoadAddress: recRoadAddress, //설치(필수)
        };

        dispatch({
            type: GET_TRANSFER_PRICE_REQUEST,
            data: params,
        });
    };

    const handleChange = useCallback(
        (e) => {
            const { name, value, type, checked } = e.target;
            let newValue = type === "checkbox" ? checked : value;

            switch (name) {
                case "telNumb01_01":
                case "telNumb01_02":
                    newValue = Validation.inputOnlyNum(newValue);
                    break;
                case "location":
                    serviceTransferFormData.insDate = "";
                    break;
                default:
                    break;
            }

            setServiceTransferFormData((prevFormData) => ({
                ...prevFormData,
                [name]: newValue,
            }));
        },
        [serviceTransferFormData]
    );

    const handleSameInfo = useCallback(() => {
        setServiceTransferFormData((prevFormData) => ({
            ...prevFormData,
            receiver: serviceTransferFormData.orderPerson,
            telNumb02: serviceTransferFormData.telNumb01,
            telNumb02_01: serviceTransferFormData.telNumb01_01,
            telNumb02_02: serviceTransferFormData.telNumb01_02,
        }));
    }, [serviceTransferFormData]);

    return (
        <>
            <article className="form-write-basic">
                {orderTypeInit.showUnInstall && (
                    <>
                        <FormWriteTitle
                            title={`1. ${orderTypeInit.unInstallTxt} 정보`}
                            service="serviceTransfer"
                        />

                        {/* 회수 이름 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">이름</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-input">
                                        <input
                                            type="text"
                                            placeholder="이름을 입력하세요"
                                            name="orderPerson"
                                            value={
                                                serviceTransferFormData.orderPerson
                                            }
                                            onChange={handleChange}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 회수 연락처 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">연락처</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-selectbox form-call">
                                        <div>
                                            <label htmlFor="telNumb01">
                                                010
                                            </label>
                                            <select
                                                name="telNumb01"
                                                value={
                                                    serviceTransferFormData.telNumb01
                                                }
                                                onChange={handleChange}
                                            >
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
                                                className="call-num-box"
                                                maxLength="4"
                                                name="telNumb01_01"
                                                value={
                                                    serviceTransferFormData.telNumb01_01
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                        <span className="call-txt">-</span>
                                        <div>
                                            <input
                                                type="text"
                                                className="call-num-box"
                                                maxLength="4"
                                                name="telNumb01_02"
                                                value={
                                                    serviceTransferFormData.telNumb01_02
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 회수 주소 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">
                                    {orderTypeInit.unInstallTxt} 주소
                                </p>
                            </div>
                            <AddrForm
                                zipcode="zipCode1"
                                addr1="orderAddr1"
                                addr2="orderAddr2"
                                postData="orderPostData"
                                formData={serviceTransferFormData}
                                setFormData={setServiceTransferFormData}
                            />
                        </div>

                        {/* 회수 희망일 */}
                        {orderTypeInit.showInstallDate && (
                            <div className="form-write-item">
                                <div className="form-title">
                                    <p className="necessary">
                                        {orderTypeInit.unInstallTxt} 희망일
                                    </p>
                                </div>
                                <div className="form-item col-1">
                                    <div className="col">
                                        <div className="form-input">
                                            <Calendar
                                                name="unInsDate"
                                                grpCode={formData.prdtCate}
                                                formData={
                                                    serviceTransferFormData
                                                }
                                                setFormData={
                                                    setServiceTransferFormData
                                                }
                                                orderTypeSub={
                                                    formData.orderTypeSub
                                                }
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </>
                )}

                {orderTypeInit.showInstall && (
                    <>
                        <FormWriteTitle
                            title={`2. ${orderTypeInit.installTxt} 정보`}
                            service="serviceTransfer"
                        />

                        <div className="form-write-item">
                            <div className="form-write-same-chk right">
                                <input
                                    type="checkbox"
                                    name=""
                                    id="sameInfo"
                                    onChange={handleSameInfo}
                                />
                                <label htmlFor="sameInfo">
                                    {orderTypeInit.unInstallTxt} 정보와 동일
                                </label>
                            </div>
                        </div>

                        {/* 설치자 이름 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">이름</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-input">
                                        <input
                                            type="text"
                                            placeholder="이름을 입력하세요"
                                            name="receiver"
                                            value={
                                                serviceTransferFormData.receiver
                                            }
                                            onChange={handleChange}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 회수 연락처 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">연락처</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-selectbox form-call">
                                        <div>
                                            <label htmlFor="telNumb01">
                                                010
                                            </label>
                                            <select
                                                name="telNumb02"
                                                value={
                                                    serviceTransferFormData.telNumb02
                                                }
                                                onChange={handleChange}
                                            >
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
                                                className="call-num-box"
                                                maxLength="4"
                                                name="telNumb02_01"
                                                value={
                                                    serviceTransferFormData.telNumb02_01
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                        <span className="call-txt">-</span>
                                        <div>
                                            <input
                                                type="text"
                                                className="call-num-box"
                                                maxLength="4"
                                                name="telNumb02_02"
                                                value={
                                                    serviceTransferFormData.telNumb02_02
                                                }
                                                onChange={handleChange}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 설치 주소 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">
                                    {orderTypeInit.installTxt} 주소
                                </p>
                            </div>
                            <AddrForm
                                zipcode="zipCode2"
                                addr1="receiveAddr1"
                                addr2="receiverAddr2"
                                postData="receivePostData"
                                formData={serviceTransferFormData}
                                setFormData={setServiceTransferFormData}
                            />
                        </div>

                        {/* 이동 구분 */}
                        {orderTypeInit.showLocation && (
                            <div className="form-write-item">
                                <div className="form-title">
                                    <p className="necessary">
                                        {orderTypeInit.installTxt} 이동 구분
                                    </p>
                                </div>
                                <div className="form-item col-1">
                                    <div className="col">
                                        <div className="form-selectbox form-width-half">
                                            <div>
                                                <label htmlFor="location">
                                                    선택
                                                </label>
                                                <select
                                                    name="location"
                                                    value={
                                                        serviceTransferFormData.location
                                                    }
                                                    onChange={handleChange}
                                                >
                                                    <option value="">
                                                        선택
                                                    </option>
                                                    <option value="1">
                                                        외륙(제주도 등) ↔ 내륙
                                                    </option>
                                                    <option value="2">
                                                        내륙간 이동
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                        {/* 설치 희망일 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="necessary">
                                    {orderTypeInit.installTxt} 희망일
                                </p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-input">
                                        <Calendar
                                            name="insDate"
                                            grpCode={formData.prdtCate}
                                            formData={serviceTransferFormData}
                                            setFormData={
                                                setServiceTransferFormData
                                            }
                                            orderTypeSub={formData.orderTypeSub}
                                        />
                                    </div>
                                    <p className="txt color-grey-b4">
                                        ※ 희망일은 접수일 기준 7일 이후로 선택해
                                        주세요.
                                    </p>
                                </div>
                            </div>
                        </div>
                        {/* 이사 예정일 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="">이사 예정일</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-input">
                                        <Calendar
                                            name="moveDate"
                                            grpCode={formData.prdtCate}
                                            formData={serviceTransferFormData}
                                            setFormData={
                                                setServiceTransferFormData
                                            }
                                            orderTypeSub={formData.orderTypeSub}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        {/* 요청사항 */}
                        <div className="form-write-item">
                            <div className="form-title">
                                <p className="">요청 사항</p>
                            </div>
                            <div className="form-item col-1">
                                <div className="col">
                                    <div className="form-input">
                                        <textarea
                                            name="comment"
                                            placeholder="요청 사항을 입력하세요.(최대 100자)"
                                            maxLength="100"
                                            onChange={handleChange}
                                        ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </>
                )}
            </article>
        </>
    );
};

export default ServiceTransferForm2;
