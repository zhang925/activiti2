package com.zzy.service;

import com.zzy.model.User;

public interface UserService {
    public User getModelByID(Integer id);
    public User getModelByParam(String hql,Object obj[]);


}
