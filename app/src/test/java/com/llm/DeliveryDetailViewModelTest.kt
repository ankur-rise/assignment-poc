package com.llm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.llm.data.models.LatLongDataModel
import com.llm.ui.viewmodels.DetailViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DeliveryDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var descriptionObserver:Observer<String>
    @Mock
    lateinit var imageObserver:Observer<String>
    @Mock
    lateinit var latlngObserver:Observer<LatLongDataModel>

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewModel = DetailViewModel()
        viewModel.description.observeForever(descriptionObserver)
        viewModel.imageUrl.observeForever(imageObserver)
        viewModel.latLng.observeForever(latlngObserver)
    }

    @Test
    fun testDataLoad() {
        val model = getDummyDeliveryItem()
        viewModel.loadData(model)



        verify(descriptionObserver).onChanged(anyString())
        verify(imageObserver).onChanged(anyString())
        verify(latlngObserver).onChanged(any())

    }

}