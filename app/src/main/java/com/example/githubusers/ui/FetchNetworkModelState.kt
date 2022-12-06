package com.example.githubusers.ui

sealed class FetchNetworkModelState<out T> {
    object NeverFetched : FetchNetworkModelState<Nothing>()
    object Fetching : FetchNetworkModelState<Nothing>()
    class RefreshedOK<out T>(val data: T) : FetchNetworkModelState<T>()
    class FetchedError(val exception: Exception) : FetchNetworkModelState<Nothing>()
}
