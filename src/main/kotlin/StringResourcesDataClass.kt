class StringResourcesDataClass {
    val botHello: String = "Вас приветствуте бот по подбору вакансий в Колледже Связи №54"
    val getVacancyName : String = "Введите вакансию"
    val inputSetString : String = "Обработанная вакансия: \n"
    val stringSaved : String = "Строка сохранена"
    val info : String = "В данном чат боте релизована функция автоматического форматирования вакансии." +
            "Используйте кнопки снизу, чтобы пройтись по навигации бота:"

    companion object Factory{
        fun create() = StringResourcesDataClass()
    }
}