package iut.nantes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SuperUserService() {
    @Autowired
    lateinit var database: HashDatabase

    fun findAll(): List<User> {
        return database.findAll(null)
    }
}