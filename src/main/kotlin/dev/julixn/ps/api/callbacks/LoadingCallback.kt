package dev.julixn.ps.api.callbacks

import dev.julixn.ps.api.eventsystem.EventHandler

class LoadingCallback {

    private var success: Boolean = false

    fun back(value: Boolean) {
        this.success = value
    }

    fun isSuccess(): Boolean {
        return success
    }

}