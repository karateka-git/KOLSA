package com.example.kolsa.di

import com.example.kolsa.BuildConfig
import com.example.kolsa.data.repositories.WorkoutRepositoryImpl
import com.example.kolsa.data.services.WorkoutApi
import com.example.kolsa.domain.interactors.LoadDetailWorkoutUseCase
import com.example.kolsa.domain.repositories.WorkoutRepository
import com.example.kolsa.presentation.workout.workout_detail.WorkoutDetailViewModel
import com.example.kolsa.presentation.workout.workout_list.WorkoutListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val presentationModules = module {
    viewModelOf(::WorkoutListViewModel)
    viewModelOf(::WorkoutDetailViewModel)
}

private val apiModules = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_DOMAIN)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<WorkoutApi>{ get<Retrofit>().create(WorkoutApi::class.java) }
}

private val repositoryModules = module {
    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }
}

private val dataModules = listOf(
    apiModules,
    repositoryModules,
)

private val domainModules = module {
    singleOf(::LoadDetailWorkoutUseCase)
}

val koinAppModules =
    domainModules +
    dataModules +
    presentationModules
