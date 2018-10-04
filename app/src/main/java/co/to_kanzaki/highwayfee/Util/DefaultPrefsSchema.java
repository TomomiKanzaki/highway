package co.to_kanzaki.highwayfee.Util;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table(name = "co.to_kanzaki.highwayfee.user_preferences")
public abstract class DefaultPrefsSchema {
    @Key(name = "routeId")
    int routeId;
    @Key(name = "routesId")
    int routesId;
    @Key(name = "routeNo")
    int routeNo;
    @Key(name = "sectionOrder")
    int sectionOrder;
    @Key(name = "depart")
    String depart;
    @Key(name = "destination")
    String destination;
}
