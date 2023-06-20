package com.example.playlistmaker



import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class SearchActivity :AppCompatActivity() {



    private companion object { const val SEARCH_REQUEST = "SEARCH_REQUEST" }


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchAPIService = retrofit.create<SearchAPIService>(SearchAPIService::class.java)


    private lateinit var goBackButtonId    :FrameLayout
    private lateinit var clearTextButtonId :ImageView
    private lateinit var editTextId        :EditText
    private lateinit var recyclerView      :RecyclerView

    private lateinit var switchForData  :ViewSwitcher
    private lateinit var switchForInfo  :ViewSwitcher
    private lateinit var noDataFrame    :FrameLayout
    private lateinit var noNetworkFrame :FrameLayout



    private var data = ArrayList<Track>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        switchForData  = findViewById<ViewSwitcher>( R.id.search_show_tracks_or_info )
        switchForInfo  = findViewById<ViewSwitcher>( R.id.search_no_tracks_no_data_or_error )
        noDataFrame    = findViewById<FrameLayout> ( R.id.search_no_data_frame )
        noNetworkFrame = findViewById<FrameLayout> ( R.id.search_no_network_frame )

        goBackButtonId = findViewById<FrameLayout>( R.id.search_go_back_button )
        goBackButtonId.setOnClickListener{ finish() }


        clearTextButtonId = findViewById<ImageView>( R.id.search_clear_edit_text_button )
        clearTextButtonId.setOnClickListener{

            editTextId.setText("")

            data.clear()
            recyclerView.adapter?.notifyDataSetChanged()

            this.currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


        editTextId = findViewById<EditText>    ( R.id.search_edit_text )


        recyclerView = findViewById<RecyclerView>( R.id.rView )
        recyclerView.adapter = SearchTrackAdapter( data )


        editTextId.requestFocus()
        editTextId.setOnEditorActionListener { _, actionId, _ ->

            if( actionId == EditorInfo.IME_ACTION_DONE ){

                    //TEST SWITCHERS BLOCK

                    if( editTextId.text.toString() == "1" ){

                        // SHOW TRACKS
                        if( switchForData.currentView != recyclerView){ switchForData.showNext() }

                    }else
                    if( editTextId.text.toString() == "2" ){

                        // NO DATA
                        if( switchForData.currentView != switchForInfo){ switchForData.showNext() }
                        if( switchForInfo.currentView != noDataFrame)  { switchForInfo.showNext() }

                    }else
                    if( editTextId.text.toString() == "3" ){

                        // NO NETWORK (OR ERROR)
                        if( switchForData.currentView != switchForInfo) { switchForData.showNext() }
                        if( switchForInfo.currentView != noNetworkFrame){ switchForInfo.showNext() }

                    }else






                    searchAPIService.getTracksByTerm( editTextId.text.toString() ).enqueue( object :Callback<ResponseData>{

                        override fun onResponse(
                            call     :Call     <ResponseData> ,
                            response :Response <ResponseData> )
                        {
                            if( response.isSuccessful ){

                                val responseData = response.body()?.results.orEmpty()
                                data.clear()



                                responseData.forEach{
                                    data.add( Track(
                                        trackName     = it.trackName     ,
                                        artistName    = it.artistName    ,
                                        artworkUrl100 = it.artworkUrl100 ,
                                        trackTime     = SimpleDateFormat("mm:ss", Locale.getDefault()).format( it.trackTimeMillis )
                                    ))
                                }




                                recyclerView.adapter?.notifyDataSetChanged()

                            }else{

                                //Toast.makeText(applicationContext, data[0].artistName, Toast.LENGTH_LONG).show()

                            }
                        }

                        override fun onFailure(
                            call :Call<ResponseData> ,
                            t    :Throwable          )
                        {
                            //TODO("Not yet implemented")
                            //Toast.makeText(applicationContext, "FAILURE:\n\n"+t.toString(), Toast.LENGTH_LONG).show()
                            Log.e( "ERROR: ", t.toString() )
                        }

                    })

                    //Toast.makeText(this, editTextId.text.toString(), Toast.LENGTH_LONG).show()

                true
            }
            false
        }

















        /*
        val data     :List<Track> = SearchActivityMockData().getData()
        val dataSize :Int         = 55

        recyclerView.adapter = SearchTrackAdapter(
            List<Track>(dataSize) {
                data[   Random.nextInt(0, data.size) %data.size   ]
            }
        )
        */







        savedInstanceState?.let {

            val s = savedInstanceState.getString( SEARCH_REQUEST, "" )

            if( s != "" ){
                editTextId.setText( s )
                clearTextButtonId.visibility = View.VISIBLE
            }
        }





        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* empty */ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearTextButtonId.visibility = View.GONE
                } else {
                    clearTextButtonId.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) { /* empty */  }
        }
        editTextId.addTextChangedListener(simpleTextWatcher)


    }



    override fun onSaveInstanceState( outState :Bundle ){

        super.onSaveInstanceState(outState)
        outState.putString( SEARCH_REQUEST, editTextId.text.toString() )
    }
}