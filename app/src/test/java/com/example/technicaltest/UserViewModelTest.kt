package com.example.technicaltest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.technicaltest.data.repository.UsersRepository
import com.example.technicaltest.model.UserItem
import com.example.technicaltest.utils.State
import com.example.technicaltest.viewmodel.UsersViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class UserViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UsersViewModel
    private val usersRepository = mockk<UsersRepository>()

    @Test
    fun `GIVEN a user of the app WHEN starting to view user list THEN userList is retrieved`(){
        val user1 = mockk<UserItem>()
        val user2 = mockk<UserItem>()
        every { usersRepository.getUsersList() } returns
                                            flow{ emit(State.Success(
                                                listOf(user1, user2))
                                            )}
        viewModel = UsersViewModel(usersRepository)

        val observerListUser = mockk<Observer<State<List<UserItem>>>>(relaxed = true)
        viewModel.usersListLiveData.observeForever(observerListUser)

        verify {
            observerListUser.onChanged(State.Success(listOf(user1, user2)))
        }
    }

    @Test
    fun `GIVEN a user of the app WHEN error occured THEN state is on ERROR`(){
        val error = State.Error<List<UserItem>>("")
        every { usersRepository.getUsersList() } returns  flow{ emit(error) }

        viewModel = UsersViewModel(usersRepository)

        val observerListUser = mockk<Observer<State<List<UserItem>>>>(relaxed = true)
        viewModel.usersListLiveData.observeForever(observerListUser)

        verifyAll {
            observerListUser.onChanged(error)
        }
    }
}