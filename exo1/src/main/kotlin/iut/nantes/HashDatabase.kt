package iut.nantes

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class HashDatabase: Database {
    private val map: MutableMap<UUID, User> = mutableMapOf()

    override fun save(user: User) {
        if (map[user.id] == null) {
            map[user.id] = user
        }
    }

    override fun delete(user: User) {
        if (map[user.id] != null) {
            map.remove(user.id)
        }
    }

    override fun update(user: User) {
        if (map[user.id] != null) {
            map[user.id] = user
        }
    }

    override fun findOne(id: UUID): User? {
        return map[id]
    }

    override fun findAll(name: String?): List<User> {
        return if (name != null)
            map.filter { (_, v) -> v.name == name }.values.toList()
        else map.values.toList()
    }
}