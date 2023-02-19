package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.adapter.MovieAdapter
import com.qwk.chandrsekhar.adapter.MovieClickListener
import com.qwk.chandrsekhar.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), MovieClickListener{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var searchMovieAdapter: MovieAdapter
    private lateinit var popularLayoutManager: LinearLayoutManager
    private lateinit var searchLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // instantiate
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // popular movie recycler setup
        popularLayoutManager = LinearLayoutManager(requireContext())
        popularMovieAdapter = MovieAdapter(requireContext(), this)
        binding.popularRecycler.layoutManager = popularLayoutManager
        binding.popularRecycler.adapter = popularMovieAdapter

        // search movie recycler setup
        searchLayoutManager = LinearLayoutManager(requireContext())
        searchMovieAdapter = MovieAdapter(requireContext(), this)
        binding.searchResultRecycler.layoutManager = searchLayoutManager
        binding.searchResultRecycler.adapter = searchMovieAdapter

    }

    override fun onResume() {
        super.onResume()

        viewModel.popularMovieData.observe(this) {
            popularMovieAdapter.submitList(it)
        }
        viewModel.popularMovieProgress.observe(this) {
            binding.progressBar.visibility = when (it!!) {
                Progress.LOADING -> View.VISIBLE
                Progress.SUCCESSFUL -> View.GONE
                Progress.FAILED -> View.GONE
            }
            if (it == Progress.FAILED) {
                Toast.makeText(requireContext(), "Check your internet", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.searchMovieData.observe(this) {
            showSearchLayout(true)
            searchMovieAdapter.submitList(it)

        }
        viewModel.searchProgress.observe(this) {
            binding.progressBar.visibility = when (it!!) {
                Progress.LOADING -> View.VISIBLE
                Progress.SUCCESSFUL -> View.GONE
                Progress.FAILED -> View.GONE
            }
            if (it == Progress.FAILED) {
                Toast.makeText(requireContext(), "Check your internet", Toast.LENGTH_LONG).show()
            }
        }

        // for pagination : Listening to Popular movie recycler scrolling
        binding.popularRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = popularLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = popularLayoutManager.itemCount
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.loadNexPopularMoviePage()
                }
            }
        })

        // for pagination : Listening to Search movie recycler scrolling
        binding.searchResultRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisible = searchLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = searchLayoutManager.itemCount
                if (lastVisible == totalItemCount - 1) {
                    viewModel.loadNexSearchMoviePage()
                }
            }
        })

        // search for movies
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text == null || text.isEmpty()) {
                    showSearchLayout(false)
                }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text == null || text.isEmpty()) {
                    showSearchLayout(false)
                }else {
                    viewModel.searchMovie(text)
                }
                return true
            }
        })
    }

    private fun showSearchLayout(show: Boolean) {
        if (show) {
            binding.searchResultTxt.visibility = View.VISIBLE
            binding.searchResultRecycler.animate()
                .alpha(1f)
                .setDuration(300)
                .withEndAction { binding.searchResultRecycler.visibility = View.VISIBLE }
        } else {
            binding.searchResultTxt.visibility = View.GONE
            binding.searchResultRecycler.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction { binding.searchResultRecycler.visibility = View.GONE }
        }
    }
    // click listener callback  from Adapter
    override fun onMovieItemCLickListener(movieId: Int) {
        // swap in details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }
}