import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.domain.usecase.GetExperienceDetailUseCase
import com.example.aroundegyptapplication.domain.usecase.LikeExperienceUseCase
import com.example.aroundegyptapplication.presentation.experience.viewmodel.ExperienceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ExperienceViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getExperienceDetailUseCase: GetExperienceDetailUseCase

    @Mock
    private lateinit var likeExperienceUseCase: LikeExperienceUseCase

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ExperienceViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle().apply {
            set("experienceId", "test_id")
        }
    }

    @Test
    fun `loadExperience - success - updates state with experience`() = runTest {
        // Given
        val experience = createMockExperience()
        whenever(getExperienceDetailUseCase("test_id")).thenReturn(experience)

        // When
        viewModel = ExperienceViewModel(
            savedStateHandle,
            getExperienceDetailUseCase,
            likeExperienceUseCase,
            testDispatcher
        )

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(experience, state.experience)
        assertEquals(null, state.error)
    }

    private fun createMockExperience(): Experience {
        return Experience(
            id = "test_id",
            title = "Test Experience",
            description = "Test Description",
            coverPhoto = "https://test.com/image.jpg",
            city = "Cairo",
            viewsNo = 100,
            likesNo = 50,
            isLiked = false,
            recommended = true
        )
    }
}