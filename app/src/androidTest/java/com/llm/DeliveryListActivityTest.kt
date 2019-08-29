package com.llm

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasKey
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import com.llm.ui.DeliveryDetailActivity
import com.llm.ui.DeliveryListActivity
import com.llm.ui.KEY_DELIVERY_ITEM
import com.llm.ui.adapter.DeliveryItemViewHolder
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DeliveryListActivityTest {

    @Before
    fun setup() {
        Intents.init()
    }
    @After
    fun clear() {
        Intents.release()
    }

    @get:Rule
    var activityRule: ActivityTestRule<DeliveryListActivity> = ActivityTestRule(DeliveryListActivity::class.java)


    @Test
    fun testSwipeToRefresh(){
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),  isDisplayingAtLeast(85)))
    }

    @Test
    fun testDataLoaded() {
        val launchActivity = launchActivity()
        val idlingResource = launchActivity.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        val scrollToPosition = RecyclerViewActions.scrollToPosition<DeliveryItemViewHolder>(10)

        onView(withId(R.id.rl))
            .perform(scrollToPosition).check(ViewAssertions.matches(scrollToPosition.constraints))

        if (idlingResource != null)
            IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testListItemClick() {
        val launchActivity = launchActivity()
        val idlingResource = launchActivity.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)

        val action = RecyclerViewActions.actionOnItemAtPosition<DeliveryItemViewHolder>(
            2,
            ViewActions.click()
        )
         onView(withId(R.id.rl)).perform(action)

        intended(hasComponent(DeliveryDetailActivity::class.java!!.name))
        intended(hasExtras(hasKey(KEY_DELIVERY_ITEM)))


        if (idlingResource != null)
            IdlingRegistry.getInstance().unregister(idlingResource)
    }


    private fun launchActivity(): DeliveryListActivity {
        return activityRule.launchActivity(getIntent())
    }

    private fun getIntent(): Intent = Intent()

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }

}