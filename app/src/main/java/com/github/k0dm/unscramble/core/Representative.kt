package com.github.k0dm.unscramble.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Representative<T> {

    fun startGettingUpdates(observer: UiObserver<T>)

    fun stopGettingUpdates(observer: UiObserver<T> = UiObserver.Empty())

    abstract class Abstract<T : Any>(
        private val runAsync: RunAsync
    ) : Representative<T> {

        private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

        protected fun <T : Any> handleAsync(
            backgroundBlock: suspend () -> T,
            uiBLock: (T) -> Unit
        ) {
            runAsync.runAsync(coroutineScope, backgroundBlock, uiBLock)
        }

        protected fun clear() = runAsync.clear()
    }
}

interface RunAsync {

    fun <T : Any> runAsync(
        scope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBLock: (T) -> Unit
    )

    fun clear()

    class Base(private val dispatchersList: DispatchersList = DispatchersList.Base()) : RunAsync {
        private var job: Job? = null

        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBLock: (T) -> Unit
        ) {
            job = scope.launch(dispatchersList.background()) {
                val result = backgroundBlock.invoke()
                withContext(dispatchersList.ui()) {
                    uiBLock.invoke(result)
                }
            }
        }

        override fun clear() {
            job?.cancel()
            job = null
        }
    }
}