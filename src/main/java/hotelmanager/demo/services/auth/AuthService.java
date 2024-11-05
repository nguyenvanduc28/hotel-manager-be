package hotelmanager.demo.services.auth;

import hotelmanager.demo.dto.auth.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.exceptions.UnAuthorizedException;
import hotelmanager.demo.models.Role;
import hotelmanager.demo.models.UserEntity;
import hotelmanager.demo.repositories.RoleRepository;
import hotelmanager.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(AuthLoginDto authDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getUsername(),
                        authDto.getPassword()
                )
        );
        String token = jwtService.generateToken(authentication.getName());
        UserEntity userEntity = userRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new NotFoundException("profile not found"));
        ModelMapper modelMapper = new ModelMapper();
        return AuthResponse.builder()
                .user(modelMapper.map(userEntity, UserInfoDto.class))
                .token(token)
                .build();
    }
    @Transactional
    public AuthResponse register(AuthDto authDto) {
        if (userRepository.findByUsername(authDto.getUsername()).isPresent()) throw new RuntimeException("User exits");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(authDto.getPassword()));
        List<Role> roles = new ArrayList<>();
        for (RoleDto roleDto: authDto.getRoles()) {
            Role role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() -> new NotFoundException("role not found: "+roleDto.getName()));
            roles.add(role);
        }
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        var jwtToken = jwtService.generateToken(userEntity.getUsername());
        ModelMapper modelMapper = new ModelMapper();
        return AuthResponse.builder()
                .user(modelMapper.map(userEntity, UserInfoDto.class))
                .token(jwtToken)
                .build();
    }

    public UserInfoDto verifyToken(VerifyTokenRequest request) {
        String username = jwtService.extractUsername(request.getToken());
        if (!jwtService.isTokenValid(request.getToken(), username)) {
            throw new UnAuthorizedException();
        }

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("profile not found"));
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userEntity, UserInfoDto.class);
    }
}
