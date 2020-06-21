package com.teltronic.app112.screens.notices


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.adapters.NoticeListener
import com.teltronic.app112.adapters.NoticesAdapter
import com.teltronic.app112.database.room.notices.NoticeEntity
import com.teltronic.app112.databinding.FragmentNoticesBinding
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class NoticesFragment : Fragment() {

    private lateinit var binding: FragmentNoticesBinding
    private lateinit var viewModel: NoticesViewModel
    private var job = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notices,
            container,
            false
        )
        //Inicializo el viewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = NoticesViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoticesViewModel::class.java)
        //"Uno" el layout con esta clase por medio del binding
        binding.noticesViewModel = viewModel

        //RecyclerView adapter
        val adapter = NoticesAdapter(NoticeListener { noticeId ->
            uiScope.launch {
                viewModel.readNotice(noticeId)
                viewModel.getModalNotice(noticeId)
            }
        })

        binding.rvNotices.adapter = adapter
        viewModel.notices.observe(this as LifecycleOwner,
            Observer { listNotices ->
                listNotices?.let {
                    adapter.submitList(listNotices)
                }
            })

        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        configureNoticeModalObserver()

        return binding.root
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
                val actionNavigate = NoticesFragmentDirections.actionNoticesFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }

    private fun configureNoticeModalObserver() {
        viewModel.noticeModal.observe(
            this as LifecycleOwner,
            Observer { notice ->
                if (notice != null) {
                    uiScope.launch {
                        showDialogNotice(notice)
                        viewModel.clearNoticeModal()
                    }
                }
            }
        )

    }

    private suspend fun showDialogNotice(notice: NoticeEntity) {
        val dialog = AlertDialog.Builder(activity)
        val dialogView = View.inflate(activity, R.layout.dialog_notice, null)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)

        tvTitle.text = notice.title
        tvMessage.text = notice.message

        if (notice.photo != null) {
            withContext(Dispatchers.IO) {
                val bitmapImage = viewModel.getBitmapImage(notice.id)
                val ivDialog = dialogView.findViewById<ImageView>(R.id.ivImageNotice)
                if (bitmapImage != null) {
                    ivDialog.setImageBitmap(bitmapImage)
                }
            }
        }

        dialog.setView(dialogView)
        dialog.setCancelable(true)
        activity?.runOnUiThread {
            dialog.show()
        }
    }
}
