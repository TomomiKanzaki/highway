package co.to_kanzaki.highwayfee.presenter

import android.content.Intent
import android.util.Log
import co.to_kanzaki.highwayfee.Util.SchedulerProvider
import co.to_kanzaki.highwayfee.Util.ResourceResolver
import co.to_kanzaki.highwayfee.db.DataBaseRoute
import co.to_kanzaki.highwayfee.di.scope.ActivityScope
import co.to_kanzaki.highwayfee.exception.AppException
import co.to_kanzaki.highwayfee.usecase.MainActivityUseCase
import co.to_kanzaki.highwayfee.view.MainActivityView
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import javax.inject.Inject

@ActivityScope
class MainActivityPresenter @Inject constructor(
        private val compositeDisposable: CompositeDisposable,
        private val useCase: MainActivityUseCase,
        private val schedulerProvider: SchedulerProvider) {

    val TAG = MainActivityPresenter::class.java.simpleName

    var view: MainActivityView? = null
    private set

    fun onCreate() {
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun takeView(view: MainActivityView) {
        this.view = view
    }

    fun dropView() {
        this.view = null
    }

    val isViewAttached: Boolean
        get() = view != null

    fun onClickBtn(from: String,
                   to: String,
                   carType: String,
                   prefecture: String?,
                   sortBy: String?){

        getRouteHighWay(from, to, carType, prefecture, sortBy)
    }

    private fun getRouteHighWay(from: String,
                                to: String,
                                carType: String,
                                prefecture: String?,
                                sortBy: String?){
        val view = view?: return

        if (useCase.existSameRoute(from, to, carType)){
            view.hideProgress()
            view.goNextView(from, to)
        } else {
            val disposable = useCase.getRouteHighWay(from, to, carType, prefecture, sortBy)
                    .doOnSubscribe{
                        view.showProgress()
                    }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        val insert = useCase.insertRoute(it)
                                .observeOn(schedulerProvider.ui())
                                .subscribe({
                                    view.hideProgress()
                                    if (it.Routes?.get(0) != null){
                                        view.goNextView(from, to)
                                    } else {
                                        view.showToast("該当するICが見つかりません。")
                                    }
                                    Log.i("success ", "${it.From}")
                                },{
                                    Log.e("Exception ", "$TAG insertRoute $it")
                                })
                        compositeDisposable.add(insert)
                    },{
                        view.hideProgress()
                        Log.e("Exception ", "$TAG getRouteHighWay" + it.toString())
                        //処理済み例外の場合は内部メッセージをログ出力し、ユーザ向けメッセージがある場合はトーストで出す
                        if (it is AppException) {
                            Log.e("Exception ", "$TAG getRouteHighWay ${it.innerMessage}")
                            it.userMessage?.let {
                                view.showToast(it)
                            }
                        }
                    })

            compositeDisposable.add(disposable)
        }
    }
}