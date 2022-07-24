package com.bytecoders.coinscanner.repository.uistate.impl

import com.bytecoders.coinscanner.data.database.UiStateDao
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.repository.uistate.UiStateRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class UiStateRepositoryImpl @Inject constructor(
    private val uiStateDao: UiStateDao,
) : UiStateRepository {
    private var _cachedUIState = HomeUiState()

    override val homeUiStateFlow: Flow<HomeUiState> = uiStateDao.getHomeUiState().map {
        val newState = it?.apply {
            _cachedUIState = this
        } ?: _cachedUIState

        newState.copy(stale = false)
    }.distinctUntilChanged()

    override val homeUiState: HomeUiState
        get() = _cachedUIState

    override suspend fun updateHomeUiState(newUiState: HomeUiState) {
        _cachedUIState = newUiState
        uiStateDao.clearHomeUiState()
        uiStateDao.insertHomeUiState(newUiState)
    }
}