package com.microsoft.mytodo

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
    private var adapter: MyCustomAdapter = MyCustomAdapter(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadTodoItems()

        content_listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + position, Toast.LENGTH_SHORT).show()
        }
        fab.setOnClickListener {
            adapter.updateResults()
        }
    }

    fun loadTodoItems(){
        content_listview.adapter = adapter;
    }

    inner class MyCustomAdapter(context: Context) : BaseAdapter() {
        private val _context: Context;
        init {
            _context = context;
        }
        public var rowCount: Int = 4;

        override fun getItem(position: Int): Any {
            return "TEST";
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getCount(): Int {
            return rowCount;
        }

        fun updateResults() {
            rowCount ++;
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

            //var mNote = todoList[position]

            vh.todoTitle.text = "Title"//mNote.title
            vh.todoContent.text = "Content"//.content

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
