package org.xyp.todoapp.core.statepattern

interface StateEvent<S> {
    fun update(business: HasState<S>?) : StateTransferResult<S>

    fun supportedSourceStates(): Set<S>

    fun supportNullBusiness() = false
}