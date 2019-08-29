package com.llm

import com.llm.data.db.AppDB
import com.llm.data.db.DeliveryDao
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.LatLongDataModel
import com.llm.data.network.Apis
import com.llm.data.network.CallbackWithRetry
import com.llm.data.repository.DeliveryRepo
import com.llm.ui.utils.Utils
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

open class DeliveryRepoTest {
    private lateinit var repo: DeliveryRepo

    @Mock
    lateinit var api: Apis

    @Mock
    lateinit var db: AppDB
    @Mock
    lateinit var dao:DeliveryDao

    @Mock
    lateinit var utils: Utils

    @Mock
    lateinit var executor: Executor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repo = DeliveryRepo(api, db, utils = utils, executor = executor)
    }

    @Test
    fun testRepoResult() {

        whenever(db.deliveryDao()).thenReturn(dao)
        whenever(dao.get()).thenReturn(MockDataSourceFactory())
        val repoResult = repo.getDeliveryItems()
        assertNotNull(repoResult)
    }

    @Test
    fun testRefreshData() {
        whenever(utils.isConnectedToInternet()).thenReturn(true)
        val response = listOf(DeliveryItemDataModel(0, "", "", LatLongDataModel(0.0, 0.0, "")))
        val mockCall = mock<Call<List<DeliveryItemDataModel>>> {
            on { execute() } doReturn Response.success(response)
        }
        whenever(api.getDeliveries(any())).thenReturn(mockCall)

        val spyRepo = spy(repo)
        spyRepo.refreshData {

        }
        verify(spyRepo).getDeliveries(any(), any(), any(), any())
    }




    @Test
    open fun testDeliveries() {
        val response = listOf(DeliveryItemDataModel(0, "", "", LatLongDataModel(0.0, 0.0, "")))
        whenever(utils.isConnectedToInternet()).thenReturn(true)

        val resp = Response.success(response)

        val mockCall = mock<Call<List<DeliveryItemDataModel>>> {
            on { execute() } doReturn resp
        }

        whenever(api.getDeliveries(any())).thenReturn(mockCall)

        var isSuccessBlockCalled = false
        val successCallback = fun(_: List<DeliveryItemDataModel>) {
            isSuccessBlockCalled = true
        }

        repo.getDeliveries(0, 20, successCallback, {})
        argumentCaptor<CallbackWithRetry<List<DeliveryItemDataModel>>>().apply {
            verify(mockCall).enqueue(capture())
            firstValue.onResponse(mockCall, resp)
            assertNotNull(resp)
            assertTrue(isSuccessBlockCalled)
        }

    }


}
