package com.murali.test.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.murali.test.R
import com.murali.test.adapter.WidgetsListAdapter
import com.murali.test.api.APIService
import com.murali.test.databinding.ActivityMainBinding
import com.murali.test.utils.Common
import com.murali.test.viewmodel.StudentRepository
import com.murali.test.viewmodel.StudentViewModel
import com.murali.test.viewmodel.StudentViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainRepository: StudentRepository
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: StudentViewModel
    private val totalPages = arrayListOf<String>()
    private var pageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveAndCotinue.setOnClickListener(clickListener)
        binding.btnStartOver.setOnClickListener(clickListener)

        val retrofitService = APIService.getInstance()
        mainRepository = StudentRepository(retrofitService)

        viewModel = ViewModelProvider(this, StudentViewModelFactory(mainRepository)).get(
            StudentViewModel::class.java
        )

        if (Common.isNetworkConnected(this)) {
            observeUserInfoFromService()
        } else {
            Toast.makeText(this, getString(R.string.network_connnectivity), Toast.LENGTH_LONG).show()
            binding.progressDialog.visibility = View.GONE
        }
    }

    private fun bindWidgetAdapter(currentPage: Int) {

        val widgetAdapter = WidgetsListAdapter(
            {
                if (it > currentPage) {
                    binding.btnSaveAndCotinue.performClick()
                } else {
                    if (it <currentPage) {
                        Toast.makeText(this, getString(R.string.visited_page_text), Toast.LENGTH_LONG).show()
                    } else if (it == currentPage && it < totalPages.size-1) {
                        Toast.makeText(this, getString(R.string.next_page_text), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, getString(R.string.user_final_page_info), Toast.LENGTH_LONG).show()
                    }
                }
            },
            totalPages, currentPage, this
        )
        binding.pagesWidgetView.bindWidgetAdapter(widgetAdapter)
    }

    fun observeUserInfoFromService() {
        viewModel.getAllStudentsInfo(getString(R.string.code_value))
        for (i in 1..3) {
            totalPages.add("" + i)
        }
        bindWidgetAdapter(0)
        viewModel.studetsList.observe(
            this,
            {
                viewsVisibility()
                binding.tvDob.setText(it.dob.toString())
                binding.tvEmail.setText(it.email.toString())
                binding.tvName.setText(it.name.toString())
                binding.tvPhone.setText(it.phone.toString())
            }
        )

        viewModel.errorMessage.observe(
            this,
            {
                viewsVisibility()
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )

        viewModel.loading.observe(
            this,
            {
                if (it) {
                    binding.progressDialog.visibility = View.VISIBLE
                } else {
                    binding.progressDialog.visibility = View.GONE
                }
            }
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    fun viewsVisibility() {
        binding.userContainerView.visibility = View.VISIBLE
    }

    val clickListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_save_and_cotinue -> {
                if (totalPages.size - 1> pageCount) {
                    pageCount++
                    bindWidgetAdapter(pageCount)
                } else {
                    Toast.makeText(this, getString(R.string.user_final_page_info), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
