package com.klt.unsplash

class NativeLib {

    /**
     * A native method that is implemented by the 'unsplash' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'unsplash' library on application startup.
        init {
            System.loadLibrary("unsplash")
        }
    }
}