package com.serviconli.auth.service;

import com.serviconli.auth.dto.AuthResponse;
import com.serviconli.auth.dto.LoginRequest;
import com.serviconli.auth.dto.RegisterRequest;
import com.serviconli.auth.model.Usuario;
import com.serviconli.auth.repository.UsuarioRepository;
import com.serviconli.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private static final String DOMINIO_PERMITIDO = "@gruposerviconli.com";

    public AuthResponse register(RegisterRequest request) {
        // 1. Validar el dominio del correo
        if (!request.getEmail().toLowerCase().endsWith(DOMINIO_PERMITIDO)) {
            throw new IllegalArgumentException("El correo debe pertenecer al dominio " + DOMINIO_PERMITIDO);
        }

        // 2. Validar que el usuario no exista
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // 3. Crear y guardar el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuarioRepository.save(usuario);

        // 4. Generar el token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        // Autenticar con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Si la autenticación es exitosa, generar el token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
