package com.qwk.chandrsekhar.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.adapter.MovieAdapter
import com.qwk.chandrsekhar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popularTxt.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment)
        }

        // instantiate
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        adapter = MovieAdapter(requireContext())
        binding.popularRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecycler.adapter = adapter
        showProgressBar(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.movieData.observe(this){
            adapter.submitList(it)
            showProgressBar(false)
        }
    }

    private fun showProgressBar(show: Boolean){
        binding.progressBar.visibility = if(show) View.VISIBLE else View.GONE
    }
}