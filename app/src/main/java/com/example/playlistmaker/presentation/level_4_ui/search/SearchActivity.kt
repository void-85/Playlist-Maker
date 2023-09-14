package com.example.playlistmaker.presentation.level_4_ui.search


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.DTO.ResponseData
import com.example.playlistmaker.domain.level_1_entities.Track
import com.example.playlistmaker.data.level_4_web.SearchAPIService
import com.example.playlistmaker.presentation.level_3_presenters.millisToMinSec
import com.example.playlistmaker.presentation.level_4_ui.MediaActivity
import com.example.playlistmaker.sharedPrefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList




lateinit var historyRView: RecyclerView

val historyData = ArrayList<Track>()
var isSearchHistoryEmpty = true



class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchAPIService = retrofit.create<SearchAPIService>(SearchAPIService::class.java)

    private lateinit var goBackButtonId    :FrameLayout
    private lateinit var clearTextButtonId :ImageView
    private lateinit var editTextId        :EditText

    private lateinit var searchHistory :LinearLayout
    private lateinit var clearHistory  :Button

    private lateinit var recyclerView :RecyclerView

    private lateinit var noDataFrame :FrameLayout

    private lateinit var noNetworkFrame        :FrameLayout
    private lateinit var noNetworkUpdateButton :Button

    private lateinit var progressBar :ProgressBar



    private var data = ArrayList<Track>()



    private val handler = Handler( Looper.getMainLooper() )
    private val searchRunnable =
        Runnable {
            // otherwise search can happen after editText.len < App.SEARCH_DEBOUNCE_REQ_MIN_LEN
            if (editTextId.text.length >= App.SEARCH_DEBOUNCE_REQ_MIN_LEN)
                onSearchEntered()
        }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed    (searchRunnable, App.SEARCH_DEBOUNCE_DELAY)
    }

    fun switchToPlayer(){
        val mediaIntent = Intent( this, MediaActivity::class.java )
        startActivity(mediaIntent)
    }

    private fun showHistory() {

        searchHistory.visibility  = if(isSearchHistoryEmpty) View.GONE else View.VISIBLE
        recyclerView.visibility   = View.GONE
        noDataFrame.visibility    = View.GONE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility    = View.GONE
    }

    private fun showTracks() {

        searchHistory.visibility  = View.GONE
        recyclerView.visibility   = View.VISIBLE
        noDataFrame.visibility    = View.GONE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility    = View.GONE
    }

    private fun showNoData() {
        searchHistory.visibility  = View.GONE
        recyclerView.visibility   = View.GONE
        noDataFrame.visibility    = View.VISIBLE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility    = View.GONE
    }

    private fun showNoNetwork() {
        searchHistory.visibility  = View.GONE
        recyclerView.visibility   = View.GONE
        noDataFrame.visibility    = View.GONE
        noNetworkFrame.visibility = View.VISIBLE
        progressBar.visibility    = View.GONE
    }



    private fun onSearchEntered() {

        progressBar.visibility = View.VISIBLE

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
                                    trackName     = it.trackName     ,
                                    artistName    = it.artistName    ,
                                    artworkUrl100 = it.artworkUrl100 ,
                                    trackTime     = it.trackTimeMillis.millisToMinSec(),

                                    collectionName    = it.collectionName   ,
                                    releaseDate       = it.releaseDate      ,
                                    primaryGenreName  = it.primaryGenreName ,
                                    country           = it.country          ,

                                    previewUrl        = it.previewUrl       )
                                )
                            }

                            showTracks()

                        } else { showNoData() }

                        recyclerView.adapter?.notifyDataSetChanged()

                    } else { showNoNetwork() }
                }

                override fun onFailure(
                    call: Call<ResponseData>,
                    t: Throwable
                ) {
                    showNoNetwork()
                }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.search_progress_bar)

        searchHistory = findViewById<LinearLayout>(R.id.search_history)
        historyRView = findViewById<RecyclerView>(R.id.history_rView)
        historyRView.adapter = SearchTrackAdapter(historyData, ::switchToPlayer)

        //sharedPrefs = getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)
        isSearchHistoryEmpty = sharedPrefs.getBoolean(App.IS_SEARCH_HISTORY_EMPTY, true)
        if (!isSearchHistoryEmpty) {

            val json = sharedPrefs.getString(App.SEARCH_HISTORY_KEY, "") ?: ""
            //Log.d("JSON", json)

            if (json.isNotEmpty()) {

                historyData.clear()


                //------------------------------------------------------------------------------------------
                /* <!> ОШИБКА:
                java.lang.ClassCastException:
                    com.google.gson.internal.LinkedTreeMap cannot be cast to com.example.playlistmaker.Track

                Gson().fromJson(json, ArrayList<Track>()::class.java).forEach {
                    history_data.add( it )
                }*/
                //------------------------------------------------------------------------------------------
                /*Gson().fromJson(json, ArrayList<LinkedTreeMap<String,String>>()::class.java)
                    .forEach {
                        history_data.add( Track(
                            artistName    = it["artistName"]    ?: "ERROR" ,
                            artworkUrl100 = it["artworkUrl100"] ?: "ERROR" ,
                            trackName     = it["trackName"]     ?: "ERROR" ,
                            trackTime     = it["trackTime"]     ?: "ERROR" )
                        )}*/
                //------------------------------------------------------------------------------------------

                Gson().fromJson<ArrayList<Track>>(
                    json,
                    object : TypeToken<ArrayList<Track>>() {}.type
                ).forEach {
                    historyData.add(it)
                }

                historyRView.adapter?.notifyDataSetChanged()
            }
        }

        clearHistory = findViewById<Button>(R.id.clear_search_history)
        clearHistory.setOnClickListener {

            historyData.clear()
            historyRView.adapter?.notifyDataSetChanged()
            isSearchHistoryEmpty = true
            showHistory()

            sharedPrefs.edit().putBoolean(App.IS_SEARCH_HISTORY_EMPTY, true).apply()
        }


        recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.adapter = SearchTrackAdapter(data, ::switchToPlayer )

        noDataFrame = findViewById<FrameLayout>(R.id.search_no_data_frame)

        noNetworkFrame = findViewById<FrameLayout>(R.id.search_no_network_frame)

        noNetworkUpdateButton = findViewById<Button>(R.id.no_network_update_button)
        noNetworkUpdateButton.setOnClickListener { onSearchEntered() }

        goBackButtonId = findViewById<FrameLayout>(R.id.search_go_back_button)
        goBackButtonId.setOnClickListener { finish() }


        clearTextButtonId = findViewById<ImageView>(R.id.search_clear_edit_text_button)
        clearTextButtonId.setOnClickListener {

            editTextId.setText("")
            data.clear()
            showHistory()

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
                //true
            }
            false
        }
        /*editTextId.setOnFocusChangeListener { _, hasFocus ->

        }*/


        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                s     :CharSequence? ,
                start :Int           ,
                count :Int           ,
                after :Int           )
            {
                /* empty */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {

                    clearTextButtonId.visibility = View.GONE
                    showHistory()

                } else {

                    searchDebounce()
                    clearTextButtonId.visibility = View.VISIBLE
                    showTracks()

                }
            }

            override fun afterTextChanged(s: Editable?)
            {
                /* empty */
            }
        }
        editTextId.addTextChangedListener(simpleTextWatcher)



        savedInstanceState?.let {
            val s = savedInstanceState.getString(SEARCH_REQUEST, "")
            if (s != "") {
                editTextId.setText(s)
                clearTextButtonId.visibility = View.VISIBLE
            }
        }

        //1st to show
        showHistory()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, editTextId.text.toString())
    }
}