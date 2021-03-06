package com.blackbelt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.tomcat.jni.Mmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackbelt.model.JudgeDto;
import com.blackbelt.model.LevelPoomsaeDto;
import com.blackbelt.model.service.JudgeService;
import com.blackbelt.util.JwtTokenProvider;

@RestController
@RequestMapping("api/judge")
public class JudgeController {
	@Autowired
	private JudgeService judgeService;
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@PostMapping("")
	public ResponseEntity<Map<String, String>> postjudge(@RequestBody Map<String, String> map) throws Exception{
		String user_id = map.get("user_id");
		String level_id = map.get("level_id");
		String fail = "Y";
		if( map.get("fail") == "0") {
			fail = "N";
		}else {
			fail = "Y";
		}
		
		
		String judge_score = map.get("judge_score");
		
		JudgeDto judgeDto = new JudgeDto(user_id, level_id, fail, judge_score);
		String judge_ox = judgeService.insertJudge(judgeDto);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("judge_ox", judge_ox);
		
		return ResponseEntity.status(200).body(result);
	}
	
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> getjudge(@RequestHeader("Authorization") String authorization) throws Exception{
		String user_id = "";
        if(authorization.indexOf("Bearer") != -1) {
           authorization = authorization.replaceAll("^Bearer\\s", "");
        }
        if (tokenProvider.validateToken(authorization)) {// 유효하면
        	user_id = String.valueOf(tokenProvider.getSubject(authorization));
            System.out.println("유효함!");
         }
		List<LevelPoomsaeDto> lplist = judgeService.getJudge(user_id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("judgeLevelId", lplist.get(0).getLevel_id());
		result.put("judgeLevelName", lplist.get(0).getLevel_name());
		ArrayList<String> randomPoomsaeId = new ArrayList<String>();
		ArrayList<String> randomPoomsaeName = new ArrayList<String>();
		ArrayList<String> randomPoomsaeName_e = new ArrayList<String>();
		ArrayList<String> randomPoomsaeTime = new ArrayList<String>();
		ArrayList<String> randomAnswer = new ArrayList<String>();		
		ArrayList<String> randomAnswerIndex = new ArrayList<String>();
		ArrayList<String> randomPoomsaeExplain = new ArrayList<String>();
		ArrayList<String> randomPoomsaeExplainE = new ArrayList<String>();
		String essentialPoomsaeId = "";
		String essentialPoomsaeName = "";
		String essentialPoomsaeName_e = "";
		String essentialPoomsaeTime = "";
		String essentialAnswer = "";
		String essentialAnswerIndex = "";
		String essentialPoomsaeExplain = "";
		String essentialPoomsaeExplainE = "";

		for(LevelPoomsaeDto lp:lplist) {
			if(lp.getIs_essential().equals("Y")) {
				essentialPoomsaeId = lp.getPoomsae_id();
				essentialPoomsaeName = lp.getPoomsae_name();
				essentialPoomsaeName_e = lp.getPoomsae_name_e();
				essentialPoomsaeTime = lp.getPoomsae_time();
				essentialAnswer = lp.getPoomsae_answer();
				essentialAnswerIndex = lp.getPoomsae_answer_index();
				essentialPoomsaeExplain = lp.getPoomsae_explain();
				essentialPoomsaeExplainE = lp.getPoomsae_explain_e();
			}else if(lp.getIs_essential().equals("N")) {
				randomPoomsaeId.add(lp.getPoomsae_id());
				randomPoomsaeName.add(lp.getPoomsae_name());
				randomPoomsaeName_e.add(lp.getPoomsae_name_e());
				randomPoomsaeTime.add(lp.getPoomsae_time());
				randomAnswer.add(lp.getPoomsae_answer());
				randomAnswerIndex.add(lp.getPoomsae_answer_index());
				randomPoomsaeExplain.add(lp.getPoomsae_explain());
				randomPoomsaeExplainE.add(lp.getPoomsae_explain_e());
			}
		}
		Random rand = new Random();
		int rint = rand.nextInt(randomPoomsaeId.size());
		rint = 0;//test용으로 1로 고정.(0번째가 id 1임) 랜덤으로 할 거면 이 부분 주석 처리. 
		result.put("randomPoomsaeId", randomPoomsaeId.get(rint));
		result.put("randomPoomsaeName", randomPoomsaeName.get(rint));
		result.put("randomPoomsaeNameE", randomPoomsaeName_e.get(rint));
		//randomAnswer
		String random_answer_list = randomAnswer.get(rint);
		List<String[]> random_answer_array = new ArrayList<String[]>();
		if(random_answer_list != null) {
			for(String random_answer: random_answer_list.split("\\]\\s*,\\s*\\[")) {
				random_answer = random_answer.replaceAll("\\[", "").replaceAll("\\]", "");
				String[] random_answer_str = random_answer.replace(" \"", "").replace("\"", "").split(",");
				random_answer_array.add(random_answer_str);
			}
			result.put("randomAnswer", random_answer_array);
		}else {
			result.put("randomAnswer", "");
		}
		//randomAnswerIndex
		String random_answer_index_list = randomAnswerIndex.get(rint);
		List<String[]> random_answer_index_array = new ArrayList<String[]>();
		if(random_answer_index_list != null) {
			for(String random_answer_index: random_answer_index_list.split("\\]\\s*,\\s*\\[")) {
				random_answer_index = random_answer_index.replaceAll("\\[", "").replaceAll("\\]", "");
				String[] random_answer_index_str = random_answer_index.replace(" ", "").split(",");
				random_answer_index_array.add(random_answer_index_str);
			}
			result.put("randomAnswerIndex", random_answer_index_array);
		}else {
			result.put("randomAnswerIndex", "");
		}
		//randomPoomsaeExplain
		String random_poomsae_explain_list = randomPoomsaeExplain.get(rint);
		List<String> random_poomsae_explain_array = new ArrayList<String>();
		if(random_poomsae_explain_list != null) {
			for(String random_poomsae_explain: random_poomsae_explain_list.split("/|#")) {
				random_poomsae_explain_array.add(random_poomsae_explain);
			}
			result.put("randomPoomsaeExplain", random_poomsae_explain_array);
		}else {
			result.put("randomPoomsaeExplain", "");
		}
		//randomPoomsaeExplainE
		String random_poomsae_explain_e_list = randomPoomsaeExplainE.get(rint);
		List<String> random_poomsae_explain_e_array = new ArrayList<String>();
		if(random_poomsae_explain_e_list != null) {
			for(String random_poomsae_e_explain: random_poomsae_explain_e_list.split("/|#")) {
				random_poomsae_explain_e_array.add(random_poomsae_e_explain);
			}
			result.put("randomPoomsaeExplainE", random_poomsae_explain_e_array);
		}else {
			result.put("randomPoomsaeExplainE", "");
		}
		//essentialPoomsaeId, essentialPoomsaeName
		result.put("essentialPoomsaeId", essentialPoomsaeId);
		result.put("essentialPoomsaeName", essentialPoomsaeName);
		result.put("essentialPoomsaeNameE", essentialPoomsaeName_e);
		//randomPoomsaeTime
		result.put("randomPoomsaeTime", randomPoomsaeTime.get(rint));
		//essentialPoomsaeTime 
		result.put("essentialPoomsaeTime", essentialPoomsaeTime);
		//essentialAnswer
				String essential_answer_list = essentialAnswer;
				List<String[]> essential_answer_array = new ArrayList<String[]>();
				if(essential_answer_list != null) {
					for(String essential_answer: essential_answer_list.split("\\]\\s*,\\s*\\[")) {
						essential_answer = essential_answer.replaceAll("\\[", "").replaceAll("\\]", "");
						String[] essential_answer_str = essential_answer.replace(" \"", "").replace("\"", "").split(",");
						essential_answer_array.add(essential_answer_str);
					}
					result.put("essentialAnswer", essential_answer_array);
				}else {
					result.put("essentialAnswer", "");
				}
				//essentialAnswerIndex
				String essential_answer_index_list = essentialAnswerIndex;
				List<String[]> essential_answer_index_array = new ArrayList<String[]>();
				if(essential_answer_index_list != null) {
					for(String essential_answer_index:essential_answer_index_list.split("\\]\\s*,\\s*\\[")) {
						essential_answer_index = essential_answer_index.replaceAll("\\[", "").replaceAll("\\]", "");
						String[] essential_answer_index_str = essential_answer_index.replace(" ", "").split(",");
						essential_answer_index_array.add(essential_answer_index_str);
					}
					result.put("essentialAnswerIndex", essential_answer_index_array);
				}else {
					result.put("essentialAnswerIndex", "");
				}
				//result.put("essentialAnswerIndex", essentialAnswerIndex);
		//essentialPoomsaeExplain
		String essential_poomsae_explain_list = essentialPoomsaeExplain;
		List<String> essential_poomsae_explain_array = new ArrayList<String>();
		if(essential_poomsae_explain_list != null) {
			for(String essential_poomsae_explain: essential_poomsae_explain_list.split("/|#")) {
				essential_poomsae_explain_array.add(essential_poomsae_explain);
			}
			result.put("enssentialPoomsaeExplain", essential_poomsae_explain_array);
		}else {
			result.put("enssentialPoomsaeExplain", "");
		}
		//essentialPoomsaeExplainE
		String essential_poomsae_explain_e_list = essentialPoomsaeExplainE;
		List<String> essential_poomsae_explain_e_array = new ArrayList<String>();
		if(essential_poomsae_explain_e_list != null) {
			for(String essential_poomsae_explain_e: essential_poomsae_explain_e_list.split("/|#")) {
				essential_poomsae_explain_e_array.add(essential_poomsae_explain_e);
			}
			result.put("enssentialPoomsaeExplainE", essential_poomsae_explain_e_array);
		}else {
			result.put("enssentialPoomsaeExplainE", "");
		}
		return ResponseEntity.status(200).body(result);
	}
}
