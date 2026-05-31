package org.xyp.todoapp.view.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("")
@Controller
class IndexController {
    @GetMapping(value = ["", "/"])
    fun index(): String {
        return "redirect:/ui";
    }
}