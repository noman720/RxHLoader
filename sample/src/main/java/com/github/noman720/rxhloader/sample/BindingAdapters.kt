package com.github.noman720.rxhloader.sample

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.noman720.rxhloader.R
import com.github.noman720.rxhloader.SyncMaster
import com.github.noman720.rxhloader.sample.model.User
import com.github.noman720.rxhloader.sample.model.UserData
import com.github.noman720.rxhloader.sample.ui.UserAdapter
import com.github.noman720.rxhloader.sample.utils.ApiStatus
import timber.log.Timber

/**
 * Created by Abu Noman on 7/11/19.
 */
@BindingAdapter("showHideMessage")
fun bindShowHideMessage(view: View, apiStatus: ApiStatus) {
    when(apiStatus){
        ApiStatus.IN_PROGRESS -> view.visibility = View.VISIBLE
        ApiStatus.ERROR -> view.visibility = View.VISIBLE
        ApiStatus.SUCCESS -> view.visibility = View.GONE
    }
}

@BindingAdapter("showHideList")
fun bindShowHideList(view: View, apiStatus: ApiStatus) {
    when(apiStatus){
        ApiStatus.IN_PROGRESS -> view.visibility = View.GONE
        ApiStatus.ERROR -> view.visibility = View.GONE
        ApiStatus.SUCCESS -> view.visibility = View.VISIBLE
    }
}

@BindingAdapter("setImage")
fun bindUserImage(view: ImageView, imageUrl: String?) {
    Timber.w("imageUrl: %s", imageUrl)
    imageUrl?.let {
        SyncMaster.with(view.context).load(it).into(view)
    }?:view.setImageResource(R.drawable.ic_person)
}

@BindingAdapter("data")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<UserData>?) {
    val adapter = recyclerView.adapter as UserAdapter
    adapter.submitList(data)
}
