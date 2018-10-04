package co.to_kanzaki.highwayfee.db.Routes

import android.content.Context
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import org.jetbrains.anko.db.MapRowParser

class ListDataParserRoutes(val ctx: Context): MapRowParser<ListDataRoutes> {

    override fun parseRow(columns: Map<String, Any?>): ListDataRoutes {
        return ListDataRoutes(columns[DataBaseRoute(ctx).ID] as Long?,
                columns[DataBaseRoute(ctx).ROUTE_NO] as Long?,
                columns[DataBaseRoute(ctx).TOTAL_TOLL] as Long?,
                columns[DataBaseRoute(ctx).TOTAL_TIME] as Long?,
                columns[DataBaseRoute(ctx).TOTAL_LENGTH] as Any?,
                columns[DataBaseRoute(ctx).DETAIL_NO] as Long?,
                columns[DataBaseRoute(ctx).SECTION_ORDER] as Long?,
                columns[DataBaseRoute(ctx).SECTION_FROM] as String?,
                columns[DataBaseRoute(ctx).SECTION_TO] as String?,
                columns[DataBaseRoute(ctx).SECTION_LENGTH] as Any?,
                columns[DataBaseRoute(ctx).SECTION_TIME] as Long?,
                columns[DataBaseRoute(ctx).SUBSECTION_NO] as Long?,
                columns[DataBaseRoute(ctx).SUBSECTION_FROM] as String?,
                columns[DataBaseRoute(ctx).SUBSECTION_ROAD] as String?,
                columns[DataBaseRoute(ctx).SUBSECTION_TO] as String?,
                columns[DataBaseRoute(ctx).SUBSECTION_LENGTH] as Any?,
                columns[DataBaseRoute(ctx).SUBSECTION_TIME] as Long?,
                columns[DataBaseRoute(ctx).TOLL_NO] as Long?,
                columns[DataBaseRoute(ctx).TOLL_NORMAL] as String?,
                columns[DataBaseRoute(ctx).TOLL_ETC] as String?,
                columns[DataBaseRoute(ctx).TOLL_ETC2] as String?,
                columns[DataBaseRoute(ctx).TOLL_NIGHT] as String?,
                columns[DataBaseRoute(ctx).TOLL_HOLIDAY] as String?)
    }
}
