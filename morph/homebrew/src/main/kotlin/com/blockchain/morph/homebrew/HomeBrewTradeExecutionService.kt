package com.blockchain.morph.homebrew

import com.blockchain.morph.exchange.mvi.Quote
import com.blockchain.morph.exchange.service.TradeExecutionService
import com.blockchain.morph.exchange.service.TradeTransaction
import com.blockchain.nabu.api.QuoteJson
import com.blockchain.nabu.api.TradeDiagnostics
import com.blockchain.nabu.api.TradeRequest
import com.blockchain.nabu.service.NabuMarketsService
import io.reactivex.Completable
import io.reactivex.Single
import java.math.BigDecimal
import java.math.BigInteger

internal class HomeBrewTradeExecutionService(private val marketsService: NabuMarketsService) : TradeExecutionService {

    private var diagnostics: TradeDiagnostics? = null

    override fun executeTrade(
        quote: Quote,
        destinationAddress: String,
        refundAddress: String
    ): Single<TradeTransaction> {
        val rawQuote = quote.rawQuote ?: throw IllegalArgumentException("No quote supplied")
        val quoteJson = rawQuote as? QuoteJson ?: throw IllegalArgumentException("Quote is not expected type")

        return marketsService.executeTrade(
            TradeRequest(
                destinationAddress = destinationAddress,
                refundAddress = refundAddress,
                quote = quoteJson
            )
        ).map { it }
    }

    override fun putTradeFailureReason(tradeRequest: TradeTransaction, txHash: String?, message: String?): Completable {
        return marketsService.putTradeFailureReason(
            tradeRequestId = tradeRequest.id,
            txHash = txHash,
            message = message,
            diagnostics = diagnostics
        )
    }

    override fun updateDiagnotics(
        maxAvailable: BigInteger,
        tradeValueCrypto: BigInteger,
        tradeValueFiat: BigDecimal
    ) {
        diagnostics = TradeDiagnostics(
            maxAvailable = maxAvailable.toString(),
            tradeValueCrypto = tradeValueCrypto.toString(),
            tradeValueFiat = tradeValueFiat.toString()
        )
    }
}
