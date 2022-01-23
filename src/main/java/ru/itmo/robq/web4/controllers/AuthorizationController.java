package ru.itmo.robq.web4.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.robq.web4.data.RefreshTokenRepository;
import ru.itmo.robq.web4.data.RoleRepository;
import ru.itmo.robq.web4.data.UserRepository;
import ru.itmo.robq.web4.exceptions.ValidationException;
import ru.itmo.robq.web4.model.RefreshToken;
import ru.itmo.robq.web4.model.Role;
import ru.itmo.robq.web4.model.User;
import ru.itmo.robq.web4.network.*;
import ru.itmo.robq.web4.security.JwtTokenService;
import ru.itmo.robq.web4.security.userDetails.CustomUserDetails;
import ru.itmo.robq.web4.security.userDetails.UserRole;
import ru.itmo.robq.web4.validators.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class AuthorizationController {

    private final JwtTokenService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;

    private final String TOKEN_TYPE = "Bearer";

    public AuthorizationController(JwtTokenService authService,
                                   AuthenticationManager authenticationManager,
                                   UserRepository userRepo,
                                   RefreshTokenRepository refreshTokenRepo,
                                   UserValidator userValidator,
                                   PasswordEncoder passwordEncoder,
                                   RoleRepository roleRepo) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    private Optional<String> createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<User> userOptional = userRepo.findById(userId);

        if (userOptional.isPresent()) {
            refreshToken.setUser(userOptional.get());
        } else {
            return Optional.empty();
        }

        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepo.save(refreshToken);
        return Optional.of(refreshToken.getRefreshToken());
    }

    private int deleteByUserId(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        return userOptional.map(refreshTokenRepo::deleteByUser).orElse(0);
    }

    private Optional<String> updateRefreshToken(Long userId) {
        deleteByUserId(userId);
        return createRefreshToken(userId);
    }

    private User saveUser(User user) {
        String pwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pwd));
        Set<Role> rolePersistSet = user.getRoleSet().stream()
                .map(rt -> roleRepo.findByRoleName(rt.getRoleName()))
                .collect(Collectors.toSet());
        user.setRoleSet(rolePersistSet);
        user = userRepo.save(user);
        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            userValidator.validate(new User(req.getUsername(), req.getPassword()));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            String accessToken = authService.createToken(userDetails);

            refreshTokenRepo.deleteByUser(userRepo.getById(userDetails.getId()));

            Optional<String> refreshTokenOptional = updateRefreshToken(userDetails.getId());
            if (!refreshTokenOptional.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(accessToken, TOKEN_TYPE,
                    refreshTokenOptional.get(), userDetails.getId(), userDetails.getUsername(), roles));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = new User(req.getUsername(), req.getPassword());
        try {
            userValidator.validate(user);
            if (userRepo.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User with this username already exists"));
            }
            user.addRole(UserRole.ROLE_USER);
            return ResponseEntity.status(HttpStatus.CREATED).body(this.saveUser(user));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest req) {
        try {
            RefreshToken refreshToken = refreshTokenRepo.findByRefreshToken(req.getRefreshToken());
            if (refreshToken == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Token not found"));
            }
            User user = refreshToken.getUser();
            Optional<String> optionalRT = updateRefreshToken(user.getUid());
            if (!optionalRT.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User is not present in database"));
            }
            CustomUserDetails customUserDetails = CustomUserDetails.build(user);
            String accessToken = authService.createToken(customUserDetails);

            return ResponseEntity.status(HttpStatus.CREATED).body(new RefreshResponse(accessToken, optionalRT.get(), TOKEN_TYPE));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }
}
