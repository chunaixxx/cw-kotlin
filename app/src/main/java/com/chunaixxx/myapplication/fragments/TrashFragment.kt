package com.chunaixxx.myapplication.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.chunaixxx.myapplication.R
import com.chunaixxx.myapplication.databinding.FragmentTrashBinding
import com.chunaixxx.myapplication.adapter.ITrashRVAdapter
import com.chunaixxx.myapplication.adapter.TrashAdapter
import com.chunaixxx.myapplication.helpers.MusicHelper
import com.chunaixxx.myapplication.room.note.Note
import com.chunaixxx.myapplication.room.note.NoteViewModel
import com.chunaixxx.myapplication.room.trash.Trash
import com.chunaixxx.myapplication.room.trash.TrashViewModel


class TrashFragment : Fragment(), ITrashRVAdapter {

    private var _binding: FragmentTrashBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TrashViewModel
    private lateinit var notesViewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrashBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        // настройка скролла
        binding.rvShowTrash.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val adapter = TrashAdapter(requireContext(), this)
        binding.rvShowTrash.adapter = adapter

        // установка viewmodel и слежка livedata
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TrashViewModel::class.java]
        viewModel.allTrashNotes.observe(requireActivity()) { list ->
            list?.let {
                adapter.updateList(it)
            }
            if (adapter.allTrashNotes.isEmpty()) {
                binding.tvNoTrash.visibility = View.VISIBLE
            } else {
                binding.tvNoTrash.visibility = View.INVISIBLE
            }
        }

        notesViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        return binding.root
    }

    override fun onNoteCardClicked(trash: Trash) {
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.ic_delete)
            .setTitle("Удаление/Восстановление")
            .setMessage("Выберите что хотите сделать - удалить или восстановить")
            .setPositiveButton("Восстановить"){_,_->
                notesViewModel.insertNote(
                    Note(
                        id = trash.id,
                        title = trash.title,
                        description = trash.description,
                        date = trash.date,
                        backgroundColor = trash.backgroundColor
                    )
                )
                viewModel.deleteTrash(trash)
            }.setNegativeButton("Удалить"){_,_->
                viewModel.deleteTrash(trash)
                MusicHelper(requireActivity().applicationContext).deleteSound()
            }.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.tvNoTrashText.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white, null
                    )
                )
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                binding.tvNoTrashText.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.black, null
                    )
                )
            }
        }
        super.onConfigurationChanged(newConfig)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.miEmptyTrash)!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuInflater(requireActivity().applicationContext).inflate(R.menu.trash_menu_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miEmptyTrash -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setIcon(R.drawable.ic_delete_forever)
                    .setTitle("Очистить корзину?")
                    .setMessage("Вы действительно хотите очистить корзину?")
                    .setPositiveButton("Да"){_,_->
                        viewModel.allTrashNotes.observe(this@TrashFragment){
                            if(it.isNotEmpty()){
                                viewModel.clearTrash()
                                MusicHelper(requireActivity().applicationContext).deleteSound()
                            }
                        }
                    }.setNegativeButton("Нет"){_,_->

                    }.show()
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.allTrashNotes.removeObservers(requireActivity())
    }

}