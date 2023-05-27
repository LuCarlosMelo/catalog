package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto, entity);
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

    private void copyDtoToEntity(UserDTO userDto, User entity) {
        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        entity.setEmail(userDto.getEmail());

        entity.getRoles().clear();
        userDto.getRoles().forEach(role -> entity.getRoles().add(roleRepository.getOne(role.getId())));

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if(user == null){
            logger.error("User not found" + username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("User found: " + username);
        return user;
    }
}
