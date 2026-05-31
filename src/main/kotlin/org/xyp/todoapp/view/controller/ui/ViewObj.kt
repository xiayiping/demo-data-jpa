package org.xyp.todoapp.view.controller.ui

data class ViewObj(
    val page: Page
)

data class Page(
    val nav: String
)


object PageConst {
    const val NAV_PROGRESS = "progress"
    const val NAV_DRAFT = "draft"
    const val NAV_CLOSED = "closed"
    const val NAV_FORM = "form"
}