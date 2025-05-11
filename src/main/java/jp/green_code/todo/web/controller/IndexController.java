package jp.green_code.todo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static jp.green_code.todo.web.controller.TodoController.LIST_PATH;


@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "redirect:" + LIST_PATH;
    }
}
