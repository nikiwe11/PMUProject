package com.example.chatapp.general

import android.app.Application
import kotlinx.coroutines.runBlocking

//class ChatApplication : Application() {
//
//    /**
//     * AppContainer instance used by the rest of classes to obtain dependencies
//     */
////    lateinit var userPreferencesRepository: UserPreferencesRepository
//    lateinit var container: AppContainer
//
//    override fun onCreate() {
//        super.onCreate()
//        container = AppDataContainer(this)
//
//        // todo example datastore usage
////        userPreferencesRepository = UserPreferencesRepository(dataStore)
////        val ipAddress = runBlocking {
////            userPreferencesRepository.ip.firstOrNull() ?: "localhost"
////
////        }
////        val port = runBlocking {
////            userPreferencesRepository.port.firstOrNull()?: "1024"
////        }
////        val debugReceipt = runBlocking {
////            userPreferencesRepository.debugReceipt.firstOrNull()?: false
////        }
////        val useDarkTheme = runBlocking {
////            userPreferencesRepository.useDarkTheme.firstOrNull()?: false
////        }
//
//        // Assign the collected IP address to DataBaseInfo.deviceIp
////        BufferedDataBase.Settings.deviceIp = ipAddress
////        BufferedDataBase.Settings.port = port
////        BufferedDataBase.Settings.debugReceipt = debugReceipt
////        BufferedDataBase.Settings.useDarkTheme = useDarkTheme
//    }
//}