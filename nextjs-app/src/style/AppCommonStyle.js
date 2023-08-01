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
export { Container, ModalPost };
