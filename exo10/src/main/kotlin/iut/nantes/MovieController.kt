package iut.nantes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/movies")
class MovieController {

    @Autowired
    private lateinit var db: Database

    private val movies = mutableMapOf<String, Movie>()

    init {
        MOVIES.forEach { movies[it.name] = it }
    }

    @PostMapping
    fun create(@RequestBody body: Movie) = try {
            val movie = db.create(body)
            ResponseEntity(movie, HttpStatus.CREATED)
        } catch(e: ResponseStatusException) {
            ResponseEntity(e.body, e.statusCode)
        }

    @GetMapping
    fun findAll(@RequestParam filters: Map<String, String>) =
        ResponseEntity.ok(db.findAll(filters))

    @GetMapping("/{name}")
    fun findOne(@PathVariable name: String) = try {
            val movie = db.findOne(name)
            ResponseEntity(movie, HttpStatus.OK)
        } catch(e: ResponseStatusException) {
            ResponseEntity(e.body, e.statusCode)
        }

    @PutMapping("/{name}")
    fun update(@PathVariable name: String,
               @RequestBody body: Movie) =
        try {
            val movie = db.update(name, body)
            ResponseEntity(movie, HttpStatus.OK)
        } catch(e: ResponseStatusException) {
            ResponseEntity(e.body, e.statusCode)
        }

    @DeleteMapping("/{name}")
    fun delete(@PathVariable name: String) =
        try {
            db.delete(name)
            ResponseEntity.ok().build()
        } catch(e: ResponseStatusException) {
            ResponseEntity(e.body, e.statusCode)
        }
}