package bg.deliev.quasarapp.service.impl;

import bg.deliev.quasarapp.model.dto.UpdateUserRolesDTO;
import bg.deliev.quasarapp.model.dto.UserDetailsDTO;
import bg.deliev.quasarapp.model.dto.UserManagementDTO;
import bg.deliev.quasarapp.model.dto.UserRegistrationDTO;
import bg.deliev.quasarapp.model.entity.UserActivationCodeEntity;
import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.model.events.UserRegisteredEvent;
import bg.deliev.quasarapp.repository.RoleRepository;
import bg.deliev.quasarapp.repository.UserActivationCodeRepository;
import bg.deliev.quasarapp.repository.UserRepository;
import bg.deliev.quasarapp.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;
    private final ApplicationEventPublisher appEventPublisher;


    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserActivationCodeRepository userActivationCodeRepository,
                           ApplicationEventPublisher appEventPublisher) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.appEventPublisher = appEventPublisher;
    }

    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDTO) {
        UserEntity user = modelMapper.map(userRegistrationDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        try {
            UserRoleEntity userRole = roleRepository.getByRole(UserRoleEnum.USER);
            user.getRoles().add(userRole);

            userRepository.save(user);

            appEventPublisher.publishEvent(
                    new UserRegisteredEvent(
                            "UserService",
                            userRegistrationDTO.getEmail(),
                            userRegistrationDTO.fullName()
                    )
            );

            return true;
        } catch (Exception e) {
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


    @Override
    public List<UserManagementDTO> getAllUsers() {

        List<UserEntity> entities = userRepository.findAll();

        return entities
                .stream()
                .map(e -> {
                    UserManagementDTO dto = modelMapper.map(e, UserManagementDTO.class);
                    mapRolesToDTO(e, dto);
                    return dto;
                })
                .sorted(Comparator.comparing(UserManagementDTO::getId))
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

        userRepository.deleteById(id);
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

    private static void mapRolesToDTO(UserEntity userEntity, Object dto) {
        List<String> roles = userEntity
                .getRoles()
                .stream()
                .map(roleEntity -> roleEntity.getRole().name())
                .toList();

        if (dto instanceof UserDetailsDTO) {
            ((UserDetailsDTO) dto).setRoles(roles);
            return;
        }

        if (dto instanceof UserManagementDTO) {
            ((UserManagementDTO) dto).setRoles(roles);
        }
    }
}
