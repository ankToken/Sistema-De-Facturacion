package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager sessionFlashManager = new SessionFlashMapManager();
		
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", "Hi, " + authentication.getName() + ", you're logged successfully!!!");
		
		sessionFlashManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}