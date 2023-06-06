package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.users.UserDTO;
import com.devsuperior.dscatalog.dtos.users.UserInsertRequest;
import com.devsuperior.dscatalog.dtos.users.UserUpdateRequest;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService implements IUserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        var list = userRepository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        var obj = userRepository.findById(id);
        var entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDTO insert(UserInsertRequest userInsertDTO) {
        var entity = new User();
        copyUserInsertToEntity(userInsertDTO, entity);
        entity.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateRequest userUpdateDTO) {
        try {
            var entity = userRepository.getOne(id);
            copyUpdateDtoToEntity(userUpdateDTO, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id not found" + id);
        }

    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id not found" + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if(user == null){
            LOGGER.error("User not found" + username);
            throw new UsernameNotFoundException("Email not found");
        }
        LOGGER.info("User found: " + username);
        return user;
    }

    private void copyUpdateDtoToEntity(UserUpdateRequest userDTO, User entity) {
        if(userDTO.getFirstName() != null && !userDTO.getFirstName().isBlank()){
            entity.setFirstName(userDTO.getFirstName());
        }
        if(userDTO.getLastName() != null && !userDTO.getLastName().isBlank()){
            entity.setLastName(userDTO.getLastName());
        }
        if(userDTO.getEmail() != null){
            entity.setEmail(userDTO.getEmail());
        }
        entity.getRoles().clear();
        userDTO.getRoles().forEach(role -> entity.getRoles().add(roleRepository.getOne(role.getId())));
    }

    private void copyUserInsertToEntity(UserInsertRequest userInsertDTO, User entity){
        BeanUtils.copyProperties(userInsertDTO, entity);
        entity.getRoles().clear();
        userInsertDTO.getRoles().forEach(role -> entity.getRoles().add(roleRepository.getOne(role.getId())));
    }

}
