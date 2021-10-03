package com.murali.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.murali.test.model.Student
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.lang.Exception
import java.lang.StringBuilder

class StudentViewModel constructor(private val mainRepository: StudentRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val studetsList = MutableLiveData<Student>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllStudentsInfo(code: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getAllMovies(code)
            withContext(Dispatchers.Main) {
                try {
                    studetsList.postValue(Gson().fromJson(formatString(response.first().toString()), Student::class.java))
                } catch (ex: Exception) {
                    val msg = ex.toString()
                }
                loading.value = false
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    private fun formatString(text: String): String {
        val json = StringBuilder()
        var indentString = ""
        for (i in 0 until text.length) {
            val letter = text[i]
            when (letter) {
                '{', '[' -> {
                    json.append(
                        """
                        
                        $indentString$letter
                        
                        """.trimIndent()
                    )
                    indentString = indentString + "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                        
                        $indentString$letter
                        """.trimIndent()
                    )
                }
                ',' -> json.append(
                    """
                    $letter
                    $indentString
                    """.trimIndent()
                )
                else -> json.append(letter)
            }
        }
        return json.toString()
    }
}
