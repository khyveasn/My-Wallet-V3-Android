package com.blockchain.kycui.status

import com.blockchain.BaseKycPresenter
import com.blockchain.kyc.models.nabu.Kyc2TierState
import com.blockchain.kyc.models.nabu.KycState
import com.blockchain.kycui.settings.KycStatusHelper
import com.blockchain.nabu.NabuToken
import com.blockchain.notifications.NotificationTokenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import piuk.blockchain.kyc.R
import timber.log.Timber

class KycStatusPresenter(
    nabuToken: NabuToken,
    private val kycStatusHelper: KycStatusHelper,
    private val notificationTokenManager: NotificationTokenManager
) : BaseKycPresenter<KycStatusView>(nabuToken) {

    override fun onViewReady() {
        compositeDisposable +=
            kycStatusHelper.getKyc2TierStatus()
                .map { it.toKycState() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgressDialog() }
                .doOnEvent { _, _ -> view.dismissProgressDialog() }
                .doOnError { Timber.e(it) }
                .subscribeBy(
                    onSuccess = { view.renderUi(it) },
                    onError = { view.finishPage() }
                )
    }

    private fun Kyc2TierState.toKycState(): KycState =
        when (this) {
            Kyc2TierState.Hidden -> KycState.None
            Kyc2TierState.Locked -> KycState.None
            Kyc2TierState.Tier1InReview -> KycState.Pending
            Kyc2TierState.Tier1Approved -> KycState.Verified
            Kyc2TierState.Tier1Failed -> KycState.Rejected
            Kyc2TierState.Tier2InReview -> KycState.Pending
            Kyc2TierState.Tier2Approved -> KycState.Verified
            Kyc2TierState.Tier2Failed -> KycState.Rejected
        }

    internal fun onClickNotifyUser() {
        compositeDisposable +=
            notificationTokenManager.enableNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgressDialog() }
                .doOnEvent { view.dismissProgressDialog() }
                .subscribeBy(
                    onComplete = {
                        view.showNotificationsEnabledDialog()
                    },
                    onError = {
                        view.showToast(R.string.kyc_status_button_notifications_error)
                        Timber.e(it)
                    }
                )
    }

    internal fun onClickContinue() {
        view.startExchange()
    }

    internal fun onProgressCancelled() {
        // Cancel subscriptions
        compositeDisposable.clear()
        // Exit page as UI won't be rendered
        view.finishPage()
    }
}
