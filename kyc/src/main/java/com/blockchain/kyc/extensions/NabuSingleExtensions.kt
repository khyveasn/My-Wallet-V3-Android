package com.blockchain.kyc.extensions

import com.blockchain.kyc.models.nabu.NabuApiException
import io.reactivex.Completable
import io.reactivex.Single
import piuk.blockchain.kyc.BuildConfig
import retrofit2.HttpException
import timber.log.Timber

internal fun <T> Single<T>.wrapErrorMessage(): Single<T> = this.onErrorResumeNext {
    if (BuildConfig.DEBUG) {
        Timber.e("RX Wrapped Error: {${it.message}")
    }
    when (it) {
        is HttpException -> Single.error(NabuApiException.fromResponseBody(it.response()))
        else -> Single.error(it)
    }
}

internal fun Completable.wrapErrorMessage(): Completable = this.onErrorResumeNext {
    if (BuildConfig.DEBUG) {
        Timber.e("RX Wrapped Error: {${it.message}")
    }

    when (it) {
        is HttpException -> Completable.error(NabuApiException.fromResponseBody(it.response()))
        else -> Completable.error(it)
    }
}