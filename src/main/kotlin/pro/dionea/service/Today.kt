package pro.dionea.service

import java.sql.Timestamp
import java.util.*

class Today {
    fun timeInMillis() : Long {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        return today.timeInMillis
    }

    fun timestamp() : Timestamp {
        return Timestamp(timeInMillis())
    }
}