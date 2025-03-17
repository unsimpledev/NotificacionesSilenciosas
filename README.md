# NotificacionesSilenciosas
Las Notificaciones Silenciosas son aquellas que no se muestran en pantalla, son atendidas por el servicio y realizan alguna tarea en segundo plano.

En el código subido:

Desde el proyecto BE enviamos una notificacion silenciosa (solo con el campo DATA).

En la App recibimos la notificación, evaluamos ciertos datos de la base de datos del dispositivo y si se cumple la condición enviamos desde la app una notificación al usuario.
