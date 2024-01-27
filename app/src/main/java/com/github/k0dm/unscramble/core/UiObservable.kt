package com.github.k0dm.unscramble.core

interface UiObservable<T> : UiUpdate<T>, UpdateObserver<T>, Clear {

    abstract class Abstract<T : Any>(private val empty: T) : UiObservable<T> {

        private var cache: T = empty
        private var observer: UiObserver<T> = UiObserver.Empty()

        override fun update(data: T) {
            cache = data
            observer.update(data)
        }

        override fun updateObserver(observer: UiObserver<T>) {
            this.observer = observer
            observer.update(cache)
        }

        override fun clear() {
            cache = empty
        }
    }
}

interface UiUpdate<T> {
    fun update(data: T)
}

interface UiObserver<T> : UiUpdate<T> {
    class Empty<T> : UiObserver<T> {
        override fun update(data: T) = Unit
    }
}

interface UpdateObserver<T> {
    fun updateObserver(observer: UiObserver<T>)

    object Empty : UiObservable<Any> {
        override fun clear() = Unit
        override fun updateObserver(observer: UiObserver<Any>) = Unit
        override fun update(data: Any) = Unit
    }
}