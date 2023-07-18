package com.example.baseproject.ui.home.detailchat

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailChatBinding
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
    BaseFragment<FragmentDetailChatBinding, ChatViewModel>(R.layout.fragment_detail_chat) {
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
    private var emojiListClicked = ArrayList<Emoji>()

    private var isOpenGallery = true
    private var isOpenEmoji = true

    private var uidReceiver = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        uidReceiver = arguments?.getString(KEY_ID_RECEIVER).toString()

        setEdittextUsableWhenFullScreen()
        setRecyclerViewChat()
        listenEdittextChat()
        setUpBottomSheetGalleryAndEmoji()
    }

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
                if (isOpenGallery) openGallery() else closeGallery()
            }

            ivSendGallery.setOnClickListener {
                bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                coordinatorLayoutGallery.gone()
                ivSendGallery.gone()
                edtChat.isEnabled = true
                getListPhotoClicked()
            }

            ivEmoji.setOnClickListener {
                hideKeyboard()
                ivEmoji.setImageResource(R.drawable.ic_smile_clicked)

                if (!isOpenGallery) closeGallery()
                if (isOpenEmoji) openEmoji() else closeEmoji()
            }

            ivSendEmoji.setOnClickListener {
                bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                coordinatorLayoutEmoji.gone()
                coordinatorLayoutGallery.gone()
                ivSendEmoji.gone()
                edtChat.isEnabled = true
                getListEmojiClicked()
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

            sendMessageResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                    }

                    is Response.Success -> {
                    }

                    is Response.Failure -> {
                        "Error Message: ${response.e}".toast(requireContext())
                    }
                }
            }
            sendEmojiResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                    }

                    is Response.Success -> {
                    }

                    is Response.Failure -> {
                        "Error Emoji: ${response.e}".toast(requireContext())
                    }
                }
            }
            sendPhotoResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                    }

                    is Response.Success -> {
                    }

                    is Response.Failure -> {
                        "Error Photo: ${response.e}".toast(requireContext())
                    }
                }
            }

            messageListLiveData.observe(viewLifecycleOwner) {
                chatAdapter.submitList(it.toMutableList())
                binding.rvChat.smoothScrollToPosition(it.size)
            }
        }
    }

    private fun getListPhotoClicked() {
        viewModel.sendPhoto(photoListClicked[0].uri, uidReceiver)
        binding.ivSendChat.gone()
        binding.ivGallery.setImageResource(R.drawable.ic_gallery_no_click)
        isOpenGallery = !isOpenGallery
    }

    private fun getListEmojiClicked() {
        viewModel.sendEmoji(emojiListClicked[0].content.toString(), uidReceiver)
        binding.ivSendChat.gone()
        binding.ivEmoji.setImageResource(R.drawable.ic_smile_no_click)
        isOpenEmoji = !isOpenEmoji
    }

    private fun closeEmoji() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.apply {
            coordinatorLayoutEmoji.gone()
            coordinatorLayoutGallery.gone()
            ivSendEmoji.gone()
            ivSendChat.gone()
            edtChat.isEnabled = true
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
        emojiListClicked.clear()

        binding.apply {
            val layoutManager = GridLayoutManager(requireActivity(), 3)
            rvEmojiList.layoutManager = layoutManager

            val divider = GridItemSpacingDecoration(convertDpToPixel(requireActivity(), 1), 3)
            rvEmojiList.addItemDecoration(divider)
            val emojiObjectList = ArrayList<Emoji>()

            for (i in 1..10) {
                emojiObjectList.add(Emoji(i, false))
            }
            emojiAdapter = EmojiAdapter(emojiObjectList)
            rvEmojiList.adapter = emojiAdapter
            emojiAdapter.notifyDataSetChanged()

            emojiAdapter.onClickListener = object : OnPhotoAdapterListener {
                override fun pickPhoto(position: Int) {
                    val emoji = emojiObjectList[position]
                    val isClicked = emoji.isClicked
                    if (isClicked) {
                        emoji.isClicked = false
                        emojiListClicked.remove(emoji)
                    }
                    if (!isClicked) {
                        emoji.isClicked = true
                        emojiListClicked.add(emoji)
                    }
                    emojiAdapter.notifyItemChanged(position)
                    if (emojiListClicked.size > 0) {
                        ivSendEmoji.visible()
                        ivSendChat.invisible()
                    } else {
                        ivSendEmoji.gone()
                        ivSendChat.gone()
                    }
                }
            }
        }
    }

    private fun closeGallery() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.apply {
            coordinatorLayoutGallery.gone()
            ivSendGallery.gone()
            ivSendChat.gone()
            edtChat.isEnabled = true
            ivGallery.setImageResource(R.drawable.ic_gallery_no_click)
        }
        isOpenGallery = !isOpenGallery
    }

    private fun openGallery() {
        checkPermission()
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetGalleryBehavior.isHideable = false
        binding.apply {
            coordinatorLayoutGallery.visible()
            ivSendChat.gone()
            edtChat.isEnabled = false
        }
        isOpenGallery = !isOpenGallery
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
            loadGallery()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadGallery() {
        photoListClicked.clear()
        binding.apply {
            val layoutManager = GridLayoutManager(requireActivity(), 3)
            rvGallery.layoutManager = layoutManager

            val divider = GridItemSpacingDecoration(convertDpToPixel(requireActivity(), 1), 3)
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
                if (textInputUser.isNullOrBlank()) {
                    ivSendChat.gone()
                } else {
                    ivSendChat.visible()
                }
            }
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

    private fun setRecyclerViewChat() {
        binding.rvChat.apply {
            chatAdapter = ChatAdapter2()
            adapter = chatAdapter
        }
    }
}