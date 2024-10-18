package com.abbosidev.infrastructure.util

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async

fun String.removePrePlus() = if (this.first() == '+') this.drop(1) else this

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> withSession(block: suspend () -> T): Uni<T> =
    Panache.withSession {
        CoroutineScope(Dispatchers.Unconfined).async { block() }.asUni()
    }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> withTransaction(block: suspend () -> T): Uni<T> =
    Panache.withTransaction {
        CoroutineScope(Dispatchers.Unconfined).async { block() }.asUni()
    }