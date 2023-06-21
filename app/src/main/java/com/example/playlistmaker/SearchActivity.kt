package com.example.playlistmaker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity() {


    private companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchAPIService = retrofit.create<SearchAPIService>(SearchAPIService::class.java)

    private lateinit var goBackButtonId: FrameLayout
    private lateinit var clearTextButtonId: ImageView
    private lateinit var editTextId: EditText

    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataFrame: FrameLayout
    private lateinit var noNetworkFrame: FrameLayout

    private lateinit var noNetworkUpdateButton: Button

    private var data = ArrayList<Track>()


    private fun showTracks() {
        recyclerView.visibility = View.VISIBLE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.GONE
    }

    private fun showNoData() {
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.VISIBLE
        noNetworkFrame.visibility = View.GONE
    }

    private fun showNoNetwork() {
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.VISIBLE
    }


    private fun onSearchEntered() {

        searchAPIService.getTracksByTerm(editTextId.text.toString())
            .enqueue(object : Callback<ResponseData> {

                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.code() == 200) {

                        val responseData = response.body()?.results.orEmpty()
                        data.clear()

                        if (responseData.isNotEmpty()) {
                            responseData.forEach {
                                data.add( Track(
                                    trackName = it.trackName,
                                    artistName = it.artistName,
                                    artworkUrl100 = it.artworkUrl100,
                                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault() ).format(it.trackTimeMillis)
                                ) )
                            }
                            showTracks()

                        } else { showNoData() }

                        recyclerView.adapter?.notifyDataSetChanged()

                    } else { showNoNetwork() }
                }

                override fun onFailure(
                    call: Call<ResponseData>,
                    t: Throwable
                ) { showNoNetwork() }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.adapter = SearchTrackAdapter(data)

        noDataFrame = findViewById<FrameLayout>(R.id.search_no_data_frame)
        noNetworkFrame = findViewById<FrameLayout>(R.id.search_no_network_frame)
        showTracks()

        noNetworkUpdateButton = findViewById<Button>(R.id.no_network_update_button)
        noNetworkUpdateButton.setOnClickListener { onSearchEntered() }

        goBackButtonId = findViewById<FrameLayout>(R.id.search_go_back_button)
        goBackButtonId.setOnClickListener { finish() }



        clearTextButtonId = findViewById<ImageView>(R.id.search_clear_edit_text_button)
        clearTextButtonId.setOnClickListener {

            editTextId.setText("")

            data.clear()
            recyclerView.adapter?.notifyDataSetChanged()

            showTracks()

            this.currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }

        }



        editTextId = findViewById<EditText>(R.id.search_edit_text)
        editTextId.requestFocus()
        editTextId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchEntered()
                true
            }
            false
        }



        savedInstanceState?.let {
            val s = savedInstanceState.getString(SEARCH_REQUEST, "")
            if (s != "") {
                editTextId.setText(s)
                clearTextButtonId.visibility = View.VISIBLE
            }
        }



        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                /* empty */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearTextButtonId.visibility = View.GONE
                } else {
                    clearTextButtonId.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                /* empty */
            }
        }
        editTextId.addTextChangedListener(simpleTextWatcher)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, editTextId.text.toString())
    }
}