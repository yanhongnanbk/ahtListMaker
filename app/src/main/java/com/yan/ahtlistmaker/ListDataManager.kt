package com.yan.ahtlistmaker

import android.content.Context
import androidx.preference.PreferenceManager

class ListDataManager(private val context: Context) {
    fun saveList(task: TaskList) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context).edit()
// 2
        sharedPreferences.putStringSet(task.name, task.tasks.toHashSet())
// 3
        sharedPreferences.apply()
    }

    fun readLists(): ArrayList<TaskList> {
// 1    Grab reference to the default SharedPref file. Dont need .Editor coz Read not write
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
// 2    Get the content of the SharedPref file as a map
        val sharedPreferenceContents = sharedPreferences.all
// 3    Create an empty list to store value from SharedPref
        val taskLists = ArrayList<TaskList>()
// 4    Iterate over the items in the map to add item to TaskList
        for (taskList in sharedPreferenceContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val item = TaskList(taskList.key, itemsHashSet)
// 5        Add new item to the task List
            taskLists.add(item)
        }
//6     return the List of the task
        return taskLists
    }
}