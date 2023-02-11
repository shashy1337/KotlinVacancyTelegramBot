import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DataBaseConnect {

    private val url = API._URL
    private val user = API._USER
    private val password = API._PASSWORD


    fun setData(id : Long, data : String) {
        val sqlQ = ("INSERT INTO request_data_table(id,text_data) "
                + "VALUES(?,?)")

        try {
            connectS().use { conn ->
                conn.prepareStatement(sqlQ).use { statement ->
                    statement.setLong(1, id)
                    statement.setString(2, data)
                    statement.addBatch()
                    statement.executeBatch()
                }
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }

    }

    fun getDataId() : Long{
        val sqlQ = ("SELECT ID FROM request_data_table ORDER BY ID DESC LIMIT 1")
        try {
            connectS().use { conn ->
                conn.createStatement().use { statement ->
                    val result = statement.executeQuery(sqlQ)
                    if (result.next()) return result.getLong(1)
                }
            }
        } catch (ex : SQLException){
            ex.printStackTrace()
        }
        return 0
    }

    @Throws(SQLException::class)
    private fun connectS() : Connection {
        return DriverManager.getConnection(url, user, password)
    }

}
