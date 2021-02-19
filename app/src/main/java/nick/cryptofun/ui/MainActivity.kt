package nick.cryptofun.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nick.cryptofun.databinding.MainActivityBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TransactionAdapter { binding.recyclerView.smoothScrollToPosition(0) }
        binding.recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this, factory.create(this))
            .get(MainViewModel::class.java)
        lifecycle.addObserver(viewModel)

        viewModel.transactions
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)
    }
}