package org.xyp.todoapp.core.statepattern

interface StateTransferService<S> {

    fun finalStates(): Set<S>

    fun transfer(business: HasState<S>?, event: StateEvent<S>): StateTransferResult<S> {
        return doTransfer(business, event, 10)
    }

    fun transfer(
        business: HasState<S>?, event: StateEvent<S>,
        remainDepth: Int
    ): StateTransferResult<S> {
        return doTransfer(business, event, remainDepth)
    }

    private tailrec fun doTransfer(
        business: HasState<S>?,
        event: StateEvent<S>?,
        remainDepth: Int
    ): StateTransferResult<S> {
        if (remainDepth <= 0) {
            return StateTransferResult(business, null)
        }
        if (null == event) {
            return StateTransferResult(business, null)
        }
        if (null != business && finalStates().contains(business.getState())) {
            return StateTransferResult(business, null)
        }
        if (null == business && !event.supportNullBusiness()) {
            throw IllegalArgumentException("event [${event.javaClass.name}] does not support null Business]")
        }
        if (null != business && !event.supportedSourceStates().contains(business.getState())) {
            throw IllegalStateException("event [${event.javaClass.name}] does not support source states [${business.getState()}]")
        }
        val result = event.update(business)
        return doTransfer(result.updated, result.publishedEvent, remainDepth - 1)
    }

}