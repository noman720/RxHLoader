package com.github.noman720.rxhloader.sample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.noman720.rxhloader.R
import com.github.noman720.rxhloader.SyncMaster
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.image)

        SyncMaster.with(this)
            .load("https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=200\\u0026fit=max\\u0026s=9fba74be19d78b1aa2495c0200b9fbce")
            .into(imageView)

        val syncButton = findViewById<Button>(R.id.btn_sync)
        syncButton.setOnClickListener {
            SyncMaster.with(this)
                .fetch("http://pastebin.com/raw/wgkJgazE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("Sync", it)
                }
        }
    }

}
