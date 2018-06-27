package co.to_kanzaki.highwayfee.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import co.to_kanzaki.highwayfee.BaseActivity
import co.to_kanzaki.highwayfee.CustomApplication
import co.to_kanzaki.highwayfee.R
import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.presenter.MainActivityPresenter
import kotlinx.android.synthetic.main.activity_main_view.*
import org.jetbrains.anko.toast
import retrofit2.http.Url
import javax.inject.Inject

interface MainActivityView {

    fun showProgress()
    fun hideProgress()
    fun showToast(toastMessage: String)

    class MainActivityActivity : BaseActivity(), MainActivityView {

        @Inject
        lateinit var presenter: MainActivityPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main_view)

            //Injection
            (application as CustomApplication).component.plus(ActivityModule()).inject(this)


            this.initView()

            presenter.takeView(this)
        }

        private fun initView(){

            fab.setOnClickListener { view ->
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
                presenter.onClickBtn("岡山", "広島", "普通車", null, null)
            }

        }

        override fun showProgress() {
            progressDialog.show()
        }

        override fun hideProgress() {
            progressDialog.dismiss()
        }

        override fun showToast(toastMessage: String) {
            toast(toastMessage)
        }
    }

}