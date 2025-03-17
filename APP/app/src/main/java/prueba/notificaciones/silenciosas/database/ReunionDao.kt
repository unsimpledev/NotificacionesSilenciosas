package prueba.notificaciones.silenciosas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReunionDao {
    @Query("SELECT * FROM Reunion WHERE date(timestamp / 1000, 'unixepoch') = date(:fecha / 1000, 'unixepoch')")
    fun obtenerReunionesDelDia(fecha: Long): List<Reunion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReunion(reunion: Reunion)
}
