package com.d5error.web;

import com.d5error.mapper.AccountsMapper;
import com.d5error.pojo.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
    @ Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("username=" + username + ", password=" + password + ", option=" + option);

        if (option.equals("登录")) {
            if (tryToLogin(username, password)) {
                // 登录成功
            } else {
                // 登录失败
            }
        } else if (option.equals("注册")) {
            if (tryToRegister(username, password)) {
                // 注册成功
            } else {
                // 注册失败
            }
        } else {
            System.out.println("提交表单中的option有误");
        }
    }

    private boolean tryToRegister(String username, String password) throws IOException {
        if (tryToLogin(username, password)) {
            System.out.println("账号已存在");
            return false;
        }
        SqlSession sqlSession = getSqlSession();
        AccountsMapper accountsMapper = sqlSession.getMapper(AccountsMapper.class);
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        accountsMapper.addAccount(account);
        sqlSession.commit();
        sqlSession.close();
        return true;
    }

    private boolean tryToLogin(String username, String password) throws IOException {
        SqlSession sqlSession = getSqlSession();
        AccountsMapper accountsMapper = sqlSession.getMapper(AccountsMapper.class);

        Account account = accountsMapper.selectByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            System.out.println("密码正确");
            return true;
        }
        if (account == null) {
            System.out.println("账号不存在");
        } else {
            System.out.println("密码错误");
        }
        sqlSession.close();
        return false;
    }

    private SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        return sqlSessionFactory.openSession();
    }
}
