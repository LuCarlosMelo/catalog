package com.lucarlosmelo.catalog.services;

import com.lucarlosmelo.catalog.dtos.users.UserInsertRequest;
import com.lucarlosmelo.catalog.dtos.users.UserResponse;
import com.lucarlosmelo.catalog.dtos.users.UserUpdateRequest;
import com.lucarlosmelo.catalog.entities.User;
import com.lucarlosmelo.catalog.repositories.UserRepository;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;

import com.lucarlosmelo.catalog.services.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService implements ImplUserService {
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final Util util;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, Util util) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.util = util;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAllPaged(Pageable pageable) {
        var list = userRepository.findAll(pageable);
        return list.map(UserResponse::new);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        var obj = userRepository.findById(id);
        var entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserResponse(entity);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserInsertRequest insert(UserInsertRequest userInsertDTO) {
        var entity = new User();
        util.copyProperties(userInsertDTO, entity);
        entity.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        entity = userRepository.save(entity);
        return new UserInsertRequest(entity);
    }

    @Transactional
    public UserUpdateRequest update(Long id, UserUpdateRequest userUpdateRequest) {
        try {
            var user = userRepository.getOne(id);
            util.copyProperties(userUpdateRequest, user);
            user = userRepository.save(user);
            return new UserUpdateRequest(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id não encontrado" + id);
        }

    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id não encontrado" + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Violação de integridade");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Email não encontrado");
        }
        return user;
    }
}
