package com.example.bookmate.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[PHOTO_KEY] = user.photoUrl
            preferences[IS_LOGIN_KEY] = true
            preferences[IS_NEW_USER_KEY] = user.isNewUser
        }
    }

    suspend fun setName(name: String) {
        dataStore.edit { preference ->
            run {
                preference[NAME_KEY] = name
            }
        }
    }

    suspend fun setPhotoImage(url: String) {
        dataStore.edit { preference ->
            run {
                preference[PHOTO_KEY] = url
            }
        }
    }

    suspend fun setNewUserKey() {
        dataStore.edit { preferencees ->
            preferencees[IS_NEW_USER_KEY] = false
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[PHOTO_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IS_NEW_USER_KEY] ?: false
            )
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences -> preferences[TOKEN_KEY] ?: "" }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val PHOTO_KEY = stringPreferencesKey("photoUrl")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val IS_NEW_USER_KEY = booleanPreferencesKey("isNewUser")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}