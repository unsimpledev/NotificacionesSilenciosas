package prueba.notificaciones.silenciosas.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reunion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val timestamp: Long //La fecha en epoch time
)
