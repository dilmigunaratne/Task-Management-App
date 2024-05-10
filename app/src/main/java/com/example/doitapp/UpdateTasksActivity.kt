package com.example.doitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.doitapp.databinding.ActivityUpdateTasksBinding

class UpdateTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTasksBinding
    private lateinit var db: TaskDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDatabaseHelper(this)

        taskId = intent.getIntExtra("task_id", -1)
        if (taskId==-1){
            finish()
            return
        }

        val task = db.getTaskByID(taskId)
        binding.updateTitleEditText.setText(task.title)
        binding.updatePriorityEditText.setText(task.priority)
        binding.updateContentEditText.setText(task.content)

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newPriority = binding.updatePriorityEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updatedTask = Task(taskId, newTitle, newPriority, newContent)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this,"Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}