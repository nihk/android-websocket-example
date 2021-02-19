package nick.cryptofun.data.remote

import android.util.Log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import nick.cryptofun.TAG
import nick.cryptofun.data.BlockchainTransactionJsonAdapterDelegate
import nick.cryptofun.di.BlockchainRequest
import okhttp3.*
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class BlockchainWebSocket @Inject constructor(
    @BlockchainRequest private val blockchainRequest: Request,
    private val client: OkHttpClient,
    private val adapter: BlockchainTransactionJsonAdapterDelegate
) {

    fun cryptoTransactions(): Flow<BlockchainTransaction> = callbackFlow {
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Opening WebSocket")
                webSocket.send("""{"op":"unconfirmed_sub"}""")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                offer(adapter.fromJson(text))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "Closing websocket because: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                val msg = "WebSocketListener.onFailure"
                Log.e(TAG, msg, t)
                cancel(cause = CancellationException(msg, t))
            }
        }

        val webSocket = client.newWebSocket(blockchainRequest, listener)

        awaitClose {
            webSocket.close(CLOSED_NORMALLY, "Job cancelled")
        }
    }


    companion object {
        private const val CLOSED_NORMALLY = 1000
    }
}