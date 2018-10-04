package co.to_kanzaki.highwayfee.di.component

import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.scope.ActivityScope
import co.to_kanzaki.highwayfee.view.MainActivityView
import co.to_kanzaki.highwayfee.view.RouteDetailView
import co.to_kanzaki.highwayfee.view.RouteResultView
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivityView.MainActivityActivity)
    fun inject(activity: RouteResultView.RouteResultActivity)
    fun inject(activity: RouteDetailView.RouteDetailActivity)
}