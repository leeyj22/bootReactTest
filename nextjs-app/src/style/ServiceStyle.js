import styled from "styled-components";
import { ImgUrl } from "../hooks/imgurl";

const ServiceProgressStyle = styled.div`
    ul {
        display: flex;
        justify-content: space-around;
        margin-top: 100px;
        li {
            position: relative;
            text-align: center;
            width: ${(props) => 100 / props.step + "%"};
            &:after {
                content: "";
                position: absolute;
                right: calc(50% + 40px);
                top: 40px;
                width: calc(100% - 80px);
                height: 1px;
                background: var(--color-grey-b4);
            }
            &:first-child:after {
                display: none;
            }
        }
        span,
        em {
            display: block;
            color: var(--color-grey-b4);
        }
        span {
            margin: 0 auto;
            width: 80px;
            height: 80px;
            line-height: 80px;
            border-radius: 50%;
            border-width: 1px 1px 1px 1px;
            border-style: solid;
            border-color: var(--color-grey-b4);
            font-size: 3rem;
            font-weight: 700;
        }
        em {
            margin-top: 20px;
            font-size: 2rem;
        }
        li:nth-child(${(props) => props.progress}),
        li.active {
            span,
            em {
                border-color: var(--color-primary);
                color: var(--color-primary);
            }
            &:after {
                background: var(--color-primary);
            }
        }
        li.active {
            span {
                background: url(${ImgUrl}/cs/icon/icon_check_on.svg) no-repeat
                    center 100%/100%;
                color: transparent;
            }
        }
    }
`;

const NoticeServiceStyle = styled.section`
    margin-top: 100px;
    * {
        color: var(--color-black2);
        font-size: 2rem;
    }
    h4 {
        margin-bottom: 20px;
        font-size: 2rem;
    }
    div {
        padding: 30px 20px;
        background-color: var(--color-secondary);
        border: 1px solid var(--color-border1);
        border-radius: 10px;
        span {
            color: var(--color-redf0);
        }
        ul {
            li {
                margin-left: 30px;
                margin-bottom: 4px;
                list-style: disc;
            }
        }
    }
`;
export { NoticeServiceStyle, ServiceProgressStyle };
