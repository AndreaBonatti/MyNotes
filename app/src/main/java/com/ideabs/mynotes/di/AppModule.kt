package com.ideabs.mynotes.di

import com.ideabs.mynotes.auth.presentation.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RegisterViewModel() }
}