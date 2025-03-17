package prueba.notificaciones.silenciosas.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import prueba.notificaciones.silenciosas.R
import prueba.notificaciones.silenciosas.database.AppDatabase
import prueba.notificaciones.silenciosas.database.Reunion

class SincronizarReunionesWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("WorkManager", "Sincronizando reuniones con el servidor...")

        val db = AppDatabase.getDatabase(applicationContext)
        val reunionesDao = db.reunionDao()

        // Simulación de descarga desde el servidor (aquí iría una API real)
        val nuevasReuniones = listOf(
            Reunion(titulo = "Reunión de equipo", timestamp = System.currentTimeMillis()),
            Reunion(titulo = "Llamada con cliente", timestamp = System.currentTimeMillis() + 3600000) // +1 hora
        )

        // 🔹 Guardamos en la base de datos local
        nuevasReuniones.forEach { reunionesDao.insertarReunion(it) }

        Log.d("WorkManager", "Reuniones sincronizadas en la base de datos")

        //Verificamos si hay reuniones para hoy y mostramos notificación
        val reunionesHoy = reunionesDao.obtenerReunionesDelDia(System.currentTimeMillis())
        if (reunionesHoy.isNotEmpty()) {
            Log.d("WorkManager", "Hay reuniones hoy, mostrando notificación")
            mostrarNotificacion("Tienes reuniones programadas para hoy")
        } else {
            Log.d("WorkManager", "No hay reuniones hoy, no se muestra notificación")
        }

        return Result.success()
    }

    private fun mostrarNotificacion(mensaje: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "recordatorio_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle("¡Recordatorio!")
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(100, notification)
    }
}