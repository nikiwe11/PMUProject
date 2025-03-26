package com.example.chatappsingle.general

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.chatappsingle.viewmodel.SettingsDetails
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Constants {

    object Gradient {
        val RED_TO_BLACK = listOf(Color(0xFFBE1E2D), Color(0xFF231F20))
        val BLACK_TO_GRAY = listOf(Color(0xFF231F20), Color(0xFF414042))
        val GRAY_TO_WHITE = listOf(Color(0xFF414042), Color.White)
    }

    object Routes {
        const val LOGIN = "login"
        const val MAIN_MENU = "main_menu"
        const val CHAT = "chat"
        const val CHAT_SETTINGS = "chat_settings"
        const val PROFILE_SETTINGS ="profile_settings"

        const val REGISTRATION = "registration"

        const val Z_REPORT = "#@Z"
        const val X_REPORTS = "x_reports"
        const val HOURLY_REPORT = "hourly_report"
        const val ARTICLE_DETAILS = "article_details"
        const val PERIODIC_REPORT = "periodic_report"
        const val FISCAL_MEMORY = "fiscal_memory"
        const val KLEN = "klen"
        const val PROGRAMMING = "programming"
        const val ARTICLE_PROGRAMMING = "article_programming"
        const val EDIT_ARTICLE = "edit_article"
        const val DEPARTMENT_PROGRAMMING = "department_programming"
        const val DEPARTMENT_DETAILS = "department_details"
        const val EDIT_DEPARTMENT = "edit_department"
        const val PAYMENT_DETAILS = "payment_details"
        const val PAYMENT_EDIT = "payment_edit"
        const val PAYMENT_PROGRAMMING = "payment_programming"
        const val CLIENT_PROGRAMMING = "client_programming"
        const val EDIT_CLIENT = "edit_client"
        const val OPERATOR_PROGRAMMING = "operator_programming"
        const val EDIT_OPERATOR = "edit_operator"
        const val SETTINGS = "settings"
        const val OPERATOR_DETAILS = "operator_details"
        const val CLIENT_DETAILS = "client_details"
    }

}
object BufferedDataBase { // Stores all database items for quicker access


    object Settings {
        var deviceIp: String = "localhost"
        var port: String = "1024"
        var debugReceipt: Boolean = false
        var useDarkTheme: Boolean = false

        private val currentDate = LocalDate.now()
        private val currentTime = LocalTime.now()

        fun setDetails(details: SettingsDetails) {
            useDarkTheme = details.useDarkTheme
        }

        /**
         * Returns current date with passed day month or year
         */

        fun getDate(
            pattern: String = "dd.MM.yyyy",
            day: Int = currentDate.dayOfMonth,
            month: Int = currentDate.monthValue,
            year: Int = currentDate.year,
        ): String {
            val adjustedDate = try {
                LocalDate.of(year, month, day) // Directly create the date to ensure validity
            } catch (e: DateTimeException) {
                Log.d(
                    "DateTimeException",
                    "Failed to get date with input: $day $month $year with e: $e"
                )
                currentDate // Fallback to current date if invalid values are provided
            }
            return adjustedDate.format(DateTimeFormatter.ofPattern(pattern))
        }

        fun getTime(
            pattern: String = "HH:mm:ss",
        ): String {
            return currentTime.format(DateTimeFormatter.ofPattern(pattern))
        }

    }
}

fun <T> selectFromTheme(lightThemeValue: T, darkThemeValue: T): T {
    return if (BufferedDataBase.Settings.useDarkTheme) darkThemeValue else lightThemeValue
}