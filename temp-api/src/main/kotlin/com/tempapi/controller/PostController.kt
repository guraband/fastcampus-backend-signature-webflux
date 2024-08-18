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
        println("id : $id")

        if (id > 9L) {
            throw Exception("존재하지 않는 게시물입니다.")
        }

        Thread.sleep(1_000)

        return mapOf(
            "id" to id.toString(),
            "contents" to "%d의 본문".format(id)
        )
    }
}