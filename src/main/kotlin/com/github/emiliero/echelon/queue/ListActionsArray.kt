package com.github.emiliero.echelon.queue

import com.github.emiliero.echelon.authorization.isUserAuthorizedForStudentAssistantCommands
import com.github.emiliero.echelon.model.Student

object ListActionsArray : IListActions {
    private var studentList : MutableList<Student> = ArrayList<Student>()


    override fun addPersonToQueue(username : String, discriminator : String, userID : String) : String {
        val studentInQue = isStudentInQueue(
            username,
            discriminator
        )
        val p = Student(username, discriminator, userID, studentList.size+1)

        return if(!studentInQue){
            if(studentList.any()){
                studentList.add(studentList.lastIndex+1, p)
                "<@${p.snowflake}>"+" has been added to the queue you are at spot ${p.placeInQue}"

            } else {
                studentList.add(p)
                "<@${p.snowflake}>"+ " has been added to the queue, you are the first one up at place ${p.placeInQue}"
            }
        } else {
            "<@${p.snowflake}>"+" is already in the queue, you can't join twice <:pepega:687979040019054803>"
        }
    }

    override fun removePersonFromQueue(username: String, discriminator: String) : String {
        if (studentList.none()) {
            return "There's no one in the queue"
        }

        var result = "You are not in the queue, ${username}. If you want to join, use `!join`"
        var snowFlakeToBeRemoved = ""
        var placeInQueToBeRemoved=0

        if(studentList.isNotEmpty()) {
            run {
                val iterator: MutableIterator<Student> = studentList.iterator()
                while (iterator.hasNext()) {
                    val value = iterator.next()
                    if (value.name == username && value.discordId == discriminator) {
                        placeInQueToBeRemoved = value.placeInQue
                        snowFlakeToBeRemoved= value.snowflake
                        iterator.remove()
                    }
                }
            }
            if(snowFlakeToBeRemoved.isNotEmpty()){
                shiftList()
                result = "<@$snowFlakeToBeRemoved>"+
                        " has been removed from their spot in the queue, spot $placeInQueToBeRemoved"
            }
        }

        return result
    }

    override fun clearList(username: String, discriminator: String): String {
        //TODO: Hvis lista alt er tom, ikke clear
        if (isUserAuthorizedForStudentAssistantCommands(username, discriminator)) {
            studentList.clear()
        }

        return if (studentList.isEmpty()){
            "Queue cleared"
        } else {
            "You do not have permission to clear the list"
        }
    }

    private fun isStudentInQueue(username: String, discriminator: String) : Boolean {
        val iterator = studentList.iterator()
        var isInQue = false

        iterator.forEach { student ->
            if(student.name == username && student.discordId == discriminator) {
                isInQue = true
            }
        }
        return isInQue
    }

    fun moveNextStudentIntoChannel(username: String, discriminator: String, id : String): String {
        val studentAssistantId = id.split("{", "}")[1]

        if(studentList.isNotEmpty() && isUserAuthorizedForStudentAssistantCommands(username, discriminator)) {
            val user: Student = studentList[0];
            studentList.removeAt(0)
            shiftList()
            return "<@${user.snowflake}> is the next one up with <@${studentAssistantId}> <:pepeHack:687975058215403552>"
        }
        return "<@${studentAssistantId}>, there are no students in queue"
    }

    fun checkPositionInQueue(username : String, discriminator: String) : String{
        var message = "You are not in the queue at the moment. Use !join"
        val iterator = studentList.iterator()

        iterator.forEach {student ->
            if (student.name == username && student.discordId == discriminator) {
                val position = student.placeInQue
                message = "<@${student.snowflake}>" +
                        " is at position $position in the queue"
            }
        }
        return message
    }

    private fun shiftList() {
        val iterator = studentList.iterator()

        for ((index, value) in iterator.withIndex()) {
            if (value.placeInQue != index + 1) {
                value.placeInQue = index + 1
            }
        }
    }

    override fun printList(): String{
        val builder : StringBuilder = StringBuilder()
        val iterator = studentList.iterator()

        // TODO: @ person som kaller på metoden (bruker kommandoen)
        if(studentList.isNotEmpty()) {
            iterator.forEach { student ->
                builder.append(student.placeInQue.toString() + ": " + student.name + "\n")
            }
        } else {
            builder.append("The list is empty")
        }

        return builder.toString()
    }
}
