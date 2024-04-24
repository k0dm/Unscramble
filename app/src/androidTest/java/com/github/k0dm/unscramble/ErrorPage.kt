package com.github.k0dm.unscramble

import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.CoreMatchers.allOf

class ErrorPage {

    private val textInputLayout = onView(
        allOf(
            withId(R.id.textInputLayout),
            isAssignableFrom(TextInputLayout::class.java),
            withParent(withId(R.id.mainLayout)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )
    fun checkVisible() {
        textInputLayout.check(matches(InputErrorMatcher("Wrong answer")))
    }

    fun checkNotVisible() {
        textInputLayout.check(matches(InputErrorMatcher(null)))
    }
}

