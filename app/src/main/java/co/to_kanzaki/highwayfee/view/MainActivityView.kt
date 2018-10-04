package co.to_kanzaki.highwayfee.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import co.to_kanzaki.highwayfee.BaseActivity
import co.to_kanzaki.highwayfee.CustomApplication
import co.to_kanzaki.highwayfee.R
import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import co.to_kanzaki.highwayfee.presenter.MainActivityPresenter
import kotlinx.android.synthetic.main.activity_main_view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import retrofit2.http.Url
import javax.inject.Inject

interface MainActivityView {

    fun showProgress()
    fun hideProgress()
    fun showToast(toastMessage: String)
    fun goNextView(depart: String, destination: String)

    class MainActivityActivity : BaseActivity(), MainActivityView, View.OnClickListener {

        @Inject
        lateinit var presenter: MainActivityPresenter
        @Inject
        lateinit var defaultPrefs: ApplicationModule.DefaultPrefsWrapper

        lateinit var listOfCarType: MutableList<Int>
        lateinit var listOfSortType: MutableList<Int>
        var beforeCarType = 0
        var beforeSortType = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main_view)

            //Injection
            (application as CustomApplication).component.plus(ActivityModule()).inject(this)


            this.initView()

            presenter.takeView(this)
        }

        private fun initView(){

            listOfCarType = mutableListOf(tv_cartype1.id,tv_cartype2.id,tv_cartype3.id,tv_cartype4.id,tv_cartype5.id)
            listOfSortType = mutableListOf(tv_sort1.id,tv_sort2.id,tv_sort3.id)
            beforeCarType = listOfCarType[0]
            beforeSortType = listOfSortType[0]

            tv_cartype1.setOnClickListener(this)
            tv_cartype2.setOnClickListener(this)
            tv_cartype3.setOnClickListener(this)
            tv_cartype4.setOnClickListener(this)
            tv_cartype5.setOnClickListener(this)

            tv_sort1.setOnClickListener(this)
            tv_sort2.setOnClickListener(this)
            tv_sort3.setOnClickListener(this)

            search.setOnClickListener { _ ->
                if (et_depart.text.isEmpty() || et_destination.text.isEmpty()){
                    toast("出発ICまたは目的ICを入力してください。")
                    return@setOnClickListener
                }
                val depart = et_depart.text.toString()
                val destination = et_destination.text.toString()
                var carType = findViewById<TextView>(beforeCarType).text.toString()
                if (carType == "軽自動車")carType = "軽自動車等"
                val sortType = findViewById<TextView>(beforeSortType).text.toString()

                presenter.onClickBtn(depart, destination, carType, null, sortType)
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
        override fun onClick(view: View?) {
            view?:return

            val selectedView = findViewById<TextView>(view.id)

            if (selectedView.id == tv_sort1.id || selectedView.id == tv_sort2.id || selectedView.id == tv_sort3.id){
                Log.i("selected ", "sorttype")
                //一つ前に選択されたviewの色を戻す
                val beforeView = findViewById<TextView>(beforeSortType)
                when {
                    beforeView.id == tv_sort2.id -> beforeView.background = getDrawable(R.drawable.radius_none_stroke_top_bottom)
                    beforeView.id == tv_sort1.id -> beforeView.background = getDrawable(R.drawable.radius_7_left)
                    else -> beforeView.background = getDrawable(R.drawable.radius_7_right)
                }
                beforeView.setTextColor(getColor(R.color.grey))
                beforeSortType = selectedView.id
                //今回選択されたviewの色を変更
                when {
                    selectedView.id == tv_sort2.id -> selectedView.background = getDrawable(R.color.colorAccent)
                    selectedView.id == tv_sort1.id -> selectedView.background = getDrawable(R.drawable.radius_7_left_solid_accent)
                    else -> selectedView.background = getDrawable(R.drawable.radius_7_right_solid_accent)
                }
                selectedView.setTextColor(getColor(R.color.white))
            } else {
                Log.i("selected ", "cartype")
                //一つ前に選択されたviewの色を戻す
                val beforeView = findViewById<TextView>(beforeCarType)
                if (beforeView.id == tv_cartype2.id || beforeView.id == tv_cartype3.id || beforeView.id == tv_cartype4.id)
                    beforeView.background = getDrawable(R.drawable.radius_none_stroke_top_bottom)
                else if (beforeView.id == tv_cartype1.id) beforeView.background = getDrawable(R.drawable.radius_7_left)
                else beforeView.background = getDrawable(R.drawable.radius_7_right)
                beforeView.setTextColor(getColor(R.color.grey))
                beforeCarType = view.id
                //今回選択されたviewの色を変更
                if (selectedView.id == tv_cartype2.id || view.id == tv_cartype3.id || view.id == tv_cartype4.id)
                    selectedView.background = getDrawable(R.color.colorAccent)
                else if (selectedView.id == tv_cartype1.id) selectedView.background = getDrawable(R.drawable.radius_7_left_solid_accent)
                else selectedView.background = getDrawable(R.drawable.radius_7_right_solid_accent)
                selectedView.setTextColor(getColor(R.color.white))
            }

        }
        override fun goNextView(depart: String, destination: String) {
            defaultPrefs.prefs.depart = depart
            defaultPrefs.prefs.destination = destination
            startActivity<RouteResultView.RouteResultActivity>()
        }

    }
}