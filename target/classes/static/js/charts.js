// charts.js - Gráficos y visualizaciones de datos

let parkingCharts = {};

// Inicializar gráficos
function initializeCharts() {
    if (typeof Chart === 'undefined') {
        console.warn('Chart.js no está cargado');
        return;
    }
    
    createOccupancyChart();
    createDemandPredictionChart();
    createEnvironmentalImpactChart();
}

// Crear gráfico de ocupación
function createOccupancyChart() {
    const ctx = document.getElementById('occupancyChart');
    if (!ctx) return;
    
    parkingCharts.occupancy = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['6:00', '8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00'],
            datasets: [{
                label: 'Ocupación (%)',
                data: [25, 65, 45, 35, 40, 75, 85, 60],
                borderColor: '#007bff',
                backgroundColor: 'rgba(0, 123, 255, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Ocupación Diaria Promedio'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    ticks: {
                        callback: function(value) {
                            return value + '%';
                        }
                    }
                }
            }
        }
    });
}

// Crear gráfico de predicción de demanda
function createDemandPredictionChart() {
    const ctx = document.getElementById('demandChart');
    if (!ctx) return;
    
    parkingCharts.demand = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
            datasets: [{
                label: 'Demanda Esperada',
                data: [85, 78, 82, 88, 92, 45, 35],
                backgroundColor: 'rgba(40, 167, 69, 0.8)',
                borderColor: '#28a745',
                borderWidth: 1
            }, {
                label: 'Demanda Real',
                data: [82, 80, 85, 90, 95, 50, 40],
                backgroundColor: 'rgba(255, 193, 7, 0.8)',
                borderColor: '#ffc107',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Predicción vs Demanda Real'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    ticks: {
                        callback: function(value) {
                            return value + '%';
                        }
                    }
                }
            }
        }
    });
}

// Crear gráfico de impacto ambiental
function createEnvironmentalImpactChart() {
    const ctx = document.getElementById('environmentChart');
    if (!ctx) return;
    
    parkingCharts.environment = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['CO₂ Reducido', 'Combustible Ahorrado', 'Tiempo Ahorrado'],
            datasets: [{
                data: [45, 30, 25],
                backgroundColor: [
                    '#28a745',
                    '#17a2b8',
                    '#ffc107'
                ],
                borderWidth: 2,
                borderColor: '#fff'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Impacto Ambiental Positivo'
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// Actualizar gráficos con datos en tiempo real
async function updateChartsWithRealData() {
    try {
        const response = await fetch('/api/analytics/current');
        if (response.ok) {
            const data = await response.json();
            updateCharts(data);
        }
    } catch (error) {
        console.error('Error actualizando gráficos:', error);
        // Usar datos de ejemplo
        updateChartsWithSampleData();
    }
}

// Actualizar gráficos con datos específicos
function updateCharts(data) {
    if (parkingCharts.occupancy && data.occupancy) {
        parkingCharts.occupancy.data.datasets[0].data = data.occupancy;
        parkingCharts.occupancy.update();
    }
    
    if (parkingCharts.demand && data.demand) {
        parkingCharts.demand.data.datasets[0].data = data.demand.predicted;
        parkingCharts.demand.data.datasets[1].data = data.demand.actual;
        parkingCharts.demand.update();
    }
}

// Datos de ejemplo para desarrollo
function updateChartsWithSampleData() {
    const sampleData = {
        occupancy: [30, 70, 50, 40, 45, 80, 90, 65],
        demand: {
            predicted: [80, 75, 78, 85, 90, 40, 30],
            actual: [78, 77, 80, 88, 92, 45, 35]
        }
    };
    
    updateCharts(sampleData);
}

// Exportar funciones
window.initializeCharts = initializeCharts;
window.updateChartsWithRealData = updateChartsWithRealData;

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Esperar a que Chart.js esté cargado
    if (typeof Chart !== 'undefined') {
        initializeCharts();
    } else {
        // Intentar nuevamente después de un delay
        setTimeout(initializeCharts, 1000);
    }
});