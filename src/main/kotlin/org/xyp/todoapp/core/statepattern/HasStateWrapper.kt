package org.xyp.todoapp.core.statepattern

abstract class HasStateWrapper<S, T>(
    val business: T
) : HasState<S>
