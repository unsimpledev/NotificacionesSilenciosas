package prueba.notificaciones.silenciosas.services

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import prueba.notificaciones.silenciosas.workers.SincronizarReunionesWorker

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.let { data ->
            val accion = data["accion"]

            if (accion == "sincronizar_reuniones") {
                Log.d("PushSilenciosa", "Sincronizando reuniones")

                val workRequest = OneTimeWorkRequestBuilder<SincronizarReunionesWorker>()
                    .build()
                WorkManager.getInstance(applicationContext).enqueue(workRequest)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token generado: $token")
    }

}
