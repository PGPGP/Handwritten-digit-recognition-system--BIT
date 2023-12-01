package com.segroup.seproject_backend.repository;

import com.segroup.seproject_backend.data_item.UserDBItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    UserDBItem users;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDBItem check(String name, String password) {
        UserDBItem user=null;
        try {
            String sql = "select * from users where user_name=? and user_password=?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserDBItem.class), name, password);
            //UserDBItem user = (UserDBItem) jdbcTemplate.queryForList(sql, UserDBItem.class,new Object[]{name, password});

        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }


    @Override
    public UserDBItem addUser(String name, String password) {
            String sql = "insert into users(user_name,user_password) values(?,?)";
            jdbcTemplate.update(sql, name, password);
            UserDBItem user=null;
            try{
                String sql2 = "select * from users where user_name=?";
                user = jdbcTemplate.queryForObject(sql2, new BeanPropertyRowMapper<>(UserDBItem.class),name);
            }catch (EmptyResultDataAccessException e){
                return null;
            }
            return user;
            //return null;
    }

    @Override
    public UserDBItem unit(String name) {
        UserDBItem user=null;
        try{
            String sql = "select * from users where user_name=?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserDBItem.class),name);
            //UserDBItem user = (UserDBItem) jdbcTemplate.queryForList(sql, UserDBItem.class,name);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }
}
