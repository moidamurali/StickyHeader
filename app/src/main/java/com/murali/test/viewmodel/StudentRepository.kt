package com.murali.test.viewmodel

import com.google.gson.JsonObject
import com.murali.test.api.APIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StudentRepository constructor(private val retrofitService: APIService) {

    suspend fun getAllMovies(code: String): Flow<JsonObject> {
        return flow {
            emit(retrofitService.getAllStudents(code))
        }
    }
}
