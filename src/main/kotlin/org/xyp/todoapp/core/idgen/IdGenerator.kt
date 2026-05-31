package org.xyp.todoapp.core.idgen

interface IdGenerator<T> {
    fun generate(keyName: String): T

    fun generate(keyName: String, size: Long): List<T>
}