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
import kotlinx.android.synthetic.main.recycler_adapter_fee.view.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.find

class RecyclerAdapter_Fee: RecyclerView.Adapter<RecyclerAdapter_Fee.ViewHolder>  {
    val TAG = RecyclerAdapter_Fee::class.java.simpleName

    var helper: DataBaseRoute
    var context: Context? = null
    var inflater: LayoutInflater? = null
    var layoutId: Int = 0
    var routesId: Int = 0
    var routeNo: Int = 0
    var sectionOrder: Int = 0

    var listRoutesDetail: List<ListDataRoutes>? = null

    var listForText: MutableList<String>? = null

    constructor(context: Context, layoutId: Int, routesId: Int, routeNo: Int, sectionOrder: Int){
        helper = DataBaseRoute(context)

        this.context = context
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        this.layoutId = layoutId
        this.routesId = routesId
        this.routeNo = routeNo
        this.sectionOrder = sectionOrder

        this.listRoutesDetail = makeListRoutesDetail(sectionOrder)
        setListForText()
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvFee: TextView? = null
        init {
            tvFee = v.findViewById(R.id.tv_fee)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerAdapter_Fee.ViewHolder {
        val view: View = this.inflater!!.inflate(layoutId, parent, false)
        return RecyclerAdapter_Fee.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            listForText!!.lastIndex + 1
        } catch (e: Exception){
            Log.e("Exception ", "$TAG getItemCount $e")
        }
    }
    override fun onBindViewHolder(holder: RecyclerAdapter_Fee.ViewHolder?, position: Int) {
        holder?.tvFee?.text = listForText?.get(position)
    }

    private fun setListForText(){
        listForText = mutableListOf()
        try {
            listForText?.add(listRoutesDetail?.get(0)?.tollNormal!!)
        } catch (e: Exception){
            Log.e("Exception ", "$TAG tollNormal $e")
        }
        try {
            listForText?.add(listRoutesDetail?.get(0)?.tollEtc!!)
        } catch (e: Exception){
            Log.e("Exception ", "$TAG tollETC $e")
        }
        try {
            listForText?.add(listRoutesDetail?.get(0)?.tollEtc2!!)
        } catch (e: Exception){
            Log.e("Exception ", "$TAG tollEtc2 $e")
        }
        try {
            listForText?.add(listRoutesDetail?.get(0)?.tollNight!!)
        } catch (e: Exception){
            Log.e("Exception ", "$TAG tollNight $e")
        }
        try {
            listForText?.add(listRoutesDetail?.get(0)?.tollHoliday!!)
        } catch (e: Exception){
            Log.e("Exception ", "$TAG tollHoliday $e")
        }
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