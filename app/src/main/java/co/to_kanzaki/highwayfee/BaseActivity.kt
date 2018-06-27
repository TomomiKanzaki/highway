package co.to_kanzaki.highwayfee

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View

open class BaseActivity : Activity() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
        initProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
    }

    override fun onBackPressed() {
        //Backキーを無効化
    }

    private fun hideSystemBars(){

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Now Loading...")
    }

}