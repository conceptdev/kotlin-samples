package com.microsoft.device.dualscreen.alertdialogtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 * https://www.dev2qa.com/android-alert-dialog-example/
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

//
//    // Show basic alert dialog.
//    private fun showBasicAlertDialog(textView: TextView, view: View) {
//        val alertDialogButton =
//            view.findViewById(R.id.alertDialogButton) as Button
//        alertDialogButton.setOnClickListener {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this@AlertDialogActivity)
//            //builder.setIcon(R.mipmap.ic_launcher)
//            builder.setTitle("Customized Alert Dialog")
//            builder.setMessage("This is a simple alert dialog.")
//            builder.setPositiveButton("Ok",
//                DialogInterface.OnClickListener { dialogInterface, i ->
//                    textView.text = "You clicked OK button."
//                })
//            builder.setNegativeButton("Cancel",
//                DialogInterface.OnClickListener { dialogInterface, i ->
//                    textView.text = "You clicked Cancel button."
//                })
//            builder.setNeutralButton("Neutral",
//                DialogInterface.OnClickListener { dialogInterface, i ->
//                    textView.text = "You clicked Neutral button."
//                })
//            builder.setCancelable(false)
//            builder.show()
//        }
//    }
//
//    // Show simple list items in alert dialog.
//    private fun showSimpleListItemsInAlertDialog(textView: TextView, view: View) {
//        val alertDialogButton =
//            view.findViewById(R.id.alertDialogSimpleListButton) as Button
//        alertDialogButton.setOnClickListener {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this@AlertDialogActivity)
//            builder.setIcon(R.mipmap.ic_launcher)
//            builder.setTitle("Customized Alert Dialog")
//            val listItemArr =
//                arrayOf("Java", "C++", "Python", "Javascript", "Html", "Android")
//            builder.setItems(listItemArr,
//                DialogInterface.OnClickListener { dialogInterface, itemIndex ->
//                    textView.text = "You choose item " + listItemArr[itemIndex]
//                })
//            builder.setCancelable(true)
//            builder.show()
//        }
//    }
//
//    // Show single choice radio button in alert dialog.
//    private fun showSingleChoiceRadioInAlertDialog(textView: TextView, view: View) {
//        val alertDialogButton =
//            view.findViewById(R.id.alertDialogSingleChoiceButton) as Button
//        alertDialogButton.setOnClickListener(object : View.OnClickListener {
//            private var chooseItem: String? = null
//            override fun onClick(view: View) {
//                val builder: AlertDialog.Builder = AlertDialog.Builder(this@AlertDialogActivity)
//                //builder.setIcon(R.mipmap.ic_launcher)
//                builder.setTitle("Customized Alert Dialog")
//                val listItemArr =
//                    arrayOf("Java", "C++", "Python", "Javascript", "Html", "Android")
//                builder.setSingleChoiceItems(
//                    listItemArr,
//                    0,
//                    DialogInterface.OnClickListener { dialogInterface, itemIndex ->
//                        chooseItem = listItemArr[itemIndex]
//                    })
//                builder.setPositiveButton("Ok",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        textView.text = "You select $chooseItem"
//                    })
//                builder.show()
//            }
//        })
//    }
//
//    // Show multiple choice checkbox in alert dialog.
//    private fun showMultipleCheckboxInAlertDialog(textView: TextView, view: View) {
//        val alertDialogButton =
//            findViewById(R.id.alertDialogMultipleCheckboxButton) as Button
//        alertDialogButton.setOnClickListener(object : View.OnClickListener {
//            // Store user checked checkbox values.
//            private val chooseDataMap: MutableMap<String, String?> =
//                HashMap()
//
//            override fun onClick(view: View) {
//                val builder: AlertDialog.Builder = AlertDialog.Builder(this@AlertDialogActivity)
//                //builder.setIcon(R.mipmap.ic_launcher)
//                builder.setTitle("Customized Alert Dialog")
//                val listItemArr =
//                    arrayOf("Java", "C++", "Python", "Javascript", "Html", "Android")
//                builder.setMultiChoiceItems(
//                    listItemArr,
//                    booleanArrayOf(true, false, false, false, false, false),
//                    OnMultiChoiceClickListener { dialogInterface, itemIndex, checked ->
//                        val checkboxString = listItemArr[itemIndex]
//                        if (checked) {
//                            if (chooseDataMap[checkboxString] == null) {
//                                chooseDataMap[checkboxString] = checkboxString
//                            }
//                        } else {
//                            if (chooseDataMap[checkboxString] != null) {
//                                chooseDataMap.remove(checkboxString)
//                            }
//                        }
//                    })
//                builder.setPositiveButton("Ok",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        textView.text = "You select " + chooseDataMap.keys.toString()
//
//                        // Empty the data map.
//                        var keySet: Set<String> = chooseDataMap.keys
//                        var keySetIt: Iterator<*> = keySet.iterator()
//                        while (keySetIt.hasNext()) {
//                            // Remove one element.
//                            val key = keySetIt.next() as String
//                            chooseDataMap.remove(key)
//
//                            // Recalculate keyset.
//                            keySet = chooseDataMap.keys
//                            keySetIt = keySet.iterator()
//                        }
//                    })
//                builder.show()
//            }
//        })
//    }
}