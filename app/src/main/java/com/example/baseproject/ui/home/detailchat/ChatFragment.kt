package com.example.baseproject.ui.home.detailchat

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailChatBinding
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.*
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment :
    BaseFragment<FragmentDetailChatBinding, ChatViewModel>(R.layout.fragment_detail_chat) {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: ChatViewModel by viewModels()
    override fun getVM() = viewModel

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var chatAdapter: ChatAdapter2
    var photoListClicked = ArrayList<Photo>()

    private lateinit var bottomSheetGalleryBehavior: BottomSheetBehavior<View>

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEdittextUsableWhenFullScreen()
        setPermission()
        setRecyclerViewChat()
        binding.edtChat.validate { textInputUser ->
            if (textInputUser.isNullOrBlank()) {
                binding.ivSendChat.visibility = View.GONE
            } else {
                binding.ivSendChat.visibility = View.VISIBLE
            }
        }

        bottomSheetGalleryBehavior = BottomSheetBehavior.from(binding.clBottomSheet)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val peekHeight = displayMetrics.heightPixels * 2 / 5

        bottomSheetGalleryBehavior.peekHeight = peekHeight
    }

    override fun bindingAction() {
        super.bindingAction()
        binding.ivGalleryBottomSheet.setOnClickListener {
            bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.coordinatorLayoutGallery.visibility = View.GONE
            binding.clChatSendBottomSheet.visibility = View.GONE
            binding.clChatSend.visibility = View.VISIBLE
        }

        binding.ivGallery.setOnClickListener {
            photoListClicked.clear()
            openGallery()
            bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetGalleryBehavior.isHideable = false
            binding.coordinatorLayoutGallery.visibility = View.VISIBLE
            binding.clChatSendBottomSheet.visibility = View.VISIBLE
            binding.clChatSend.visibility = View.GONE
        }

        binding.ivBack.setOnClickListener {
            appNavigation.navigateUp()
        }

        binding.ivSendChatBottomSheet.setOnClickListener {
            binding.coordinatorLayoutGallery.visibility = View.GONE
            binding.clChatSendBottomSheet.visibility = View.GONE
            binding.clChatSend.visibility = View.VISIBLE
            bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getListPhotoClicked()
            }
        }

        binding.ivSendChat.setOnClickListener {
            viewModel.sendMessage(
                getTextSend(),
                "OVC9HAzZmFPmHrfYi7IZNExg8Us2"
            )
        }
    }

    private fun setEdittextUsableWhenFullScreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.viewChatParent) { v, insets ->
            val animator =
                ValueAnimator.ofInt(0, insets.getInsets(WindowInsetsCompat.Type.ime()).bottom)
            animator.addUpdateListener { valueAnimator ->
                v.setPadding(0, 0, 0, valueAnimator.animatedValue as? Int ?: 0)
            }
            animator.duration = 200
            animator.start()
            insets
        }
    }

    private fun getListPhotoClicked() {
        val photoList: ArrayList<Uri> = ArrayList()
        if (photoListClicked.size > 1) {
            photoListClicked.forEach {
                photoList.add(it.uri)
            }
            val myChat = Chat(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                null,
                null,
                SEND_PHOTOS,
                getTimeCurrent(),
                null,
                photoList
            )
            viewModel.sendPhoto(myChat, "OVC9HAzZmFPmHrfYi7IZNExg8Us2")

        } else if (photoListClicked.size == 1) {
            photoList.add(photoListClicked[0].uri)
            val myChat = Chat(
                FirebaseDatabase.getInstance().reference.push().key.toString(),
                null,
                SEND_PHOTOS,
                null,
                getTimeCurrent(),
                null,
                photoList
            )
            viewModel.sendPhoto(myChat, "OVC9HAzZmFPmHrfYi7IZNExg8Us2")

        }
    }

    private fun setRecyclerViewChat() {
        binding.rvChat.apply {
            chatAdapter = ChatAdapter2()
            adapter = chatAdapter
        }
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openGallery() {
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvPhotoList.layoutManager = layoutManager

        val divider = GridItemSpacingDecoration(convertDpToPixel(requireActivity(), 1), 3)
        binding.rvPhotoList.addItemDecoration(divider)

        val gallery = getAllImagesFromDevice(requireContext())
        val photoList = ArrayList<Photo>()
        for (photo in gallery) {
            photoList.add(Photo(photo, false))
        }
        photoAdapter = PhotoAdapter(requireActivity(), photoList)
        binding.rvPhotoList.adapter = photoAdapter
        photoAdapter.notifyDataSetChanged()

        photoAdapter.onClickListener = object : OnPhotoAdapterListener {
            override fun pickPhoto(position: Int) {
                val photo = photoList[position]
                val checkPhotoClicked = photo.isClicked
                if (checkPhotoClicked) {
                    photo.isClicked = false
                    photoListClicked.remove(photo)
                }
                if (!checkPhotoClicked) {
                    photo.isClicked = true
                    photoListClicked.add(photo)
                }
                photoAdapter.notifyItemChanged(position)
                if (photoListClicked.size > 0) binding.ivSendChatBottomSheet.visibility =
                    View.VISIBLE
                else binding.ivSendChatBottomSheet.visibility = View.GONE
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.apply {
            sendChatResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                    }

                    is Response.Success -> {
                    }

                    is Response.Failure -> {
                        response.e.toString().toast(requireContext())
                    }
                }
            }
        }

        viewModel.messageListLiveData.observe(viewLifecycleOwner) {
            chatAdapter.submitList(it.toMutableList())
            binding.rvChat.smoothScrollToPosition(it.size)
        }
    }

    private fun getAllImagesFromDevice(context: Context): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val selection: String? = null
        val selectionArgs: Array<String>? = null

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageUris.add(imageUri)
            }
        }
        return imageUris
    }

    private fun getTextSend(): Chat {
        val textSendUser = binding.edtChat.text?.trim().toString()
        binding.edtChat.text?.clear()
        return Chat(
            FirebaseDatabase.getInstance().reference.push().key.toString(),
            SEND_TEXT,
            null,
            null,
            getTimeCurrent(),
            textSendUser
        )
    }
}