package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.adapter.MovieAdapter
import com.qwk.chandrsekhar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popularTxt.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        // instantiate
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        adapter = MovieAdapter(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecycler.layoutManager = layoutManager
        binding.popularRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.movieData.observe(this) {
            adapter.submitList(it)
        }
        viewModel.progress.observe(this) {
            binding.progressBar.visibility = when (it!!) {
                Progress.LOADING -> View.VISIBLE
                Progress.SUCCESSFUL -> View.GONE
                Progress.FAILED -> View.GONE
            }
            if (it == Progress.FAILED) {
                Toast.makeText(requireContext(), "Check your internet", Toast.LENGTH_LONG).show()
            }
        }

        // for pagination : Listening to recyclerview scrolling
        binding.popularRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
    }
}