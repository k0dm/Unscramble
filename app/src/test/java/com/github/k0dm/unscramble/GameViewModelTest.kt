package com.github.k0dm.unscramble

import com.github.k0dm.unscramble.creategame.data.WordsRepository
import com.github.k0dm.unscramble.creategame.domain.GameSession
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.creategame.domain.UiMapper
import com.github.k0dm.unscramble.game.presentation.GameViewModel
import com.github.k0dm.unscramble.game.presentation.ShuffleWord
import com.github.k0dm.unscramble.game.presentation.UiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel(FakeGameInteractor())
    }

    @Test
    fun correctTwice() {
        var actual: UiState = viewModel.init()
        var expected: UiState =
            UiState.Initial(counter = "1/2", score = "0", shuffledWord = "lamina")
        assertEquals(expected, actual)

        "anima".forEachIndexed { index, _ ->

            actual = viewModel.update(text = "anima".substring(0..index))
            expected = UiState.NotReadyToSubmit
            assertEquals(expected, actual)
        }

        actual = viewModel.update("animal")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.update("animall")
        expected = UiState.NotReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.update("animal")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit(word = "animal")
        expected = UiState.Initial(counter = "2/2", score = "20", shuffledWord = "otua")
        assertEquals(expected, actual)

        "aut".forEachIndexed { index, _ ->

            actual = viewModel.update("aut".substring(0..index))
            expected = UiState.NotReadyToSubmit
            assertEquals(expected, actual)
        }
        actual = viewModel.update("auto")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("auto")
        expected = UiState.GameOver(score = "40")
        assertEquals(expected, actual)

        actual = viewModel.skip()
        expected =
            UiState.Initial(counter = "1/2", score = "0", shuffledWord = "anecdote".reversed())
        assertEquals(expected, actual)
    }

    @Test
    fun incorrectThenCorrect() {
        correctTwice()

        var actual: UiState = viewModel.update("anecdote".reversed())
        var expected: UiState = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("anecdote".reversed())
        expected = UiState.Error
        assertEquals(expected, actual)

        "anecdot".forEachIndexed { index, _ ->
            actual = viewModel.update("anecdot".substring(index))
            expected = UiState.NotReadyToSubmit
            assertEquals(expected, actual)
        }

        actual = viewModel.update("")
        expected = UiState.NotReadyToSubmit
        assertEquals(expected, actual)

        "anecdot".forEachIndexed { index, _ ->
            actual = viewModel.update("anecdot".substring(0..index))
            expected = UiState.NotReadyToSubmit
            assertEquals(expected, actual)
        }

        actual = viewModel.update("anecdote")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("anecdote")
        expected = UiState.Initial(counter = "2/2", score = "10", shuffledWord = "hello".reversed())
        assertEquals(expected, actual)

        "hell".forEachIndexed { index, _ ->
            actual = viewModel.update("hell".substring(0..index))
            expected = UiState.NotReadyToSubmit
            assertEquals(expected, actual)
        }

        actual = viewModel.update("hellq")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("hellq")
        expected = UiState.Error
        assertEquals(expected, actual)

        actual = viewModel.update("hell")
        expected = UiState.NotReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.update("hello")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("hello")
        expected = UiState.GameOver(score = "20")
        assertEquals(expected, actual)

    }

    @Test
    fun incorrectThenSkipThenIncorrectThenCorrect() {
        var actual: UiState = viewModel.init()
        var expected: UiState =
            UiState.Initial(counter = "1/2", score = "0", shuffledWord = "lamina")
        assertEquals(expected, actual)

        actual = viewModel.submit("animak")
        expected = UiState.Error
        assertEquals(expected, actual)

        actual = viewModel.skip()
        expected = UiState.Initial(counter = "2/2", score = "0", shuffledWord = "otua")
        assertEquals(expected, actual)

        actual = viewModel.submit("autq")
        expected = UiState.Error
        assertEquals(expected, actual)

        actual = viewModel.update("aut")
        expected = UiState.NotReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.update("auto")
        expected = UiState.ReadyToSubmit
        assertEquals(expected, actual)

        actual = viewModel.submit("auto")
        expected = UiState.GameOver(score = "10")
        assertEquals(expected, actual)
    }
}

private class FakeGameInteractor() : GameInteractor {

    private val repository = FakeWordsRepository()
    private val maxWords = 2
    private val shuffleWord = FakeShuffleWord()

    override fun gameSession(): GameSession {
        val listOfWords = repository.words(maxWords)
        return FakeGameSession(words = listOfWords, maxWords = maxWords, shuffleWord = shuffleWord)
    }
}

private class FakeWordsRepository : WordsRepository {

    private val listOfWords = listOf<String>(
        "animal",
        "auto",
        "anecdote",
        "hello",
        "dog",
        "forest",
        "keyboard",
        "laptop",
        "cat",
        "transparent",
        "like",
        "form",
        "powder",
        "inn",
        "boy",
        "venture",
        "ball",
        "breakfast",
        "chair",
        "economist",
        "abstract",
        "plain",
        "training",
        "index",
        "research",
        "bomber",
        "belly",
        "exercise",
        "brake",
        "perforate",
        "feminist",
        "soak",
        "advocate",
        "fly",
        "hostile",
        "recommend",
        "behead",
        "window",
        "central",
        "abuse",
        "embark",
        "bitch",
        "reduction",
        "far",
        "script",
        "personality",
        "fault",
        "subway",
        "fantasy",
        "disappoint",
        "orange",
        "slice",
        "tear",
        "retire",
        "cower",
        "salmon",
        "distribute",
        "marriage",
        "social",
        "clinic"
    )

    var currentIndex = 0
    override fun words(numberOfWords: Int): List<String> {
        val resultList = arrayListOf<String>()
        repeat(numberOfWords) {
            if (currentIndex < listOfWords.lastIndex) {
                resultList.add(listOfWords[currentIndex++])
            } else {
                resultList.add(listOfWords[currentIndex])
                currentIndex = 0
            }
        }
        return resultList
    }
}

private class FakeGameSession(
    private val words: List<String>,
    private val maxWords: Int,
    private val shuffleWord: ShuffleWord
) : GameSession {

    private var currentWord = words[0]
    private var currentIndex = 0
    private var attemts = 0
    private var score = 0
    private var isFinished = false


    override fun isTheSameWord(word: String): Boolean = word == currentWord

    override fun calculateScore() {
        score += if (attemts == 0) 20 else 10
    }

    override fun attempt() {
        attemts++
    }

    override fun finishGame() {
        isFinished = true
    }

    override fun nextWord() {
        attemts = 0
        currentWord = words[++currentIndex]
    }

    override fun isLastWord(): Boolean = currentIndex == words.lastIndex
    override fun isGameOver() = isFinished

    override fun map(uiMapper: UiMapper) = uiMapper.map(
        shuffledWord = shuffleWord.shuffle(currentWord),
        counter = "${currentIndex + 1}/$maxWords",
        score = "$score"
    )
}

private class FakeShuffleWord : ShuffleWord {

    override fun shuffle(text: String) = text.reversed()
}