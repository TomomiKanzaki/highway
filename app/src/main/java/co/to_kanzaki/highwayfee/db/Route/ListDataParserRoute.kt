package co.to_kanzaki.highwayfee.db.Route

import android.content.Context
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import org.jetbrains.anko.db.MapRowParser

class ListDataParserRoute(val ctx: Context): MapRowParser<ListDataRoute> {
    override fun parseRow(columns: Map<String, Any?>): ListDataRoute {
        return ListDataRoute(columns[DataBaseRoute(ctx).ID] as Long?,
                columns[DataBaseRoute(ctx).FROM] as String?,
                columns[DataBaseRoute(ctx).TO] as String?,
                columns[DataBaseRoute(ctx).CARTYPE] as String?,
                columns[DataBaseRoute(ctx).SORTBY] as String?,
                columns[DataBaseRoute(ctx).ROUTE_NO_MAX] as Long?)

    }
}