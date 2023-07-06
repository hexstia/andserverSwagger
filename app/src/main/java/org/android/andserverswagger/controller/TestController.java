/*
 * Copyright 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.android.andserverswagger.controller;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.andserver.annotation.Addition;
import com.yanzhenjie.andserver.annotation.CookieValue;
import com.yanzhenjie.andserver.annotation.CrossOrigin;
import com.yanzhenjie.andserver.annotation.FormPart;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.PutMapping;
import com.yanzhenjie.andserver.annotation.RequestBody;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.cookie.Cookie;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;
import com.yanzhenjie.andserver.http.session.Session;
import com.yanzhenjie.andserver.util.Executors;
import com.yanzhenjie.andserver.util.MediaType;

import org.android.andserverswagger.config.LoginInterceptor;
import org.android.andserverswagger.model.UserInfo;
import org.android.andserverswagger.utils.FileUtils;
import org.android.andserverswagger.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
@SwaggerDefinition(
        info = @Info(
                description = "This is a sample server",
                version = "1.0.0",
                title = "Swagger Sample Servlet",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(name = "Sponge-Bob", email = "apiteam@swagger.io", url = "http://swagger.io"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}
        , tags = {@Tag(name = "users", description = "Operations about user")}
)
@Api(tags = "users",value = "/user", description = "gets some data from a servlet")
@RestController
@RequestMapping(path = "/user")
public class TestController {

    @RequestMapping(
        path = "/connection",
        method = {RequestMethod.GET},
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object getConnection(HttpRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("getLocalAddr", request.getLocalAddr());
        map.put("getLocalName", request.getLocalName());
        map.put("getLocalPort", request.getLocalPort());
        map.put("getRemoteAddr", request.getRemoteAddr());
        map.put("getRemoteHost", request.getRemoteHost());
        map.put("getRemotePort", request.getRemotePort());
        Logger.i(JSON.toJSONString(map));
        return map;
    }

    @CrossOrigin(
        methods = {RequestMethod.POST, RequestMethod.GET}
    )
//    @ApiOperation(httpMethod = "GET", value = "Resource to get a user", response = SampleData.class, nickname = "getUser")
//    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input", response = ApiResponse
//            .class)})
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "integer", paramType =
//                    "query"),
//            @ApiImplicitParam(name = "name", value = "User's name", required = true, dataType = "string", paramType =
//                    "query"),
//            @ApiImplicitParam(name = "email", value = "User's email", required = true, dataType = "string", paramType
//                    = "query"),
//            @ApiImplicitParam(name = "age", value = "User's age", required = true, dataType = "integer", paramType =
//                    "query"),
//            @ApiImplicitParam(name = "dateOfBirth", value = "User's date of birth, in dd-MM-yyyy format",
//                    dataType = "java.util.Date", paramType = "query")})

    @ApiOperation(httpMethod = "GET", value = "获取用户ID",  nickname = "/get/{userId}",notes = "获取用户ID")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input", response = org.android.andserverswagger.enitiy.ApiResponse
            .class)})

        @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType =
                    "query")})
    @RequestMapping(
        path = "/get/{userId}",
        method = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.GET},
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public   String info(@PathVariable(name = "userId") String userId, HttpRequest request) {
        return userId;
    }

    @PutMapping(path = "/get/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String modify(@PathVariable("userId") String userId, @RequestParam(name = "sex") String sex,
                  @RequestParam(name = "age") int age) {
        String message = "The userId is %1$s, and the sex is %2$s, and the age is %3$d.";
        return String.format(Locale.US, message, userId, sex, age);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String login(HttpRequest request, HttpResponse response, @RequestParam(name = "account") String account,
                 @RequestParam(name = "password") String password) {
        Session session = request.getValidSession();
        session.setAttribute(LoginInterceptor.LOGIN_ATTRIBUTE, true);

        Cookie cookie = new Cookie("account", account + "=" + password);
        response.addCookie(cookie);
        return "Login successful.";
    }

    @Addition(stringType = "login", booleanType = true)
    @GetMapping(path = "/userInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserInfo userInfo(@CookieValue("account") String account) {
        Logger.i("Account: " + account);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("123");
        userInfo.setUserName("AndServer");
        return userInfo;
    }

    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String upload(@RequestParam(name = "avatar") final MultipartFile file) {
        final File localFile = FileUtils.createRandomFile(file);

        // We use a sub-thread to process files so that the api '/upload' can respond faster
        Executors.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    file.transferTo(localFile);

                    // Do something ...
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return localFile.getAbsolutePath();
    }

    @GetMapping(path = "/consume", consumes = {"application/json", "!application/xml"})
    String consume() {
        return "Consume is successful";
    }

    @GetMapping(path = "/produce", produces = {"application/json; charset=utf-8"})
    String produce() {
        return "Produce is successful";
    }

    @GetMapping(path = "/include", params = {"name=123"})
    String include(@RequestParam(name = "name") String name) {
        return name;
    }

    @GetMapping(path = "/exclude", params = "name!=123")
    String exclude() {
        return "Exclude is successful.";
    }

    @GetMapping(path = {"/mustKey", "/getName"}, params = "name")
    String getMustKey(@RequestParam(name = "name") String name) {
        return name;
    }

    @PostMapping(path = {"/mustKey", "/postName"}, params = "name")
    String postMustKey(@RequestParam(name = "name") String name) {
        return name;
    }

    @GetMapping(path = "/noName", params = "!name")
    String noName() {
        return "NoName is successful.";
    }

    @PostMapping(path = "/formPart")
    UserInfo forPart(@FormPart(name = "user") UserInfo userInfo) {
        return userInfo;
    }

    @PostMapping(path = "/jsonBody")
    UserInfo jsonBody(@RequestBody UserInfo userInfo) {
        return userInfo;
    }

    @PostMapping(path = "/listBody")
    List<UserInfo> jsonBody(@RequestBody List<UserInfo> infoList) {
        return infoList;
    }
}