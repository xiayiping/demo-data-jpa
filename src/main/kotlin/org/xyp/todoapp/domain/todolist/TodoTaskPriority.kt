package org.xyp.todoapp.domain.todolist

enum class TodoTaskPriority(
    val priority: Int
) {
    HIGHEST(5),
    HIGH(4),
    MEDIUM(3),
    LOW(2),
    LOWEST(1);

    companion object {
        val codeMap: Map<Int, TodoTaskPriority> =
            entries.associateBy(TodoTaskPriority::priority)
    }

}