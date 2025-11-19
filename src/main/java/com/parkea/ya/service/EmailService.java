package com.parkea.ya.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.parkea.ya.entity.SolicitudAcceso;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    public boolean enviarConfirmacionSolicitud(SolicitudAcceso solicitud) {
        try {
            String asunto = "Confirmación de Solicitud - Parkea Ya";
            String contenido = construirEmailConfirmacion(solicitud);
            
            logger.info("📧 EMAIL SIMULADO - Confirmación enviada a: {}", solicitud.getEmail());
            logger.info("📋 Asunto: {}", asunto);
            logger.info("📝 Contenido: {}", contenido.replace("\n", " "));
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando confirmación de solicitud: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean enviarNotificacionAdministrador(SolicitudAcceso solicitud) {
        try {
            String asunto = "Nueva Solicitud de Acceso - Parkea Ya";
            String contenido = construirEmailAdministrador(solicitud);
            
            logger.info("📧 EMAIL SIMULADO - Notificación admin para solicitud ID: {}", solicitud.getId());
            logger.info("📋 Asunto: {}", asunto);
            logger.info("📝 Contenido: {}", contenido.replace("\n", " "));
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando notificación a administrador: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean enviarNotificacionAprobacion(SolicitudAcceso solicitud) {
        try {
            String asunto = "¡Solicitud Aprobada! - Parkea Ya";
            String contenido = construirEmailAprobacion(solicitud);
            
            logger.info("📧 EMAIL SIMULADO - Aprobación enviada a: {}", solicitud.getEmail());
            logger.info("📋 Asunto: {}", asunto);
            logger.info("📝 Contenido: {}", contenido.replace("\n", " "));
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando notificación de aprobación: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean enviarNotificacionRechazo(SolicitudAcceso solicitud) {
        try {
            String asunto = "Actualización de tu Solicitud - Parkea Ya";
            String contenido = construirEmailRechazo(solicitud);
            
            logger.info("📧 EMAIL SIMULADO - Rechazo enviado a: {}", solicitud.getEmail());
            logger.info("📋 Asunto: {}", asunto);
            logger.info("📝 Contenido: {}", contenido.replace("\n", " "));
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando notificación de rechazo: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean enviarEmailPrueba(String destinatario) {
        try {
            String asunto = "Email de Prueba - Parkea Ya";
            String contenido = "✅ Email de prueba funcionando correctamente - " + java.time.LocalDateTime.now();
            
            logger.info("📧 EMAIL DE PRUEBA enviado a: {}", destinatario);
            logger.info("📋 Asunto: {}", asunto);
            logger.info("📝 Contenido: {}", contenido);
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error enviando email de prueba: {}", e.getMessage());
            return false;
        }
    }
    
    private String construirEmailConfirmacion(SolicitudAcceso solicitud) {
        return String.format("""
            Hola %s,
            
            ¡Gracias por tu interés en Parkea Ya!
            
            Hemos recibido tu solicitud de acceso al panel general.
            
            📋 Detalles de tu solicitud:
            • Nombre: %s
            • Email: %s
            • Teléfono: %s
            • Empresa: %s
            • Fecha: %s
            
            Estado actual: En revisión
            
            Nuestro equipo revisará tu solicitud y te contactaremos en un plazo máximo de 48 horas.
            
            Saludos cordiales,
            El equipo de Parkea Ya
            """,
            solicitud.getNombreCompleto(),
            solicitud.getNombreCompleto(),
            solicitud.getEmail(),
            solicitud.getTelefono(),
            solicitud.getEmpresa() != null ? solicitud.getEmpresa() : "No especificada",
            solicitud.getFechaSolicitud().toString()
        );
    }
    
    private String construirEmailAdministrador(SolicitudAcceso solicitud) {
        return String.format("""
            📥 NUEVA SOLICITUD DE ACCESO - Requiere revisión
            
            Información del solicitante:
            • Nombre: %s
            • Email: %s
            • Teléfono: %s
            • Empresa: %s
            • Mensaje: %s
            • Ubicación: %s, %s
            • Fecha solicitud: %s
            • ID Solicitud: %d
            
            Acción: Revisar en el panel de administración.
            """,
            solicitud.getNombreCompleto(),
            solicitud.getEmail(),
            solicitud.getTelefono(),
            solicitud.getEmpresa() != null ? solicitud.getEmpresa() : "No especificada",
            solicitud.getMensaje() != null ? solicitud.getMensaje() : "Sin mensaje adicional",
            solicitud.getCiudad() != null ? solicitud.getCiudad() : "No especificada",
            solicitud.getDireccionCompleta() != null ? solicitud.getDireccionCompleta() : "No especificada",
            solicitud.getFechaSolicitud().toString(),
            solicitud.getId()
        );
    }
    
    private String construirEmailAprobacion(SolicitudAcceso solicitud) {
        return String.format("""
            ¡Felicidades %s!
            
            ✅ Tu solicitud ha sido APROBADA.
            
            Tu solicitud de acceso al panel general de Parkea Ya ha sido aprobada.
            
            📋 Próximos pasos:
            1. Accede al panel en: http://localhost:8000/admin
            2. Usa tus credenciales para iniciar sesión
            3. Explora las funcionalidades del sistema
            
            Notas del administrador: %s
            
            ¡Bienvenido a la comunidad Parkea Ya!
            
            Saludos cordiales,
            El equipo de Parkea Ya
            """,
            solicitud.getNombreCompleto(),
            solicitud.getNotasAdministrador() != null ? solicitud.getNotasAdministrador() : "Bienvenido al sistema."
        );
    }
    
    private String construirEmailRechazo(SolicitudAcceso solicitud) {
        return String.format("""
            Hola %s,
            
            Actualización de tu solicitud:
            
            Lamentamos informarte que tu solicitud no ha sido aprobada en esta ocasión.
            
            📋 Motivo: %s
            
            Esto no significa que no puedas formar parte de Parkea Ya en el futuro. 
            Te animamos a actualizar tu información y volver a solicitar.
            
            Agradecemos tu interés en Parkea Ya.
            
            Saludos cordiales,
            El equipo de Parkea Ya
            """,
            solicitud.getNombreCompleto(),
            solicitud.getNotasAdministrador() != null ? solicitud.getNotasAdministrador() : "No cumple con los criterios requeridos en este momento."
        );
    }
}