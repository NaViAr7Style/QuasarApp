package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.model.entity.UserActivationCodeEntity;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.RoleRepository;
import bg.deliev.quasarapp.repository.UserActivationCodeRepository;
import bg.deliev.quasarapp.repository.UserRepository;
import bg.deliev.quasarapp.service.aop.WarnIfExecutionExceeds;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;
//    private final ApplicationEventPublisher appEventPublisher;


    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserActivationCodeRepository userActivationCodeRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
//        this.appEventPublisher = appEventPublisher;
    }

    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {

            LOGGER.warn("Registration failed: Email {} is already taken.", userRegistrationDTO.getEmail());

            return false;
        }

        try {
            UserEntity user = modelMapper.map(userRegistrationDTO, UserEntity.class);
            user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
            user.setActive(true);

            UserRoleEntity userRole = roleRepository.getByRole(UserRoleEnum.USER);
            user.getRoles().add(userRole);

            userRepository.save(user);

//          The event is not published to avoid the need for an SMTP server and configuration
//          for hosting and portfolio purposes.

//            appEventPublisher.publishEvent(
//                    new UserRegisteredEvent(
//                            "UserService",
//                            userRegistrationDTO.getEmail(),
//                            userRegistrationDTO.fullName()
//                    )
//            );

            return true;
        } catch (MappingException | DataAccessException | NoSuchElementException ex) {

            LOGGER.warn("User registration failed: {}", ex.getMessage());

            return false;
        } catch (Exception ex) {

            LOGGER.error("Unexpected error during user registration: {}", ex.getMessage());

            return false;
        }
    }

    @Override
    public UserDetailsDTO findByUsername(String username) {
        UserEntity userEntity = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

        UserDetailsDTO userDetailsDTO = modelMapper.map(userEntity, UserDetailsDTO.class);

        mapRolesToDTO(userEntity, userDetailsDTO);

        return userDetailsDTO;
    }

    @WarnIfExecutionExceeds(timeInMillis = 1000)
    @Override
    public List<UserManagementDTO> getAllUsers() {

        List<UserEntity> entities = userRepository.findAll();

        return entities
                .stream()
                .map(userEntity -> {
                    UserManagementDTO dto = modelMapper.map(userEntity, UserManagementDTO.class);
                    mapRolesToDTO(userEntity, dto);
                    return dto;
                })
                .sorted(
                    Comparator
                        .<UserManagementDTO>comparingInt(dto -> hasAdminRole(dto) ? 0 : 1)
                        .thenComparing(UserManagementDTO::getEmail, String.CASE_INSENSITIVE_ORDER)
                )
                .toList();
    }

    @Override
    public Optional<UserManagementDTO> findUserById(Long id) {

        return userRepository
                .findById(id)
                .map(user -> {
                    UserManagementDTO dto = modelMapper.map(user, UserManagementDTO.class);
                    mapRolesToDTO(user, dto);
                    return dto;
                });
    }

    @Override
    public void deleteUserById(Long id) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userEntity.setRoles(new HashSet<>());
        List<UserActivationCodeEntity> allByUserId = userActivationCodeRepository.findAllByUserId(id);
        userActivationCodeRepository.deleteAll(allByUserId);

        userRepository.save(userEntity);

        userRepository.delete(userEntity);
    }

    @Override
    public void updateUserRoles(UpdateUserRolesDTO updateUserRolesDTO) {
        UserEntity userEntity = userRepository
                .findById(updateUserRolesDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Set<UserRoleEnum> roleEnums = updateUserRolesDTO
                .getRoles()
                .stream()
                .map(UserRoleEnum::valueOf)
                .collect(Collectors.toSet());

        Set<UserRoleEntity> roleEntities = roleRepository.findAllByRoleIn(roleEnums);

        userEntity.setRoles(roleEntities);

        userRepository.save(userEntity);
    }

    @Override
    public void verifyUser(String activationCode) {
        UserActivationCodeEntity byActivationCode = userActivationCodeRepository
                .findByActivationCode(activationCode)
                .orElseThrow(() -> new NoSuchElementException("No such activation code"));

        UserEntity user = byActivationCode.getUser();

        user.setActive(true);
        userRepository.save(user);

        userActivationCodeRepository.delete(byActivationCode);
    }

    private static void mapRolesToDTO(UserEntity userEntity, UserDetailsDTO dto) {
        dto.setRoles(extractRoles(userEntity));
    }

    private static void mapRolesToDTO(UserEntity userEntity, UserManagementDTO dto) {
        dto.setRoles(extractRoles(userEntity));
    }

    private static List<String> extractRoles(UserEntity userEntity) {
        return userEntity.getRoles()
            .stream()
            .map(role -> role.getRole().name())
            .toList();
    }

    private boolean hasAdminRole(UserManagementDTO dto) {
        return dto.getRoles().contains(UserRoleEnum.ADMIN.name());
    }
}