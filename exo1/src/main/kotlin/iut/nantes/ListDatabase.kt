package iut.nantes

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ListDatabase : Database {
    private val list: MutableList<User> = mutableListOf()

    override fun save(user: User) {
        if (!list.contains(user)) {
            list.add(user)
        }
    }

    override fun delete(user: User) {
        list.remove(user)
    }

    override fun update(user: User) {
        val storedUser = list.find { e -> e.id == user.id }
        if (storedUser != null) {
            list.remove(storedUser)
            list.add(user)
        }
    }

    override fun findOne(id: UUID): User? {
        return list.find { e -> e.id == id }
    }

    override fun findAll(name: String?): List<User> {
        return if (name != null) {
            list.filter { e -> e.name == name }
        } else list
    }
}