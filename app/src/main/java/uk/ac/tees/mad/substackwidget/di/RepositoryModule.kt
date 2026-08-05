package uk.ac.tees.mad.substackwidget.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.substackwidget.data.repository.SubstackRepositoryImpl
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindSubstackRepository(impl: SubstackRepositoryImpl): SubstackRepository
}