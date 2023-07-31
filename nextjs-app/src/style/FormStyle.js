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

export { InputChkbox };
