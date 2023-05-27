package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        val editTextId = findViewById<EditText>(R.id.search_edit_text)
        editTextId.requestFocus()



        val clearTextButtonId = findViewById<ImageView>(R.id.search_clear_edit_text_button)
        clearTextButtonId.setOnClickListener{

            editTextId.setText("")

            this.currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }



        val goBackButtonId = findViewById<FrameLayout>(R.id.search_go_back_button)
        goBackButtonId.setOnClickListener{ finish() }



        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {

                    clearTextButtonId.visibility = View.GONE

                } else {

                    clearTextButtonId.visibility = View.VISIBLE

                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editTextId.addTextChangedListener(simpleTextWatcher)


    }
}