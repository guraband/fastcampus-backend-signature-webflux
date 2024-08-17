package com.tempapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController {
    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): Map<String, String> {
        return mapOf(
            "id" to id.toString(),
            "contents" to "%d의 본문".format(id)
        )
    }
}