package iut.nantes.exo20

import org.springframework.stereotype.Repository

@Repository
class Database {
    fun findAll(): List<Int>  {
        return listOf(1, 2, 3, 4)
    }
}