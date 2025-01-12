package piuk.blockchain.android.ui.confirm;

import android.support.annotation.Nullable;

import piuk.blockchain.android.ui.account.PaymentConfirmationDetails;
import piuk.blockchain.androidcoreui.ui.base.UiState;
import piuk.blockchain.androidcoreui.ui.base.View;

public interface ConfirmPaymentView extends View {

    PaymentConfirmationDetails getPaymentDetails();

    @Nullable String getContactNote();

    @Nullable String getContactNoteDescription();

    void setUiState(@UiState.UiStateDef int uiState);

    void setFromLabel(String fromLabel);

    void setToLabel(String toLabel);

    void setAmount(String amount);

    void setFee(String fee);

    void setTotals(String totalCrypto, String totalFiat);

    void setFiatTotalOnly(String fiatTotal);

    void closeDialog();

    void setContactNote(String contactNote);

    void setContactNoteDescription(String contactNoteDescription);

    void setWarning(String warning);

    void setWarningSubText(String warningSubText);
}
