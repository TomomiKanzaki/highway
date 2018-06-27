package co.to_kanzaki.highwayfee

import co.to_kanzaki.highwayfee.api.response.GetRouteHighWay
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    /**
     * ルート取得API
     */
    @GET("route.php")
    fun getRouteHighWay(@Query ("f") from: String,	//出発IC名を指定します。
                        @Query ("t") to: String,	//到着IC名を指定します。
                        @Query ("c") carType: String,//車種を指定します。(デフォルト：普通車) 軽自動車等/普通車/中型車/大型車/特大車の五種類の指定ができます。
                        @Query ("s") prefecture: String?,// s	都道府県名を指定します。(省略可)
                        @Query ("sortBy") sortBy: String?	//複数の経路があるときの、並べかえの順序を指定します。(デフォルト：距離) 距離/料金の二種類の指定ができます。
    ): Single<GetRouteHighWay>
}