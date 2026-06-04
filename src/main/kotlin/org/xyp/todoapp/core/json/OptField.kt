package org.xyp.todoapp.core.json

//@Serializable(with = OptFieldSerializer::class)
data class OptField<T>(
    val value: T? = null,
    val present: Boolean
) {
    companion object {
        fun <T> absent(): OptField<T> = OptField(null, false)
        fun <T> ofNull(): OptField<T> = OptField(null, true)
        fun <T> of(value: T?): OptField<T> = OptField(value = value, present = true)
    }

    constructor(v: T) : this(v, true)

    fun ifPresent(consumer: (T?) -> Unit) {
        if (present) {
            consumer.invoke(value)
        }
    }

}