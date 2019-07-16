package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question:Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        
        val isAnswerValid = question.validateAnswer(answer)
        if (isAnswerValid.first) {
            return if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                "Отлично - это правильный ответ\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                "Это неправильный ответ!\n${question.question}" to status.color
            }
        } else {
            return "${isAnswerValid.second}\n${question.question}" to status.color
        }

    }

    enum class Status(val color: Triple<Int,Int,Int>) {
        NORMAL(Triple(255,255,255)),
        WARNING(Triple(255,120,0)),
        DANGER(Triple(255,60,60)),
        CRITICAL(Triple(255,0,0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    /*
    Question.NAME -> "Имя должно начинаться с заглавной буквы"
    Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
    Question.MATERIAL -> "Материал не должен содержать цифр"
    Question.BDAY -> "Год моего рождения должен содержать только цифры"
    Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
    Question.IDLE -> //игнорировать валидацию
     */
    

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер","bender")) {
            override fun nextQuestion(): Question = PROFESSION

            override fun validateAnswer(answer: String): Pair<Boolean, String?> {
                val isValid =  answer.substring(0,1) != answer.substring(0,1).toUpperCase()
                val strError = if (isValid) null else "Имя должно начинаться с заглавной буквы"
                return isValid to strError
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик","bender")){
            override fun nextQuestion(): Question = MATERIAL

            override fun validateAnswer(answer: String): Pair<Boolean, String?> {
                val isValid =  answer.substring(0,1) == answer.substring(0,1).toLowerCase()
                val strError = if (isValid) null else "Профессия должна начинаться со строчной буквы"
                return isValid to strError
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл","дерево","metal", "iron", "wood")){
            override fun nextQuestion(): Question = BDAY

            override fun validateAnswer(answer: String): Pair<Boolean, String?> {
                val isValid = !answer.matches("\\d+".toRegex())
                val strError = if (isValid) null else "Материал не должен содержать цифр"
                return isValid to strError
            }
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL

            override fun validateAnswer(answer: String): Pair<Boolean, String?> {
                val isValid =   answer.matches("[0-9]*".toRegex())
                val strError = if (isValid) null else "Год моего рождения должен содержать только цифры"
                return isValid to strError
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE

            override fun validateAnswer(answer: String): Pair<Boolean, String?> {
                val isValid =   answer.matches("[0-9]*".toRegex()) && answer.length == 7
                val strError = if (isValid) null else "Серийный номер содержит только цифры, и их 7"
                return isValid to strError
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun nextQuestion(): Question = IDLE

            override fun validateAnswer(answer: String) = true to null
        };

        abstract fun nextQuestion(): Question
        
        abstract fun validateAnswer(answer: String): Pair<Boolean,String?>
        
    }
}