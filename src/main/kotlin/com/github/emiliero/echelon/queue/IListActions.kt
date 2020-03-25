package com.github.emiliero.echelon.queue



interface IListActions {
    fun addPersonToQueue(username : String, discriminator : String, userID : String): String
    fun removePersonFromQueue(username: String, discriminator: String): String
    fun printList(): String
    fun clearList(username: String, discriminator: String) : String
}