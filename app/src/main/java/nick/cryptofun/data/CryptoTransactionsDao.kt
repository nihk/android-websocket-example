package nick.cryptofun.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nick.cryptofun.data.local.CryptoTransaction

@Dao
interface CryptoTransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cryptoTransaction: CryptoTransaction): Long

    @Query(
        """
            SELECT *
            FROM crypto_transactions
            ORDER BY id DESC
        """
    )
    fun queryAllByRecency(): Flow<List<CryptoTransaction>>
}