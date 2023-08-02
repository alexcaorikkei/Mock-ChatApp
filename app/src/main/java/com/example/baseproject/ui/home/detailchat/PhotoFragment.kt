package com.example.baseproject.ui.home.detailchat

import android.os.Bundle
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPhotoBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragmentNotRequireViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PhotoFragment : BaseFragmentNotRequireViewModel<FragmentPhotoBinding>(R.layout.fragment_photo) {

    @Inject
    lateinit var appNavigation: AppNavigation
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val photo = arguments?.getString("photo")
        val name = arguments?.getString("name")

        Glide.with(requireContext())
            .load(photo?.toUri())
            .into(binding.imgPhoto)
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnBack.setOnClickListener {
            appNavigation.navigateUp()
        }
    }
}