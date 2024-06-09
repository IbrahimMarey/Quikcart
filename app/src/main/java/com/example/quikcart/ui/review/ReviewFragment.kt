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
        adapter.submitList(reviewList)}
    private fun shoesReview() :List<Review> {
        return listOf(
            Review("Great product!", 4.5f),
            Review("Good value for money.", 4.0f),
            Review("Good quality.", 5.0f)
        ).also { reviewList = it }

    }
    private fun shirtReview():List<Review> {
        return listOf(
            Review("Good product!", 4.5f),
            Review("Good value for money.", 4.0f),
            Review("Average quality.", 3.0f)
        ).also { reviewList = it }


    }
    private fun accessoriesReview():List<Review> {
      return listOf(
          Review("Great product!", 4.5f),
          Review("Good value for money.", 3.0f),
          Review("Average quality.", 3.5f)
      ).also { reviewList = it }
    }

}