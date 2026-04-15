package com.Seia.controllers;

import com.Seia.dtos.AutenticacaoDTO;
import com.Seia.dtos.LoginResponseDTO;
import com.Seia.dtos.RegistroDTO;
import com.Seia.enums.UserRole;
import com.Seia.models.Usuario;
import com.Seia.repositories.UsuarioRepository;
import com.Seia.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // A nossa ferramenta de embaralhar senhas

    public AutenticacaoController(AuthenticationManager authenticationManager,
                                  TokenService tokenService,
                                  UsuarioRepository usuarioRepository,
                                  PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AutenticacaoDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();
        String token = tokenService.gerarToken(usuarioAutenticado);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody RegistroDTO data) {

        if (this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Este e-mail já está em uso.");
        }

        String senhaCriptografada = passwordEncoder.encode(data.senha());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(data.nome());
        novoUsuario.setEmail(data.email());
        novoUsuario.setSenha(senhaCriptografada);

        novoUsuario.setRole(UserRole.PROFESSOR);

        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
}