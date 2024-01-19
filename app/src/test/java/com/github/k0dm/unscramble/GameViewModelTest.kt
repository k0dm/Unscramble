package com.github.k0dm.unscramble

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel(Repository.Base())
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

        actual = viewModel.submit(word ="animal")
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
        expected = UiState.Initial(counter = "2/2", score = "20", shuffledWord = "hello".reversed())
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
        expected = UiState.GameOver(score = "30")
        assertEquals(expected, actual)

    }
}

private class FakeRepository : Repository {

    private val listOfWords = listOf<WordWithScore>(
        WordWithScore(word = "animal", score = 20),
        WordWithScore(word = "auto", score = 20),
        WordWithScore(word = "anecdote", score = 40),
        WordWithScore(word = "hello", score = 20),
        WordWithScore(word = "dog", score = 10),
        WordWithScore(word = "forest", score = 20),
        WordWithScore(word = "keyboard", score = 30),
        WordWithScore(word = "laptop", score = 20),
        WordWithScore(word = "cat", score = 10),
        WordWithScore(word = "transparent", score = 50)
    )

    private var currentIndex = 0
    override fun wordWithScore(): WordWithScore {
        return if (currentIndex != listOfWords.lastIndex) {
            listOfWords[currentIndex++]
        } else {
            val word = listOfWords[currentIndex]
            currentIndex = 0
            word
        }
    }
}
