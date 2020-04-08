package com.example.searchmovielocalcache.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        const val TAG = "MainViewModel"
        const val QUERY_EXHAUSTED = "No more results."
    }

    private var viewState: MutableLiveData<ViewState>? = null

    enum class ViewState{
        IMAGE, MOVIES
    }

    init {
        initState()
    }

    private fun initState(){
        if (viewState == null){
            viewState = MutableLiveData()
            viewState!!.value = ViewState.IMAGE
        }
    }

    fun getViewState(): LiveData<ViewState>{
        return viewState!!
    }


}