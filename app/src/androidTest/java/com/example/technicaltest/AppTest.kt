package com.example.technicaltest

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.technicaltest.view.users.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class AppTest {

    @get:Rule
    val rule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    @FlowPreview
    @Before
    fun waitForSplashscreenEndingAndRegisterIdlingResource(){

        rule.launch()
        rule.getScenario().onActivity {
            IdlingRegistry.getInstance().register(ApplicationCore.countingIdlingResource)
        }
    }

    @FlowPreview
    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(ApplicationCore.countingIdlingResource)
    }


    @Test
    fun appTest() {
        //users list
        val usersRecyclerView = Espresso.onView(ViewMatchers.withId(R.id.usersRecyclerView))
        Espresso.onView(ViewMatchers.withId(R.id.searchBar)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed()))
        usersRecyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        usersRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            ViewActions.click()
        ))
        //Animation duration
        SystemClock.sleep(1000)

        //albums list
        val albumsRecyclerView = Espresso.onView(ViewMatchers.withId(R.id.albumsRecyclerView))
        albumsRecyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        albumsRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            ViewActions.click()
        ))
        //Animation duration
        SystemClock.sleep(1000)

        //photos list
        val photosRecyclerView = Espresso.onView(ViewMatchers.withId(R.id.photosRecyclerView))
        photosRecyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}
