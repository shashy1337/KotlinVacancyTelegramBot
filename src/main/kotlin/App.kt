import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromId
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import java.io.ByteArrayInputStream
import java.io.File
import java.lang.Exception

class App {

    private val tokenBot = API.KEY_
    private val preprocessData = PreprocessData()
    private val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
        listOf(
            InlineKeyboardButton
                .CallbackData(text = "Ввести и отформатировать вакансию", callbackData = "getRawVacancy")
        ),
        listOf(
            InlineKeyboardButton
                .CallbackData(text = "Выйти в главное меню", callbackData = "exitCallback")
        )
    )


    private val bot = bot {
        token = tokenBot
        timeout = 30
        logLevel = LogLevel.Network.Body
        dispatch {
            command("start") {

                bot.sendMessage(
                    chatId = fromId(update.message!!.chat.id),
                    text = StringResourcesDataClass.create().botHello
                )
                bot.sendMessage(
                    chatId = fromId(update.message!!.chat.id),
                    text = "Введите пароль для использования бота"
                )

            }

            text("45034691") {
                bot.sendMessage(
                    chatId = fromId(update.message!!.chat.id),
                    text = StringResourcesDataClass.create().info,
                    replyMarkup = inlineKeyboardMarkup
                )

                update.consume()
            }

            callbackQuery("getRawVacancy") {
                val chatId = update.callbackQuery?.message?.chat?.id ?: return@callbackQuery
                bot.sendMessage(
                    chatId = fromId(chatId),
                    text = "Для составления вакансии заполните таблицу, прикрепленную ниже.\n" +
                            "*Аккуратно заполняйте таблицу под каждым столбцом*",
                    parseMode = ParseMode.MARKDOWN,
                )

                bot.sendDocument(
                    fromId(chatId),
                    TelegramFile.ByFile(
                        File("/Users/igorvasilcev/IdeaProjects/Ks54TelegramBotProject/src/main/resources/example/example.xlsx")
                    )
                )

                bot.sendMessage(
                    fromId(chatId),
                    text = "Загрузите файл",
                )

                document {
                    val fileName = update.message!!.document?.fileName
                    if ((fileName?.contains(".xlsx")) == true or (fileName?.contains(".xls") == true)) {
                        bot.sendMessage(
                            fromId(
                                update.message!!.chat.id
                            ),
                            text = "Файл загружен",
                        )
                        try {
                            val file = File(
                                "/Users/igorvasilcev/IdeaProjects/Ks54TelegramBotProject/src/main/resources/$fileName"
                            )
                            val inputSteam = ByteArrayInputStream(update.message!!.document?.fileId?.let {
                                bot.downloadFileBytes(
                                    it
                                )
                            })
                            inputSteam.use { input ->
                                file.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            val excelFile = preprocessData.readFile(file)
                            bot.sendMessage(
                                chatId = fromId(update.message!!.chat.id),
                                text = "Обработанные вакансии: "
                            )

                            val generatedVacancy = preprocessData.generateVacancy(excelFile)
                            generatedVacancy.forEach {
                                bot.sendMessage(
                                    chatId = fromId(update.message!!.chat.id),
                                    parseMode = ParseMode.MARKDOWN,
                                    text = it
                                )
                            }
                            bot.sendMessage(
                                chatId = fromId(update.message!!.chat.id),
                                text = "Хотите ввести еще вакансии?",
                                replyMarkup = inlineKeyboardMarkup
                            )

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    } else {
                        bot.sendMessage(fromId(update.message!!.chat.id), text = "Вы прикрепляете не .xlsx/.xls файл")
                    }
                    update.consume()
                }
                return@callbackQuery
            }

            callbackQuery("exitCallback") {
                val chatId = update.callbackQuery?.message?.chat?.id ?: return@callbackQuery
                bot.sendMessage(
                    fromId(chatId),
                    text = "Возвращаемся в главное меню: ",
                    replyMarkup = inlineKeyboardMarkup
                )
                update.consume()
            }

            telegramError {
                println(error.getErrorMessage())
            }
        }
    }
    fun start() {
        bot.startPolling()
    }
}

fun main() {
    App().start()
}