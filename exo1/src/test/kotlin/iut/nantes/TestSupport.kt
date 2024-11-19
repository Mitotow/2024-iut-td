package iut.nantes

import java.util.*

fun user(uuid: UUID = UUID(0, 1)) = User(uuid, "John Doe", "email@noop.pony", 42)
fun user2(uuid: UUID = UUID(1, 2)) = User(uuid, "Thomas Ambroise", "email@noop.kalashnikov", 42)