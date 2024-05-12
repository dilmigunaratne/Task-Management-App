package com.example.doitapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: List<Task>, context: Context): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable{

    private val db: TaskDatabaseHelper = TaskDatabaseHelper(context)
    private var tasksFiltered: List<Task> = tasks

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasksFiltered.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksFiltered[position]
        holder.titleTextView.text = task.title
        holder.priorityTextView.text = task.priority
        holder.contentTextView.text = task.content

        // Set background color based on priority
        val priorityColor = when(task.priority.toLowerCase()) {
            "high" -> R.color.highPriority
            "medium" -> R.color.mediumPriority
            else -> R.color.otherPriority
        }
        holder.itemView.findViewById<CardView>(R.id.cardView).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, priorityColor))

        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateTasksActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener{
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newTasks: List<Task>){
//        tasks = newTasks.sortedByDescending { it.priority }
//        notifyDataSetChanged()
        tasksFiltered = newTasks.sortedWith(compareByDescending<Task> {
            when(it.priority.toLowerCase()){
                "high" -> 3
                "medium" -> 2
                else -> 1
            }
        })
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Task>()
                if (constraint.isNullOrEmpty()){
                    filteredList.addAll(tasks)
                }else{
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (task in tasks){
                        if (task.title.toLowerCase().contains(filterPattern) ||
                            task.content.toLowerCase().contains(filterPattern)){
                            filteredList.add(task)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tasksFiltered = results?.values as List<Task>
                notifyDataSetChanged()
            }

        }
    }
}