package nick.cryptofun.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.retryWhen
import nick.cryptofun.TAG
import nick.cryptofun.data.TransactionsRepository
import javax.inject.Inject

class MainViewModel(
    private val handle: SavedStateHandle,
    private val repository: TransactionsRepository
) : ViewModel(), DefaultLifecycleObserver {

    val transactions = repository.cachedTransactions()
    private var job: Job? = null

    override fun onStart(owner: LifecycleOwner) {
        streamTransactions()
    }

    override fun onStop(owner: LifecycleOwner) {
        stopStreamingTransactions()
    }

    private fun streamTransactions() {
        job = repository.stream()
            .retryWhen { _, attempt ->
                if (attempt >= 3) {
                    false
                } else {
                    Log.d(TAG, "Retrying attempt #${attempt + 1}")
                    delay(3000L)
                    true
                }
            }
            .catch { throwable ->
                Log.e(TAG, "Hmm?", throwable)
            }
            .launchIn(viewModelScope)
    }

    private fun stopStreamingTransactions() {
        job?.cancel()
        job = null
    }

    class Factory @Inject constructor(private val transactionsRepository: TransactionsRepository) {
        fun create(owner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
            return object : AbstractSavedStateViewModelFactory(owner, null) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel(handle, transactionsRepository) as T
                }
            }
        }
    }
}