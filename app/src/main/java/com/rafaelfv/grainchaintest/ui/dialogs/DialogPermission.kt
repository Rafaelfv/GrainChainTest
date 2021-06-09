package com.rafaelfv.grainchaintest.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.utils.PermissionType
import kotlinx.android.synthetic.main.dialog_permission.*


class DialogPermission(context: Context, private val listener: DialogPermissionType) :
    Dialog(context) {

    lateinit var myPermission: PermissionType

    interface DialogPermissionType {
        fun showAskPermission(permission: PermissionType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setCancelable(true)
        if (savedInstanceState == null) {
            setContentView(R.layout.dialog_permission)
            btn_continue_permission.setOnClickListener {
                listener.showAskPermission(myPermission)
            }
        }
    }

    fun updateText(permission: PermissionType) {
        myPermission = permission
        when (permission) {
            PermissionType.Location -> {
                permissionMessage.text = context.getString(R.string.location_permission)
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