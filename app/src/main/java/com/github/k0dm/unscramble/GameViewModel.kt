package com.github.k0dm.unscramble

class GameViewModel(private val repository: Repository) {

    private var wordWithScore: ShuffleWordAndScore = ShuffleWordAndScore.Empty
    private val maxWords = 2
    private var currentNumber = 1
    private var currentScore = 0
    private var currentAttempt = 0
    private var isGameOver = false

    fun init(): UiState {
        wordWithScore = repository.wordWithScore()
        return UiState.Initial("1/$maxWords", "0", wordWithScore.shuffledWord())
    }

    fun update(text: String): UiState {
        return if (text.length == wordWithScore.shuffledWord().length)
            UiState.ReadyToSubmit
        else
            UiState.NotReadyToSubmit
    }

    fun submit(word: String): UiState {
        return if (wordWithScore.checkIsTheSame(word)) {
            currentScore += wordWithScore.score(currentAttempt)

            if (currentNumber == maxWords) {
                isGameOver = true
                UiState.GameOver(currentScore.toString())
            } else {
                currentAttempt = 0
                currentNumber++
                wordWithScore = repository.wordWithScore()
                UiState.Initial(
                    "$currentNumber/$maxWords",
                    currentScore.toString(),
                    wordWithScore.shuffledWord()
                )
            }
        } else {
            currentAttempt++
            UiState.Error
        }
    }

    fun skip(): UiState {
        return if (currentNumber == maxWords) {
            if (isGameOver) {
                currentNumber = 1
                currentScore = 0
                currentAttempt = 0
                isGameOver = false
                wordWithScore = repository.wordWithScore()
                UiState.Initial("1/$maxWords", "0", wordWithScore.shuffledWord())            } else {
                isGameOver = true
                UiState.GameOver(currentScore.toString())
            }

        } else {
            currentAttempt = 0
            currentNumber++
            wordWithScore = repository.wordWithScore()
            UiState.Initial(
                "$currentNumber/$maxWords",
                currentScore.toString(),
                wordWithScore.shuffledWord()
            )
        }
    }
}

