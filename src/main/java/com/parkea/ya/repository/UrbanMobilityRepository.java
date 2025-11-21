package com.parkea.ya.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parkea.ya.model.UrbanMobility;

@Repository
public interface UrbanMobilityRepository extends JpaRepository<UrbanMobility, Long> {
    
    // Buscar por zona urbana
    List<UrbanMobility> findByZonaUrbana(String zonaUrbana);
    
    // Buscar el análisis más reciente por zona
    Optional<UrbanMobility> findFirstByZonaUrbanaOrderByFechaAnalisisDesc(String zonaUrbana);
    
    // Buscar por rango de fechas
    List<UrbanMobility> findByFechaAnalisisBetween(LocalDate inicio, LocalDate fin);
    
    // Buscar zonas con alta congestión
    List<UrbanMobility> findByNivelCongestionGreaterThan(Double nivelCongestion);
    
    // Buscar zonas con alta demanda de estacionamiento
    List<UrbanMobility> findByDemandaEstacionamientoGreaterThan(Double demandaEstacionamiento);
    
    // Obtener estadísticas promedio por zona
    @Query("SELECT AVG(u.nivelTrafico), AVG(u.nivelCongestion), AVG(u.demandaEstacionamiento) " +
           "FROM UrbanMobility u WHERE u.zonaUrbana = :zona")
    Object[] findPromediosByZona(@Param("zona") String zona);
    
    // Buscar zonas con mejor índice de movilidad sostenible
    List<UrbanMobility> findByIndiceMovilidadSostenibleGreaterThanOrderByIndiceMovilidadSostenibleDesc(Double indice);
    
    // Contar análisis por zona
    Long countByZonaUrbana(String zonaUrbana);
}