package com.github.noman720.rxhloader.sample.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.noman720.rxhloader.SyncMaster
import com.github.noman720.rxhloader.sample.model.UserData
import com.github.noman720.rxhloader.sample.utils.ApiStatus
import com.github.noman720.rxhloader.sample.utils.NetworkChecker
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


/**
 * Created by Abu Noman on 7/21/19.
 */
class UserListViewModel constructor(
    private val context: Context
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder().build()
    }

    private val _usersLiveData: MutableLiveData<List<UserData>> by lazy {
        MutableLiveData<List<UserData>>()
    }

    private val _fetchingStatus: MutableLiveData<ApiStatus> by lazy {
        MutableLiveData<ApiStatus>()
    }

    fun fetchingStatus(): LiveData<ApiStatus> {
        return _fetchingStatus
    }

    private val _messageLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getMessage(): LiveData<String>{
        return _messageLiveData
    }

    fun getUsers(): LiveData<List<UserData>>{
        return _usersLiveData
    }

    fun onRefresh(){
        fetchUsers()
    }

    fun fetchUsers(){
        if (NetworkChecker.isConnected(context)) {
            _fetchingStatus.value = ApiStatus.IN_PROGRESS

            compositeDisposable.add(SyncMaster.with(context)
                .fetch("http://pastebin.com/raw/wgkJgazE")
                .subscribeOn(Schedulers.io())
                .map{
                    val listMyData = Types.newParameterizedType(List::class.java, UserData::class.java)
                    val adapter: JsonAdapter<List<UserData>> = moshi.adapter(listMyData)
                    adapter.fromJson(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d(it.toString())
                    _usersLiveData.value = it
                    _fetchingStatus.value = ApiStatus.SUCCESS
                }, {
                    _fetchingStatus.value = ApiStatus.ERROR
                    _messageLiveData.value = "Data not found!"
                })
            )
        } else {
            _fetchingStatus.value = ApiStatus.ERROR
            _messageLiveData.value = "No network found!"
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
