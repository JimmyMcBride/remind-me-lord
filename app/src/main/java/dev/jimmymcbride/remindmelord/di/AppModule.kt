package dev.jimmymcbride.remindmelord.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.data.local.VerseDatabase
import dev.jimmymcbride.remindmelord.data.repository.SeederRepositoryImpl
import dev.jimmymcbride.remindmelord.data.repository.VerseRepositoryImpl
import dev.jimmymcbride.remindmelord.domain.repository.SeederRepository
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideVerseDatabase(@ApplicationContext context: Context): VerseDatabase {
        return Room.databaseBuilder(
            context,
            VerseDatabase::class.java,
            "verses.db"
        ).build()
    }

    @Provides
    fun provideVerseDao(db: VerseDatabase): VerseDao = db.verseDao()

    @Provides
    @Singleton
    fun provideVerseRepository(dao: VerseDao): VerseRepository =
        VerseRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providesSeederRepository(
        @ApplicationContext context: Context,
        dao: VerseDao
    ): SeederRepository =
        SeederRepositoryImpl(context, dao)
}
