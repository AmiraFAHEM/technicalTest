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
import com.example.technicaltest.data.repository.UsersRepository
import com.example.technicaltest.injection.appModule
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.State
import com.example.technicaltest.view.users.MainActivity
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest


@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class AppTestFanny : KoinTest {

    @get:Rule
    val rule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    private val usersRepository = mockk<UsersRepository>()
    val user1 = UserItem(1, "Amira","Mimi","lafamille.com","email@mail.com","01010101")
    val user2 = UserItem(1, "Josiane","Jojo","superjojo.com","jojo@mail.com","02010101")


    @Before
    fun setUp() {
        loadKoinModules(
            listOf(
                appModule,
                module {
                    single(override = true) { usersRepository }
                }
            )
        )
        every { usersRepository.getUsersList() } returns flow { emit(State.Success(listOf(user1, user2))) }
    }


    @Test
    fun WHENuser_launch_main_activity_THEN_user_list_is_displayed(){
        //GIVEN
        //On setup un environnement mocké
        every { usersRepository.getUsersList() } returns  flow{ emit(
            State.Success(
                listOf(user1, user2))
        )}

        //WHEN user launch app done in before
        rule.launch()
        //THEN
        assertListItemCount(R.id.usersRecyclerView, 2)

    }
}
