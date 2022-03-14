import Icon from "components/atoms/Icons/Icon";
import GoogleButton from "components/atoms/Buttons/GoogleButton/GoogleButton";
import React from "react";
import {
  Overlay,
  ModalBox,
  ModalSection,
  ModalContent,
  ModalHeader,
  Logo,
} from "./LoginModal.styled";

const LoginModal = ({ ...props }) => {
  return (
    <>
      <Overlay>
        <ModalSection>
          <ModalContent>
            <ModalHeader>
              <Icon icon="xBtn" />
            </ModalHeader>
            <ModalBox>
              <Logo>
                <img src="/images/logo.png" alt="" />
              </Logo>
              <GoogleButton />
            </ModalBox>
          </ModalContent>
        </ModalSection>
      </Overlay>
    </>
  );
};
export default LoginModal;