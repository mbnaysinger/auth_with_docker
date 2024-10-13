package br.com.auth.service;

import br.com.auth.config.jwt.JwtService;
import br.com.auth.entity.User;
import br.com.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    //private final EmailService emailService;

    public String authenticate(String cpf, String password) {
        User user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

//        if (needs2FA(user)) {
//            generate2FACode(user);
//            return "2FA required";
//        }

        return jwtService.generateToken(user);
    }

    private boolean needs2FA(User user) {
        return user.getLast2FADate() == null ||
                ChronoUnit.DAYS.between(user.getLast2FADate(), LocalDateTime.now()) >= 15;
    }

//    private void generate2FACode(User user) {
//        String code = generateRandomCode();
//        user.setTwoFACode(code);
//        user.setTwoFACodeExpiration(LocalDateTime.now().plusMinutes(15));
//        userRepository.save(user);
//        emailService.sendTwoFACode(user.getEmail(), code);
//    }

    public String verify2FA(String cpf, String code) {
        User user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getTwoFACode().equals(code) || LocalDateTime.now().isAfter(user.getTwoFACodeExpiration())) {
            throw new BadCredentialsException("Invalid or expired 2FA code");
        }

        user.setLast2FADate(LocalDateTime.now());
        user.setTwoFACode(null);
        user.setTwoFACodeExpiration(null);
        userRepository.save(user);

        return jwtService.generateToken(user);
    }
}