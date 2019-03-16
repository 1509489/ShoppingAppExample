package com.pixelart.shoppingappexample.common

import androidx.annotation.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

enum class RxBus{
    INSTANCE;

    private val behaviorSubject: BehaviorSubject<Any> = BehaviorSubject.create()

    fun subscribe(@NonNull event: Consumer<Any>): Disposable = behaviorSubject.subscribe(event)

    fun post(@NonNull data: Any){
        behaviorSubject.onNext(data)
    }
}