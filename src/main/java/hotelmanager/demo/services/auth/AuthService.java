package hotelmanager.demo.services.auth;

import hotelmanager.demo.dto.HotelDto;
import hotelmanager.demo.dto.auth.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.exceptions.UnAuthorizedException;
import hotelmanager.demo.models.Role;
import hotelmanager.demo.models.UserEntity;
import hotelmanager.demo.repositories.RoleRepository;
import hotelmanager.demo.repositories.UserRepository;
import hotelmanager.demo.services.HotelService;
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

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final HotelService hotelService;
    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       HotelService hotelService, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.hotelService = hotelService;
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
    @Transactional
    public AuthResponse registerAdmin(AuthDto authDto) {
        if (userRepository.findByUsername(authDto.getUsername()).isPresent()) {
            throw new RuntimeException("User exists");
        }
        
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(authDto.getPassword()));
        
        // Get admin role
        Role adminRole = roleRepository.findByName("ADMIN");
        
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        userEntity.setRoles(roles);
        


        HotelDto hotelDto = new HotelDto();
        HotelDto hotelDto1 = hotelService.createHotel(hotelDto);
        userEntity.setHotelId(hotelDto1.getId());
        userRepository.save(userEntity);
        
        var jwtToken = jwtService.generateToken(userEntity.getUsername());
        ModelMapper modelMapper = new ModelMapper();
        
        return AuthResponse.builder()
                .user(modelMapper.map(userEntity, UserInfoDto.class))
                .token(jwtToken)
                .build();
    }

    @Transactional
    public UserEntity createUser(AuthDto authDto, int hotelId) {
        if (userRepository.findByUsername(authDto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(authDto.getPassword()));
        userEntity.setHotelId(hotelId);
        
        List<Role> roles = new ArrayList<>();
        for (RoleDto roleDto: authDto.getRoles()) {
            Role role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() -> new NotFoundException("Role not found: " + roleDto.getName()));
            roles.add(role);
        }
        userEntity.setRoles(roles);
        
        return userRepository.save(userEntity);
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
