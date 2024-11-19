package iut.nantes

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.UUID

@SpringBootTest
class Exo8 {

    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun exo_8() {
        assertDoesNotThrow { AnnotationConfigApplicationContext(AppConfig::class.java) }
    }

    @Test
    fun exo_9() {
        every { userService.delete(any()) } returns Unit
        every { userService.delete(user()) } throws NoSuchElementException()

        assertThrows<NoSuchElementException> { userService.delete(user()) }
        userService.delete(user(UUID.randomUUID()))
    }
}