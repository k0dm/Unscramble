package com.github.k0dm.unscramble.game.presentation

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.github.k0dm.unscramble.core.DisableAndEnable
import com.github.k0dm.unscramble.core.HideAndShow
import com.google.android.material.button.MaterialButton

class SubmitButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr), HideAndShow, DisableAndEnable {


    override fun onSaveInstanceState(): Parcelable = super.onSaveInstanceState().let {
        val state = VisibilityAndEnabledState(it)
        state.save(this)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as VisibilityAndEnabledState
        super.onRestoreInstanceState(restoredState.superState)
        restoredState.restore(this)
    }

    override fun show() {
        visibility = View.VISIBLE
        this.currentTextColor
    }

    override fun hide() {
        visibility = View.GONE
    }

    override fun disable() {
        isEnabled = false
    }

    override fun enable() {
        isEnabled = true
    }
}

class VisibilityAndEnabledState : View.BaseSavedState {

    private var visibility: Int = View.VISIBLE
    private var isEnabled: Boolean = true

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        visibility = parcelIn.readInt()
        isEnabled = parcelIn.readByte() == 1.toByte()
    }

    fun save(view: View) {
        visibility = view.visibility
        isEnabled = view.isEnabled
    }

    fun restore(view: View) {
        view.visibility = visibility
        view.isEnabled = isEnabled
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(visibility)
        out.writeByte(if (isEnabled) 1 else 0)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<VisibilityAndEnabledState> {
        override fun createFromParcel(parcel: Parcel): VisibilityAndEnabledState =
            VisibilityAndEnabledState(parcel)

        override fun newArray(size: Int): Array<VisibilityAndEnabledState?> = arrayOfNulls(size)
    }
}