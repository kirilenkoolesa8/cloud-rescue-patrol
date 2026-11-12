package com.Xiphuqo7.Zomsop.presentation.app

import androidx.lifecycle.ViewModel
import com.Xiphuqo7.Zomsop.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun navigate(target: Screen) {
        backStack.addLast(_screen.value)
        show(target)
    }

    fun replace(target: Screen) {
        show(target)
    }

    fun resetTo(target: Screen) {
        backStack.clear()
        show(target)
    }

    fun openFromResult(target: Screen) {
        backStack.clear()
        if (target != Screen.Menu) backStack.addLast(Screen.Menu)
        show(target)
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        show(previous)
    }

    private fun show(target: Screen) {
        _screen.value = target
        _canGoBack.value = backStack.isNotEmpty()
    }
}
