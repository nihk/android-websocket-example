package nick.cryptofun.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nick.cryptofun.R
import nick.cryptofun.data.local.CryptoTransaction
import nick.cryptofun.databinding.TransactionItemBinding

class TransactionAdapter(
    private val onCurrentListChanged: () -> Unit
) : ListAdapter<CryptoTransaction, TransactionViewHolder>(TransactionDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return LayoutInflater.from(parent.context)
            .let { inflater -> TransactionItemBinding.inflate(inflater, parent, false) }
            .let(::TransactionViewHolder)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<CryptoTransaction>,
        currentList: MutableList<CryptoTransaction>
    ) {
        onCurrentListChanged()
    }
}

object TransactionDiffCallback : DiffUtil.ItemCallback<CryptoTransaction>() {
    override fun areItemsTheSame(oldItem: CryptoTransaction, newItem: CryptoTransaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CryptoTransaction,
        newItem: CryptoTransaction
    ): Boolean {
        return oldItem == newItem
    }
}

class TransactionViewHolder(
    private val binding: TransactionItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: CryptoTransaction) {
        val context = itemView.context
        binding.hash.text = context.getString(
            R.string.hash_prefix,
            transaction.hash
        )
        binding.value.text = context.getString(
            R.string.value_prefix,
            transaction.value.toString()
        )
    }
}