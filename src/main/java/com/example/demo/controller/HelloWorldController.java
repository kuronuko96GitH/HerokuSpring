package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloWorldController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello World!!");
        return "index";
    }

    @RequestMapping(value = "/sample.html", method = RequestMethod.GET)
    public String Sample(Model model) {
        model.addAttribute("message", "Sample World!!");
        // ここでsampleを返して、『templates』フォルダにある、sample.htmlを呼び出してるらしい。
        return "sample"; 
    }
}