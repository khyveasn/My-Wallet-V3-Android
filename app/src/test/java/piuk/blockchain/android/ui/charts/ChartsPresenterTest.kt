package piuk.blockchain.android.ui.charts

import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import info.blockchain.balance.CryptoCurrency
import info.blockchain.wallet.prices.data.PriceDatum
import io.reactivex.Observable
import org.amshove.kluent.`should be`
import org.amshove.kluent.any
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test
import piuk.blockchain.androidcore.data.charts.ChartsDataManager
import piuk.blockchain.androidcore.data.charts.TimeSpan
import piuk.blockchain.androidcore.data.charts.models.ChartDatumDto
import piuk.blockchain.androidcore.data.currency.CurrencyFormatManager
import piuk.blockchain.androidcore.data.exchangerate.ExchangeRateDataManager
import piuk.blockchain.androidcore.utils.PersistentPrefs
import java.util.Locale

class ChartsPresenterTest {

    private lateinit var subject: ChartsPresenter
    private val chartsDataManager: ChartsDataManager = mock()
    private val exchangeRateFactory: ExchangeRateDataManager = mock()
    private val prefs: PersistentPrefs = mock()
    private val view: ChartsView = mock()
    private val currencyFormatManager: CurrencyFormatManager = mock()

    @Before
    fun setUp() {

        subject = ChartsPresenter(
            chartsDataManager,
            exchangeRateFactory,
            prefs,
            currencyFormatManager
        )

        subject.initView(view)
    }

    @Test
    fun `onViewReady success`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.BTC, fiat)).thenReturn(13950.0)
        whenever(currencyFormatManager.getFiatSymbol(any(), any())).thenReturn(fiat)
        whenever(chartsDataManager.getMonthPrice(CryptoCurrency.BTC, fiat))
            .thenReturn(Observable.just(chartData))
        // Act
        subject.onViewReady()
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.MONTH
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.MONTH))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Data(listOf(chartData), fiat))
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.BTC)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getMonthPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `onViewReady failure`() {
        // Arrange
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.BTC, fiat)).thenReturn(13950.0)
        whenever(chartsDataManager.getMonthPrice(CryptoCurrency.BTC, fiat))
            .thenReturn(Observable.error(Throwable()))
        // Act
        subject.onViewReady()
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.MONTH
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.MONTH))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Error)
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.BTC)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getMonthPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `setSelectedTimeSpan day`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.BTC, fiat)).thenReturn(13950.0)
        whenever(chartsDataManager.getDayPrice(CryptoCurrency.BTC, fiat))
            .thenReturn(Observable.just(chartData))
        whenever(currencyFormatManager.getFiatSymbol(any(), any())).thenReturn(fiat)
        // Act
        subject.selectedTimeSpan = TimeSpan.DAY
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.DAY
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.DAY))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Data(listOf(chartData), fiat))
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.BTC)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getDayPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.BTC, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `setSelectedTimeSpan week ETH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.ETHER)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.ETHER, fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getWeekPrice(CryptoCurrency.ETHER, fiat))
            .thenReturn(Observable.just(chartData))
        whenever(currencyFormatManager.getFiatSymbol(any(), any())).thenReturn(fiat)
        // Act
        subject.selectedTimeSpan = TimeSpan.WEEK
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.WEEK
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.WEEK))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Data(listOf(chartData), fiat))
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.ETHER)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getWeekPrice(CryptoCurrency.ETHER, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.ETHER, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `setSelectedTimeSpan year ETH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.ETHER)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.ETHER, fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getYearPrice(CryptoCurrency.ETHER, fiat))
            .thenReturn(Observable.just(chartData))
        whenever(currencyFormatManager.getFiatSymbol(any(), any())).thenReturn(fiat)
        // Act
        subject.selectedTimeSpan = TimeSpan.YEAR
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.YEAR
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.YEAR))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Data(listOf(chartData), fiat))
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.ETHER)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getYearPrice(CryptoCurrency.ETHER, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.ETHER, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `setSelectedTimeSpan all time BCH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrency.BCH)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefs.selectedFiatCurrency).thenReturn(fiat)
        whenever(exchangeRateFactory.getLastPrice(CryptoCurrency.BCH, fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getAllTimePrice(CryptoCurrency.BCH, fiat))
            .thenReturn(Observable.just(chartData))
        whenever(currencyFormatManager.getFiatSymbol(any(), any())).thenReturn(fiat)
        // Act
        subject.selectedTimeSpan = TimeSpan.ALL_TIME
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.ALL_TIME
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        verify(view).updateChartState(ChartsState.TimeSpanUpdated(TimeSpan.ALL_TIME))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Data(listOf(chartData), fiat))
        verify(view).updateCurrentPrice(any())
        verify(view).updateSelectedCurrency(CryptoCurrency.BCH)
        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getAllTimePrice(CryptoCurrency.BCH, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastPrice(CryptoCurrency.BCH, fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefs, atLeastOnce()).selectedFiatCurrency
        verifyNoMoreInteractions(prefs)
    }
}