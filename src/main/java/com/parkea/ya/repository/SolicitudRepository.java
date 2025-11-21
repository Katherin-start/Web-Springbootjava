package com.parkea.ya.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parkea.ya.entity.SolicitudAcceso;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudAcceso, Long> {

    // Buscar por email
    List<SolicitudAcceso> findByEmail(String email);
    
    // Buscar por estado
    List<SolicitudAcceso> findByEstado(SolicitudAcceso.EstadoSolicitud estado);
    
    // Buscar solicitudes pendientes
    List<SolicitudAcceso> findByEstadoOrderByFechaSolicitudAsc(SolicitudAcceso.EstadoSolicitud estado);
    
    // Buscar por rango de fechas
    List<SolicitudAcceso> findByFechaSolicitudBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar por ciudad
    List<SolicitudAcceso> findByCiudadIgnoreCase(String ciudad);
    
    // Buscar solicitudes cerca de una ubicación (radio en kilómetros)
    @Query("SELECT s FROM SolicitudAcceso s WHERE " +
           "6371 * acos(cos(radians(:lat)) * cos(radians(s.latitud)) * " +
           "cos(radians(s.longitud) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(s.latitud))) < :radio")
    List<SolicitudAcceso> findNearLocation(@Param("lat") Double latitud, 
                                          @Param("lng") Double longitud, 
                                          @Param("radio") Double radioKm);
    
    // Contar por estado
    Long countByEstado(SolicitudAcceso.EstadoSolicitud estado);
    
    // Encontrar la última solicitud por email
    Optional<SolicitudAcceso> findFirstByEmailOrderByFechaSolicitudDesc(String email);
}