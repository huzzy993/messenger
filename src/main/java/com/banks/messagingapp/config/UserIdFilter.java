package com.banks.messagingapp.config;

import com.banks.messagingapp.controller.GlobalControllerAdvice;
import com.banks.messagingapp.dto.ErrorDto;
import com.banks.messagingapp.entity.AppUser;
import com.banks.messagingapp.exception.UserNotFoundException;
import com.banks.messagingapp.model.CurrentUserDetails;
import com.banks.messagingapp.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.banks.messagingapp.util.Constants.USER_ID_HEADER;
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
		String userId = request.getHeader(USER_ID_HEADER);
		Optional<AppUser> user = appUserService.findUser(Long.valueOf(userId));
		log.info("current user {}", user);
		if (user.isPresent()) {
			currentUserDetails.setAppUser(user.get());
			filterChain.doFilter(request, response);
		} else {
			ResponseEntity<ErrorDto> errorDtoResponseEntity =
							globalControllerAdvice.exceptionHandler(new UserNotFoundException("UserId " + userId + " found in header does not exist"));
			response.setContentType(APPLICATION_JSON_VALUE);
			response.setStatus(errorDtoResponseEntity.getStatusCode().value());
			try (PrintWriter writer = response.getWriter()) {
				writer.write(objectMapper.writeValueAsString(errorDtoResponseEntity.getBody()));
				writer.flush();
			}
		}

	}

}
