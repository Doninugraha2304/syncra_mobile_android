package com.syncra.pos.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.syncra.pos.data.PosDatabase
import com.syncra.pos.data.ProductRepositoryImpl
import com.syncra.pos.domain.ProductRepository
import com.syncra.pos.presentation.InventoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = PosDatabase.Schema,
            context = androidContext(),
            name = "pos.db"
        )
    }

    single {
        PosDatabase(driver = get())
    }

    single<ProductRepository> { ProductRepositoryImpl(get()) }
    viewModel { InventoryViewModel(get()) }
    viewModel { com.syncra.pos.presentation.SettingsViewModel(androidContext()) }
}
