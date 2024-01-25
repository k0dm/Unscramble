package com.github.k0dm.unscramble.creategame.data

interface WordsRepository {

    suspend fun words(numberOfWords: Int): List<String>

    class Base(private val wordsService: WordsService): WordsRepository {
        override suspend fun words(numberOfWords: Int) = wordsService.words(numberOfWords)
    }

    class Fake : WordsRepository {

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

        private var currentIndex = 0
        override suspend fun words(numberOfWords: Int): List<String> {
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
}

