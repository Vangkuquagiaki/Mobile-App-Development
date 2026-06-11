package NguyenMinhThien.authapp.service.impl;

import NguyenMinhThien.authapp.dto.request.LoginRequest;
import NguyenMinhThien.authapp.dto.request.RegisterRequest;
import NguyenMinhThien.authapp.dto.response.AuthResponse;
import NguyenMinhThien.authapp.entity.RefreshToken;
import NguyenMinhThien.authapp.entity.Role;
import NguyenMinhThien.authapp.entity.User;
import NguyenMinhThien.authapp.exception.AppException;
import NguyenMinhThien.authapp.repository.RefreshTokenRepository;
import NguyenMinhThien.authapp.repository.RoleRepository;
import NguyenMinhThien.authapp.repository.UserRepository;
import NguyenMinhThien.authapp.security.CustomUserDetails;
import NguyenMinhThien.authapp.service.AuthService;
import NguyenMinhThien.authapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Lazy
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.refreshExpirationMs}")
    private Long refreshExpirationMs;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new AppException("Email is already in use");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        User user = User.builder()
                .name(registerRequest.getName())
                .email(email)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .provider("local")
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().trim().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        // Delete existing refresh token if exists
        refreshTokenRepository.deleteByUser(userDetails.getUser());
        refreshTokenRepository.flush();
        
        RefreshToken refreshToken = createRefreshToken(userDetails.getUser());

        User user = userDetails.getUser();
        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    CustomUserDetails userDetails = new CustomUserDetails(user);
                    String token = jwtService.generateToken(userDetails);
                    return AuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .name(user.getName())
                            .email(user.getEmail())
                            .build();
                })
                .orElseThrow(() -> new AppException("Refresh token is not in database"));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new AppException("Refresh token was expired. Please sign in again");
        }
        return token;
    }

    @Override
    @Transactional
    public void logout(String email) {
        if (email != null) {
            userRepository.findByEmail(email.trim().toLowerCase())
                    .ifPresent(refreshTokenRepository::deleteByUser);
        }
    }
}
