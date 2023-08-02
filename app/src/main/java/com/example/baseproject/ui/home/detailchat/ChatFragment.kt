package com.example.baseproject.ui.home.detailchat

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailChatBinding
import com.example.baseproject.domain.model.ChatModel
import com.example.baseproject.domain.model.Response
import com.example.baseproject.extension.*
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment :
    BaseFragment<FragmentDetailChatBinding, ChatViewModel>(R.layout.fragment_detail_chat),
    OnPhotoClickListener {
    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: ChatViewModel by viewModels()
    override fun getVM() = viewModel

    private lateinit var bottomSheetGalleryBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetEmojiBehavior: BottomSheetBehavior<View>

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var emojiAdapter: EmojiAdapter
    private lateinit var chatAdapter: ChatAdapter2

    private var photoListClicked = ArrayList<Photo>()

    private var isOpenGallery = true
    private var isOpenEmoji = true

    private var uidReceiver = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        if(!viewModel.canOpen()) {
            appNavigation.openChatToLoginScreen()
        }

        uidReceiver = arguments?.getString(KEY_ID_RECEIVER).toString()

        binding.viewChatParent.setEdittextUsableWhenFullScreen()
        setRecyclerViewChat()
        listenEdittextChat()
        setUpBottomSheetGalleryAndEmoji()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setOnClick() {
        super.setOnClick()

        binding.apply {
            ivBack.setOnClickListener {
                appNavigation.navigateUp()
            }

            ivGallery.setOnClickListener {
                coordinatorLayoutEmoji.gone()
                hideKeyboard()
                ivGallery.setImageResource(R.drawable.ic_gallery_clicked)

                if (!isOpenEmoji) closeEmoji()
                if (isOpenGallery) checkPermission() else closeGallery()
            }

            ivSendGallery.setOnClickListener {
                bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                coordinatorLayoutGallery.gone()
                ivSendGallery.gone()
                edtChat.isEnabled = true
                sendPhotoClicked()
            }

            ivEmoji.setOnClickListener {
                hideKeyboard()
                ivEmoji.setImageResource(R.drawable.ic_smile_clicked)
                if (!isOpenGallery) closeGallery()
                if (isOpenEmoji) openEmoji() else closeEmoji()
            }

            ivSendChat.setOnClickListener {
                viewModel.sendMessage(
                    getTextSend(),
                    uidReceiver
                )
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.apply {
            getReceiver(uidReceiver)

            receiver.observe(viewLifecycleOwner) {
                binding.tvNameReceiver.text = it.displayName
                Glide.with(binding.ivAvatarReceiver)
                    .load(it.profilePicture)
                    .circleCrop()
                    .placeholder(R.drawable.ic_avatar_default)
                    .error(R.drawable.ic_avatar_default)
                    .into(binding.ivAvatarReceiver)
            }

            getListMessage(uidReceiver)

            messageListLiveData.observe(viewLifecycleOwner) {
                chatAdapter.submitList(it.toMutableList())
                binding.rvChat.smoothScrollToPosition(it.size)
            }

            sendMessageResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Failure -> {
                        "Error Message: ${response.e}".toast(requireContext())
                    }
                }
            }

            sendEmojiResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Failure -> {
                        "Error Emoji: ${response.e}".toast(requireContext())
                    }
                }
            }

            getReceiverResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Failure -> {
                        "Error Receiver: ${response.e}".toast(requireContext())
                    }
                }
            }

            sendPhotoResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Failure -> {
                        "Error Photo: ${response.e}".toast(requireContext())
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendPhotoClicked() {
        viewModel.sendPhoto(photoListClicked[0].uri, uidReceiver)
        binding.apply {
            edtChat.text?.let {
                if (it.isNotBlank()) ivSendChat.visible() else ivSendChat.gone()
            }
            ivGallery.setImageResource(R.drawable.ic_gallery_no_click)
        }
        isOpenGallery = !isOpenGallery
    }

    private fun closeEmoji() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.apply {
            coordinatorLayoutEmoji.gone()
            coordinatorLayoutGallery.gone()
            edtChat.isEnabled = true
            edtChat.text?.let {
                if (it.isNotBlank()) ivSendChat.visible() else ivSendChat.gone()
            }
            ivEmoji.setImageResource(R.drawable.ic_smile_no_click)
        }
        isOpenEmoji = !isOpenEmoji
    }

    private fun openEmoji() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetGalleryBehavior.isHideable = false
        binding.apply {
            coordinatorLayoutGallery.invisible()
            coordinatorLayoutEmoji.visible()
            ivSendChat.gone()
            edtChat.isEnabled = false
        }
        isOpenEmoji = !isOpenEmoji
        loadEmoji()
    }

    private fun loadEmoji() {
        binding.apply {
            val layoutManager = GridLayoutManager(requireActivity(), 7)
            rvEmojiList.layoutManager = layoutManager

            val divider = GridItemSpacingDecoration(convertDpToPixel(requireActivity(), 3), 7)
            rvEmojiList.addItemDecoration(divider)
            val emojiObjectList = ArrayList<Emoji>()

            for (i in 1..32) {
                emojiObjectList.add(Emoji(content = i))
            }
            emojiAdapter = EmojiAdapter(emojiObjectList)
            rvEmojiList.adapter = emojiAdapter
            emojiAdapter.notifyDataSetChanged()

            emojiAdapter.onClickListener = object : OnEmojiAdapterListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun pickEmoji(position: Int) {
                    isOpenEmoji = !isOpenEmoji

                    bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    coordinatorLayoutEmoji.gone()
                    coordinatorLayoutGallery.gone()
                    ivEmoji.setImageResource(R.drawable.ic_smile_no_click)
                    edtChat.isEnabled = true
                    edtChat.text?.let {
                        if (it.isNotBlank()) ivSendChat.visible() else ivSendChat.gone()
                    }
                    viewModel.sendEmoji(emojiObjectList[position].content.toString(), uidReceiver)
                }
            }
        }
    }

    private fun closeGallery() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.apply {
            coordinatorLayoutGallery.gone()
            ivSendGallery.gone()
            edtChat.isEnabled = true
            edtChat.text?.let {
                if (it.isNotBlank()) ivSendChat.visible() else ivSendChat.gone()
            }
            ivGallery.setImageResource(R.drawable.ic_gallery_no_click)
        }
        isOpenGallery = !isOpenGallery
    }

    private fun openGallery() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetGalleryBehavior.isHideable = false
        binding.apply {
            coordinatorLayoutGallery.visible()
            ivSendChat.gone()
            edtChat.isEnabled = false
        }
        isOpenGallery = !isOpenGallery
        loadGallery()
    }

    private fun checkPermission() {
        if (context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        } else {
            openGallery()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadGallery() {
        photoListClicked.clear()
        binding.apply {
            val layoutManager = GridLayoutManager(requireActivity(), 3)
            rvGallery.layoutManager = layoutManager

            val divider = GridItemSpacingDecoration(convertDpToPixel(requireActivity(), 2), 3)
            rvGallery.addItemDecoration(divider)

            val gallery = getAllImagesFromDevice(requireContext())
            val photoList = ArrayList<Photo>()
            for (photo in gallery) {
                photoList.add(Photo(photo.toString(), false))
            }
            photoAdapter = PhotoAdapter(photoList)
            rvGallery.adapter = photoAdapter
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
                    if (photoListClicked.size > 0) {
                        ivSendGallery.visible()
                        ivSendChat.invisible()
                    } else {
                        ivSendGallery.gone()
                        ivSendChat.gone()
                    }
                }
            }
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

    private fun getTextSend(): String {
        val textSendUser = binding.edtChat.text?.trim().toString()
        binding.edtChat.text?.clear()
        return textSendUser
    }

    private fun setUpBottomSheetGalleryAndEmoji() {
        binding.apply {
            bottomSheetGalleryBehavior = BottomSheetBehavior.from(clBottomSheetGallery)
            bottomSheetEmojiBehavior = BottomSheetBehavior.from(clBottomSheetEmoji)
        }
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val peekHeightBottomSheet = displayMetrics.heightPixels * 2 / 5

        bottomSheetGalleryBehavior.peekHeight = peekHeightBottomSheet
        bottomSheetEmojiBehavior.peekHeight = peekHeightBottomSheet

    }

    private fun listenEdittextChat() {
        binding.apply {
            edtChat.validate { textInputUser ->
                if (textInputUser.isBlank()) ivSendChat.gone() else ivSendChat.visible()
            }
        }
    }


    private fun setRecyclerViewChat() {
        binding.rvChat.apply {
            chatAdapter = ChatAdapter2(this@ChatFragment)
            adapter = chatAdapter
        }
    }

    override fun onPhotoClick(chat: ChatModel) {
        val bundle = Bundle()
        bundle.putString("photo", chat.photo)
        appNavigation.openChatToPhotoFragment(bundle)
    }
}