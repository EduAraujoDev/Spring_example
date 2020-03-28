package br.com.alura.forum.security.controller;

import br.com.alura.forum.security.controller.dto.input.LoginInputDto;
import br.com.alura.forum.security.controller.dto.output.AuthenticationTokenOutputDto;
import br.com.alura.forum.security.jwt.TokenManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {

    private AuthenticationManager authManager;
    private TokenManager tokenManager;

    public UserAuthenticationController(AuthenticationManager authManager, TokenManager tokenManager) {
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationTokenOutputDto> authenticate(@RequestBody LoginInputDto logininfo) {
        UsernamePasswordAuthenticationToken authenticationToken = logininfo.build();

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);

            String jwt = tokenManager.generateToken(authentication);

            AuthenticationTokenOutputDto tokenResponse = new AuthenticationTokenOutputDto("Bearer", jwt);

            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}