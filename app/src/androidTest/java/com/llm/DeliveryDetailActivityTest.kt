package com.llm

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.LatLongDataModel
import com.llm.ui.DeliveryDetailActivity
import com.llm.ui.KEY_DELIVERY_ITEM
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeliveryDetailActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<DeliveryDetailActivity> =
        ActivityTestRule(DeliveryDetailActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun clear() {
        Intents.release()
    }

    @Test
    fun testLaunch(){
        launchActivity()
        intended(hasComponent(DeliveryDetailActivity::class.java.name))
    }

    @Test
    fun testData() {
        launchActivity()
        onView(withId(R.id.tv_desc)).check(matches(withText("Android")))
    }

    @Test
    fun testDataNameShown() {
        launchActivity()
        onView(withText("Android"))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testImageShown() {
        launchActivity()
        onView(withId(R.id.iv))
            .check(matches(ViewMatchers.isDisplayed()))
    }


    private fun launchActivity(): DeliveryDetailActivity {
        return activityRule.launchActivity(getIntent())
    }

    private fun getIntent(): Intent = Intent().apply {

        val model = DeliveryItemDataModel(
            1, "Android", "droid.jpg",
            LatLongDataModel(0.0, 0.0, "Equator")
        )

        val bundle = Bundle()
        bundle.putParcelable(KEY_DELIVERY_ITEM, model)
        putExtras(bundle)


    }


}