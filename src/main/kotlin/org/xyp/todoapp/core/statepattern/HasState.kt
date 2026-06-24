package org.xyp.todoapp.core.statepattern

interface HasState<S> {

    fun getState(): S

}