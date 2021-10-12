package io.github.wulkanowy.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration42 : Migration(41, 42) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE TimetableAdditional ADD COLUMN is_added_by_user INTEGER NOT NULL DEFAULT 0")
    }
}
