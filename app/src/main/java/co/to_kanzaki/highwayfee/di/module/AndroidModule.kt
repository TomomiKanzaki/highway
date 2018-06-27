package co.to_kanzaki.highwayfee.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideTelephonyManager(context: Context): TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}