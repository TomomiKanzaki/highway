package co.to_kanzaki.highwayfee.usecase


import android.content.Context
import android.util.Log
import co.to_kanzaki.highwayfee.Client
import co.to_kanzaki.highwayfee.Util.SchedulerProvider
import co.to_kanzaki.highwayfee.api.response.GetRouteHighWay
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import co.to_kanzaki.highwayfee.db.Route.ListDataParserRoute
import co.to_kanzaki.highwayfee.db.Route.ListDataRoute
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import co.to_kanzaki.highwayfee.di.scope.ActivityScope
import io.reactivex.Completable
import io.reactivex.Single
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import javax.inject.Inject

@ActivityScope
class MainActivityUseCase@Inject constructor(
        private val apiClient: Client,
        private val defaultPrefsWrapper: ApplicationModule.DefaultPrefsWrapper,
        private val schedulerProvider: SchedulerProvider,
        private val database: DataBaseRoute,
        private val context: Context) {

    val TAG = MainActivityUseCase::class.java.simpleName

    open fun getRouteHighWay(from: String,
                             to: String,
                             carType: String,
                             prefecture: String?,
                             sortBy: String?): Single<GetRouteHighWay>{

        return apiClient.getRouteHighWay(from, to, carType, prefecture, sortBy)
    }

    /**
     * DB内に既に同じ検索ワードがある場合 true
     * そのレコードのIDをsharedPreferencesに入れる
     */
    fun existSameRoute(from: String, to: String, carType: String): Boolean{
        var sameRoute = false
        var list: List<ListDataRoute>? = null
        database.use {
            try {
                list = this.select(database.TABLE_NAME_ROUTE)
                        .whereArgs("${database.FROM} = '$from' " +
                                "AND ${database.TO} = '$to' AND ${database.CARTYPE} = '$carType'")
                        .parseList(ListDataParserRoute(context))
            } catch (e: Exception) {
                Log.e("Exception ", "$TAG sameRoute $e")
            }
        }
        if (list?.isNotEmpty()!!) {
            sameRoute = true
            defaultPrefsWrapper.prefs.routeId = list!![0].id!!.toInt()
            defaultPrefsWrapper.prefs.routesId = list!![0].id!!.toInt()
        }
        return sameRoute
    }
    fun insertRoute(param: GetRouteHighWay): Completable {

        return Completable.create { emitter ->
            try {
                database.use {
                    val maxId = try {
                        this.select(database.TABLE_NAME_ROUTE, "MAX(${database.ID})")
                                .parseSingle(IntParser)
                    } catch (e: Exception) {
                        Log.e("Exception ", "$TAG insertRoute0.1 $e")
                        0
                    }
                    val newId = if (maxId == 0) 1 else maxId + 1
                    defaultPrefsWrapper.prefs.routeId = newId
//                Log.i("newId ", "$newId")
                    this.insert(database.TABLE_NAME_ROUTE, database.ID to newId,
                            database.FROM to param.From,
                            database.TO to param.To,
                            database.CARTYPE to param.CarType,
                            database.SORTBY to param.SortBy,
                            database.ROUTE_NO_MAX to param.Routes?.lastIndex!! + 1)
                }
            } catch (e: Exception) {
                Log.e("Exception ", "$TAG getRouteHighWay1 $e")
            }
            try {
                database.use {
                    param.Routes.let {
                        val maxId = try {
                            this.select(database.TABLE_NAME_ROUTES, "MAX(${database.ID})")
                                    .parseSingle(IntParser)
                        } catch (e: Exception) {
                            Log.e("Exception ", "$TAG insertRoute0.2 $e")
                            0
                        }
                        val newId = if (maxId == 0) 1 else maxId + 1

                        it?.forEach {
                            val routeNo = it.RouteNo
                            val totalToll = it.Summary?.TotalToll
                            val totalTime = it.Summary?.TotalTime
                            val totalLength = it.Summary?.TotalLength
                            val detailNo = it.Details?.No
                            var num = 1
                            it.Details?.Section?.forEach {
                                try {
                                    val order = it.Order
                                    Log.i("order ", "$order")
                                    val from = it.From
                                    val to = it.To
                                    val length = it.Length
                                    val time = it.Time
                                    val no = it.Tolls!![0].No
                                    var tollNormal: String? = null
                                    var tollETC: String? = null
                                    var tollETC2: String? = null
                                    var tollNight: String? = null
                                    var tollHoliday: String? = null
                                    try {
                                        tollNormal = it.Tolls!![0].Toll?.get(0)
                                        tollETC = it.Tolls!![0].Toll?.get(1)
                                        tollETC2 = it.Tolls!![0].Toll?.get(2)
                                        tollNight = it.Tolls!![0].Toll?.get(3)
                                        tollHoliday = it.Tolls!![0].Toll?.get(4)
                                    } catch (e: Exception) {
                                        Log.e("Exception ", "$TAG options $e")
                                    }
                                    Log.i("tollHoliday ", "$tollHoliday")
                                    it.SubSections?.forEach {
                                        try {
                                            defaultPrefsWrapper.prefs.routesId = newId
                                            this.insert(database.TABLE_NAME_ROUTES, database.ID to newId,
                                                    database.ROUTE_NO to routeNo,
                                                    database.TOTAL_TOLL to totalToll,
                                                    database.TOTAL_TIME to totalTime,
                                                    database.TOTAL_LENGTH to totalLength,
                                                    database.DETAIL_NO to detailNo,
                                                    database.SECTION_ORDER to order,
                                                    database.SECTION_FROM to from,
                                                    database.SECTION_TO to to,
                                                    database.SECTION_LENGTH to length,
                                                    database.SUBSECTION_NO to num,
                                                    database.SUBSECTION_FROM to it.From,
                                                    database.SUBSECTION_TO to it.To,
                                                    database.SUBSECTION_ROAD to it.Road,
                                                    database.SUBSECTION_LENGTH to it.Length,
                                                    database.SUBSECTION_TIME to it.Time,
                                                    database.SECTION_TIME to time,
                                                    database.TOLL_NO to no,
                                                    database.TOLL_NORMAL to tollNormal,
                                                    database.TOLL_ETC to tollETC,
                                                    database.TOLL_ETC2 to tollETC2,
                                                    database.TOLL_NIGHT to tollNight,
                                                    database.TOLL_HOLIDAY to tollHoliday)
                                            Log.i("num ", "$num")
                                        } catch (e: Exception) {
                                            Log.e("Exception ", "$TAG SubSections?.forEach $e")
                                        }
                                            num++
                                    }
                                } catch (e: Exception) {
                                    Log.e("Exception ", "$TAG Details?.Section?.forEach $e")
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Exception ", "$TAG getRouteHighWay2 $e")
            }
            emitter.onComplete()
        }.subscribeOn(schedulerProvider.io())
    }
}