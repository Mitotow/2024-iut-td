package iut.nantes

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class Exercies {

    private val context: ApplicationContext =
        AnnotationConfigApplicationContext(AppConfig::class.java)

    @Test
    fun exo1_1() {
        val userService: UserService = context.getBean(UserService::class.java)
        userService.save(user())
        val user = userService.findOne(user().id)

        assertThat(user).isEqualTo(user())
    }

    @Test
    fun exo1_2() {
        val userService: UserService = context.getBean(UserService::class.java)
        val superUserService: SuperUserService = context.getBean(SuperUserService::class.java)
        userService.save(user())

        assertThat(superUserService.findAll()).isEqualTo(listOf(user()))
    }

    @Test
    fun exo1_3() {
        val userService: UserService = context.getBean(UserService::class.java)
        val superUserService: SuperUserService = context.getBean(SuperUserService::class.java)
        userService.save(user())

        assertThat(superUserService.findAll()).isEmpty()
    }

    @Test
    fun exo1_7() {
        val context = AnnotationConfigApplicationContext(AppConfig::class.java)
        val userService = context.getBean(UserService::class.java)
        val superUserService = context.getBean(SuperUserService::class.java)

        assertThat(userService.database).isInstanceOf(ListDatabase::class)
        assertThat(superUserService.database).isInstanceOf(HashDatabase::class)
    }
}