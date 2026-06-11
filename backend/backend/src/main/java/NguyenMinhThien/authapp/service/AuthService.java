package NguyenMinhThien.authapp.service;

import NguyenMinhThien.authapp.dto.request.LoginRequest;
import NguyenMinhThien.authapp.dto.request.RegisterRequest;
import NguyenMinhThien.authapp.dto.response.AuthResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(String requestRefreshToken);
    void logout(String email);
}
