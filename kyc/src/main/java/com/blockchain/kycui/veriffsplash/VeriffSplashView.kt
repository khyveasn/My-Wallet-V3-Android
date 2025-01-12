package com.blockchain.kycui.veriffsplash

import com.blockchain.kyc.models.nabu.SupportedDocuments
import com.blockchain.veriff.VeriffApplicantAndToken
import io.reactivex.Observable
import piuk.blockchain.androidcoreui.ui.base.UiState
import piuk.blockchain.androidcoreui.ui.base.View

interface VeriffSplashView : View {

    val countryCode: String

    val nextClick: Observable<Unit>

    val swapClick: Observable<Unit>

    fun continueToVeriff(applicant: VeriffApplicantAndToken)

    fun continueToCompletion()

    fun continueToSwap()

    fun supportedDocuments(documents: List<SupportedDocuments>)

    fun setUiState(@UiState.UiStateDef state: Int)

    fun showErrorToast(message: Int)

    fun showProgressDialog(cancelable: Boolean)

    fun dismissProgressDialog()
}