package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.UserEntity;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.OrderResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private Environment env;
    private OrderServiceClient orderServiceClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
                           , BCryptPasswordEncoder passwordEncoder
                           , Environment env
                           , OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = new UserEntity(userDto);
        userEntity.createUserPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(userEntity);

        // User 클래스에 getter가 있어야 UserDto에 매핑된다.
        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /* RestTemplate 적용*/
        /*String orderUrl = String.format(env.getProperty("order_service.uri"), userId);
        ResponseEntity<List<OrderResponse>> orderResponses = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OrderResponse>>() {
                });
        */
        /*Feign 적용*/
        /*
        List<OrderResponse> orders = null;
        try {
            orderServiceClient.getOrders(userId);
        } catch (FeignException ex) {
            log.error(ex.getMessage());
        }*/

        /*FeignerrorDecoder 활용한 에러 핸들링*/
        List<OrderResponse> orders = orderServiceClient.getOrders(userId);

        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        return new ModelMapper().map(userRepository.findByEmail(userEmail), UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException(username);

        return new User(user.getEmail(), user.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>()) {
        };
    }
}
