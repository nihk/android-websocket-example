package nick.cryptofun.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import nick.cryptofun.data.remote.BlockchainTransaction
import nick.cryptofun.data.remote.BlockchainTransactionJsonAdapter
import javax.inject.Inject

class BlockchainTransactionJsonAdapterDelegate @Inject constructor(moshi: Moshi) : JsonAdapter<BlockchainTransaction>() {

    // Dagger has problems injecting kapt generated code, so instantiate here.
    private val adapter = BlockchainTransactionJsonAdapter(moshi)

    override fun fromJson(reader: JsonReader) = adapter.fromJson(reader)

    override fun toJson(writer: JsonWriter, value: BlockchainTransaction?) = adapter.toJson(writer, value)
}