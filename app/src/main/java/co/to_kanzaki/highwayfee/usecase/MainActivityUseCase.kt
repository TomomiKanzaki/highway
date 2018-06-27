package co.to_kanzaki.highwayfee.usecase


import co.to_kanzaki.highwayfee.Client
import co.to_kanzaki.highwayfee.Util.SchedulerProvider
import co.to_kanzaki.highwayfee.api.response.GetRouteHighWay
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import co.to_kanzaki.highwayfee.di.scope.ActivityScope
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class MainActivityUseCase@Inject constructor(
        private val apiClient: Client,
        private val defaultPrefsWrapper: ApplicationModule.DefaultPrefsWrapper,
        private val schedulerProvider: SchedulerProvider) {

    val TAG = MainActivityUseCase::class.java.simpleName

    open fun getRouteHighWay(from: String,
                             to: String,
                             carType: String,
                             prefecture: String?,
                             sortBy: String?): Single<GetRouteHighWay>{

        return apiClient.getRouteHighWay(from, to, carType, prefecture, sortBy)
    }
}