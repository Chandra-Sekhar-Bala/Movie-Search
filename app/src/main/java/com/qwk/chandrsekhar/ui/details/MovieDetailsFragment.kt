package com.qwk.chandrsekhar.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.qwk.chandrsekhar.Constants
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.databinding.FragmentMovieDetailsBinding
import com.qwk.chandrsekhar.model.MovieDetails
import com.qwk.chandrsekhar.repository.database.DatabaseProvider


class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
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
        val dao = DatabaseProvider.getDatabase(requireContext()).movieDAO()
        val viewModelFactory = MovieDetailsViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieDetailsViewModel::class.java]
        viewModel.getMovieDetails(args.movieId)
    }

    override fun onResume() {
        super.onResume()

        // get the details and setup UI
        viewModel.movieDetails.observe(this) {
            setupUI(it!!)
        }

        // if the movie is saved or not?
        viewModel.ifMovieSaved.observe(this) {
            binding.saveAsFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), when (it) {
                        true -> R.drawable.favorite_icon_filled
                        false -> R.drawable.favorite_icon
                    }
                )
            )
        }
        // save this as favorite
        binding.saveAsFavorite.setOnClickListener {
            viewModel.saveMovie()
        }
    }

    private fun setupUI(item: MovieDetails) {
        binding.movieTitle.text = item.title
        binding.tagLine.text = item.tagline
        binding.movieDescription.text = item.about
        binding.movieRating.text = requireActivity().getString(R.string.imdb_rating, item.rating.toString())

        // to set background
        Glide.with(requireActivity())
            .load(Constants.posterPrefix + item.poster_img)
            .centerCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.posterImage.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        Glide.with(requireActivity())
            .load(Constants.posterPrefix + item.poster_img)
            .placeholder(R.drawable.movie_icon)
            .fitCenter()
            .into(binding.posterImage)

        binding.tag1Movie.text = item.genres[0].name
        binding.tag2Movie.text = item.genres[1].name
        binding.tag3Movie.text = item.genres[2].name
    }
}