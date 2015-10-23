package com.robert.whatdo.service;

import com.robert.whatdo.model.User;

/**
 * Created by RobertXi on 10/22/15.
 */
public class UserService {

    public static void register(User user){
        WhatDoDAO.registerUser(user);
    }

    public static boolean checkUsername(String username){
        return WhatDoDAO.checkUsername(username);
    }
}
