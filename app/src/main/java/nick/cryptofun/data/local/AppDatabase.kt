package nick.cryptofun.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.cryptofun.data.CryptoTransactionsDao

@Database(
    version = 1,
    entities = [CryptoTransaction::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cryptoTransactionsDao(): CryptoTransactionsDao

    companion object {
        const val name = "app.db"
    }
}