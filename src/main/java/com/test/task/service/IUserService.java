package com.test.task.service;

import com.test.task.model.User;

public interface IUserService{

    User save(User request);

    String login(String email, String password);

}
