package uk.ac.tees.mad.substackwidget.domain.usecase

import uk.ac.tees.mad.substackwidget.domain.model.PublicationFeed
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository

sealed class ValidationResult {
    data object Valid: ValidationResult()
    data object NotFound: ValidationResult()
    data object Empty: ValidationResult()
}

class ValidatePublicationUseCase(
    private val repository: SubstackRepository
) {
    suspend operator fun invoke(handle: String): ValidationResult {
        val trimmed = handle.trim()
        if(trimmed.isBlank()) return ValidationResult.Empty
        val isValid = repository.validatePublication(trimmed)
        return if (isValid) ValidationResult.Valid else ValidationResult.NotFound
    }
}