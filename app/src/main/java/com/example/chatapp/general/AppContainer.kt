package com.example.chatapp.general

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer { // Interface for repositories - if another database is added it needs to be implemented
//    val clientRepository: ClientRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineArticleRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ClientRepository]
     */

//    override val clientRepository: ClientRepository by lazy {
//        OfflineClientRepository(AppDatabase.getDatabase(context).clientDao())
//    }


}