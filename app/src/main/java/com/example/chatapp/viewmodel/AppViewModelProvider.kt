package com.example.chatapp.viewmodel
//
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.CreationExtras
//import androidx.lifecycle.viewmodel.initializer
//import androidx.lifecycle.viewmodel.viewModelFactory
//import com.example.chatapp.general.ChatApplication
//
//object AppViewModelProvider {
//    val Factory = viewModelFactory {
//        // Other Initializers
//        // Initializer for
//
//        // todo If MainViewModel is needed
////        initializer {
////            val application = (this[APPLICATION_KEY] as ChatApplication)
////            MainViewModel(application.Repository)
////        }
//        initializer {
//            LoginScreenViewModel()
//        }
////        initializer {
////            SignUpScreenViewModel( chatApplication().container)
////        }
//
//
//    }
//}
//
//fun CreationExtras.chatApplication(): ChatApplication =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ChatApplication)