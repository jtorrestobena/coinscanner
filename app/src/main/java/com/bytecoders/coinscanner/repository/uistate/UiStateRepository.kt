package com.bytecoders.coinscanner.repository.uistate

import com.bytecoders.coinscanner.data.state.HomeUiState
import kotlinx.coroutines.flow.Flow

interface UiStateRepository {
    val homeUiStateFlow: Flow<HomeUiState>

    val homeUiState: HomeUiState

    suspend fun updateHomeUiState(newUiState: HomeUiState)
}
