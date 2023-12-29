package com.example.playlistmaker.ui.fragsHolderActivity.ui.search


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapters.RecyclerViewTrackAdapter
import com.example.playlistmaker.ui.playerActivity.act.MediaActivity
import com.example.playlistmaker.ui.utils.hideKeyboard


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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

    private val viewModel by viewModel<SearchFragmentViewModel>()


    private var previousSearchText: String = ""


    private var isClickAllowed: Boolean = true
    private fun isClickAllowed(): Boolean {
        val startState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return startState
    }


    private fun switchToPlayer() {
        val mediaIntent = Intent(context, MediaActivity::class.java)
        startActivity(mediaIntent)
    }

    private val saveSearchHistoryAndCurrentlyPlayingFun: (List<Track>, Track) -> Unit =
        { tracks, track ->
            viewModel.saveSearchHistoryAndCurrentlyPlaying(tracks, track)
        }


    private val trackViewHolderItemClicked: (Track) -> Unit = { item ->
        run {

            if (isClickAllowed()) {

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

    private fun showDataLoading() {
        searchHistory.visibility = View.GONE
        recyclerView.visibility = View.GONE
        noDataFrame.visibility = View.GONE
        noNetworkFrame.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        data.clear()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun onSearchEntered() {

        showDataLoading()
        viewModel.searchTracksDebounced(editTextId.text.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {

                is SearchFragmentUpdate.NoNetwork -> {
                    showNoNetwork()
                }

                is SearchFragmentUpdate.SearchResult -> {

                    data.clear()

                    if (it.tracks.isEmpty()) {
                        showNoData()
                    } else {
                        data.addAll(it.tracks)
                        showTracks()
                    }

                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.scrollToPosition(0)
                }

                is SearchFragmentUpdate.SearchHistoryData -> {

                    historyData.clear()

                    if (it.tracks.isEmpty()) {
                        isSearchHistoryEmpty = true
                    } else {
                        isSearchHistoryEmpty = false
                        historyData.addAll(it.tracks)
                    }

                    historyRView.adapter?.notifyDataSetChanged()
                    historyRView.scrollToPosition(0)
                    showHistory()
                }
            }
        }

        progressBar = binding.searchProgressBar

        searchHistory = binding.searchHistory
        historyRView = binding.historyRView
        historyRView.adapter = RecyclerViewTrackAdapter(
            historyData, trackViewHolderItemClicked
        )

        clearHistory = binding.clearSearchHistory
        clearHistory.setOnClickListener {

            historyData.clear()
            historyRView.adapter?.notifyDataSetChanged()
            isSearchHistoryEmpty = true
            showHistory()
            viewModel.clearSearchHistory()
        }

        recyclerView = binding.rView
        recyclerView.adapter = RecyclerViewTrackAdapter(
            data, trackViewHolderItemClicked
        )

        noDataFrame = binding.searchNoDataFrame

        noNetworkFrame = binding.searchNoNetworkFrame
        noNetworkUpdateButton = binding.noNetworkUpdateButton
        noNetworkUpdateButton.setOnClickListener { onSearchEntered() }

        clearTextButtonId = binding.searchClearEditTextButton
        clearTextButtonId.setOnClickListener {

            editTextId.setText("")
            data.clear()
            recyclerView.adapter?.notifyDataSetChanged()

            viewModel.cancelSearch()
            viewModel.requestSearchHistory()

            //------------------------------------------------------------------------?
            /*editTextId.clearFocus()
             requireActivity().currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService( INPUT_METHOD_SERVICE ) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }*/
            //------------------------------------------------------------------------?

            hideKeyboard()
        }

        editTextId = binding.searchEditText
        editTextId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchEntered()
            }
            false
        }


        editTextId.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                if (charSequence.isNullOrEmpty()) {

                    clearTextButtonId.visibility = View.GONE
                    showHistory()

                } else {

                    if (previousSearchText != charSequence.toString()) {
                        showDataLoading()
                        viewModel.searchTracksDebounced(charSequence.toString())
                        previousSearchText = charSequence.toString()
                    }
                    clearTextButtonId.visibility = View.VISIBLE
                }
            }
        )


        savedInstanceState?.let {
            val s = savedInstanceState.getString(SEARCH_REQUEST_KEY, "")
            if (s != "") {
                previousSearchText = s
                editTextId.setText(s)
                clearTextButtonId.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST_KEY, editTextId.text.toString())
    }

    override fun onStart() {
        super.onStart()
        editTextId.requestFocus()
    }

    private companion object {
        const val SEARCH_HISTORY_MAX_LENGTH = 10
        const val SEARCH_REQUEST_KEY = "SEARCH_REQUEST"

        const val CLICK_DEBOUNCE_DELAY_MILLIS = 400L
    }
}