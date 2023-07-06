package org.android.andserverswagger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yanzhenjie.andserver.annotation.CrossOrigin;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestMethod;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.framework.body.JsonBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.MediaType;

import org.android.andserverswagger.App;

import io.swagger.models.Swagger;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

@RestController
@RequestMapping(path = "/swagger")
public class SwaggerController {

    @CrossOrigin(
            methods = {RequestMethod.POST, RequestMethod.GET}
    )
    @RequestMapping(
            path = "/api/{filename}",
            method = { RequestMethod.POST, RequestMethod.GET},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseBody info(@PathVariable(name = "filename") String fileName, HttpRequest request, HttpResponse response) throws JsonProcessingException {
        Swagger swagger = App.getSwagger();
        if (swagger == null) {
            response.setStatus(404);
            return null;
        }
        if ("swagger.json".equals(fileName)) {
//           return  ;
            JsonBody jsonBody = new JsonBody(Json.mapper().writeValueAsString(swagger));
            return jsonBody;
        } else if ("swagger.yaml".equals(fileName)) {
            JsonBody jsonBody = new JsonBody(Yaml.mapper().writeValueAsString(swagger));
            return jsonBody;
        } else {
            response.setStatus(404);
            return null;
        }

    }
}
