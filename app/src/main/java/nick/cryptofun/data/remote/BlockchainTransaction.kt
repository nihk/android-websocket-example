package nick.cryptofun.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockchainTransaction(
    val op: String,
    val x: X
)

@JsonClass(generateAdapter = true)
data class X(
    val lock_time: Int,
    val ver: Int,
    val size: Int,
    val inputs: List<Input>,
    val time: Long,
    val tx_index: Long,
    val vin_sz: Int,
    val hash: String,
    val vout_sz: Int,
    val relayed_by: String,
    val out: List<Out>
)

@JsonClass(generateAdapter = true)
data class Input(
    val sequence: Long,
    val prev_out: PrevOut,
    val script: String
)

@JsonClass(generateAdapter = true)
data class PrevOut(
    val spent: Boolean,
    val tx_index: Long,
    val type: Int,
    val addr: String?,
    val value: Long,
    val n: Int,
    val script: String
)

@JsonClass(generateAdapter = true)
data class Out(
    val spent: Boolean,
    val tx_index: Long,
    val type: Int,
    val addr: String?,
    val value: Long,
    val n: Int,
    val script: String
)