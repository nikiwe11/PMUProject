package com.example.chatapp.general

import android.content.Context
import com.example.chatapp.data.repository.AuthRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer { // Interface for repositories - if another database is added it needs to be implemented

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