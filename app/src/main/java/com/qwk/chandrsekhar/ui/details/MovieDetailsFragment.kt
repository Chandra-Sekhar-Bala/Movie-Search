package com.qwk.chandrsekhar.ui.details

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.databinding.FragmentMovieDetailsBinding
import kotlin.properties.Delegates

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private var movieId by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MovieDetailsFragmentArgs by navArgs()
        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        viewModel.getMovieDetails(args.movieId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.movieDetails.observe(this){
            Log.e("TAGTAG", it.toString())
        }
    }
}