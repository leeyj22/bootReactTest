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

export { Container };
