package com.example.playlistmaker.ui.search.act


import android.content.Context
import android.content.Intent
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
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.player.act.MediaActivity
import com.example.playlistmaker.ui.search.vm.SearchActivityUpdate
import com.example.playlistmaker.ui.search.vm.SearchActivityViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var goBackButtonId: FrameLayout
    private lateinit var clearTextButtonId: ImageView
    private lateinit var editTextId: EditText
    private lateinit var searchHistory: LinearLayout
    private lateinit var clearHistory: Button
    private lateinit var noDataFrame: FrameLayout
    private lateinit var noNetworkFrame: FrameLayout
    private lateinit var noNetworkUpdateButton: Button
    private lateinit var progressBar: ProgressBar


    private lateinit var recyclerView: RecyclerView
    private var data = ArrayList<Track>()


    private lateinit var historyRView: RecyclerView
    private val historyData = ArrayList<Track>()
    private var isSearchHistoryEmpty = true

    private val viewModel by viewModel<SearchActivityViewModel>()

    private val searchRunnable =
        Runnable {
            // otherwise search can happen after editText.len < App.SEARCH_DEBOUNCE_REQ_MIN_LEN
            if (editTextId.text.length >= SEARCH_DEBOUNCE_REQ_MIN_LEN)
                onSearchEntered()
        }


    private fun switchToPlayer() {
        val mediaIntent = Intent(this, MediaActivity::class.java)
        startActivity(mediaIntent)
    }

    private val saveSearchHistoryAndCurrentlyPlayingFun: (List<Track>, Track) -> Unit =
        { tracks, track ->
            viewModel.saveSearchHistoryAndCurrentlyPlaying(tracks, track)
        }


    private val trackViewHolderItemClicked: (Track, Boolean, Runnable, Runnable) -> Unit =
        { item, isClickAllowed, enableClick, disableClick ->
            run {

                // swap old items
                if (historyData.contains(item)) {

                    val oldPos: Int = historyData.indexOf(item)
                    historyData.remove(item)
                    historyData.add(0, item)

                    historyRView.adapter?.notifyItemMoved(oldPos, 0)
                    historyRView.scrollToPosition(0)

                // insert new item
                } else {

                    historyData.add(0, item)

                    historyRView.adapter?.notifyItemInserted(0)
                    historyRView.scrollToPosition(0)

                    if (historyData.size > SEARCH_HISTORY_MAX_LENGTH) {

                        historyData.removeAt(SEARCH_HISTORY_MAX_LENGTH)
                        historyRView.adapter?.notifyItemRemoved(SEARCH_HISTORY_MAX_LENGTH)
                    }
                }

                isSearchHistoryEmpty = false

                saveSearchHistoryAndCurrentlyPlayingFun(historyData, item)

                if (viewModel
                        .clickDebounce(
                            isClickAllowed,
                            enableClick,
                            disableClick
                        )
                ) {
                    switchToPlayer()
                }

            }
        }



    private fun showHistory() {

        searchHistory.visibility = if (isSearchHistoryEmpty) View.GONE else View.VISIBLE
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showTracks() {

        searchHistory.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showNoData() {
        searchHistory.visibility = View.GONE
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.VISIBLE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showNoNetwork() {
        searchHistory.visibility = View.GONE
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


    private fun onSearchEntered() {

        viewModel.searchTracks(editTextId.text.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel.getState().observe(this) {
            when (it) {
                is SearchActivityUpdate.Loading -> {

                    progressBar.visibility = View.VISIBLE
                    data.clear()
                    recyclerView.adapter?.notifyDataSetChanged()
                }

                is SearchActivityUpdate.SearchResult -> {

                    data.clear()
                    if (it.tracks.isEmpty()) {
                        showNoData()
                    } else {
                        data.addAll(it.tracks)
                        showTracks()
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }

                is SearchActivityUpdate.SearchHistoryData -> {

                    historyData.clear()

                    if (it.tracks.isEmpty()) {
                        isSearchHistoryEmpty = true
                    } else {
                        isSearchHistoryEmpty = false
                        historyData.addAll(it.tracks)
                    }

                    historyRView.adapter?.notifyDataSetChanged()
                    showHistory()
                }
            }
        }

        progressBar = findViewById(R.id.search_progress_bar)

        searchHistory = findViewById<LinearLayout>(R.id.search_history)
        historyRView = findViewById<RecyclerView>(R.id.history_rView)
        historyRView.adapter = SearchTrackAdapter(
            historyData,
            trackViewHolderItemClicked
        )

        clearHistory = findViewById<Button>(R.id.clear_search_history)
        clearHistory.setOnClickListener {

            historyData.clear()
            historyRView.adapter?.notifyDataSetChanged()
            isSearchHistoryEmpty = true
            showHistory()
            viewModel.clearSearchHistory()
        }

        recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.adapter = SearchTrackAdapter(
            data,
            trackViewHolderItemClicked
        )

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
                    showHistory()

                } else {

                    viewModel.searchDebounce(searchRunnable)
                    clearTextButtonId.visibility = View.VISIBLE
                    showTracks()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                /* empty */
            }
        }
        editTextId.addTextChangedListener(simpleTextWatcher)


        savedInstanceState?.let {
            val s = savedInstanceState.getString(SEARCH_REQUEST_KEY, "")
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
        outState.putString(SEARCH_REQUEST_KEY, editTextId.text.toString())
    }

    private companion object {

        const val SEARCH_HISTORY_MAX_LENGTH = 10
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN = 3

        const val SEARCH_REQUEST_KEY = "SEARCH_REQUEST"
    }
}