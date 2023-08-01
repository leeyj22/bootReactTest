import styled from "styled-components";
import { ImgUrl } from "../hooks/imgurl";

const InputChkbox = styled.span`
    input[type="checkbox"] {
        & + label {
            display: inline-block;
            padding-left: 40px;
            height: 30px;
            background-repeat: no-repeat;
            background-position: 0 center;
            background-image: url(${ImgUrl}/cs/icon/icon_check_off.svg);
            font-size: 2rem;
            color: var(--color-grey-db);
            cursor: pointer;
        }
        &:checked + label {
            background-image: url(${ImgUrl}/cs/icon/icon_check_on.svg);
            color: var(--color-black2);
        }
    }
`;

const SearchInputStyle = styled.div`
    display: flex;
    justify-content: ${(props) => (props.pos == "right" ? "end" : "initial")};
    margin-top: ${(props) => (props.pos == "right" ? "-50px" : "0")};
    input[type="text"] {
        padding: 0 10px 0 50px;
        height: 50px;
        font-size: 2rem;
        border: 1px solid var(--color-border1);
        border-radius: 40px;
    }
`;

const TabStyle1 = styled.div`
    ul {
        display: flex;
    }
    button {
        margin-right: 15px;
        font-size: 2rem;
        color: var(--color-primary);
        display: inline-block;
        padding: 16px 20px;
        border-radius: 30px;
        background-color: var(--color-white);
        &.active,
        &:active {
            background-color: var(--color-secondary);
            font-weight: 700;
        }
    }
`;
export { InputChkbox, SearchInputStyle, TabStyle1 };
