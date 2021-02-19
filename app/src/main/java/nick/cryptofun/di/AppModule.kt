package nick.cryptofun.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nick.cryptofun.data.*
import nick.cryptofun.data.local.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun moshi(): Moshi = Moshi.Builder().build()

        @Provides
        fun okHttpClient() = OkHttpClient()

        @Singleton
        @Provides
        fun appDatabase(
            @ApplicationContext appContext: Context
        ): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.name)
                .build()
        }

        @Provides
        fun cryptoTransactionDao(appDatabase: AppDatabase): CryptoTransactionsDao {
            return appDatabase.cryptoTransactionsDao()
        }

        @Provides
        @BlockchainRequest
        fun blockchainRequest(): Request {
            return Request.Builder()
                .url("wss://ws.blockchain.info/inv")
                .build()
        }
    }

    @Binds
    abstract fun transactionsRepository(repository: BlockchainTransactionsRepository): TransactionsRepository
}
