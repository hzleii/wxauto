package com.hzlei.wxauto.controller;

import cn.hutool.core.text.UnicodeUtil;
import com.hzlei.wxauto.service.WebConsoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/")
public class DemoController {
    @Resource
    private WebConsoleService webConsoleService;

    @GetMapping("start")
    public String helloWorld() {
//        webConsoleService.webConsole("qweasd", "", 9);
        String string = UnicodeUtil.toString("\\u5347\\u7ea7\\u5750\\u9a91#\\u5927\\u62a4\\u6cd5");
        return string;
    }
    @GetMapping("stop")
    public String helloWorld2() {
//        webConsoleService.webConsole("qweasd", "shutdown", 9);
        return "hello world";
    }
    @PostMapping ("callback")
    public String callback(@RequestBody String body) {
        System.out.println("body = " + body);
        return "hello world";
    }
}
