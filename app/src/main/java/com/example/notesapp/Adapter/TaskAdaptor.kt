package com.example.notesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Models.Task
import com.example.notesapp.R
import kotlin.random.Random

class TaskAdaptor(private val context: Context, val listener:NotesClickListener) : RecyclerView.Adapter<TaskAdaptor.NoteViewHolder>() {


    private val allTasks= ArrayList<Task>()
    private val tasks= ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_task,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = tasks[position]
        holder.title.text = currentNote.title
        holder.title.isSelected=true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected=true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor()))

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(tasks[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(tasks[holder.adapterPosition],holder.notes_layout)
            true
        }
    }

    fun updateList(newList: List<Task>){
        allTasks.clear()
        allTasks.addAll(newList)

        tasks.clear()
        tasks.addAll(allTasks)

        notifyDataSetChanged()
    }

    fun filterList(search:String){
        tasks.clear()
        for(item in allTasks){
            if(item.title?.lowercase()?.contains(search.lowercase())==true || item.note?.lowercase()?.contains(search.lowercase())==true){
                tasks.add(item)
            }
        }

        notifyDataSetChanged()
    }

    fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.TVtitle)
        val note = itemView.findViewById<TextView>(R.id.TVnote)
        val date = itemView.findViewById<TextView>(R.id.TVdate)


    }

    interface NotesClickListener{
        fun onItemClicked(task:Task)
        fun onLongItemClicked(task:Task, cardView: CardView)
    }

}