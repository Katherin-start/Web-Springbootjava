package com.parkea.ya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parkea.ya.model.ParkingData;

@Repository
public interface ParkingDataRepository extends JpaRepository<ParkingData, Long> {
    
    // Buscar por zona urbana
    List<ParkingData> findByZonaUrbana(String zonaUrbana);
    
    // Buscar estacionamientos con disponibilidad
    List<ParkingData> findByPlazasDisponiblesGreaterThan(Integer plazasDisponibles);
    
    // Buscar por nivel de ocupación
    List<ParkingData> findByNivelOcupacionGreaterThan(Double nivelOcupacion);
    
    // Buscar por tipo de estacionamiento
    List<ParkingData> findByTipoEstacionamiento(String tipoEstacionamiento);
    
    // Buscar estacionamientos activos
    List<ParkingData> findByActivoTrue();
    
    // Buscar por rango de tarifas
    List<ParkingData> findByTarifaHoraBetween(Double minTarifa, Double maxTarifa);
    
    // Buscar estacionamientos cerca de una ubicación (simplificado)
    @Query("SELECT p FROM ParkingData p WHERE " +
           "p.latitud BETWEEN :minLat AND :maxLat AND " +
           "p.longitud BETWEEN :minLng AND :maxLng")
    List<ParkingData> findNearLocation(@Param("minLat") Double minLat, 
                                      @Param("maxLat") Double maxLat,
                                      @Param("minLng") Double minLng, 
                                      @Param("maxLng") Double maxLng);
    
    // Contar estacionamientos por zona
    Long countByZonaUrbana(String zonaUrbana);
    
    // Obtener el estacionamiento con mejor rating
    Optional<ParkingData> findFirstByOrderByRatingPromedioDesc();
}