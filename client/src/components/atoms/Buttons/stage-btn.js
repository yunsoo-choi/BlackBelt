import React from "react";
import styled, { css } from "styled-components";
import { colors, fontSize } from "_foundation";
import Icon from "../Icons/Icon";

function StageBtn({ children, isHome, onClick }) {
  return (
    <StyledBtn onClick={onClick}>
      <Icon icon={isHome ? "home" : "restart"} />
      {children}
    </StyledBtn>
  );
}
export default StageBtn;

const StyledBtn = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;

  width: ${(props) => props.width};}
  height: 36px;
  padding: 1rem 1rem 1rem 0.6rem;

  border-radius: 4rem;
  border: 1px solid ${colors.gray0};
  color: ${colors.gray0};
  background-color: transparent;

  font-family: inherit;
  font-size: ${fontSize.lg};
  line-height: 1.75rem;
  text-align: center;
  text-decoration: none;

  transition: 0.2s;
  cursor: pointer;

  ${(props) =>
    css`
      :hover {
        background: ${colors.blue1};
      }
    `}

  svg {
    width: 20px;
    height: 20px;
    margin-right: 10px;
    margin-left: 3px;
    vertical-align: top;
  }
`;
