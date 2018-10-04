package co.to_kanzaki.highwayfee.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import co.to_kanzaki.highwayfee.BaseActivity
import co.to_kanzaki.highwayfee.CustomApplication
import co.to_kanzaki.highwayfee.R
import co.to_kanzaki.highwayfee.Recycler.RecyclerAdapter_Fee
import co.to_kanzaki.highwayfee.Recycler.RecyclerAdapter_RouteDetail
import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import kotlinx.android.synthetic.main.activity_route_detail.*
import javax.inject.Inject

interface RouteDetailView{

    class RouteDetailActivity: BaseActivity(), RouteDetailView {

        @Inject
        lateinit var defaultPrefs: ApplicationModule.DefaultPrefsWrapper

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_route_detail)

            //Injection
            (application as CustomApplication).component.plus(ActivityModule()).inject(this)

            setRecyclerView()

            tv_back.setOnClickListener { finish() }

            tv_title.text = "${defaultPrefs.prefs.depart} → ${defaultPrefs.prefs.destination}"

        }
        private fun setRecyclerView() {
            recycler_route_detail.setHasFixedSize(true)
            val layoutManagerRouteDetail = LinearLayoutManager(this)
            recycler_route_detail.layoutManager = layoutManagerRouteDetail
            val adapterRouteDetail = RecyclerAdapter_RouteDetail(this, R.layout.recycler_adapter_route_detail,
                    defaultPrefs.prefs.routesId, defaultPrefs.prefs.routeNo)
            recycler_route_detail.adapter = adapterRouteDetail

            recycler_fee.setHasFixedSize(true)
            val layoutManagerFee = LinearLayoutManager(this)
            recycler_fee.layoutManager = layoutManagerFee
            val adapterFee = RecyclerAdapter_Fee(this, R.layout.recycler_adapter_fee,
                    defaultPrefs.prefs.routesId, defaultPrefs.prefs.routeNo, defaultPrefs.prefs.sectionOrder)
            recycler_fee.adapter = adapterFee
        }

    }
}
