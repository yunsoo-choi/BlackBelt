package com.blackbelt.model;

import java.util.List;

import lombok.Data;
import springfox.documentation.spring.web.json.Json;

@Data
public class ComboDto {
	private String combo_id;
	private String poomsae_id;
	private String combo_name;
	private String combo_name_e;
	private Object combo_explain;
	private Object combo_explain_e;
	private String combo_img_path;
	private String combo_movie_path;
	private String combo_clear;
	private String combo_score;
	private String combo_locked;
	private Object combo_answer;
	private Object combo_answer_index;
	
	public ComboDto() {
		super();
	}

	public ComboDto(String combo_id, String poomsae_id, String combo_name, String combo_name_e, Object combo_explain,
			Object combo_explain_e, String combo_img_path, String combo_movie_path, 
			String combo_clear, String combo_score, String combo_locked,
			Object combo_answer, Object combo_answer_index) {
		super();
		this.combo_id = combo_id;
		this.poomsae_id = poomsae_id;
		this.combo_name = combo_name;
		this.combo_name_e = combo_name_e;
		this.combo_explain = combo_explain;
		this.combo_explain_e = combo_explain_e;
		this.combo_img_path = combo_img_path;
		this.combo_movie_path = combo_movie_path;
		this.combo_clear = combo_clear;
		this.combo_score = combo_score;
		this.combo_locked = combo_locked;
		this.combo_answer = combo_answer;
		this.combo_answer_index = combo_answer_index;
	}
	public ComboDto(String combo_id, String poomsae_id, String combo_name, String combo_name_e, Object combo_explain,
			Object combo_explain_e, String combo_img_path, String combo_movie_path,
			String combo_clear, String combo_score, String combo_locked) {
		super();
		this.combo_id = combo_id;
		this.poomsae_id = poomsae_id;
		this.combo_name = combo_name;
		this.combo_name_e = combo_name_e;
		this.combo_explain = combo_explain;
		this.combo_explain_e = combo_explain_e;
		this.combo_img_path = combo_img_path;
		this.combo_movie_path = combo_movie_path;
		this.combo_clear = combo_clear;
		this.combo_score = combo_score;
		this.combo_locked = combo_locked;

	}

	public ComboDto(String combo_id, String poomsae_id, String combo_name, String combo_name_e, Object combo_explain,
			Object combo_explain_e, String combo_img_path, String combo_movie_path) {
		super();
		this.combo_id = combo_id;
		this.poomsae_id = poomsae_id;
		this.combo_name = combo_name;
		this.combo_name_e = combo_name_e;
		this.combo_explain = combo_explain;
		this.combo_explain_e = combo_explain_e;
		this.combo_img_path = combo_img_path;
		this.combo_movie_path = combo_movie_path;

	}
	
}