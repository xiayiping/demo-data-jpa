package org.xyp.todoapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class TodoappApplication

fun main(args: Array<String>) {
    SpringApplication.run(TodoappApplication::class.java, *args)
}
