package com.github.k0dm.unscramble.game.presentation

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton

class SkipButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable = super.onSaveInstanceState().let {
        val state = TextColorState(it)
        state.save(this)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as TextColorState
        super.onRestoreInstanceState(state.superState)
        restoredState.restore(this)
    }
}

class TextColorState : View.BaseSavedState {

    private var textColor: Int = 0

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        textColor = parcelIn.readInt()
    }

    fun save(view: TextView) {
        textColor = view.currentTextColor
    }

    fun restore(view: TextView) {
        view.setTextColor(textColor)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(textColor)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TextColorState> {
        override fun createFromParcel(parcel: Parcel): TextColorState = TextColorState(parcel)

        override fun newArray(size: Int): Array<TextColorState?> = arrayOfNulls(size)
    }
}