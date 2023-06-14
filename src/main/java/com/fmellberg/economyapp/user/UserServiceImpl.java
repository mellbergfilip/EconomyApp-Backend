package com.fmellberg.economyapp.user;

import com.fmellberg.economyapp.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        User createdUser = userRepository.save(user);
        logger.info("User created: {}", createdUser);
        return UserMapper.toDTO(createdUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("Total users found: {}", users.size());
        List<UserDTO> userDTOs = UserMapper.toDTOList(users);
        return userDTOs;
    }

    public UserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = UserMapper.toDTO(user.get());
            return userDTO;
        } else {
            logger.error("User not found with ID: {}", id);
            throw new ResourceNotFoundException("User","id", id);
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> existingUserOptional = userRepository.findById(userDTO.getId());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setUserName(userDTO.getUserName());

            User updatedUser = userRepository.save(existingUser);
            logger.info("User updated: {}", updatedUser);

            return UserMapper.toDTO(updatedUser);
        } else {
            logger.error("User not found with ID: {}", userDTO.getId());
            throw new ResourceNotFoundException("User","id", userDTO.getId());
        }
    }

    public void deleteUser(int id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User with ID {} deleted", id);
        } else {
            logger.error("User not found with ID: {}", id);
            throw new ResourceNotFoundException("User","id", id);
        }
    }
}