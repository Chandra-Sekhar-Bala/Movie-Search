package com.qwk.chandrsekhar.ui.favorite

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qwk.chandrsekhar.adapter.MovieAdapter
import com.qwk.chandrsekhar.adapter.MovieClickListener
import com.qwk.chandrsekhar.databinding.FragmentFavoriteBinding
import com.qwk.chandrsekhar.repository.database.DatabaseProvider

class FavoriteFragment : Fragment(), MovieClickListener{
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = DatabaseProvider.getDatabase(requireContext()).movieDAO()
        val factory = FavoriteViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MovieAdapter(requireContext(), this,true)
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllMovies()
        viewModel.movies.observe(this){
            handleViewShowing(it.size)
            adapter.submitList(it)
        }

        binding.gotHomeBtn.setOnClickListener{
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun handleViewShowing(size: Int) {
        if(size > 0){
            binding.noSavedMovie.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.savedTxt.visibility = View.VISIBLE
        }else{
            binding.noSavedMovie.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.savedTxt.visibility = View.GONE
        }
    }

    override fun onMovieItemCLickListener(movieId: Int) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)

    }

    override fun deleteFromDatabase(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove from favorite")
            .setMessage("Do you really want to delete this movie from favorite")
            .setPositiveButton("Yes"){_, _ ->
                viewModel.deleteFromDatabase(id)
            }.setNegativeButton("No"){_,_->}
            .show()
    }

}