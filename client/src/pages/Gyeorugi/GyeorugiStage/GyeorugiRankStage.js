import StageBtn from "components/atoms/Buttons/stage-btn";
import EvaluationTemplateGyeorugi from "components/templates/EvaluationTemplateGyeorugi";
import VSTemplate from "components/templates/VSTemplate";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "utils/API";
import VideoRoomComponent from "../../../components/organisms/VideoRoom/VideoRoomComponent";

function GyeorugiRankStage() {
  const [result, setResult] = useState(0);
  const [tier, setTier] = useState(undefined);
  const [isWin, setIsWin] = useState(undefined);
  const [info, setInfo] = useState(undefined);
  const [myInfo, setMyInfo] = useState(undefined);
  const [red, setRed] = useState(undefined);
  const [blue, setBlue] = useState(undefined);
  const [introduce, setIntroduce] = useState(true);
  const navigate = useNavigate();
  const state = useLocation().state;
  let otherNick;

  const tiers = {
    1: "bronze",
    2: "silver",
    3: "gold",
    4: "platinum",
    5: "dia",
    6: "master",
  };

  const getInfoData = async () => {
    const data = await axiosInstance.post("/api/battle", {
      hostId: state.hostId,
      guestId: state.guestId,
      isHost: state.isHost ? 0 : 1,
      roomSeq: state.roomSeq,
    });
    let imHost = state.isHost;
    setInfo(data);
    setMyInfo(imHost ? data.BattleInfo[0] : data.BattleInfo[1]);
    otherNick = imHost ? data.BattleInfo[1].userNick : data.BattleInfo[0].userNick;

    if (imHost) {
      setRed(data.battleInfo[0]);
      setBlue(data.battleInfo[1]);
    } else {
      setBlue(data.battleInfo[0]);
      setRed(data.battleInfo[1]);
    }
  };

  useEffect(() => {
    getInfoData();
    setTimeout(() => {
      setIntroduce(false);
    }, 3000);
  }, []);

  useEffect(() => {
    console.log(result, isWin, "!!??");
    if (result === 1 && isWin !== undefined) {
      getResultData().then((result) => {
        setTier({ tier: tiers[result.tierId], score: result.userScore });
      });
    }
  }, [result, isWin]);

  const getResultData = async () => {
    const data = await axiosInstance.post("/api/battle/end", {
      team: state.isHost ? "red" : "blue",
      redWinLoseDraw: isWin ? "W" : "L",
      battleSeq: info.BattleInfo,
      token: info.token,
      isRank: 1,
    });
    return data;
  };

  const restartFunc = () => {
    navigate("/gyeorugi/rank");
  };
  const homeFunc = () => {
    navigate("/");
  };

  return (
    <>
      {introduce && red && blue && <VSTemplate red={red} blue={blue} />}
      {!introduce && info && myInfo && (
        <VideoRoomComponent
          setResult={setResult}
          setIsWin={setIsWin}
          info={myInfo}
          token={info.token}
          otherNick={otherNick}
        />
      )}
      {tier !== undefined && isWin !== undefined ? (
        <EvaluationTemplateGyeorugi
          isWin={isWin}
          tier={tier}
          restart={<StageBtn onClick={restartFunc}>????????????</StageBtn>}
          home={
            <StageBtn onClick={homeFunc} isHome>
              ????????? ??????
            </StageBtn>
          }
        />
      ) : null}
    </>
  );
}
export default GyeorugiRankStage;
