package com.llm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.RepoResult
import com.llm.data.network.NetworkState
import com.llm.data.repository.DeliveryRepo
import com.llm.ui.viewmodels.DeliveryItemsViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(JUnit4::class)
class DeliveryItemViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repo:DeliveryRepo

    @Mock
    lateinit var refreshObserver: Observer<String>
    @Mock
    lateinit var networkStateObserver: Observer<NetworkState>

    @Mock
    lateinit var listObserver: Observer<PagedList<DeliveryItemDataModel>>

    private lateinit var viewModel:DeliveryItemsViewModel

    private val mutableLiveData = MutableLiveData<PagedList<DeliveryItemDataModel>>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    private val repoResult = getDummyResult()


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = DeliveryItemsViewModel(repo)
        viewModel.networkState.observeForever(networkStateObserver)
        viewModel.errRefreshLiveData.observeForever(refreshObserver)
        viewModel.resultLiveData.observeForever(listObserver)

    }

    @Test
    fun testData_Loading() {
        networkStateLiveData.value = NetworkState.LOADING
        whenever(repo.getDeliveryItems()).thenReturn(repoResult)
            viewModel.loadDelivery()

        verify(networkStateObserver).onChanged(NetworkState.LOADING)
    }

    @Test
    fun testData_Loaded() {
        networkStateLiveData.value = NetworkState.LOADED
        val list = ArrayList<DeliveryItemDataModel>()
        list.add(getDummyDeliveryItem())

        mutableLiveData.value = mockPagedList(list)

        whenever(repo.getDeliveryItems()).thenReturn(repoResult)
        viewModel.loadDelivery()

        verify(networkStateObserver).onChanged(NetworkState.LOADED)
        verify(listObserver).onChanged(any())
    }


    @Test
    fun testData_Failure() {
        networkStateLiveData.value = NetworkState.error("Error")
        whenever(repo.getDeliveryItems()).thenReturn(repoResult)
        viewModel.loadDelivery()

        verify(networkStateObserver).onChanged(NetworkState.error("Error"))
    }


    private fun retry() {
        //dummy method for retry
    }

    private fun getDummyResult(): RepoResult {

        val repoResult = RepoResult(mutableLiveData, networkStateLiveData, ::retry)
        return repoResult

    }


    fun  mockPagedList(list: List<DeliveryItemDataModel>): PagedList<DeliveryItemDataModel> {
        val pagedList = mock<PagedList<DeliveryItemDataModel>>()
        whenever(pagedList.get(anyInt())).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        whenever(pagedList.size).thenReturn(list.size)
        return pagedList
    }



}