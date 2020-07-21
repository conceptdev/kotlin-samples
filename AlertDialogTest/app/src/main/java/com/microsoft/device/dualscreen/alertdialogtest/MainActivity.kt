package com.microsoft.device.dualscreen.alertdialogtest

//import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()


            val configuration: Configuration = resources.configuration
            configuration.orientation = Configuration.ORIENTATION_PORTRAIT
            resources.updateConfiguration(configuration, resources.displayMetrics)
            val alertDialog =
                AlertDialog.Builder(this) //set icon
                    .setIcon(android.R.drawable.ic_dialog_alert) //set title
                    .setTitle("Are you sure to Exit") //set message
                    .setMessage("Exiting will call finish() method") //set positive button
                    .setPositiveButton(
                        "Yes"
                    ) { dialogInterface, i -> //set what would happen when positive button is clicked
                        finish()
                    } //set negative button
                    .setNegativeButton(
                        "No"
                    ) { dialogInterface, i -> //set what should happen when negative button is clicked
                        Toast.makeText(
                            applicationContext,
                            "Nothing Happened",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .show()

            // http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog.html
//            var input = EditText(this);
//            AlertDialog.Builder(this@MainActivity)
//                    .setTitle("Update Status")
//                    .setMessage("message")
//                    .setView(input)
//                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, whichButton -> val value: Editable = input.getText() }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
//                        // Do nothing.
//                    }).show()

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