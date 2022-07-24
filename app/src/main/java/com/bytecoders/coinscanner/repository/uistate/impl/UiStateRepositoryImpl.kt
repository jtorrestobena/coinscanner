package com.bytecoders.coinscanner.repository.uistate.impl

import androidx.room.withTransaction
import com.bytecoders.coinscanner.data.database.AppDatabase
import com.bytecoders.coinscanner.data.database.UiStateDao
import com.bytecoders.coinscanner.data.state.HomeUiState
import com.bytecoders.coinscanner.repository.uistate.UiStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UiStateRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val uiStateDao: UiStateDao
) : UiStateRepository {

    override val homeUiStateFlow: Flow<HomeUiState> = uiStateDao.getHomeUiState().map {
        val newState = it ?: HomeUiState()
        newState.copy(stale = false)
    }.distinctUntilChanged()

    override suspend fun updateHomeUiState(newUiState: HomeUiState) {
        appDatabase.withTransaction {
            uiStateDao.clearHomeUiState()
            uiStateDao.insertHomeUiState(newUiState)
        }
    }
}
