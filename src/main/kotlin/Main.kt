/**
 * Simple Hangman game in Kotlin.
 * */
class HangmanGame(val secretWord: String, var guessesLeft: Int) {

    // Stores all guessed letters (mutable collection)
    val guessedLetters = mutableSetOf<Char>()

    // Track if the player won by guessing the whole word on the last chance
    var lastChance = false

    /**
     * Returns the current progress of the game as a list of characters.
     * Correctly guessed letters are shown, others are hidden as "_".
     * */
    fun getProgress(): List<Any> {

        val progress = secretWord.map { if (guessedLetters.contains(it)) it else "_" }

        return progress
    }

    /**
     * Handles a single-letter guess.
     * Validates input, updates guessed letters, returns feedback to the player, decrease number of attempts if wrong guess.
     * */
    fun guessSingleLetter(guess: String): String {
        val message: String

        // Validate that input is exactly one character
        if (guess.length == 1) {
            guessedLetters.add(guess[0])

            // Provide feedback based on whether the guess is correct. If it's wrong, decrease the number of attempts left
            if (secretWord.contains(guess)) {
                message = "Good guess! '$guess' is in the word"
            } else {
                message = "Wrong guess! '$guess' is not in the word, try another"
                guessesLeft--
            }

        } else {
            // Handle invalid input (more than one character), decrease the number of attempts left
            message = "Please, enter a single letter"
            guessesLeft--
        }

        return message
    }

    /**
     * Gives the player one final opportunity to guess the entire word when all attempts are used.
     * Returns true if the word is guessed correctly.
     * */
    fun guessTheWord(): Boolean {

        println("There are no guesses left.")

        print("Do you want to try to guess the whole word? (Y/N): ")

        val answer = readln().lowercase()

        if (answer.contains("y")) {

            print("What is the secret word? ")

            val guessWholeWord = readln().lowercase()

            if (guessWholeWord == secretWord) {

                // Mark all letters as guessed so the player can win
                for (letter in guessWholeWord) {

                    guessedLetters.add(letter)

                }
                lastChance = true
                return true

            }
        }

        return false
    }

    /**
     * Checks if the player has guessed the entire word.
     * */
    fun hasWon(): Boolean {

        val condition = !getProgress().contains("_")

        return condition
    }

}

/**
 * Returns a prompt message depending on how many guesses are left.
 * */
fun guessPrompt(guessesLeft: Int): String {

    val message: String = when (guessesLeft) {

        // All the attempts
        4 -> "What is your first guess? "

        // Last attempt
        1 -> "What is your last guess? "

        // Any attempt
        else -> "What is your next guess? "

    }
    return message
}

/**
 * Returns the theme name selected by the player.
 * */
fun getTheme(theme: String): String {

    return when (theme) {

        "1" -> "Animals"

        "2" -> "Nature"

        "3" -> "Objects"

        else -> "Random"
    }
}

/**
 * Returns a word bank (list of words) based on the selected theme.
 * */
fun getWordBank(theme: String): List<String> {

    return when (theme) {

        // Animals
        "Animals" -> listOf("lion", "wolf", "frog", "bear", "crab", "deer", "mole", "toad", "goat", "duck")

        // Nature
        "Nature" -> listOf("tree", "rain", "wind", "snow", "rock", "leaf", "sand", "lava", "star", "moon")

        // Objects
        "Objects" -> listOf("book", "lamp", "ring", "door", "fork", "ship", "coin", "bell", "rope", "mask")

        // Random
        else -> listOf("book", "tree", "lamp", "frog", "star", "coin", "leaf", "rock", "milk", "fish", "sand", "bell", "wind", "door", "snow", "fire", "cake", "ring", "wolf", "duck")
    }
}

/**
 * Entry point of the program. Runs the Hangman game.
 * */
fun main() {

    // Welcome message and theme selection
    println("\nWelcome to my Hangman Game!\n")

    print("Would you like to guess:\n" +
            "1. Animals\n" +
            "2. Nature\n" +
            "3. Objects\n" +
            "4. Random\n" +
            "Select one theme (1-4): ")

    val theme = getTheme(readln())

    println("You've chosen $theme to guess. Good luck!")

    // Select a random word from the chosen theme
    val wordsBank = getWordBank(theme)
    val secretWord = wordsBank.random()
//    println(secretWord)

    val game = HangmanGame(secretWord, 4)

    // Main game loop. Stop the loop once the attempts reach 0.
    while (game.guessesLeft > 0 ) {
        val progress = game.getProgress()

        // Check if the player already won
        if (game.hasWon()) {
            println("\nCongratulations! You guessed the word: $secretWord")

            if (!game.lastChance) {
                println("And you even have ${game.guessesLeft} attempt${if (game.guessesLeft == 1) "" else "s"} left")
            }

            println("Let's play again soon!")
            return // End the game
        }

        // Display game status
        print("\nGuess the Secret word: ")
        println(progress.joinToString(separator = " "))
        println("\nAttempts left: ${game.guessesLeft}")

        // Prompt and handle guess
        print(guessPrompt(game.guessesLeft))
        val guess = readln().lowercase()

        print("\n***- ${game.guessSingleLetter(guess)} -***\n")

        // If no attempts left, offer a final chance to guess the whole word
        if (game.guessesLeft == 0 && !game.hasWon()) {

            if (game.guessTheWord()) {

                game.guessesLeft++ // If correct, don't let the while loop end

                continue
            }
        }
    }

    // If the loop ends without winning
    println("Game over!")
    println("Sorry, the secret word was: $secretWord")
    println("Better luck next time, see you!")
}
