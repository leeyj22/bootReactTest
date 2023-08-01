import styled from "styled-components";

const Container = styled.main`
    margin: 0 auto;
    padding: 60px 0 160px 0;
    width: 1200px;
    min-height: 100vh;
    .page-name {
        font-size: 3.2rem;
    }
    @media screen and (max-width: 1200px) {
        width: 100%;
    }
`;

const ModalPost = styled.section`
    position: fixed;
    z-index: 3;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 640px;
    min-height: 400px;
    background: var(--color-white);
    .top {
        display: flex;
        justify-content: space-between;
        padding: 20px;
        h3 {
            font-size: 2rem;
        }
        button {
            background: var(--color-white);
            position: relative;
            overflow: hidden;
            width: 25px;
            height: 25px;
            text-indent: -9999px;
            &:before,
            &:after {
                content: "";
                position: absolute;
                top: 0;
                width: 20px;
                height: 1px;
                background: #000;
            }
            &:before {
                left: 0;
                top: 50%;
                transform: rotate(45deg);
            }
            &:after {
                left: 0;
                top: 50%;
                transform: rotate(-45deg);
            }
        }
    }
`;

const LoginContainer = styled.section`
    display: flex;
    justify-content: space-around;
    margin: 140px 30px;
    & > div {
        padding: 30px;
        width: 404px;
        border: 1px solid var(--color-black2);
        border-radius: 10px;
        text-align: center;
        strong {
            display: block;
            margin: 30px auto;
            font-size: 3.2rem;
            font-weight: 700;
        }
        p {
            font-size: 2rem;
            line-height: 1.8;
            color: var(--color-grey-9);
        }
        button {
            display: inline-block;
            margin-top: 30px;
            padding: 23px 36px;
            background: ${(props) =>
                props.costomer === "Y"
                    ? "linear-gradient(180deg, #5C6A82 0%, #272D37 100%)"
                    : "#bbbcc8"};
            border-radius: 27px;
            font-size: 2rem;
            color: var(--color-white);
        }
    }
`;
export { Container, ModalPost, LoginContainer };
