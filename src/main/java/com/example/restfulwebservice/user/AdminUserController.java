package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value = "/users/{id}", produces = "application/vnd.heidi.appv1+json")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "password", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value = "/users/{id}", produces = "application/vnd.heidi.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 반환받은 User 값을 UserV2로 변환한다.
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);
        // 따라서 userV2는 user의 모든 메서드를 갖고있고, grade에 대한 속성도 추가로! 갖고있는것임
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }

}