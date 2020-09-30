package com.yan.ahtlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListDetailActivity : AppCompatActivity() {
    lateinit var addTaskButton: FloatingActionButton
    lateinit var list: TaskList
    lateinit var listItemsRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

//        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY) as TaskList
        list = intent.getParcelableExtra<TaskList>(MainActivity.INTENT_LIST_KEY) as TaskList
// 2
        title = list.name

        // 1
        listItemsRecyclerView =
            findViewById(R.id.list_items_recyclerview)
// 2
        listItemsRecyclerView.adapter =
            ListItemsRecyclerViewAdapter(list)
// 3
        listItemsRecyclerView.layoutManager = LinearLayoutManager(this)

        addTaskButton = findViewById(R.id.add_task_button)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        
    }

    private fun showCreateTaskDialog() {
//1
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
//2
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
// 3
                val task = taskEditText.text.toString()
                list.tasks.add(task)
// 4
                val recyclerAdapter = listItemsRecyclerView.adapter
                        as ListItemsRecyclerViewAdapter
                recyclerAdapter.notifyItemInserted(list.tasks.size-1)
//5
                dialog.dismiss()
            }
//6
            .create()
            .show()
    }

}

