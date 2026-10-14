package com.tiever.qxn

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tiever.qxn.data.db.AppDatabase
import com.tiever.qxn.data.prefs.SettingsStore
import com.tiever.qxn.data.repo.TimetableRepository
import com.tiever.qxn.notify.ReminderScheduler
import com.tiever.qxn.notify.SilenceScheduler
import com.tiever.qxn.util.CrashLogger

class QxnApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        CrashLogger.install(this)
        container = AppContainer(this)
    }
}

/** 极简手工依赖容器 —— 这个规模的 App 引入 Hilt 不划算。 */
class AppContainer(context: Context) {

    val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "qxn_timetable.db",
    ).fallbackToDestructiveMigration().build()

    val settings: SettingsStore = SettingsStore(context.applicationContext)

    val repo: TimetableRepository = TimetableRepository(context.applicationContext, db, settings)

    val reminders: ReminderScheduler = ReminderScheduler(context.applicationContext, repo)

    /** 上课自动静音（依赖「通知策略访问权限」）。 */
    val silence: SilenceScheduler = SilenceScheduler(context.applicationContext, repo)
}
