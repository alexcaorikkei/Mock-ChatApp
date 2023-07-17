package com.example.baseproject.ui.home.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentBottomSheetBinding
import com.example.baseproject.extension.toFile
import com.example.baseproject.navigation.AppNavigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

interface BottomSheetListener {
    fun onGetImageSuccess(uri: Uri)
}

@AndroidEntryPoint
class BottomSheetFragment(private val listener: BottomSheetListener) : BottomSheetDialogFragment() {
    @Inject
    lateinit var appNavigation: AppNavigation
    lateinit var binding: FragmentBottomSheetBinding
    override fun getTheme() = R.style.BottomSheetDialogTheme

    private lateinit var getCameraResult: ActivityResultLauncher<Intent>
    private lateinit var getGalleryResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        getCameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val bitmap =  result.data?.extras?.get("data") as Bitmap
                listener.onGetImageSuccess(bitmap.toFile().toUri())
            }
            dismiss()
        }
        getGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imgUri = data?.data
                if(imgUri != null) {
                    listener.onGetImageSuccess(imgUri)
                }
            }
            dismiss()
        }
        return inflater.inflate(R.layout.fragment_bottom_sheet,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.tvTakeAPhoto.setOnClickListener {
            getCameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        binding.tvChooseFromGallery.setOnClickListener {
            getGalleryResult.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }
}