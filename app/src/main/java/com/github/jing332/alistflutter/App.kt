package com.github.jing332.alistflutter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Looper
import com.github.jing332.alistandroid.BuildConfig
import com.github.jing332.alistandroid.CrashHandler
import com.github.jing332.alistandroid.data.appDb
import com.github.jing332.alistandroid.data.entities.ServerLog
import com.github.jing332.alistandroid.util.ClipboardUtils
import com.github.jing332.alistandroid.util.ToastUtils.longToast
import com.github.jing332.alistflutter.model.alist.Logger
import com.github.jing332.alistflutter.model.alist.Logger.Listener
import xcrash.XCrash
import java.io.File
import java.net.InetAddress
import java.net.UnknownHostException

val app by lazy { App.application }

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appDb.serverLogDao.deleteAll()
        Logger.addListener(object : Listener {
            override fun onLog(level: Int, time: String, msg: String) {
                appDb.serverLogDao.insert(ServerLog(level = level, time = time, message = msg))
            }
        })
        // CrashHandler(this)
        // startService(Intent(this, AListService::class.java))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        application = this

        XCrash.init(
            this,
            XCrash.InitParameters()
                .setLogDir(this.getExternalFilesDir("xCrash")!!.absolutePath)
                .setAppVersion(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")")
                .setJavaCallback { logPath, emergency ->
                    longToast("程序崩溃：${logPath}")
                    copyLog(logPath)
                }
                .setNativeCallback { logPath, emergency ->
                    longToast("底层程序崩溃：${logPath}")
                    copyLog(logPath)
                }
                .setJavaRethrow(true)
                .setNativeRethrow(true)
        )
    }

    private fun copyLog(logPath: String) {
        val file = File(logPath)
        file.inputStream().use {
            it.bufferedReader().use { reader ->
                val sb = StringBuilder()
                for (i in 0..300) {
                    sb.appendLine(reader.readLine() ?: break)
                }
                ClipboardUtils.copyText(sb.toString())
            }
        }
    }

    companion object {
        lateinit var application: Application

        fun getByName(ip: String?): InetAddress? {
            return try {
                InetAddress.getByName(ip)
            } catch (unused: UnknownHostException) {
                null
            }
        }

        val isMainThread: Boolean
            get() = Looper.getMainLooper().thread.id == Thread.currentThread().id
    }
}
