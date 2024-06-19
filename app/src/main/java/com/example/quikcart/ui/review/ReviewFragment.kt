package com.example.quikcart.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentReviewBinding
import com.example.quikcart.models.entities.Review

class ReviewFragment : Fragment() {
    private lateinit var reviewList:List<Review>
    private  val adapter = ReviewAdapter()
    private lateinit var binding: FragmentReviewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentReviewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
         binding.ReviewRecyclerView.adapter=adapter
        binding.ReviewRecyclerView.layoutManager=layoutManager
        val productType = arguments?.getString("productType")
        when (productType) {
            "SHOES" -> {
                reviewList = shoesReview()
            }
            "T-SHIRTS" -> {
                reviewList = shirtReview()
            }
            "ACCESSORIES" -> {
                reviewList = accessoriesReview()
            }
        }
        val randomReviews = reviewList.shuffled().take(3)
        adapter.submitList(randomReviews)
    }
    private fun shoesReview(): List<Review> {
        return listOf(
            Review("Super comfortable and stylish!", 4.5f),
            Review("Excellent value, very durable.", 4.0f),
            Review("Top-notch quality, exceeded expectations.", 5.0f),
            Review("Decent for the price, but not the best.", 3.5f),
            Review("A bit tight, but overall good shoes.", 3.0f),
            Review("Not impressed, expected better support.", 2.5f)
        ).also { reviewList = it }
    }

    private fun shirtReview(): List<Review> {
        return listOf(
            Review("Fantastic fit and feel!", 4.5f),
            Review("Good value for the price, would buy again.", 4.0f),
            Review("Quality is okay, not the best but decent.", 3.5f),
            Review("Material feels a bit cheap, but still wearable.", 3.0f),
            Review("Not very satisfied, expected better quality.", 2.5f),
            Review("Poor quality, fabric is uncomfortable.", 1.5f)
        ).also { reviewList = it }
    }

    private fun accessoriesReview(): List<Review> {
        return listOf(
            Review("Excellent build quality and design!", 4.5f),
            Review("Worth the price, highly recommend.", 4.0f),
            Review("Satisfactory performance for daily use.", 3.5f),
            Review("Not as expected, could be better.", 2.5f),
            Review("Poor quality, very disappointed.", 1.5f),
            Review("Terrible experience, do not buy.", 1.0f)
        ).also { reviewList = it }
    }

}