package com.teltronic.app112.screens.chat


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.teltronic.app112.R
import com.teltronic.app112.adapters.MessageListener
import com.teltronic.app112.adapters.MessagesAdapter
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.enums.IntCodes
import com.teltronic.app112.classes.enums.PermissionsApp
import com.teltronic.app112.database.room.chats.ChatWithMessages
import com.teltronic.app112.databinding.FragmentChatBinding
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: MessagesAdapter
    private var job = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )
        //Inicializo el viewModel
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val idChat = args.idChat
        val idUserRoom = args.idUserRoom
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ChatViewModelFactory(application, idChat, idUserRoom)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.chatViewModel = viewModel
        adapter = MessagesAdapter(idUserRoom, MessageListener { idMessage ->
            showModalImage(idMessage)
        })
        binding.rvMessages.adapter = adapter
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha

        configureErrorSendMessageObserver()
        configureClearMessageObserver()

        configureChatObserver()
        configureStartSendMessageObserver()
        configureAskCameraPermissionObserver()
        configureStartCameraObserver()
        configureInitUIObserver()
        //Retorno el binding root (no el inflater)
        return binding.root
    }

    private fun configureChatObserver() {
        viewModel.startChatObserver.observe(
            this as LifecycleOwner,
            Observer { startObserver ->
                if (startObserver) {
                    startChatObserver()
                }
            }
        )
    }

    private fun configureAskCameraPermissionObserver() {
        viewModel.boolAskCameraPermission.observe(
            this as LifecycleOwner,
            Observer { askPermission ->
                if (askPermission) {
                    Phone.askPermission(
                        requireActivity(),
                        PermissionsApp.CAMERA
                    )
                    viewModel.cameraPermissionAsked()
                }
            }
        )
    }

    private fun configureStartCameraObserver() {
        viewModel.boolStartCamera.observe(
            this as LifecycleOwner,
            Observer { startCamera ->
                if (startCamera) {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    requireContext(),
                                    "com.example.android.fileprovider",
                                    it
                                )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(takePictureIntent, IntCodes.CODE_PHOTO.code)
                            }
                        }
                    }
                    viewModel.cameraStarted()
                }
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val storageDir: File? =
            requireActivity().getExternalFilesDir((Environment.DIRECTORY_PICTURES))

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            viewModel.currentPhotoPath = absolutePath
        }
    }

    private fun configureInitUIObserver() {
        viewModel.boolInitUI.observe(
            this as LifecycleOwner,
            Observer { shouldInit ->
                if (shouldInit) {
                    uiScope.launch {
                        viewModel.initIO(requireActivity())
                    }
                }
            }
        )
    }

    //TEXT MESSAGE
    private fun configureStartSendMessageObserver() {
        viewModel.startSendMessage.observe(
            this as LifecycleOwner,
            Observer { shouldSendMessage ->
                if (shouldSendMessage) {
                    val message = binding.etMessage.text.toString()
                    if (message != "") {
                        uiScope.launch {
                            trySendTextMessageIO(message)
                        }
                    }
                    viewModel.textMessageSent()
                }
            }
        )
    }

    private suspend fun trySendTextMessageIO(message: String) {
        withContext(Dispatchers.IO) {
            viewModel.trySendTextMessageIO(message)
        }
    }

    //IMAGE MESSAGE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntCodes.CODE_PHOTO.code && resultCode == Activity.RESULT_OK) {
            //"reinicio" el uiScope"
            job = Job()
            uiScope = CoroutineScope(Dispatchers.Main + job)
            uiScope.launch {
                trySendImageMessageIO()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private suspend fun trySendImageMessageIO() {
        withContext(Dispatchers.IO) {
            viewModel.trySendImageMessageIO()
        }
    }

    private fun startChatObserver() {
        viewModel.chat.observe(
            this as LifecycleOwner,
            Observer { chatWithMessages ->
                uiScope.launch {
                    if (chatWithMessages != null) {
                        updateScreenIO(chatWithMessages)
                    } else {
                        //El chat aún no existe en room (pasa si se eliminan los datos de la app y llega una notificación y se le da tap)
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private suspend fun updateScreenIO(chatWithMessages: ChatWithMessages) {
        withContext(Dispatchers.IO) {
            val messages = chatWithMessages.messages
            viewModel.updateHeader(chatWithMessages.chat)
            adapter.submitMessagesList(messages)
            try {
                binding.rvMessages.smoothScrollToPosition(messages.count() - 1)
            } catch (e: Exception) {
                Timber.i("Exception ${e.message}}")
            }
        }
    }


    private fun configureErrorSendMessageObserver() {
        viewModel.strErrorSendMessage.observe(
            this as LifecycleOwner,
            Observer { strError ->
                if (strError != "") {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        strError,
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.clearStrErrorSendMessage()
                }
            }
        )
    }

    private fun configureClearMessageObserver() {
        viewModel.clearMessage.observe(
            this as LifecycleOwner,
            Observer { shouldClear ->
                if (shouldClear) {
                    binding.etMessage.setText("")
                    viewModel.messageCleared()
                }
            }
        )
    }


    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_right_menu, menu)
    }

    //Navega al main fragment cuando se presiona el icono home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeIconItem -> {
                val actionNavigate = ChatFragmentDirections.actionChatFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }

    private fun showModalImage(idMessage: String) {
        uiScope.launch {
            showModalImageIO(idMessage)
        }
    }

    private suspend fun showModalImageIO(idMessage: String) {
        withContext(Dispatchers.IO) {
            val dialog = AlertDialog.Builder(activity)
            val dialogView = View.inflate(activity, R.layout.dialog_image, null)
            val ivDialog = dialogView.findViewById<ImageView>(R.id.ivDialogPicture)

            val bitmapImage = viewModel.getBitmapImage(idMessage)
            if (bitmapImage != null) {
                ivDialog.setImageBitmap(bitmapImage)
                dialog.setView(dialogView)
                dialog.setCancelable(true)
                activity?.runOnUiThread {
                    dialog.show()
                }
            } else {
                activity?.runOnUiThread {
                    Toast.makeText(activity, R.string.image_not_found, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
