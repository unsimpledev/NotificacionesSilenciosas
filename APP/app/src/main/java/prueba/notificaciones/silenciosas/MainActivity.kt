package prueba.notificaciones.silenciosas

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import prueba.notificaciones.silenciosas.database.AppDatabase
import prueba.notificaciones.silenciosas.database.ReunionDao
import prueba.notificaciones.silenciosas.ui.theme.NotificacionesSilenciosasTheme

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var reunionesDao: ReunionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //FUNDAMENTAL: Permiso para mostrar notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        //Inicializar la base de datos y Firebase Messaging
        inicializarFirebaseYBaseDeDatos()

        setContent {
            NotificacionesSilenciosasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun inicializarFirebaseYBaseDeDatos() {
        db = AppDatabase.getDatabase(this)
        reunionesDao = db.reunionDao()

        //Suscribimos al topic en Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("reuniones")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Suscripción al topic exitosa")
                } else {
                    println("Error al suscribirse al topic")
                }
            }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NotificacionesSilenciosasTheme {
            Greeting("Android")
        }
    }
}