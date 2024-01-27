package com.github.k0dm.unscramble.main

import com.github.k0dm.unscramble.core.UiObservable

interface Navigation : UiObservable<Screen> {
    class Base: Navigation, UiObservable.Abstract<Screen>(Screen.Empty)
}