package com.github.k0dm.unscramble

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

class InitialPage(
    private val counter: String,
    private val score: String,
    private val shuffledWord: String,
) {

    private val submitButton = onView(
        allOf(
            withId(R.id.submitButton),
            isAssignableFrom(Button::class.java),
            withText("Submit"),
            withParent(withId(R.id.rootLayout)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )

    private val skipButton = onView(
        allOf(
            withId(R.id.skipButton),
            isAssignableFrom(Button::class.java),
            withText("Skip"),
            withParent(withId(R.id.rootLayout)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )

    private val textInputEditText = onView(
        allOf(
            withId(R.id.textInputEditText),
            isAssignableFrom(TextInputEditText::class.java)
        )
    )

    fun checkVisible(input: String = "") {

        submitButton.check(
            matches(
                if (input.length == shuffledWord.length)
                    isEnabled()
                else
                    not(isEnabled())
            )
        )
        skipButton.check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.counterTextView),
                isAssignableFrom(TextView::class.java),
                withParent(withId(R.id.mainLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java))
            )
        ).check(matches(withText(counter)))

        onView(
            allOf(
                withId(R.id.scoreTextView),
                isAssignableFrom(TextView::class.java),
                withParent(withId(R.id.scoreLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java))
            )
        ).check(matches(withText(score)))

        onView(
            allOf(
                withId(R.id.shuffledWordTextView),
                isAssignableFrom(TextView::class.java),
                withParent(withId(R.id.mainLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java))
            )
        ).check(matches(withText(shuffledWord)))

        onView(
            allOf(
                withId(R.id.textInputLayout),
                isAssignableFrom(TextInputLayout::class.java),
                withParent(withId(R.id.mainLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java))
            )
        ).check(matches(InputErrorMatcher(null)))

        textInputEditText.check(matches(withText(input)))
    }

    fun clickSubmit() {
        Espresso.closeSoftKeyboard()
        submitButton.perform(click())
    }

    fun typeText(word: String) {
        textInputEditText.perform(ViewActions.typeText(word))
    }

    fun typeText(word: Char) {
        typeText(word.toString())
    }

    fun replaceText(text: String) {
        textInputEditText.perform(ViewActions.replaceText(text))
    }

    fun clickSkip() {
        Espresso.closeSoftKeyboard()
        skipButton.perform(click())
    }

    fun checkNotVisible() {
        onView(
            allOf(
                withId(R.id.mainLayout),
                isAssignableFrom(LinearLayout::class.java),
                withParent(withId(R.id.rootLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java))
            )
        ).check(matches(not(isDisplayed())))

        submitButton.check(matches(not(isDisplayed())))
    }
}
