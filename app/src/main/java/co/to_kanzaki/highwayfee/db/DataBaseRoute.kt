package co.to_kanzaki.highwayfee.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseRoute @Inject constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "DataBase", null, 1)  {

    //テーブル名
    val TABLE_NAME_ROUTE = "table_route"
    val TABLE_NAME_ROUTES = "table_routes"
    //パラメータ(共有)
    val ID = "id"
    //パラメータ(TABLE_NAME_ROUTE)
    val FROM = "fromIC"
    val TO = "toIC"
    val CARTYPE = "cartype"
    val SORTBY = "sortby"
    val ROUTE_NO_MAX = "route_no_max"
    //パラメータ(TABLE_NAME_ROUTES)
    val ROUTE_NO = "route_no"
    val TOTAL_TOLL = "total_toll"
    val TOTAL_TIME = "total_time"
    val TOTAL_LENGTH = "total_length"

    val DETAIL_NO = "detail_no"

    val SECTION_ORDER = "section_order"
    val SECTION_FROM = "section_from"
    val SECTION_TO = "section_to"
    val SECTION_LENGTH = "section_length"
    val SECTION_TIME = "section_time"

    val SUBSECTION_NO = "subsection_no"
    val SUBSECTION_FROM = "subsection_from"
    val SUBSECTION_TO = "subsection_to"
    val SUBSECTION_ROAD = "subsection_road"
    val SUBSECTION_LENGTH = "subsection_length"
    val SUBSECTION_TIME = "subsection_time"

    val TOLL_NO = "toll_no"
    val TOLL_NORMAL = "toll_normal"
    val TOLL_ETC = "toll_etc"
    val TOLL_ETC2 = "toll_etc2"
    val TOLL_NIGHT = "toll_night"
    val TOLL_HOLIDAY = "toll_holiday"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(TABLE_NAME_ROUTE, true,
                ID to INTEGER + PRIMARY_KEY,
                FROM to TEXT,
                TO to TEXT,
                CARTYPE to TEXT,
                SORTBY to TEXT,
                ROUTE_NO_MAX to INTEGER
        )
        db?.createTable(TABLE_NAME_ROUTES, true,
                ID to INTEGER,
                ROUTE_NO to INTEGER,
                TOTAL_TOLL to INTEGER,
                TOTAL_TIME to INTEGER,
                TOTAL_LENGTH to REAL,
                DETAIL_NO to INTEGER,
                SECTION_ORDER to INTEGER,
                SECTION_FROM to TEXT,
                SECTION_TO to TEXT,
                SECTION_LENGTH to REAL,
                SECTION_TIME to INTEGER,
                SUBSECTION_NO to INTEGER,
                SUBSECTION_FROM to TEXT,
                SUBSECTION_ROAD to TEXT,
                SUBSECTION_TO to TEXT,
                SUBSECTION_LENGTH to REAL,
                SUBSECTION_TIME to INTEGER,
                TOLL_NO to INTEGER,
                TOLL_NORMAL to TEXT,
                TOLL_ETC to TEXT,
                TOLL_ETC2 to TEXT,
                TOLL_NIGHT to TEXT,
                TOLL_HOLIDAY to TEXT
        )

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}
