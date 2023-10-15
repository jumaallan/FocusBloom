package com.joelkanyi.focusbloom.core.data.local.sqldelight

import app.cash.sqldelight.ColumnAdapter
import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import database.TaskEntity

class SharedDatabase(
    private val databaseDriverFactory: DatabaseDriverFactory,
) {
    private var database: BloomDatabase? = null

    private val idAdapter = object : ColumnAdapter<Int, Long> {
        override fun decode(databaseValue: Long): Int {
            return databaseValue.toInt()
        }

        override fun encode(value: Int): Long {
            return value.toLong()
        }
    }

    private val activeAdapter = object : ColumnAdapter<Boolean, Long> {
        override fun decode(databaseValue: Long): Boolean {
            return databaseValue.toInt() == 1
        }

        override fun encode(value: Boolean): Long {
            return if (value) 1 else 0
        }
    }

    private val completedAdapter = object : ColumnAdapter<Boolean, Long> {
        override fun decode(databaseValue: Long): Boolean {
            return databaseValue.toInt() == 1
        }

        override fun encode(value: Boolean): Long {
            return if (value) 1 else 0
        }
    }

    private val colorAdapter = object : ColumnAdapter<Long, Long> {
        override fun decode(databaseValue: Long): Long {
            return databaseValue
        }

        override fun encode(value: Long): Long {
            return value
        }
    }

    private val consumedFocusTimeAdapter = object : ColumnAdapter<Long, Long> {
        override fun decode(databaseValue: Long): Long {
            return databaseValue
        }

        override fun encode(value: Long): Long {
            return value
        }
    }

    private val consumedLongBreakTimeAdapter = object : ColumnAdapter<Long, Long> {
        override fun decode(databaseValue: Long): Long {
            return databaseValue
        }

        override fun encode(value: Long): Long {
            return value
        }
    }

    private val consumedShortBreakTimeAdapter = object : ColumnAdapter<Long, Long> {
        override fun decode(databaseValue: Long): Long {
            return databaseValue
        }

        override fun encode(value: Long): Long {
            return value
        }
    }

    private val currentAdapter = object : ColumnAdapter<String, String> {
        override fun decode(databaseValue: String): String {
            return databaseValue
        }

        override fun encode(value: String): String {
            return value
        }
    }

    private val currentCycleAdapter = object : ColumnAdapter<Int, Long> {
        override fun decode(databaseValue: Long): Int {
            return databaseValue.toInt()
        }

        override fun encode(value: Int): Long {
            return value.toLong()
        }
    }

    private val focusSessionsAdapter = object : ColumnAdapter<Int, Long> {
        override fun decode(databaseValue: Long): Int {
            return databaseValue.toInt()
        }

        override fun encode(value: Int): Long {
            return value.toLong()
        }
    }

    private val inProgressTaskAdapter = object : ColumnAdapter<Boolean, Long> {
        override fun decode(databaseValue: Long): Boolean {
            return databaseValue.toInt() == 1
        }

        override fun encode(value: Boolean): Long {
            return if (value) 1 else 0
        }
    }

    private suspend fun initDatabase() {
        if (database == null) {
            database = BloomDatabase.invoke(
                driver = databaseDriverFactory.createDriver(
                    schema = BloomDatabase.Schema,
                ),
                taskEntityAdapter = TaskEntity.Adapter(
                    idAdapter = idAdapter,
                    colorAdapter = colorAdapter,
                    consumedFocusTimeAdapter = consumedFocusTimeAdapter,
                    consumedLongBreakTimeAdapter = consumedLongBreakTimeAdapter,
                    consumedShortBreakTimeAdapter = consumedShortBreakTimeAdapter,
                    currentAdapter = currentAdapter,
                    currentCycleAdapter = currentCycleAdapter,
                    focusSessionsAdapter = focusSessionsAdapter,
                )
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (BloomDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }
}
