package com.rafaelfv.grainchaintest.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.rafaelfv.grainchaintest.R
import kotlinx.android.synthetic.main.dialog_route_name.*

class DialogNameRoute(context: Context, val listener: DialogEvent) : Dialog(context) {


    interface DialogEvent {
        fun saveRoute(name: String)
        fun cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setCancelable(false)
        if (savedInstanceState == null) {
            setContentView(R.layout.dialog_route_name)
            btn_save.setOnClickListener {
                if (noteText.text.isNotEmpty()) {
                    listener.saveRoute(noteText.text.toString())
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.name_cart_needed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btn_cancel.setOnClickListener {
                listener.cancel()
            }
        }
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }

}