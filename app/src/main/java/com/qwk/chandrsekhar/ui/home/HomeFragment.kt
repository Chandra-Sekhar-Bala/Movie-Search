package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qwk.chandrsekhar.adapter.MovieAdapter
import com.qwk.chandrsekhar.adapter.MovieClickListener
import com.qwk.chandrsekhar.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), MovieClickListener {
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
        popularMovieAdapter = MovieAdapter(requireContext(), this, false)
        binding.popularRecycler.layoutManager = popularLayoutManager
        binding.popularRecycler.adapter = popularMovieAdapter

        // search movie recycler setup
        searchLayoutManager = LinearLayoutManager(requireContext())
        searchMovieAdapter = MovieAdapter(requireContext(), this, false)
        binding.searchResultRecycler.layoutManager = searchLayoutManager
        binding.searchResultRecycler.adapter = searchMovieAdapter

    }

    override fun onResume() {
        super.onResume()

        binding.refreshBtn.setOnClickListener {
            viewModel.getPopularMovies()
        }

        viewModel.popularMovieData.observe(this) {
            popularMovieAdapter.submitList(it)
        }
        viewModel.popularMovieProgress.observe(this) {
            when (it!!) {
                Progress.FAILED -> {
                    Toast.makeText(requireContext(), "Check your internet", Toast.LENGTH_LONG)
                        .show()
                    showNoInternetLayout(true)
                    binding.shimmerLayout.visibility = View.GONE
                }
                Progress.SUCCESSFUL -> {
                    showNoInternetLayout(false)
                    binding.shimmerLayout.visibility = View.GONE
                }
                Progress.LOADING -> {
                    showNoInternetLayout(false)
                    binding.shimmerLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.searchMovieData.observe(this) {
            showSearchLayout(true)
            searchMovieAdapter.submitList(it)

        }
        viewModel.searchProgress.observe(this) {
            when (it!!) {
                Progress.FAILED -> {
                    Toast.makeText(requireContext(), "Check your internet", Toast.LENGTH_LONG)
                        .show()
                    showNoInternetLayout(true)
                    binding.shimmerLayout.visibility = View.GONE
                }
                Progress.SUCCESSFUL -> {
                    showNoInternetLayout(false)
                    binding.shimmerLayout.visibility = View.GONE
                }
                Progress.LOADING -> {
                    showNoInternetLayout(false)
                    binding.shimmerLayout.visibility = View.VISIBLE
                }
            }
        }

        // for pagination : Listening to Popular movie recycler scrolling
        binding.popularRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
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
        binding.searchResultRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
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
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?): Boolean {
                    if (text == null || text.isEmpty()) {
                        showSearchLayout(false)
                    }else {
                        viewModel.searchMovie(text)
                    }
                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    if (text == null || text.isEmpty()) {
                        showSearchLayout(false)
                    } else {
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

    // when no internet then show this
    private fun showNoInternetLayout(show: Boolean) {
        if (show) {
            binding.noInternetLayout.visibility = View.VISIBLE
            binding.popularTxt.visibility = View.GONE
            binding.popularRecycler.visibility = View.GONE
        } else {
            binding.noInternetLayout.visibility = View.GONE
            binding.popularRecycler.visibility = View.VISIBLE
            binding.popularTxt.visibility = View.VISIBLE
        }

    }

    // click listener callback  from Adapter
    override fun onMovieItemCLickListener(movieId: Int) {
        // swap in details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }

    override fun deleteFromDatabase(id: Int) {

    }
}