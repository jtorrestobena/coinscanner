package com.bytecoders.coinscanner.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bytecoders.coinscanner.data.state.HomeUiState
import kotlinx.coroutines.flow.Flow

@Dao
interface UiStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeUiState(uiState: HomeUiState)

    @Query("SELECT * FROM home_ui_state WHERE id = 0")
    fun getHomeUiState(): Flow<HomeUiState?>

    @Query("DELETE FROM home_ui_state")
    suspend fun clearHomeUiState()
}
