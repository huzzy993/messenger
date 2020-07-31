package com.huzzy.messenger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzzy.messenger.controller.GlobalControllerAdvice;
import com.huzzy.messenger.dto.ErrorDto;
import com.huzzy.messenger.entity.AppUser;
import com.huzzy.messenger.exception.UserNotFoundException;
import com.huzzy.messenger.model.CurrentUserDetails;
import com.huzzy.messenger.service.AppUserService;
import com.huzzy.messenger.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@AllArgsConstructor
@Slf4j
public class UserIdFilter extends OncePerRequestFilter {

	private final CurrentUserDetails currentUserDetails;

	private final AppUserService appUserService;

	private final GlobalControllerAdvice globalControllerAdvice;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
																	FilterChain filterChain) throws ServletException, IOException {
		String userId = request.getHeader(Constants.USER_ID_HEADER);
		Optional<AppUser> user = appUserService.findUser(Long.valueOf(userId));
		log.info("current user {}", user);
		if (user.isPresent()) {
			currentUserDetails.setAppUser(user.get());
			filterChain.doFilter(request, response);
		} else {
			String message = "UserId " + userId + " found in header does not exist";
			ResponseEntity<ErrorDto> errorDtoResponseEntity =
							globalControllerAdvice.exceptionHandler(new UserNotFoundException(message));
			response.setContentType(APPLICATION_JSON_VALUE);
			response.setStatus(FORBIDDEN.value());
			try (PrintWriter writer = response.getWriter()) {
				writer.write(objectMapper.writeValueAsString(errorDtoResponseEntity.getBody()));
				writer.flush();
			}
		}

	}

}
