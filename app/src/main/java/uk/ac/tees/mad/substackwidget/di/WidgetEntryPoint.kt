package uk.ac.tees.mad.substackwidget.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.substackwidget.domain.usecase.GetGroupedFeedUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getGroupedFeedUseCase(): GetGroupedFeedUseCase
}