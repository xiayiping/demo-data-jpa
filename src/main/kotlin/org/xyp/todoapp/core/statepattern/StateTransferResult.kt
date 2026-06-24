package org.xyp.todoapp.core.statepattern

data class StateTransferResult<S> (
    val updated: HasState<S>?,
    val publishedEvent: StateEvent<S>?
)
