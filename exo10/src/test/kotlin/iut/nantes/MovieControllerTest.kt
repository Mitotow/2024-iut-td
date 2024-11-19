package iut.nantes

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 *
 *
 *
 * FOR EXO 19
 *
 *
 *
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun findAll() {
        mockMvc.get("/api/movies")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.length()") { value(MOVIES.size) }
            }
            .also {
                MOVIES.forEachIndexed { index, movie ->
                    // Vérifier chaque propriété de chaque film
                    it.andExpect { jsonPath("$.[$index].name") { value(movie.name) } }
                    it.andExpect { jsonPath("$.[$index].releaseDate") { value(movie.releaseDate) } }
                    it.andExpect { jsonPath("$.[$index].rating") { value(movie.rating) } }
                    // Vérification des langues (qui est une liste)
                    movie.languages.forEachIndexed { langIndex, lang ->
                        it.andExpect { jsonPath("$.[$index].languages[$langIndex]") { value(lang) } }
                    }
                }
            }
    }

    @Test
    fun findAllWithFilters() {
        val expectedMovie = MOVIES[2]
        mockMvc.get("/api/movies?languages=VO&releaseDate=2017&rating=99")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.length()") { value(1) }
                jsonPath("$.[0].name") { value(expectedMovie.name) }
                jsonPath("$.[0].releaseDate") { value(expectedMovie.releaseDate) }
                jsonPath("$.[0].rating") { value(expectedMovie.rating) }
            }
    }

    @Test
    fun findAllEmptyWithFilters() {
        mockMvc.get("/api/movies?languages=VA")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.length()") { value(0) }
            }
    }

    @Test
    fun findOne() {
        val expectedMovie = MOVIES[0]
        mockMvc.get("/api/movies/${expectedMovie.name}")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.name") { value(expectedMovie.name) }
                jsonPath("$.releaseDate") { value(expectedMovie.releaseDate) }
                jsonPath("$.rating") { value(expectedMovie.rating) }
                jsonPath("$.languages.length()") { value(expectedMovie.languages.size) }
                jsonPath("$.languages[0]") { value(expectedMovie.languages[0]) }
            }
            .andDo { print() }
    }

    @Test
    fun findUnexistingOne() {
        mockMvc.get("/api/movies/TEST_MOVIE_UNEXISTING")
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun update() {
        val originalMovie = MOVIES[0]
        val updatedMovie = Movie(
            originalMovie.name,
            originalMovie.releaseDate + 5,
            originalMovie.rating,
            originalMovie.languages,
        )

        mockMvc.perform(
            put("/api/movies/${originalMovie.name}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(updatedMovie))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedMovie.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value(updatedMovie.releaseDate))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(updatedMovie.rating))
            .andExpect(MockMvcResultMatchers.jsonPath("$.languages.length()").value(updatedMovie.languages.size))
            .andExpect(MockMvcResultMatchers.jsonPath("$.languages[0]").value(updatedMovie.languages[0]))
    }

    @Test
    fun updateNotFound() {
        val originalMovie = MOVIES[0]
        val updatedMovie = Movie(
            originalMovie.name,
            originalMovie.releaseDate + 5,
            originalMovie.rating,
            originalMovie.languages,
        )

        mockMvc.perform(
            put("/api/movies/${originalMovie.name+"123456789"}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(updatedMovie))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun updateBadRequest() {
        val originalMovie = MOVIES[0]
        val updatedMovie = Movie(
            originalMovie.name+"1234",
            originalMovie.releaseDate + 5,
            originalMovie.rating,
            originalMovie.languages,
        )

        mockMvc.perform(
            put("/api/movies/${originalMovie.name}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(updatedMovie))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun deleteValid() {
        mockMvc
            .perform(delete("/api/movies/${MOVIES[0].name}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteInvalid() {
        mockMvc
            .perform(delete("/api/movies/${MOVIES[0].name+"123456789"}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}