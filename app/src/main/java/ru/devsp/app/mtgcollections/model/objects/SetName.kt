package ru.devsp.app.mtgcollections.model.objects

import android.arch.persistence.room.ColumnInfo

data class SetName(@ColumnInfo(name = "setName")
                   var setName: String)
