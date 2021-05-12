package com.example.upraxisexam.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.upraxisexam.BuildConfig
import com.example.upraxisexam.data.api.service.PersonsService
import com.example.upraxisexam.data.database.PersonDatabase
import com.example.upraxisexam.data.database.PersonDatabaseDao
import com.example.upraxisexam.data.repository.personlist.*
import com.example.upraxisexam.data.util.NetworkConnectionInterceptor
import com.example.upraxisexam.domain.repository.PersonsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePersonDatabase(@ApplicationContext context: Context): PersonDatabase {
        return Room.databaseBuilder(
                context,
                PersonDatabase::class.java,
                "person_database"
        )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providePersonDatabaseDao(personDatabase: PersonDatabase): PersonDatabaseDao {
        return personDatabase.personDatabaseDao
    }

    @Singleton
    @Provides
    fun providePersonsLocalDataSource(personDatabaseDao: PersonDatabaseDao): PersonsLocalDataSource {
        return PersonsLocalDataSourceImpl(personDatabaseDao)
    }

    @Singleton
    @Provides
    fun provideInterceptor(@ApplicationContext context: Context): Interceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
            networkConnectionInterceptor: Interceptor,
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build())
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build()
    }

    @Singleton
    @Provides
    fun providePersonsService(retrofit: Retrofit): PersonsService {
        return retrofit.create(PersonsService::class.java)
    }

    @Singleton
    @Provides
    fun providePersonsRemoteDataSource(personsService: PersonsService): PersonsRemoteDataSource {
        return PersonsRemoteDataSourceImpl(personsService)
    }

    @Singleton
    @Provides
    fun providePersonsRepository(
            personsLocalDataSource: PersonsLocalDataSource,
            personsRemoteDataSource: PersonsRemoteDataSource
    ): PersonsRepository {
        return PersonsRepositoryImpl(
                personsLocalDataSource,
                personsRemoteDataSource
        )
    }
}