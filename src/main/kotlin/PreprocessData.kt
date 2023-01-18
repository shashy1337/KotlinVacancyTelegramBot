import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellUtil
import java.io.File
import java.io.FileInputStream

data class Vacancy(
    val nameEmploy: String? = null,
    val req: String? = null,
    val jobOpportunity: String? = null,
    val condition: String? = null,
    val salary: String? = null,
    val company: String? = null
)

class PreprocessData {

    fun readFile(file: File): List<Vacancy> {
        val vacancyDetails = mutableListOf<Vacancy>()
        val inputStream = FileInputStream(file)
        val xlWb = WorkbookFactory.create(inputStream)
        val xlWs = xlWb.getSheetAt(0)

        val i = 0
        for (row in xlWs) {
            if (row.rowNum == 0) {
                row.rowNum++
                continue
            }

            if (
                row.getCell(i) != null
                &&
                row.getCell(i + 1) != null
                &&
                row.getCell(i + 2) != null
                &&
                row.getCell(i + 3) != null
                &&
                row.getCell(i + 4) != null
                &&
                row.getCell(i + 5) != null
            ) {
                vacancyDetails.add(
                    Vacancy(
                        nameEmploy = row.getCell(i).toString(),
                        req = row.getCell(i + 1).toString(),
                        jobOpportunity = row.getCell(i + 2).toString(),
                        condition = row.getCell(i + 3).toString(),
                        salary = row.getCell(i + 4).toString(),
                        company = row.getCell(i + 5).toString()
                    )
                )
            }

        }
        return vacancyDetails
    }

    fun generateVacancy(vacancyList: List<Vacancy>): List<String> {
        val lists = mutableListOf<String>()
        vacancyList.forEach {
            lists.add(
                StringBuilder().apply {
                    append("⭐️️Вакансия: *${it.nameEmploy}*").append("\n")
                    append("\uD83C\uDFE2Компания: ${it.company}").append("\n")
                    append("\uD83D\uDCB6Зарплата: ${it.salary}").append("\n").append("\n")
                    append("\uD83D\uDC81\u200DТребования: ${it.req}").append("\n").append("\n")
                    append("\uD83D\uDCC3Должностные обязанности: ${it.jobOpportunity}").append("\n").append("\n")
                    append("✅Условия:${it.condition}").append("\n").append("\n")
                    append(
                        "\uD83D\uDCF1При заинтересованности соискателя выбранной вакансией, " +
                                "необходимо направить своё РЕЗЮМЕ на почту _employment@spo54.ru_ с указанием " +
                                "в теме письма наименования вакансии."
                    )
                }.toString()
            )
        }
        return lists
    }
}