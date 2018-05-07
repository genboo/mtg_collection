package ru.devsp.app.mtgcollections.model.objects

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("releaseDate")])
data class Set(@PrimaryKey var code : String){

    var name: String = ""

    var border: String? = ""

    var releaseDate: String = ""

    var block: String? = ""
}