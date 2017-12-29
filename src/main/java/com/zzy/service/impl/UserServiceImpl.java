package com.zzy.service.impl;

import com.zzy.dao.BaseDao;
import com.zzy.model.User;
import com.zzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BaseDao basedao;

    public User getModelByID(Integer id) {
        return (User)basedao.get(BaseDao.class,id);
    }

    public User getModelByParam(String hql, Object[] obj) {
      return (User)basedao.get(hql,obj);
    }


}
