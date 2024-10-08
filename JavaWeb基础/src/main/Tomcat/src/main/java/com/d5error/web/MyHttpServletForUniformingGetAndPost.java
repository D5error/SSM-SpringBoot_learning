package com.d5error.web;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/GetAndPost")
public class MyHttpServletForUniformingGetAndPost extends HttpServlet {
    boolean isPost = false;

    @ Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = (String) req.getAttribute("msg");
        if (msg != null) {
            System.out.println("msg: " + req.getAttribute("msg"));
        }

        System.out.println("收到一条get请求");

        req.setCharacterEncoding("UTF-8");

        Map<String, String[]> parameterMap = req.getParameterMap();
        for (String key : parameterMap.keySet()) {
            System.out.print(key + ":" );
            String[] values = parameterMap.get(key);
            for (String value : values) {
                if (!isPost) {
                    byte[] bytes = value.getBytes(StandardCharsets.ISO_8859_1);
                    value = new String(bytes, StandardCharsets.UTF_8);
                }
                System.out.print(value + " " );
            }
            System.out.println();
        }


        // 响应字节数据
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/whiteD5.png");
        ServletOutputStream outputStream = resp.getOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }


        // 响应字符数据
        resp.setHeader("content-type", "text/html;charset=utf-8");
        String name = req.getParameter("name");
        System.out.println("'/http-servlet': name = " + name);
        resp.getWriter().write("<h1>" + name + ",你好</h1>");

        outputStream.close();
        fileInputStream.close();
    }

    @ Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        isPost = true;
        System.out.println("收到了一条post请求");
        System.out.println("调用doGet方法");
        this.doGet(req, resp);
    }
}
