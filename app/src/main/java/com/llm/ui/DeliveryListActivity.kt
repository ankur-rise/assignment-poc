package com.llm.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.IdlingResource
import com.llm.IdlingResource.SimpleIdlingResource
import com.llm.R
import com.llm.data.models.DeliveryItemDataModel
import com.llm.di.Injector
import com.llm.ui.adapter.DeliveryAdapter
import com.llm.ui.viewmodels.DeliveryItemsViewModel
import com.llm.ui.viewmodels.factory.ViewModelFactory
import javax.inject.Inject

const val KEY_DELIVERY_ITEM = "key_delivery_item"

class DeliveryListActivity : AppCompatActivity(), DeliveryAdapter.OnItemSelectListener {

    @Inject // cannot be private as dagger required this variable to access
    lateinit var factory: ViewModelFactory

    private val viewModel: DeliveryItemsViewModel by viewModels { factory }
    private lateinit var recyclerView: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var adapter: DeliveryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delivery_list)
        supportActionBar?.title = getString(R.string.deliveries)

        val component = Injector.inject()
        component.inject(this)
        recyclerView = findViewById(R.id.rl)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        refreshLayout = findViewById(R.id.swipe_refresh)
        refreshLayout.setOnRefreshListener { refreshData() }

        adapter.setRetry {
            viewModel.retryFailedReq()
        }

        adapter.itemSelectListener = this

    }

    private fun refreshData() {
        viewModel.refreshData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.resultLiveData.observe(this, Observer<PagedList<DeliveryItemDataModel>> {

            if (refreshLayout.isRefreshing) {
                refreshLayout.isRefreshing = false
            }
            mIdlingResource?.setIdleState(true)
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
            /*if(it.status==Status.FAILED) {
                Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
            }*/
        })

        viewModel.errRefreshLiveData.observe(this, Observer<String> { str ->
            if (refreshLayout.isRefreshing) {
                refreshLayout.isRefreshing = false
            }
            Toast.makeText(this@DeliveryListActivity, str, Toast.LENGTH_SHORT).show()
        })

        // for testing only
        mIdlingResource?.setIdleState(false)
        viewModel.loadDelivery()
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_refresh -> {
                refreshLayout.isRefreshing = true
                refreshData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onSelect(model: DeliveryItemDataModel) {
        val intent = Intent(this, DeliveryDetailActivity::class.java).apply {
            val bundle = Bundle().apply {
                putParcelable(KEY_DELIVERY_ITEM, model)
            }
            putExtras(bundle)
        }
        startActivity(intent)
    }

    // The Idling Resource which will be null in production.
    private var mIdlingResource: SimpleIdlingResource? = null

    @VisibleForTesting
    @Inject
    fun getIdlingResource(): IdlingResource? {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource
    }


}