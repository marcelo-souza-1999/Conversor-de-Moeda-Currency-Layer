package com.marcelo.conversor.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.marcelo.conversor.BuildConfig
import com.marcelo.conversor.data.local.CurrencyDao
import com.marcelo.conversor.data.local.CurrencyDatabase
import com.marcelo.conversor.data.remote.CurrencyLayerApi
import com.marcelo.conversor.repository.CurrencyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(dao: CurrencyDao, api: CurrencyLayerApi): CurrencyRepository {
        return CurrencyRepository(dao, api)
    }

    @Singleton
    @Provides
    fun provideCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase
    {
        return Room.databaseBuilder(context, CurrencyDatabase::class.java, "conversion_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCurrencyDao(database: CurrencyDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Singleton
    @Provides
    fun provideCurrencyLayerApi(): CurrencyLayerApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(CurrencyLayerApi::class.java)
    }
}