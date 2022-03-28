import PracticeStageTemplate from "../../components/templates/PracticeStageTemplate";
import { useEffect, useState } from "react";
import LocalVideo from "../../components/atoms/Videos/LocalVideo";
import { useLocation, useNavigate } from "react-router-dom";
import EvaluationTemplate from "components/templates/EvaluationTemplate";
import UserVideoCombo from "components/atoms/Videos/UserVideoCombo";
import DescStage from "components/atoms/Texts/DescStage";
import StageBtn from "components/atoms/Buttons/stage-btn";
import axiosInstance from "utils/API";
import { useTranslation } from "react-i18next";

function ComboStage() {
  const [info, setInfo] = useState();
  // const [videoSelected, setVideoSelected] = useState("../../videos/basics1.MP4");
  // const [title, setTitle] = useState("");
  // const [desc, setDesc] = useState([]);
  // const [answer, setAnswer] = useState([]);
  // const [answerIndex, setAnswerIndex] = useState([]);
  const [isPass, setIsPass] = useState(false);
  const [grade, setGrade] = useState("Try Again");
  const [gradeNum, setGradeNum] = useState(0);
  const [nextAction, setNextAction] = useState(0);
  const state = useLocation().state;
  const { t, i18n } = useTranslation();
  const navigate = useNavigate();

  useEffect(() => {
    //받기: api 통신해서 title, description[], 비디오(Streaming이나 youtube link), 답안[], 답안의 인덱스[] 받음
    //보내기: 서버로 통과 단계를 보냄
    getComboData();
    // fetch("https://jsonplaceholder.typicode.com/posts")
    //   .then((res) => res.json())
    //   .then((json) => {
    //     // console.log(state.stageId);
    //     setTitle("연결동작");
    //     setDesc([
    //       "왼 아래(내려)막기",
    //       "오른 (몸통)지르기",
    //       "오른 아래(내려)막기",
    //       "왼 (몸통)지르기",
    //       "왼 아래(내려)막기",
    //       "오른 (몸통)지르기",
    //       // "왼 손날 아래(내려)막기",
    //       // "오른 아금손 앞치기",
    //       // "오른 앞차기",
    //       // "오른 손날 아래(내려)막기",
    //       // "왼 아금손 앞치기",
    //       // "왼 앞차기",
    //       // "왼 손날 아래(내려)막기",
    //       // "오른 아금손 앞치기",
    //       // "오른 앞차기",
    //       // "왼 (무릎)눌러꺾기",
    //       // "안팔목 (몸통)헤쳐막기",
    //       // "왼 앞차기",
    //       // "오른 (무릎)눌러꺾기",
    //       // "안팔목 (몸통)헤쳐막기",
    //     ]);
    //     setAnswer(["mask", "water jug", "mask"]);
    //     setAnswerIndex([0, 2, 4]);
    //     setVideoSelected(`https://youtu.be/o9JvP-A4TvY`);
    //   });
  }, []);

  const getComboData = async () => {
    const data = await axiosInstance.get(`/api/combo/${state.stageId}`, {});
    data.combo_answer = ["abaya", "coffee mug", "abaya"];
    data.combo_movie_path = "https://youtu.be/o9JvP-A4TvY";
    data.combo_explain = data.combo_explain.split("/");
    data.combo_explain_e = data.combo_explain_e.split("/");
    data.combo_answer_index = [0, 2, 4];
    setInfo(data);
  };

  const updateIsPass = () => {
    fetch("https://jsonplaceholder.typicode.com/posts") //통과단계보냄
      .then((res) => res.json())
      .then((json) => {
        setNextAction(info.combo_answer.length - 1);
        setIsPass(true);
      });
  };

  const updateNextAction = (value) => {
    setNextAction(value);
  };

  const testResult = (result) => {
    updateIsPass();
    result /= info.combo_answer.length;
    if (result >= 0.8) {
      setGrade("Perfect!");
      setGradeNum(3);
    } else if (result >= 0.7) {
      setGrade("Great");
      setGradeNum(2);
    } else if (result >= 0.6) {
      setGrade("Good");
      setGradeNum(1);
    } else {
      setGrade("Try Again");
      setGradeNum(0);
    }
  };

  const restartFunc = () => {
    setNextAction(0);
    setIsPass(false);
  };
  const homeFunc = () => {
    navigate("/");
  };
  return (
    <>
      {info && (
        <PracticeStageTemplate
          title={t("language") === "KOR" ? info.combo_name : info.combo_name_e}
          desc={
            <DescStage
              descArray={t("language") === "KOR" ? info.combo_explain : info.combo_explain_e}
              curIdx={info.combo_answer_index[nextAction]}
            />
          }
          video={<LocalVideo url={info.combo_movie_path} />}
          camera={
            <UserVideoCombo
              answer={info.combo_answer}
              testResult={testResult}
              updateNextAction={updateNextAction}
              isPass={isPass}
            />
          }
        />
      )}

      {isPass ? (
        <EvaluationTemplate
          grade={grade}
          gradeNum={gradeNum}
          restart={<StageBtn onClick={restartFunc}>다시하기</StageBtn>}
          home={
            <StageBtn onClick={homeFunc} isHome>
              홈으로 이동
            </StageBtn>
          }
        />
      ) : null}
    </>
  );
}

export default ComboStage;
