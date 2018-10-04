package co.to_kanzaki.highwayfee.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import co.to_kanzaki.highwayfee.*
import co.to_kanzaki.highwayfee.R.layout.recycler_adapter
import co.to_kanzaki.highwayfee.R.layout.recycler_adapter_child
import co.to_kanzaki.highwayfee.Recycler.RecyclerAdapter_Child
import co.to_kanzaki.highwayfee.Recycler.RecyclerAdapter_Parent
import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import kotlinx.android.synthetic.main.activity_route_result.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject
import android.content.DialogInterface



interface RouteResultView {
    class RouteResultActivity: BaseActivity(), RouteResultView, View.OnClickListener{

        val TAG = RouteResultActivity::class.java.simpleName

        @Inject
        lateinit var defaultPrefs: ApplicationModule.DefaultPrefsWrapper

        var routeNo: Int? = null
        var beforeFeeOption = 0
        lateinit var listOfFeeOption: MutableList<Int>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_route_result)

            //Injection
            (application as CustomApplication).component.plus(ActivityModule()).inject(this)

            routeNo = defaultPrefs.prefs.routeNo

            setRecyclerView(null)

            initView()

        }
        private fun setRecyclerView(feeOption: String?) {
            recycler.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this)
            recycler.layoutManager = layoutManager
            val adapter = RecyclerAdapter_Parent(applicationContext, recycler_adapter, recycler_adapter_child,
                    defaultPrefs.prefs.routeId, defaultPrefs.prefs.routesId, feeOption)
            recycler.adapter = adapter
        }
        private fun initView(){
            listOfFeeOption = mutableListOf(tv_fee1.id,tv_fee2.id,tv_fee3.id,tv_fee4.id,tv_fee5.id)
            beforeFeeOption = listOfFeeOption[0]

            tv_fee1.setOnClickListener(this)
            tv_fee2.setOnClickListener(this)
            tv_fee3.setOnClickListener(this)
            tv_fee4.setOnClickListener(this)
            tv_fee5.setOnClickListener(this)

            tv_back.setOnClickListener { finish() }

            tv_title.text = "${defaultPrefs.prefs.depart} → ${defaultPrefs.prefs.destination}"

            to_map.setOnClickListener {
                setDialog("GoogleMapを使用します。よろしいですか？", "OK", "NO")
            }
        }
        override fun onClick(view: View?) {
            view ?: return

            //一つ前に選択されたviewの色を戻す
            val beforeView = findViewById<TextView>(beforeFeeOption)
            if (beforeView.id == tv_fee2.id || beforeView.id == tv_fee3.id || beforeView.id == tv_fee4.id)
                beforeView.background = getDrawable(R.drawable.radius_none_stroke_top_bottom)
            else if (beforeView.id == tv_fee1.id) beforeView.background = getDrawable(R.drawable.radius_7_left)
            else beforeView.background = getDrawable(R.drawable.radius_7_right)
            beforeView.setTextColor(getColor(R.color.grey))
            beforeFeeOption = view.id
            //今回選択されたviewの色を変更
            val selectedView = findViewById<TextView>(view.id)
            if (selectedView.id == tv_fee2.id || selectedView.id == tv_fee3.id || selectedView.id == tv_fee4.id)
                selectedView.background = getDrawable(R.color.colorAccent)
            else if (selectedView.id == tv_fee1.id) selectedView.background = getDrawable(R.drawable.radius_7_left_solid_accent)
            else selectedView.background = getDrawable(R.drawable.radius_7_right_solid_accent)
            selectedView.setTextColor(getColor(R.color.white))
            /**
             * 前回選択されたviewと今回選択されたviewが異なる場合、選択されたviewのtextを渡して
             * recyclerViewを再構築する
             */
            if (beforeView != selectedView){
                setRecyclerView(selectedView.text.toString())
            }
        }

        private fun setDialog(title: String, ok: String, ng: String){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(title)

            alertDialog.setPositiveButton(ok, { _, _ ->
                try {
                    goToMap("${defaultPrefs.prefs.depart}IC", "${defaultPrefs.prefs.destination}IC")
                } catch (e: Exception){
                    Log.e("Exception ", "$TAG goToMap $e")
                    toast("GoogleMapがインストールされていません。")
                }

            })
            alertDialog.setNegativeButton(ng, { _, which ->
                Log.d("AlertDialog", "Negative which :$which")
            })
            alertDialog.show()
        }

        // googlemapへ推移
        private fun goToMap(depart: String, destination: String) {
            // 移動手段：電車:r, 車:d, 歩き:w
            val drive = "d"
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity")

            // 出発地, 目的地, 交通手段
            val str = String.format(Locale.US,
                    "http://maps.google.com/maps?saddr=%s&daddr=%s&dirflg=%s",
                    depart, destination, drive)

            intent.data = Uri.parse(str)
            startActivity(intent)

        }

    }
}