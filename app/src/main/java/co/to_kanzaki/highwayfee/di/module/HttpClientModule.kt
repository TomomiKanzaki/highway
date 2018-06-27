package co.to_kanzaki.highwayfee.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class HttpClientModule {

    private val CACHE_FILE_NAME = "okhttp.cache"

    private val MAX_CACHE_SIZE = (4 * 1024 * 1024).toLong()

    @Singleton
    @Provides
    fun provideHttpClient(context: Context,
                          defaultPrefsWrapper: ApplicationModule.DefaultPrefsWrapper): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val cacheDir = File(context.cacheDir, CACHE_FILE_NAME)
        val cache = Cache(cacheDir, MAX_CACHE_SIZE)

//        val headerInterceptor = HeaderInterceptor(defaultPrefsWrapper.prefs)


        val c = OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
//                .addInterceptor(headerInterceptor)
//                .authenticator(tokenRefreshAuthenticator)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
        return c.build()
    }

//    private class HeaderInterceptor(val defaultPrefs: DefaultPrefs) : Interceptor {
//        override fun intercept(chain: Interceptor.Chain?): Response? {
//
//            chain?: return null
//
//            val accessToken = defaultPrefs.accessToken
//
//            val request = chain.request().newBuilder()
//                    .header("Authorization", "Bearer " + accessToken)
//                    .build()
//
//            return chain.proceed(request)
//
//        }
//
//    }
}