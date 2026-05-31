package org.xyp.todoapp.domain.todolist

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class TodoTaskPriorityConverter : AttributeConverter<TodoTaskPriority, Int> {

    // Convert the enum to its database representation (priority code)
    override fun convertToDatabaseColumn(attribute: TodoTaskPriority?): Int? {
        return attribute?.priority // Map enum to priority code
    }

    // Convert the database value to its enum representation
    override fun convertToEntityAttribute(dbData: Int?): TodoTaskPriority? {
        return dbData?.let { priorityCode ->
            TodoTaskPriority.codeMap.get(priorityCode)
        } ?: throw IllegalArgumentException("Invalid priority code: $dbData")
    }
}