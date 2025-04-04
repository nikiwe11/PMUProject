package com.example.chatapp.general

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.chatapp.viewmodel.SettingsDetails
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Constants {

    object Colors {
        val DARK_BLUE = Color(0xFFBE1E2D)
    }

    object Gradient {
        val RED_TO_BLACK = listOf(Color(0xFFBE1E2D), Color(0xFF231F20))
        val BLACK_TO_GRAY = listOf(Color(0xFF231F20), Color(0xFF414042))
        val GRAY_TO_WHITE = listOf(Color(0xFF414042), Color.White)
        val GREEN_TO_BROWN = listOf(Color(0xFFC4D300),Color(0xFF795A2D))
    }
    object Regex {
        const val NO_LIMIT = ".*"
        const val ONLY_LETTERS_AND_NUMBERS = "^[\\p{L}\\p{N}]+\$"
        const val ONLY_NUMBERS_ABOVE_ZERO = "^[1-9][0-9]*\$"
        const val ONLY_NUMBERS = "^[0-9]+\$"
        const val PRICE = "^\\d+(\\.\\d{0,2})?\$"
        const val BARCODE = "^^[\\p{L}\\p{N}]+\$"
        const val ZDDS = "^[BG0-9]+\$"
        const val EDIT_PERCENT_FIELD = "^(100(\\.0{0,2})?|([1-9]?\\d(\\.\\d{0,2})?)?)\$"
    }
    object Routes {
        const val LOGIN = "login"
        const val SIGN_UP = "signup"
        const val MAIN_MENU = "main_menu"
        const val ADD_FRIEND = "add_friend"
        const val CHAT = "chat"
        const val CHAT_SETTINGS = "chat_settings"
        const val PROFILE_SETTINGS = "profile_settings"

        const val REGISTRATION = "registration"
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