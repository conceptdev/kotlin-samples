package com.microsoft.mytodo

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    private var adapter: MyCustomAdapter = MyCustomAdapter(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val todoList = findViewById<ListView>(R.id.content_listview);

        todoList.adapter = adapter;

        fab.setOnClickListener {
            adapter.updateResults()
        }
    }

    private class MyCustomAdapter(context: Context) : BaseAdapter() {
        private val _context: Context;
        init {
            _context = context;
        }
        public var rowCount: Int = 4;
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val textView = TextView(_context);
            textView.text = "Text in row";
            return textView;
        }

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
