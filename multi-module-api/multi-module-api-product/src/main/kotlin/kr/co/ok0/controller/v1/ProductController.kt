package kr.co.ok0.controller.v1

import kr.co.ok0.Log
import kr.co.ok0.Welcome
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/product")
class ProductController: Log {
  @GetMapping("/name")
  fun isHello(): String {
    logger.error("This is Product Controller.")
    return "${Welcome().isWelcome()} Product Controller"
  }
}