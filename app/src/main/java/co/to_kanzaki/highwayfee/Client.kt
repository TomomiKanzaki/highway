package co.to_kanzaki.highwayfee

import android.util.Log
import co.to_kanzaki.highwayfee.Util.SchedulerProvider
import co.to_kanzaki.highwayfee.api.response.GetRouteHighWay
import com.squareup.moshi.Moshi
import io.reactivex.Single
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import co.to_kanzaki.highwayfee.Util.ResourceResolver
import co.to_kanzaki.highwayfee.api.response.ErrorResponse
import co.to_kanzaki.highwayfee.exception.ApiException
import co.to_kanzaki.highwayfee.exception.AppException
import co.to_kanzaki.highwayfee.exception.CommunicationException

@Singleton
open class Client @Inject constructor(val service: Service,
                                      val schedulerProvider: SchedulerProvider,
                                      val resourceResolver: ResourceResolver) {

    val TAG = Client::class.java.simpleName

    /**
     * ルート取得
     */
    fun getRouteHighWay(from: String,
                        to: String,
                        carType: String,
                        prefecture: String?,
                        sortBy: String?): Single<GetRouteHighWay>{
        return service.getRouteHighWay(from,
                to,
                carType,
                prefecture,
                sortBy)
                .subscribeOn(schedulerProvider.io())
                .handleException()

    }


    /**
     * 通信中に発生した例外の振り分けはここで済ませ、全てAppExceptionの派生オブジェクトにまとめる。
     * 後続のストリームではAppExceptionの型によって処理を振り分ける。
     * 発生しうるのは
     * 通信成功だが内容がエラーの場合：ApiException
     * 通信失敗の場合：CommunicationException
     * 上記以外の場合：AppException
     */
    private fun <T> Single<T>.handleException(): Single<T> {
        return this.onErrorResumeNext { throwable ->
            Log.e("handleException ", throwable.toString())
            if (throwable is HttpException && (throwable.code() < 200 || throwable.code() >= 300)) {

                if (throwable.code() == 401) {
                    return@onErrorResumeNext Single.error(ApiException(throwable, "認証エラー", null))
                }

                if (throwable.code() == 404) {
                    return@onErrorResumeNext Single.error(ApiException(throwable, "リソースなし", resourceResolver.getString(R.string.toast_data_not_found)))
                }

                //Moshiオブジェクトを生成
                val moshi = Moshi.Builder().build()

                //JSON <-> POJO変換ためのアダプタ生成（変換対象のクラスを渡す）
                val adapter = moshi.adapter(ErrorResponse::class.java)

                //レスポンスのエラーメッセージを取り出す
                val errorString = throwable.response().errorBody()?.string() ?: ""

                println("erro json string:$errorString")

                //fromJson()でJSON文字列からオブジェクトに変換

                val errorResponse: ErrorResponse?
                try {
                    errorResponse = adapter.fromJson(if (errorString.isEmpty()) "{}" else errorString)
                } catch (e: Exception) {
                    return@onErrorResumeNext Single.error(ApiException(e, "jsonパース失敗", resourceResolver.getString(R.string.toast_something_wrong_happened_on_communication)))
                }

                if (errorResponse?.error?.messages == null) {
                    //エラーオブジェクト、エラーメッセージが取得できない場合は想定外のエラー
                    Single.error(ApiException(throwable, "エラーオブジェクト・エラーメッセージなし", resourceResolver.getString(R.string.toast_something_wrong_happened_on_communication)))
                } else {
                    //エラーメッセージが正常に返却された場合
                    //ユーザ表示エラーメッセージは現状(2018.4.3)ないので、nullをセットして表示しないようにする
                    Single.error(ApiException(throwable, errorResponse.error.messages, null))
                }

            } else if (throwable is ConnectException) {

                Single.error(CommunicationException(throwable, "接続エラー", resourceResolver.getString(R.string.toast_communication_error_occurred)))

            } else if (throwable is UnknownHostException) {

                Single.error(CommunicationException(throwable, "ホスト不明エラー", resourceResolver.getString(R.string.toast_communication_error_occurred)))

            } else if (throwable is SocketTimeoutException) {

                Single.error(CommunicationException(throwable, "ソケットタイムアウトエラー", resourceResolver.getString(R.string.toast_communication_error_occurred)))
            } else {

                kotlin.io.println(throwable)

                //上記以外のエラー
                Single.error(AppException(throwable, "予期しないエラー", resourceResolver.getString(R.string.toast_something_wrong_happened_on_communication)))

            }
        }
    }
}