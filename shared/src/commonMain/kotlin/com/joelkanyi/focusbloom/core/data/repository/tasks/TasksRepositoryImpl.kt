/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.core.data.repository.tasks

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.joelkanyi.focusbloom.core.data.local.sqldelight.SharedDatabase
import com.joelkanyi.focusbloom.core.data.mapper.toTask
import com.joelkanyi.focusbloom.core.data.mapper.toTaskEntity
import com.joelkanyi.focusbloom.core.domain.model.Task
import com.joelkanyi.focusbloom.core.domain.repository.tasks.TasksRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(
    private val database: SharedDatabase,
) : TasksRepository {
    override suspend fun getTasks(): Flow<List<Task>> =
        database { db ->
            db.taskQueries
                .getAllTasks()
                .asFlow()
                .mapToList(currentCoroutineContext())
                .map { tasks ->
                    tasks.map {
                        it.toTask()
                    }
                }
        }

    override suspend fun getTask(id: Int): Flow<Task?> =
        database { db ->
            db.taskQueries
                .getTaskById(id)
                .asFlow()
                .mapToOneNotNull(currentCoroutineContext())
                .map { taskEntity ->
                    taskEntity.toTask()
                }
        }

    override suspend fun addTask(task: Task) {
        database { db ->
            task.toTaskEntity().let {
                db.taskQueries.insertTask(
                    name = it.name,
                    description = it.description,
                    start = it.start,
                    color = it.color,
                    current = it.current,
                    date = it.date,
                    focusSessions = it.focusSessions,
                    completed = it.completed,
                    type = it.type,
                    consumedFocusTime = it.consumedFocusTime,
                    consumedShortBreakTime = it.consumedShortBreakTime,
                    consumedLongBreakTime = it.consumedLongBreakTime,
                    inProgressTask = it.inProgressTask,
                    currentCycle = it.currentCycle,
                    active = it.active,
                )
            }
        }
    }

    override suspend fun updateTask(task: Task) {
        database { db ->
            task.toTaskEntity().let {
                db.taskQueries.updateTask(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    start = it.start,
                    color = it.color,
                    current = it.current,
                    date = it.date,
                    focusSessions = it.focusSessions,
                    completed = it.completed,
                    active = it.active,
                )
            }
        }
    }

    override suspend fun deleteTask(id: Int) {
        database {
            it.taskQueries.deleteTaskById(id)
        }
    }

    override suspend fun deleteAllTasks() {
        database {
            it.taskQueries.deleteAllTasks()
        }
    }

    override suspend fun updateConsumedFocusTime(id: Int, focusTime: Long) {
        database {
            it.taskQueries.updateConsumedFocusTime(id = id, consumedFocusTime = focusTime)
        }
    }

    override suspend fun updateConsumedShortBreakTime(id: Int, shortBreakTime: Long) {
        database {
            it.taskQueries.updateConsumedShortBreakTime(
                id = id,
                consumedShortBreakTime = shortBreakTime,
            )
        }
    }

    override suspend fun updateConsumedLongBreakTime(id: Int, longBreakTime: Long) {
        database {
            it.taskQueries.updateConsumedLongBreakTime(
                id = id,
                consumedLongBreakTime = longBreakTime,
            )
        }
    }

    override suspend fun updateTaskInProgress(id: Int, inProgressTask: Boolean) {
        database {
            it.taskQueries.updateInProgressTask(id = id, inProgressTask = inProgressTask)
        }
    }

    override suspend fun updateTaskCompleted(id: Int, completed: Boolean) {
        database {
            it.taskQueries.updateTaskCompleted(id = id, completed = completed)
        }
    }

    override suspend fun updateCurrentSessionName(id: Int, current: String) {
        database {
            it.taskQueries.updateCurrentSessionName(id = id, current = current)
        }
    }

    override suspend fun updateTaskCycleNumber(id: Int, cycle: Int) {
        database {
            it.taskQueries.updateTaskCycleNumber(id = id, currentCycle = cycle)
        }
    }

    override suspend fun getActiveTask(): Flow<Task?> =
        database.invoke {
            it.taskQueries.getActiveTask()
                .asFlow()
                .mapToOneNotNull(currentCoroutineContext())
                .map { taskEntity ->
                    taskEntity.toTask()
                }
        }

    override suspend fun updateTaskActive(id: Int, active: Boolean) {
        database {
            it.taskQueries.updateTaskActiveStatus(id = id, active = active)
        }
    }

    override suspend fun updateAllTasksActiveStatusToInactive() {
        database {
            it.taskQueries.updateAllTasksActiveStatusToInactive()
        }
    }
}
