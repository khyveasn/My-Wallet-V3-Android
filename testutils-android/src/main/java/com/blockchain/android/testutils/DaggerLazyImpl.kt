package com.blockchain.android.testutils

import dagger.Lazy

class DaggerLazyImpl<T>(private val clazz: T) : Lazy<T> {
    override fun get(): T = clazz
}