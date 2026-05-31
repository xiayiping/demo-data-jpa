package org.xyp.todoapp.view.controller.ui

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.xyp.todoapp.domain.todolist.cmd.CreateDraftCmd
import java.security.Principal

@Controller
@RequestMapping("/ui")
class TodoUiController {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TodoUiController::class.java)!!
    }

    @GetMapping(value = ["", "/"])
    fun index(): ModelAndView {
        logger.debug("index ... ...")
        return ModelAndView(
            "index",
            mapOf("view" to ViewObj(page = Page(nav = PageConst.NAV_PROGRESS)))
        )
    }

    @GetMapping(value = ["/draft", "/draft/"])
    fun draft(): ModelAndView {
        logger.debug("draft ... ...")
        return ModelAndView(
            "index",
            mapOf("view" to ViewObj(page = Page(nav = PageConst.NAV_DRAFT)))
        )
    }

    @GetMapping(value = ["/form", "/form/"])
    fun form(): ModelAndView {
        logger.debug("form ... ...")
        return ModelAndView(
            "index",
            mapOf("view" to ViewObj(page = Page(nav = PageConst.NAV_FORM)))
        )
    }

    @PostMapping(value = ["/tasks"])
    fun tasks(@ModelAttribute taskCmd: CreateDraftCmd, principal: Principal?): ModelAndView {
        logger.info("create draft cmd [{}] [{}]", taskCmd , principal)
        return ModelAndView(
            "index",
            mapOf("view" to ViewObj(page = Page(nav = PageConst.NAV_FORM)))
        )
    }
}