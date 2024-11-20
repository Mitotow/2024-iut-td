package iut.nantes.exo20.controller

import iut.nantes.exo20.errors.ImATeapotException
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraints.Min
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

data class AgeRange(
    val minAge: Int?,
    val maxAge: Int?,
)

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidAgeRangeValidator::class])
annotation class ValidAgeRange(
    val message: String = "Invalid age",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class ValidAgeRangeValidator: ConstraintValidator<ValidAgeRange, AgeRange> {
    @Value("\${custom.api.pets.max-range}")
    private var maxRange: Int = 0

    override fun isValid(p0: AgeRange?,
                         p1: ConstraintValidatorContext?): Boolean {
        if (p0?.minAge != null && p0.maxAge != null)
            return ((p0.minAge <= p0.maxAge) && (p0.maxAge - p0.minAge < maxRange))
        return true
    }
}

@RestController
@Validated
class PetController(val database : MutableMap<Int, PetDto> = mutableMapOf()){
    @GetMapping("/api/v1/pets/{petId}")
    fun getPet(@PathVariable @Min(1) petId: Int) = database[petId]?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @PostMapping("/api/v1/pets")
    fun createPet(@RequestBody pet: PetDto): ResponseEntity<PetDto> {
        if (pet.name.isEmpty() || pet.age < 0)
            return ResponseEntity.badRequest().build()
        val next = (database.keys.maxOrNull() ?: 0) + 1
        val withId = pet.copy(id = next)
        database[next] = withId
        return ResponseEntity.status(HttpStatus.CREATED).body(withId)
    }

    @PutMapping("/api/v1/pets/{petId}")
    fun updatePet(@RequestBody pet: PetDto, @PathVariable @Min(1) petId: Int): ResponseEntity<PetDto> {
        if (pet.id != petId) {
            throw IllegalArgumentException("Pet ID in path and body do not match")
        }
        val previous = database[petId]
        if (previous == null) {
            throw ImATeapotException()
        } else {
            database[petId] = pet
        }
        return ResponseEntity.ok(pet)
    }

    @GetMapping("/api/v1/pets")
    fun getPets(
        @ValidAgeRange
        range: AgeRange
    ): ResponseEntity<List<PetDto>> {
       var result: List<PetDto> = database.values.toList()
        if (range.minAge != null) result = result.filter { it.age >= range.minAge }
        if (range.maxAge != null) result = result.filter { it.age <= range.maxAge }
        return ResponseEntity.ok(result)
    }
}