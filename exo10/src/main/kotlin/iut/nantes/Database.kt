package iut.nantes

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException

@Repository
class Database {
    private val movies = mutableMapOf<String, Movie>()

    init {
        MOVIES.forEach { movies[it.name] = it }
    }

    private fun throwMovieNotFound(name: String): Nothing = throw ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Movie with name: $name not found"
    )

    fun findAll(filters: Map<String, String>?): List<Movie> {
        // Si aucun filtre n'est fourni, retourne tous les films
        if (filters.isNullOrEmpty())
            return movies.values.toList()
        return movies.values.filter { movie ->
            // Vérifie que chaque filtre correspond au champ du film
            filters.all { (key, value) ->
                val property = movie::class.members.find { it.name == key }?.call(movie)

                // Si la propriété est une liste, vérifier si l'élément est dans la liste
                when (property) {
                    is List<*> -> property.contains(value)  // Vérifie si la liste contient l'élément
                    else -> property?.toString() == value   // Sinon, faire une comparaison simple
                }
            }
        }
    }

    fun findOne(name: String): Movie? {
        if (!movies.containsKey(name)) throwMovieNotFound(name)
        return movies[name]
    }

    fun create(movie: Movie): Movie {
        if (!movies.containsKey(movie.name)) {
            movies[movie.name] = movie
            return movie
        }
        else throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Movie with name: ${movie.name} already exists",
        )
    }

    fun update(name: String, movie: Movie): Movie {
        if (!movies.containsKey(name)) throwMovieNotFound(name)
        if (name != movie.name)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Can't change a movie name",
            )
        movies[movie.name] = movie
        return movie
    }

    fun delete(name: String) {
        if (!movies.containsKey(name)) throwMovieNotFound(name)
        movies.remove(name)
    }
}