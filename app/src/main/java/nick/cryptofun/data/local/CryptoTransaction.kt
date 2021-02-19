package nick.cryptofun.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto_transactions")
data class CryptoTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val hash: String,
    val value: Long
)