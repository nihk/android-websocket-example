package nick.cryptofun.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import nick.cryptofun.data.local.CryptoTransaction
import nick.cryptofun.data.remote.BlockchainTransaction
import nick.cryptofun.data.remote.BlockchainWebSocket
import javax.inject.Inject

// todo: rethink this?
interface TransactionsRepository {
    fun cachedTransactions(): Flow<List<CryptoTransaction>>
    fun stream(): Flow<CryptoTransaction>
}

class BlockchainTransactionsRepository @Inject constructor(
    private val blockchainWebSocket: BlockchainWebSocket,
    private val dao: CryptoTransactionsDao
) : TransactionsRepository {

    override fun cachedTransactions(): Flow<List<CryptoTransaction>> {
        return dao.queryAllByRecency()
    }

    override fun stream(): Flow<CryptoTransaction> {
        return blockchainWebSocket.cryptoTransactions()
            .map { it.toCryptoTransaction() }
            .onEach { dao.insert(it) }
    }
}

private fun BlockchainTransaction.toCryptoTransaction(): CryptoTransaction {
    return CryptoTransaction(
        hash = x.out.firstOrNull()?.script.toString(),
        value = x.out.firstOrNull()?.value ?: 0L
    )
}