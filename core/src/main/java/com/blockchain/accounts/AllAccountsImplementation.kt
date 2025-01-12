package com.blockchain.accounts

import info.blockchain.balance.CryptoCurrency

internal class AllAccountsImplementation(
    private val btcAccountList: AccountList,
    private val bchAccountList: AccountList,
    private val etherAccountList: AccountList,
    private val paxAccountList: AccountList
) : AllAccountList {

    override fun get(cryptoCurrency: CryptoCurrency): AccountList {
        return when (cryptoCurrency) {
            CryptoCurrency.BTC -> btcAccountList
            CryptoCurrency.ETHER -> etherAccountList
            CryptoCurrency.BCH -> bchAccountList
            CryptoCurrency.XLM -> throw IllegalArgumentException("XLM default account access requires RX")
            CryptoCurrency.PAX -> paxAccountList
        }
    }
}
