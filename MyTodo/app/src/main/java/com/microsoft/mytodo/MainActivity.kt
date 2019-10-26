package com.microsoft.mytodo

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.*


class MainActivity : AppCompatActivity() {

    private var todoList = ArrayList<TodoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadTodoItems()

        content_listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + position, Toast.LENGTH_SHORT).show()
        }
        fab.setOnClickListener {
            var dbManager = TodoDatabase(this)

            var values = ContentValues()
            values.put("Title", "Buy Surface")//edtTitle.text.toString())
            values.put("Content", "with pen")//edtContent.text.toString())

            //if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully!", Toast.LENGTH_LONG).show()
                    //finish()
                } else {
                    Toast.makeText(this, "Fail to add note!", Toast.LENGTH_LONG).show()
                }
            //}

            //adapter.updateResults()
            loadTodoItems()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodoItems()
    }

    fun loadTodoItems(){

        var dbManager = TodoDatabase(this)
        val cursor = dbManager.queryAll()

        todoList.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val content = cursor.getString(cursor.getColumnIndex("Content"))

                todoList.add(TodoItem(id, title, content))

            } while (cursor.moveToNext())
        }

        var todoAdapter = TodoListAdapter(this, todoList)
        content_listview.adapter = todoAdapter;
    }

    inner class TodoListAdapter : BaseAdapter {
        private var _context: Context
        private var _todoList = ArrayList<TodoItem>()
        constructor(context: Context, notesList: ArrayList<TodoItem>) : super() {
            _todoList = notesList
            _context = context
        }

        override fun getItem(position: Int): Any {
            return "TEST";
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getCount(): Int {
            return _todoList.size
        }

        fun updateResults() {
            //Triggers the list update
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.todoitem, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var todoItem = todoList[position]

            vh.todoTitle.text = "Title"//todoItem.title
            vh.todoContent.text = "Content"//todoItem.content

            return view
        }
    }

    private class ViewHolder(view: View?) {
        val todoTitle: TextView
        val todoContent: TextView

        init {
            this.todoTitle = view?.findViewById(R.id.todoTitle) as TextView
            this.todoContent = view?.findViewById(R.id.todoContent) as TextView
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
