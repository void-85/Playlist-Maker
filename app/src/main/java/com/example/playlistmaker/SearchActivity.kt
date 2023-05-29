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



    companion object { const val SEARCH_REQUEST = "SEARCH_REQUEST" }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        val goBackButtonId    = findViewById<FrameLayout>( R.id.search_go_back_button )
        val clearTextButtonId = findViewById<ImageView>  ( R.id.search_clear_edit_text_button )
        val editTextId        = findViewById<EditText>   ( R.id.search_edit_text )



        editTextId.requestFocus()
        goBackButtonId.setOnClickListener{ finish() }


        savedInstanceState?.let {

            val s = savedInstanceState.getString( SEARCH_REQUEST, "" )

            if( s != "" ){
                editTextId.setText( s )
                clearTextButtonId.visibility = View.VISIBLE
            }
        }


        clearTextButtonId.setOnClickListener{

            editTextId.setText("")

            this.currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


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



    override fun onSaveInstanceState( outState :Bundle ){

        super.onSaveInstanceState(outState)

        val editTextId = findViewById<EditText>(R.id.search_edit_text)
        outState.putString( SEARCH_REQUEST, editTextId.text.toString() )

    }
}