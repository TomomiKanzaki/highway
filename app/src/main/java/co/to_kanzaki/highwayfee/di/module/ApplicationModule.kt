package co.to_kanzaki.highwayfee.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import co.to_kanzaki.highwayfee.BuildConfig
import co.to_kanzaki.highwayfee.CustomApplication
import co.to_kanzaki.highwayfee.Service
import co.to_kanzaki.highwayfee.Util.AppSchedulerProvider
import co.to_kanzaki.highwayfee.Util.DefaultPrefs
import co.to_kanzaki.highwayfee.Util.SchedulerProvider
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.URLEncoder
import javax.inject.Singleton

@Module
class ApplicationModule(val application: CustomApplication) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = application

    @Singleton
    @Provides
    fun provideDefaultPrefsWrapper(context: Context): DefaultPrefsWrapper{
        return DefaultPrefsWrapper(context)
    }

    /**
     * APで生成するDefaultPrefsはDaggerからProvideできないため、ラップして間接的に提供する
     */
    open class DefaultPrefsWrapper(val context: Context) {
        val prefs: DefaultPrefs by lazy { DefaultPrefs(context) }
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    fun provideMoshi() = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideService(client: OkHttpClient): Service {

        return Retrofit.Builder().client(client)
                .baseUrl(BuildConfig.API_ROOT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(Service::class.java)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

}