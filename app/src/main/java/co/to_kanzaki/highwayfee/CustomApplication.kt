package co.to_kanzaki.highwayfee

import android.app.Application
import co.to_kanzaki.highwayfee.di.component.ApplicationComponent
import co.to_kanzaki.highwayfee.di.component.DaggerApplicationComponent
import co.to_kanzaki.highwayfee.di.module.ApplicationModule
import com.facebook.stetho.Stetho

open class CustomApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initAppComponent()

        component.inject(this)

        initStetho()

    }

    protected fun initAppComponent() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build()
            )
        }
    }

    fun getAppComponent(): ApplicationComponent {
        return component
    }

}