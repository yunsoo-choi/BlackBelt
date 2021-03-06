import styled, { css } from "styled-components";
import { fontSize, fontWeight } from "_foundation";

export const Layout = styled.div`
  position: absolute;
  right: 22%;
  padding: 0.3rem;

  display: flex;
  align-items: center;

  width: 376px;
  height: 189px;

  background: url("data:image/svg+xml,%3csvg width='376' height='189' viewBox='0 0 376 189' fill='none' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M357 77.0492L375.276 94.5L357 111.951V77.0492Z' fill='%23212529' stroke='%23D7BE69'/%3e%3crect x='0.5' y='0.5' width='367' height='188' rx='4.5' fill='%23212529' stroke='%23D7BE69'/%3e%3cpath d='M375 94.5L355.5 113.12V75.8805L375 94.5Z' fill='%23212529'/%3e%3c/svg%3e");

  ${(props) => {
    if (props.targetUser !== props.user) {
      return css`
        display: none;
      `;
    }
  }}
`;

export const Img = styled.div`
  width: 45%;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;
  background-color: black;
`;

export const UserImg = styled.img`
  width: 100%;
`;

export const NoUserImg = styled.img`
  width: 75%;
  filter: grayscale(100%) brightness(90%);
`;

export const Gradient = styled.div`
  position: absolute;
  top: 0.3rem;
  right: 54%;
  width: 48%;
  height: 179.41px;

  background: linear-gradient(
    268.54deg,
    #212529 1.86%,
    rgba(0, 0, 0, 0) 60.97%
  );
`;

export const ProfileBox = styled.div`
  width: 55%;
  padding: 2rem;
`;

export const TierImg = styled.img`
  filter: grayscale(100%) brightness(30%);
  position: relative;

  width: 85%;
  /* padding: 2rem; */
`;

export const Contents = styled.div`
  position: absolute;
  top: 28%;
  left: 53%;

  display: flex;
  flex-direction: column;
  gap: 1.2rem;
`;

export const TextBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 0.3rem;
`;

export const Username = styled.div`
  font-size: ${fontSize.xl};
  font-weight: ${fontWeight.medium};
`;

export const UserTier = styled.div``;
