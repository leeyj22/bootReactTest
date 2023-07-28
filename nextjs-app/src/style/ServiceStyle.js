import styled from "styled-components";

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
export { NoticeServiceStyle };
