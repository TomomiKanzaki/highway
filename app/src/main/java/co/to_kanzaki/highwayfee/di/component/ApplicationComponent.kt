package co.to_kanzaki.highwayfee.di.component

import co.to_kanzaki.highwayfee.CustomApplication
import co.to_kanzaki.highwayfee.di.module.ActivityModule
import co.to_kanzaki.highwayfee.di.module.AndroidModule
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import co.to_kanzaki.highwayfee.di.module.HttpClientModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, AndroidModule::class, HttpClientModule::class))
interface ApplicationComponent {
    fun inject(application: CustomApplication)
    fun plus(module: ActivityModule): ActivityComponent
}