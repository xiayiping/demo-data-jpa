package org.xyp.todoapp.view.controller.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.xyp.todoapp.app.resp.RoleDto
import org.xyp.todoapp.app.resp.UserDto
import org.xyp.todoapp.core.idgen.IdGenerator
import org.xyp.todoapp.domain.user.RoleId
import org.xyp.todoapp.domain.user.UserId
import org.xyp.todoapp.domain.user.cmd.UpdateUser

@RequestMapping("id")
@RestController
class DemoController(
    val idGenerator: IdGenerator<Long>,
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DemoController::class.java)!!
    }

    @GetMapping
    fun getIdTable(): Long {
        return idGenerator.generate(DemoController::class.java.name)
    }

    @GetMapping("role")
    fun getRoleTable(): RoleDto {
        return RoleDto(RoleId(1L), "roleA")
    }

    @PostMapping("users")
    fun updateUser(@RequestBody user: UpdateUser): UpdateUser {
        logger.info("{}", user)
        return user
    }

    @PostMapping("users2")
    fun updateUser2(@RequestBody user: UpdateUser): UserDto {
        logger.info("{}", user)
        val main = UserDto(UserId(1L), "username1", "p****d")
        val friend = UserDto(UserId(1L), "friend1", "p****d")
//        main.friend = OptField.of(friend)
        return main
    }
}