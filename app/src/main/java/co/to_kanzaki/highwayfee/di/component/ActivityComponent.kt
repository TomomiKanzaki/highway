package co.to_kanzaki.highwayfee.di.component

import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.scope.ActivityScope
import co.to_kanzaki.highwayfee.view.MainActivityView
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivityView.MainActivityActivity)
}