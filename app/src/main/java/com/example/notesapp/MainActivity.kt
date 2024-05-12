package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.TaskAdaptor
import com.example.notesapp.Database.TaskDatabase
import com.example.notesapp.Models.Task
import com.example.notesapp.Models.TaskViewModel
import com.example.notesapp.databinding.MainBinding

class MainActivity : AppCompatActivity(), TaskAdaptor.NotesClickListener , PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: MainBinding
    private lateinit var database: TaskDatabase
    lateinit var viewModel: TaskViewModel
    lateinit var adapter: TaskAdaptor
    lateinit var selectedTask: Task

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode==Activity.RESULT_OK){
            val task = result.data?.getSerializableExtra("note") as? Task
            if(task != null){
                viewModel.updateNote(task)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()


        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(TaskViewModel::class.java)


        viewModel.allnotes.observe(this) { list ->

            list?.let {

                adapter.updateList(list)

            }
        }

        database = TaskDatabase.getDatabase(this)


    }


    private fun initUI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager  = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = TaskAdaptor(this,this)
        binding.recyclerView.adapter = adapter

        val getContent  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == Activity.RESULT_OK){
                val task = result.data?.getSerializableExtra("note") as? Task
                if(task!=null){
                    viewModel.insertNote(task)
                }
            }
        }


        binding.FABadd.setOnClickListener{
            val intent = Intent(this,AddTask::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!=null){
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(task: Task) {
        val intent = Intent(this,AddTask::class.java)
        intent.putExtra("current_note",task)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(task: Task, cardView: CardView) {
        selectedTask = task
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.delete_popup)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.delete_note){
            viewModel.deleteNote(selectedTask)
            return true
        }
        return false
    }

}