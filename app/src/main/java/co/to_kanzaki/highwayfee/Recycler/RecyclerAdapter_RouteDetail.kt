package co.to_kanzaki.highwayfee.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.to_kanzaki.highwayfee.R
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import co.to_kanzaki.highwayfee.db.Routes.ListDataParserRoutes
import co.to_kanzaki.highwayfee.db.Routes.ListDataRoutes
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import org.jetbrains.anko.db.select
import org.w3c.dom.Text

class RecyclerAdapter_RouteDetail: RecyclerView.Adapter<RecyclerAdapter_RouteDetail.ViewHolder>  {
    val TAG = RecyclerAdapter_RouteDetail::class.java.simpleName

    var helper: DataBaseRoute
    var defaultPrefs: ApplicationModule.DefaultPrefsWrapper

    var context: Context? = null
    var inflater: LayoutInflater? = null
    var layoutId: Int = 0
    var routesId: Int = 0
    var routeNo: Int = 0
    var sectionOrder: Int = -1

    var listRoutesDetail: List<ListDataRoutes>? = null

    constructor(context: Context, layoutId: Int, routesId: Int, routeNo: Int){
        helper = DataBaseRoute(context)
        defaultPrefs = ApplicationModule.DefaultPrefsWrapper(context)

        this.context = context
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        this.layoutId = layoutId
        this.routesId = routesId
        this.routeNo = routeNo
        this.sectionOrder = defaultPrefs.prefs.sectionOrder

        this.listRoutesDetail = makeListRoutesDetail(sectionOrder)
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvRoiuteDetail: TextView? = null
        var tvRouteName: TextView? = null
        var tvDistance: TextView? = null
        var tvTime: TextView? = null
        init {
            tvRoiuteDetail = v.findViewById(R.id.tv_route_detail)
            tvRouteName = v.findViewById(R.id.tv_route_name)
            tvDistance = v.findViewById(R.id.tv_distance)
            tvTime = v.findViewById(R.id.tv_time)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerAdapter_RouteDetail.ViewHolder {
        val view: View = this.inflater!!.inflate(layoutId, parent, false)
        return RecyclerAdapter_RouteDetail.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            listRoutesDetail!!.lastIndex + 1
        } catch (e: Exception){
            Log.e("Exception ", "$TAG getItemCount $e")
        }
    }
    override fun onBindViewHolder(holder: RecyclerAdapter_RouteDetail.ViewHolder?, position: Int) {
        val routeDetail = holder?.tvRoiuteDetail
        val routeName = holder?.tvRouteName
        val distance = holder?.tvDistance
        val time = holder?.tvTime
        val subSectionHour = if (listRoutesDetail?.get(position)?.subsectionTime!!/60 > 0)"${listRoutesDetail?.get(position)?.subsectionTime!!/60}時間" else ""
        val subSectionMinutes = "${listRoutesDetail?.get(position)?.subsectionTime!!%60}分"

        routeDetail?.text = "${listRoutesDetail?.get(position)?.subsectionFrom}→${listRoutesDetail?.get(position)?.subsectionTo}"
        routeName?.text = "${listRoutesDetail?.get(position)?.subsectionRoad}"
        distance?.text = "${listRoutesDetail?.get(position)?.subsectionLength}km"
        time?.text = "$subSectionHour$subSectionMinutes"
    }

    private fun makeListRoutesDetail(sectionOrder: Int): List<ListDataRoutes>?{
        var list: List<ListDataRoutes>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTES)
                        .whereArgs("${helper.ID} = $routesId " +
                                "AND ${helper.ROUTE_NO} = $routeNo " +
                                "AND ${helper.SECTION_ORDER} = $sectionOrder")
                        .parseList(ListDataParserRoutes(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG makeListRoutes $e")
            }
        }
        return list
    }
}