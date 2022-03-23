package com.blackbelt.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.blackbelt.model.CountryCrudRepository;
import com.blackbelt.model.CountryDto;
import com.blackbelt.model.UserCrudRepository;
import com.blackbelt.model.UserDto;
import com.blackbelt.model.service.UserService;
import com.blackbelt.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
//회원 처리용 controller
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	CountryCrudRepository countryRepo;
	@Autowired
	UserCrudRepository userRepo;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UserService userService;
	HttpSession httpSession;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login( @RequestBody Map<String,String> map) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			String userName = map.get("userName");
			String userEmail = map.get("userEmail");
			if (userEmail != null) {
				Optional<UserDto> user = userRepo.findByuserEmail(userEmail);
				String lastId = null; String userNick = null;
				if(user.isEmpty()) {
					//회원가입 처리 추가할 부분
					lastId = String.valueOf(Integer.parseInt(userRepo.findLastUser().getUserId()) + 1) ;
					userNick = "anonymous" + lastId;
					userRepo.save(UserDto.builder().userId(lastId).countryId("1").levelId("1").tierId("1").userScore("999")
								.userNick(userNick).userState('Y').userDelete('N').userSignupDate(new Date()).defaultLang('K')
				    			.userEmail(userEmail).userName(userName).build());
				}else if(user.get().getUserDelete() == 'Y'){//삭제했다가 가입하는 친구
					lastId = String.valueOf(Integer.parseInt(userRepo.findLastUser().getUserId()) + 1) ;
					userRepo.deleteById(user.get().getUserId()); //유니크 조건 때문에 일단 삭제하도록 구현
					userNick = "anonymous" + lastId;
					userRepo.save(UserDto.builder().userId(lastId).countryId("1").levelId("1").tierId("1").userScore("999")
							.userNick(userNick).userState('Y').userDelete('N').userSignupDate(new Date()).defaultLang('K')
			    			.userEmail(userEmail).userName(userName).build());
				}else {//현재 회원이다
					lastId = user.get().getUserId();
					user.get().setUserState('Y');
					userRepo.save(user.get());
				}
				String token = tokenProvider.createToken(lastId);// key, data, subject
				resultMap.put("Authorization","Bearer " + token);
				status = HttpStatus.OK;
				
			} else {
				status = HttpStatus.FAILED_DEPENDENCY;
			}
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	
	@PutMapping("/logout")
	public ResponseEntity<String> registerUser(@RequestBody Map<String,String> map ) {
		ResponseEntity<String> re = null;
		
		try {
			String userId = map.get("userId");
			Optional<UserDto> updateUser = userRepo.findById(userId);
			if(!updateUser.isEmpty()) {
				updateUser.get().setUserId(userId);
				updateUser.get().setUserState('N');
			}else {
				return new ResponseEntity<String>("NOT FOUND", HttpStatus.FAILED_DEPENDENCY);
			}
			
			UserDto saveUser = userRepo.save(updateUser.get());\
			re = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		}catch(Exception e) {
			re = new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return re;
	}
	
	@GetMapping("/country")
	public ResponseEntity<List<CountryDto>> getCountries() {
		ResponseEntity<List<CountryDto>> re = null;
		try {
			List<CountryDto> clist = countryRepo.findAll();
			
			System.out.println(clist.get(0).getCountryId());
		  if(clist != null) {
			  re = new ResponseEntity<List<CountryDto>>(clist, HttpStatus.OK);
		  }
		} catch (Exception e) {
			re = new ResponseEntity<List<CountryDto>>(new ArrayList<CountryDto>(), HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}		
		return re;
	}
	
	@GetMapping("/userinfo")
	public ResponseEntity<Map<String, Object>> getUserInfo( HttpServletRequest request) {
		Map<String, Object> resultMap = null;
		HttpStatus status = HttpStatus.OK;
		try {
			String authorization = request.getHeader("Authorization");
			if(authorization.indexOf("Bearer") != -1) {
				authorization = authorization.replaceAll("^Bearer\\s", "");
			}
			if (tokenProvider.validateToken(authorization)) {// 유효하면
				
				String userId = String.valueOf(tokenProvider.getSubject(authorization));
				resultMap = userService.getUserInfo(userId);
			} else {
				status = HttpStatus.FAILED_DEPENDENCY;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	@GetMapping("/usernick")
	public ResponseEntity<Map<String, Boolean>> nickCheck(@RequestBody Map<String,String> map){
		ResponseEntity<Map<String, Boolean>> re = null;
		Map<String, Boolean> resultMap = new HashMap<>();
		try {
			String nick = map.get("userNick");
			Optional<UserDto> user = userRepo.findByuserNick(nick);
			if(user.isEmpty()) resultMap.put("isUsed", false);
			else resultMap.put("isUsed", true);
			re = new ResponseEntity<Map<String, Boolean>>(resultMap, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			re = new ResponseEntity<Map<String, Boolean>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
			return re;
		}
		return re;
	}
	@PatchMapping("/userdelete")
	public ResponseEntity<?> userDelete(HttpServletRequest request){
		HttpStatus status = HttpStatus.OK;
		try {
			String authorization = request.getHeader("Authorization");
			if(authorization.indexOf("Bearer") != -1) {
				authorization = authorization.replaceAll("^Bearer\\s", "");
			}
			if (tokenProvider.validateToken(authorization)) {// 유효하면
				String userId = String.valueOf(tokenProvider.getSubject(authorization));
				Optional<UserDto> user = userRepo.findByuserId(userId);
				user.get().setUserState('N');
				user.get().setUserDelete('Y');
				userRepo.save(user.get()); //바뀐내용으로 저장
			} else {
				status = HttpStatus.FAILED_DEPENDENCY;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>("ERROR", status);
		}
		
		return new ResponseEntity<String>("SUCCESS", status);
	}
	@PatchMapping("/userinfoedit")
	public ResponseEntity<?> userEdit(HttpServletRequest request, @RequestBody Map<String,String> map){
		HttpStatus status = HttpStatus.OK;
		try {
			String authorization = request.getHeader("Authorization");
			if(authorization.indexOf("Bearer") != -1) {
				authorization = authorization.replaceAll("^Bearer\\s", "");
			}
			if (tokenProvider.validateToken(authorization)) {// 유효하면
				String userId = String.valueOf(tokenProvider.getSubject(authorization));
				System.out.println(userId);
				Optional<UserDto> user = userRepo.findByuserId(userId);
				Set<String> updates =  map.keySet();
				for(String s : updates) {
					switch(s) {
					case "userNick": 
						String nick = map.get(s);
						if(Pattern.matches("^[0-9a-zA-Z가-힣]{1,20}$", nick)) {
							user.get().setUserNick(nick);
						}
						else new ResponseEntity<String>("WRONG VALUE", HttpStatus.FAILED_DEPENDENCY);
						break;
					case "defaultLang": user.get().setDefaultLang(map.get(s).charAt(0));
						break;
					case "countryId": user.get().setCountryId(map.get(s));
						break;
					}
				}
				userRepo.save(user.get()); //바뀐내용으로 저장
			} else {
				status = HttpStatus.FAILED_DEPENDENCY;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>("ERROR", status);
		}
		
		return new ResponseEntity<String>("SUCCESS", status);
	}
}
