package co.to_kanzaki.highwayfee.Recycler

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
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
import co.to_kanzaki.highwayfee.view.RouteResultView
import org.jetbrains.anko.db.select

class RecyclerAdapter_Parent: RecyclerView.Adapter<RecyclerAdapter_Parent.ViewHolder> {

    val TAG = RecyclerAdapter_Parent::class.java.simpleName

    var helper: DataBaseRoute
    var context: Context? = null
    var inflater: LayoutInflater? = null
    var layoutId: Int = 0
    var layoutIdChild: Int = 0
    var routeId: Int = 0
    var routesId: Int = 0
    var feeOption: String? = null

    var listRoute: List<ListDataRoute>?
    var listRoutes: List<ListDataRoutes>? = null

    constructor(context: Context, layoutId: Int, layoutIdChild: Int, routeId: Int, routesId: Int, feeOption: String?){
        helper = DataBaseRoute(context)

        this.context = context
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        this.layoutId = layoutId
        this.layoutIdChild = layoutIdChild
        this.routeId = routeId
        this.routesId = routesId
        this.feeOption = feeOption

        listRoute = makeListRoute()
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvCandidate: TextView? = null
        var tvSum: TextView? = null
        var tvDistance: TextView? = null
        var tvTime: TextView? = null
        var recyclerChild: RecyclerView? = null
        init {
            tvCandidate = v.findViewById(R.id.tv_candidate)
            tvSum = v.findViewById(R.id.tv_sum)
            tvDistance = v.findViewById(R.id.tv_distance)
            tvTime = v.findViewById(R.id.tv_time)
            recyclerChild = v.findViewById(R.id.recycler_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = this.inflater!!.inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRoute!![0].routeNoMax!!.toInt()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val tvCandidate = holder?.tvCandidate
        val tvSum = holder?.tvSum
        val tvDistance = holder?.tvDistance
        val tvTime = holder?.tvTime
        holder?.recyclerChild?.let { setRecyclerView(it, position) }

        listRoutes = makeListRoutes(position)
        setTextFromDB(position, tvCandidate, tvSum, tvDistance, tvTime)
    }
    private fun setTextFromDB(position: Int, tvCandidate: TextView?, tvSum: TextView?, tvDistance: TextView?, tvTime: TextView?){
        try {
            val total = getTotalTollLengthAndTime(position)
            val totalMinutes = total?.get(0)?.totalTime
            tvCandidate?.text = "第${position + 1}候補"
            tvSum?.text = if (feeOption == null) "料金合計：${total?.get(0)?.totalToll}円"
                          else "料金合計：${getFeeAlongOption()}円"
            tvDistance?.text = "${total?.get(0)?.totalLength}km"
            tvTime?.text = "${(totalMinutes)!! /60}時間${(totalMinutes)!!%60}分"
        } catch (e: Exception){
            Log.e("Exception ", "$TAG setTextFromDB $e")
        }
    }

    private fun setRecyclerView(recycler: RecyclerView, position: Int) {
        //同行者記入覧表示
        recycler.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager
        val adapter = context?.let { RecyclerAdapter_Child(it, layoutIdChild, routeId, routesId, position) }
        recycler.adapter = adapter
    }

    private fun makeListRoute(): List<ListDataRoute>?{
        var list: List<ListDataRoute>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTE)
                        .whereArgs("${helper.ID} = $routeId")
                        .parseList(ListDataParserRoute(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG makeListRoute $e")
            }
        }
        return list
    }
    private fun getTotalTollLengthAndTime(position: Int): List<ListDataRoutes>?{
        var list: List<ListDataRoutes>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTES)
                        .whereArgs("${helper.ID} = $routesId AND ${helper.ROUTE_NO} = $position AND ${helper.SUBSECTION_NO} = 1")
                        .parseList(ListDataParserRoutes(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG getTotalToll $e")
            }
        }
        return list
    }

    private fun makeListRoutes(position: Int): List<ListDataRoutes>?{
        var list: List<ListDataRoutes>? = null
        helper.use {
            try {
                list = this.select(helper.TABLE_NAME_ROUTES)
                        .whereArgs("${helper.ID} = $routesId AND ${helper.ROUTE_NO} = $position")
                        .parseList(ListDataParserRoutes(context!!))
            } catch (e: Exception){
                Log.e("Exception ", "$TAG getTotalToll $e")
            }
        }
        return list
    }

    private fun getFeeAlongOption(): Int{
        var sum = 0
        try {
            for(i in 0.. listRoutes!!.lastIndex){
                //今回のsectionOrder と前回の sectionOrder が同じ時(＝同じルート) continue
                if (i > 0 && listRoutes!![i].sectionOrder == listRoutes!![i - 1].sectionOrder) continue
                else {
                    Log.i("i== ", "$i")
                    when(feeOption){
                        "通常" -> {
                            try {
                                if (!listRoutes!![i].tollNormal!!.isNullOrEmpty())
                                    sum += Integer.parseInt(listRoutes!![i].tollNormal!!.replace("円 通常料金", ""))
                            } catch (e: Exception){
                                Log.e("Exception ", "$TAG 通常 $e")
                            }
                        }
                        "ETC" -> {
                            try {
                                sum += if (!listRoutes!![i].tollEtc!!.isNullOrEmpty()){
                                    if (Regex("円 ETC平日料金").containsMatchIn(listRoutes!![i].tollEtc!!))
                                        Integer.parseInt(listRoutes!![i].tollEtc!!.replace("円 ETC平日料金", ""))
                                    else
                                        Integer.parseInt(listRoutes!![i].tollEtc!!.replace("円 ETC料金", ""))
                                }
                                else
                                    Integer.parseInt(listRoutes!![i].tollNormal!!.replace("円 通常料金", ""))
                            } catch (e: Exception){
                                Log.e("Exception ", "$TAG ETC $e")
                            }
                        }
                        "ETC2.0" -> {
                            try {
                                sum += if (!listRoutes!![i].tollEtc2!!.isNullOrEmpty()){
                                    if (Regex("円 ETC休日料金").containsMatchIn(listRoutes!![i].tollEtc2!!))
                                        Integer.parseInt(listRoutes!![i].tollEtc2!!.replace("円 ETC休日料金", ""))
                                    else
                                        Integer.parseInt(listRoutes!![i].tollEtc2!!.replace("円 ETC2.0料金", ""))
                                } else
                                    Integer.parseInt(listRoutes!![i].tollNormal!!.replace("円 通常料金", ""))
                            } catch (e: Exception){
                                Log.e("Exception ", "$TAG ETC2.0 $e")
                            }
                        }
                        "深夜" -> {
                            val night = try {
                                listRoutes!![i].tollNight!!
                            } catch (e: Exception){
                                listRoutes!![i].tollNormal!!
                            }
                            sum += try {
                                if (Regex("ETC夜間割引\\(22-24時\\)").containsMatchIn(night))
                                    Integer.parseInt(night.replace("ETC夜間割引(22-24時)", ""))
                                else if (Regex("円 深夜割引\\(0-4時/30%\\)").containsMatchIn(night))
                                    Integer.parseInt(night.replace("円 深夜割引(0-4時/30%)", ""))
                                else
                                    Integer.parseInt(night.replace("円 通常料金", ""))
                            } catch (e: NullPointerException){
                                Log.e("Exception ", "$TAG 深夜 $e")
                            }
                        }
                        "休日" -> {
                            val holiday = try {
                                listRoutes!![i].tollHoliday!!
                            } catch (e: Exception){
                                listRoutes!![i].tollNormal!!
                            }
                            sum += try {
                                if (Regex("円 ETC夜間割引\\(0-6時\\)").containsMatchIn(holiday))
                                    Integer.parseInt(holiday.replace("円 ETC夜間割引(0-6時)", ""))
                                else if (Regex("円 休日割引").containsMatchIn(holiday))
                                    Integer.parseInt(holiday.replace("円 休日割引", ""))
                                else
                                    Integer.parseInt(holiday.replace("円 通常料金", ""))
                            } catch (e: NullPointerException){
                                Log.e("Exception ", "$TAG 休日 $e")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception){
            Log.e("Exception ", "$TAG getFeeAlongOption $e")
        }
        return sum
    }
}