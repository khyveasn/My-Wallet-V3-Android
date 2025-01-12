package piuk.blockchain.android.ui.swap.homebrew.exchange.confirmation

import android.support.annotation.StringRes
import com.blockchain.morph.exchange.mvi.ExchangeViewState
import info.blockchain.balance.CryptoValue
import io.reactivex.Observable
import piuk.blockchain.android.ui.swap.homebrew.exchange.model.SwapErrorDialogContent
import piuk.blockchain.android.ui.swap.homebrew.exchange.model.Trade
import piuk.blockchain.androidcoreui.ui.base.View
import piuk.blockchain.androidcoreui.ui.customviews.ToastCustom
import java.util.Locale

interface ExchangeConfirmationView : View {

    val locale: Locale

    val exchangeViewState: Observable<ExchangeViewState>

    fun onTradeSubmitted(trade: Trade, firstGoldPaxTrade: Boolean)

    fun showSecondPasswordDialog()

    fun showProgressDialog()

    fun dismissProgressDialog()

    fun displayErrorDialog(@StringRes message: Int)

    fun displayErrorBottomDialog(swapErrorDialogContent: SwapErrorDialogContent)

    fun updateFee(cryptoValue: CryptoValue)

    fun goBack()

    fun openTiersCard()

    fun openMoreInfoLink(link: String)

    fun showToast(@StringRes message: Int, @ToastCustom.ToastType type: String)
}
