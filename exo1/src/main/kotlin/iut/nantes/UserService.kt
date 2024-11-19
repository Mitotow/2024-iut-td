package iut.nantes

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val database: Database) {

    fun save(user: User) {
        database.save(user)
    }

    fun delete(user: User) {
        database.delete(user)
    }

    fun update(user: User) {
        database.update(user)
    }

    fun findOne(id: UUID): User? {
        return database.findOne(id)
    }

    fun findAll(name: String?): List<User> {
        return database.findAll(name)
    }
}