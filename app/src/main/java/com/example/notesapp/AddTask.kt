package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.Models.Task
import com.example.notesapp.databinding.AddTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTask : AppCompatActivity() {
    private lateinit var binding: AddTaskBinding

    private lateinit var task: Task
    private lateinit var old_task: Task
    var isUpdate =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            old_task = intent.getSerializableExtra("current_note") as Task
            binding.ETtitle.setText(old_task.title)
            binding.ETnote.setText(old_task.note)
            isUpdate =true
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding.IVcheck.setOnClickListener{
            val title = binding.ETtitle.text.toString()
            val note_desc = binding.ETnote.text.toString()
            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if(isUpdate){
                    task = Task(
                        old_task.id,title,note_desc,formatter.format(Date())
                    )
                }else{
                    task = Task(
                        null,title,note_desc,formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("note",task)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }else{
                Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.IVbackarrow.setOnClickListener { onBackPressed() }
    }
}