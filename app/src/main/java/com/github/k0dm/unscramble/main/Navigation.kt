package com.github.k0dm.unscramble.main

import com.github.k0dm.unscramble.core.Clear
import com.github.k0dm.unscramble.core.UiObservable
import com.github.k0dm.unscramble.core.UiUpdate
import com.github.k0dm.unscramble.core.UpdateObserver

interface Navigation {

    interface Navigate : UiUpdate<Screen>

    interface Observe: UpdateObserver<Screen>

    interface Mutable : Navigate, Observe, Clear
    class Base: Mutable, UiObservable.Abstract<Screen>(Screen.Empty)
}