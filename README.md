# Reactive Hybrid Loader
Android reactive hybrid loader for loading Image, JSON, XML etc.

# Sample List View using the RxHLoader library
![Alt text](https://github.com/noman720/RxHLoader/blob/master/doc/sample_app_screen.gif "Sample List")

# How to use?

1. Import the library to your applicaton (will provide gradle dependencies later)

> Add maven repository to your root gradle
```gradle
allprojects {
        repositories {
                ...
                maven { url 'https://jitpack.io' }
        }
}
```
> Add denpendency to your project
```gradle
implementation 'com.github.noman720:RxHLoader:0.1.0'
```

2. To load data (XML, JSON, etc) add the following line

```kotlin
SyncMaster.with(this)
        .fetch("http://pastebin.com/raw/wgkJgazE")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            Log.d("Sync", it)
        }
```

3. To load image use the following line

```kotlin
SyncMaster.with(this)
        .load("https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=200\\u0026fit=max\\u0026s=9fba74be19d78b1aa2495c0200b9fbce")
        .into(imageView)
```
