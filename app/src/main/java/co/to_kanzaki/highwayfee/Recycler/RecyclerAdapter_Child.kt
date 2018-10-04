package co.to_kanzaki.highwayfee.Recycler

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.to_kanzaki.highwayfee.R
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import co.to_kanzaki.highwayfee.db.Route.ListDataParserRoute
import co.to_kanzaki.highwayfee.db.Route.ListDataRoute
import co.to_kanzaki.highwayfee.db.Routes.ListDataParserRoutes
import co.to_kanzaki.highwayfee.db.Routes.ListDataRoutes
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import co.to_kanzaki.highwayfee.view.RouteDetailView
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

class RecyclerAdapter_Child: RecyclerView.Adapter<RecyclerAdapter_Child.ViewHolder> {

    val TAG = RecyclerAdapter_Child::class.java.simpleName

    var helper: DataBaseRoute
    var defaultPrefs: ApplicationModule.DefaultPrefsWrapper

    var context: Context? = null
    var inflater: LayoutInflater? = null
    var layoutId: Int = 0
    var routeNo: Int = 0
    var routeId: Int = 0
    var routesId: Int = 0

    var listRoutes: List<ListDataRoutes>?

    constructor(context: Context, layoutId: Int, routeId: Int, routesId: Int, routeNo: Int){
        helper = DataBaseRoute(context)
        defaultPrefs = ApplicationModule.DefaultPrefsWrapper(context)

        this.context = context
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        this.layoutId = layoutId
        this.routeNo = routeNo
        this.routeId = routeId
        this.routesId = routesId

        listRoutes = makeListRoutes(routeNo)


    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvRoute: TextView? = null
        var tvDistance: TextView? = null
        var tvTime: TextView? = null

        init {
            tvRoute = v.findViewById(R.id.route)
            tvDistance = v.findViewById(R.id.tv_distance)
            tvTime = v.findViewById(R.id.tv_time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = this.inflater!!.inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            listRoutes?.get(0)?.detailNo!!.toInt()
        } catch (e: Exception){
            Log.e("Exception ", "$TAG getItemCount $e")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val sectionOrder = listRoutes?.get(0)?.sectionOrder?.plus(position)
        val list = sectionOrder?.toInt()?.let { makeListRoutesForText(it) }
        val tvRoute = holder?.tvRoute
        val tvDistance = holder?.tvDistance
        val tvTime = holder?.tvTime
        try {
            val section = list?.get(0)
            val sectionHour = if (section?.sectionTime!!/60 > 0)"${section.sectionTime!!/60}時間" else ""
            val sectionMinutes = "${section.sectionTime!!%60}分"
            tvRoute?.text = "${section.sectionFrom}→${section.sectionTo}"
            tvDistance?.text = "${section.sectionLength}km"
            tvTime?.text = "$sectionHour$sectionMinutes"
        } catch (e:Exception){
            Log.e("Exception ", "$TAG onBindViewHolder $e")
        }
        val intent = Intent(context, RouteDetailView.RouteDetailActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        tvRoute?.setOnClickListener {
            defaultPrefs.prefs.routeNo = routeNo
            defaultPrefs.prefs.sectionOrder = sectionOrder!!.toInt()
            context!!.startActivity(intent)
        }
        tvDistance?.setOnClickListener {
            defaultPrefs.prefs.routeNo = routeNo
            defaultPrefs.prefs.sectionOrder = sectionOrder!!.toInt()
            context!!.startActivity(intent)
        }
        tvTime?.setOnClickListener {
            defaultPrefs.prefs.routeNo = routeNo
            defaultPrefs.prefs.sectionOrder = sectionOrder!!.toInt()
            context!!.startActivity(intent)
        }
    }

    private fun makeListRoutes(routeNo: Int): List<ListDataRoutes>?{
        var list: List<ListDataRoutes>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTES)
                        .whereArgs("${helper.ID} = $routesId AND ${helper.ROUTE_NO} = $routeNo")
                        .parseList(ListDataParserRoutes(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG makeListRoutes $e")
            }
        }
        return list
    }
    private fun makeListRoutesForText(sectionOrder: Int): List<ListDataRoutes>?{
        var list: List<ListDataRoutes>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTES)
                        .whereArgs("${helper.ID} = $routesId AND ${helper.ROUTE_NO} = $routeNo AND ${helper.SECTION_ORDER} >= $sectionOrder")
                        .parseList(ListDataParserRoutes(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG makeListRoutes $e")
            }
        }
        return list
    }

}