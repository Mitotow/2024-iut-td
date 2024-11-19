package iut.nantes

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ListDatabaseTest {
    private val context: ApplicationContext =
        AnnotationConfigApplicationContext(AppConfig::class.java)

    private var db: Database =
        context.getBean(Database::class.java)

    @AfterEach
    fun afterEach() {
        db = context.getBean(Database::class.java)
    }

    @Test
    fun insert() {
        db.save(user())

        assertNotNull(db.findOne(user().id))
        assertTrue(db.findAll(null).isNotEmpty())
    }

    @Test
    fun insertSameMultiple() {
        db.save(user())
        db.save(user())

        assertEquals(db.findAll(null).size, 1)
    }

    @Test
    fun insertMultiple() {
        db.save(user())
        db.save(user2())

        assertEquals(db.findAll(null).size, 2)
        assertNotNull(db.findOne(user().id))
        assertNotNull(db.findOne(user2().id))
        assertTrue(db.findAll(null).containsAll(listOf(user(), user2())))
    }

    @Test
    fun findAllByName() {
        db.save(user())
        db.save(user2())

        assertEquals(db.findAll(user().name).size, 1)
    }

    @Test
    fun deleteOne() {
        db.save(user())
        db.delete(user())
        assertTrue(db.findAll(null).isEmpty())
    }

    @Test
    fun deleteUnexisting() {
        assertDoesNotThrow { db.delete(user()) }
    }

    @Test
    fun updateOne() {
        val defaultUser = user()
        val updatedUser = User(defaultUser.id, "Updated User", "email@thomas.ambroise", defaultUser.age?.plus(5))

        db.save(defaultUser)
        db.update(updatedUser)

        val inDbUser = db.findOne(updatedUser.id)

        assertNotNull(inDbUser)
        assertEquals(updatedUser.name, inDbUser.name)
        assertEquals(updatedUser.email, inDbUser.email)
        assertEquals(updatedUser.age, inDbUser.age)
    }

    @Test
    fun updateOneNonExisting() {
        db.update(user2())
        val inDbUser = db.findOne(user2().id)
        assertNull(inDbUser)
    }

    @Test
    fun updateOneNonExistingInMultiple() {
        db.save(user())
        db.update(user2())
        val inDbUser = db.findOne(user2().id)
        assertNull(inDbUser)
    }
}