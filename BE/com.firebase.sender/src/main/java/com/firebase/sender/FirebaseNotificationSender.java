package com.firebase.sender;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseNotificationSender {

    public static final String TOPIC = "reuniones";
    public static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/" + Constants.PROJECT_ID + "/messages:send";

    
    //Obtenemos el Access Token desde Firebase
    private static String obtenerAccessToken() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(Constants.CREDENTIALS_FILE_PATH))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");

        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }

    //Enviamos la notificación push a un topic
    public static void enviarNotificacion() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String accessToken = obtenerAccessToken();

            //JSON
            JsonObject message = new JsonObject();
            message.addProperty("topic", TOPIC);

            JsonObject data = new JsonObject();
            data.addProperty("accion", "sincronizar_reuniones");

            message.add("data", data);

            JsonObject payload = new JsonObject();
            payload.add("message", message);

            //HTTP POST Request a Firebase
            HttpPost request = new HttpPost(FCM_URL);
            request.setHeader("Authorization", "Bearer " + accessToken);
            request.setHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(payload.toString());
            request.setEntity(entity);

            // Manejamos la respuesta
            HttpClientResponseHandler<String> responseHandler = response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Respuesta de Firebase: " + status + " " + responseBody);
                return responseBody;
            };

            // Ejecutamos la solicitud a firebase
            client.execute(request, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        enviarNotificacion();
    }
}
