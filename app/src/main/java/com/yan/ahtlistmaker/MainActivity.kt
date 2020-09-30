package com.yan.ahtlistmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {
    lateinit var listsRecyclerView: RecyclerView
    val listDataManager: ListDataManager = ListDataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            showCreateListDialog()

        }

        // RecyclerView

        // 1
        val lists = listDataManager.readLists()
        listsRecyclerView = findViewById<RecyclerView>(R.id.lists_recyclerview)
        listsRecyclerView.layoutManager = LinearLayoutManager(this)
        // 2
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun showCreateListDialog() {

// 1
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)
// 2
        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)
// 3
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val item = TaskList(listTitleEditText.text.toString())
            listDataManager.saveList(item)
            val recyclerAdapter = listsRecyclerView.adapter as
                    ListSelectionRecyclerViewAdapter
            recyclerAdapter.addList(item)
            dialog.dismiss()
            showListDetail(item)
        }
// 4
        builder.create().show()
    }

    private fun showListDetail(list: TaskList) {
        val listDetailIntent = Intent(
            this,
            ListDetailActivity::class.java
        )
        listDetailIntent.putExtra(INTENT_LIST_KEY, list)
        startActivityForResult(
            listDetailIntent,
            LIST_DETAIL_REQUEST_CODE
        )
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun listItemClicked(list: TaskList) {
        showListDetail(list)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data:
        Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
// 1
        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode ==
            Activity.RESULT_OK
        ) {
// 2
            data?.let {
// 3
                listDataManager.saveList(data.getParcelableExtra<TaskList>(INTENT_LIST_KEY) as TaskList)
                updateLists()
            }
        }
    }
    private fun updateLists() {
        val lists = listDataManager.readLists()
        listsRecyclerView.adapter =
            ListSelectionRecyclerViewAdapter(lists, this)
    }

//    override fun onBackPressed() {
//        val bundle = Bundle()
//        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)
//        val intent = Intent()
//        intent.putExtras(bundle)
//        setResult(Activity.RESULT_OK, intent)
//        super.onBackPressed()
//    }
}
