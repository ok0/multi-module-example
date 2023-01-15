package kr.co.ok0.controller.v1

import kr.co.ok0.Log
import kr.co.ok0.Welcome
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/")
class MemberController: Log {
  @GetMapping("/name")
  fun name(): String {
    logger.error("I really don't like that guy.")
    return "${Welcome().isWelcome()} Member Controller"
  }
}