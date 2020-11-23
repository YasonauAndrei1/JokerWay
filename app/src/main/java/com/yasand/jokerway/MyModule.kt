package com.yasand.jokerway

import com.yasand.jokerway.data.GameRepository
import com.yasand.jokerway.data.Meteor
import com.yasand.jokerway.data.Player
import com.yasand.jokerway.data.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.BeanDefinition
import org.koin.dsl.module

val module = module {

    val repository: BeanDefinition<Repository> = single {
        GameRepository()
    }

    val player: BeanDefinition<Player> = single {
        Meteor(androidContext().resources)
    }

    val mainViewModel: BeanDefinition<GameViewModel> = viewModel {
        GameViewModel(
            repository = get(),
            resources = androidContext().resources,
            player = get()
        )
    }
}