// main.js - Funcionalidades generales del sitio

// Inicialización cuando el documento está listo
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    handleGeoLocation();
});

// Inicializar la aplicación
function initializeApp() {
    console.log('🚀 Parkea Ya - Inicializando aplicación');
    
    // Inicializar tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Inicializar popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    const popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

// Configurar event listeners
function setupEventListeners() {
    // Formulario de solicitud
    const solicitudForm = document.getElementById('solicitudForm');
    if (solicitudForm) {
        solicitudForm.addEventListener('submit', handleSolicitudSubmit);
    }
    
    // Formulario de contacto
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', handleContactSubmit);
    }
    
    // Navegación suave
    setupSmoothScroll();
    
    // Animaciones al hacer scroll
    setupScrollAnimations();
}

// Manejar envío del formulario de solicitud
async function handleSolicitudSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    
    // Validar formulario
    if (!validateForm(form)) {
        showAlert('Por favor completa todos los campos requeridos correctamente.', 'error');
        return;
    }
    
    // Mostrar loading
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Enviando...';
    submitButton.disabled = true;
    
    try {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData);
        
        // Aquí iría la llamada real al backend
        const response = await simulateApiCall(data);
        
        if (response.success) {
            showAlert('¡Solicitud enviada exitosamente! Te contactaremos pronto.', 'success');
            form.reset();
            
            // Redirigir después de 2 segundos
            setTimeout(() => {
                window.location.href = '/confirmacion';
            }, 2000);
        } else {
            throw new Error(response.message);
        }
        
    } catch (error) {
        console.error('Error enviando solicitud:', error);
        showAlert('Error al enviar la solicitud. Por favor intenta nuevamente.', 'error');
    } finally {
        // Restaurar botón
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
    }
}

// Manejar envío del formulario de contacto
async function handleContactSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    
    if (!validateForm(form)) {
        showAlert('Por favor completa todos los campos requeridos.', 'error');
        return;
    }
    
    // Mostrar loading
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Enviando...';
    submitButton.disabled = true;
    
    try {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData);
        
        // Simular envío
        await simulateApiCall(data);
        
        showAlert('¡Mensaje enviado! Te responderemos a la brevedad.', 'success');
        form.reset();
        
    } catch (error) {
        console.error('Error enviando mensaje:', error);
        showAlert('Error al enviar el mensaje. Por favor intenta nuevamente.', 'error');
    } finally {
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
    }
}

// Validar formulario
function validateForm(form) {
    let isValid = true;
    const requiredFields = form.querySelectorAll('[required]');
    
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            isValid = false;
            field.classList.add('is-invalid');
        } else {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
        }
    });
    
    // Validar email si existe
    const emailField = form.querySelector('input[type="email"]');
    if (emailField && emailField.value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailField.value)) {
            isValid = false;
            emailField.classList.add('is-invalid');
        }
    }
    
    return isValid;
}

// Obtener ubicación actual
function getCurrentLocation() {
    if (!navigator.geolocation) {
        showAlert('La geolocalización no es soportada por tu navegador.', 'warning');
        return;
    }
    
    const locationButton = document.querySelector('[onclick="getCurrentLocation()"]');
    const originalText = locationButton.innerHTML;
    
    locationButton.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Obteniendo ubicación...';
    locationButton.disabled = true;
    
    navigator.geolocation.getCurrentPosition(
        function(position) {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;
            
            // Rellenar campos de ubicación
            const latField = document.getElementById('latitud');
            const lngField = document.getElementById('longitud');
            
            if (latField) latField.value = lat.toFixed(6);
            if (lngField) lngField.value = lng.toFixed(6);
            
            // Intentar obtener ciudad (simulado)
            getCityName(lat, lng);
            
            showAlert('Ubicación obtenida correctamente.', 'success');
            
            locationButton.innerHTML = originalText;
            locationButton.disabled = false;
        },
        function(error) {
            console.error('Error obteniendo ubicación:', error);
            
            let errorMessage = 'No se pudo obtener la ubicación. ';
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    errorMessage += 'Permiso denegado por el usuario.';
                    break;
                case error.POSITION_UNAVAILABLE:
                    errorMessage += 'Información de ubicación no disponible.';
                    break;
                case error.TIMEOUT:
                    errorMessage += 'Tiempo de espera agotado.';
                    break;
                default:
                    errorMessage += 'Error desconocido.';
            }
            
            showAlert(errorMessage, 'error');
            
            locationButton.innerHTML = originalText;
            locationButton.disabled = false;
        },
        {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 60000
        }
    );
}

// Obtener nombre de ciudad (simulado)
function getCityName(lat, lng) {
    // En una implementación real, usarías una API de geocoding
    // Por ahora simulamos con datos estáticos
    
    const cityField = document.getElementById('ciudad');
    if (cityField && !cityField.value) {
        // Simular obtención de ciudad basada en coordenadas
        const cities = [
            { lat: -12.0464, lng: -77.0428, name: 'Lima' },
            { lat: -16.4090, lng: -71.5375, name: 'Arequipa' },
            { lat: -8.1092, lng: -79.0215, name: 'Trujillo' },
            { lat: -13.1588, lng: -74.2232, name: 'Ayacucho' }
        ];
        
        let closestCity = cities[0];
        let minDistance = Number.MAX_VALUE;
        
        cities.forEach(city => {
            const distance = Math.sqrt(
                Math.pow(city.lat - lat, 2) + Math.pow(city.lng - lng, 2)
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                closestCity = city;
            }
        });
        
        if (minDistance < 1) { // Si está dentro de 1 grado (~111km)
            cityField.value = closestCity.name;
        }
    }
}

// Configurar scroll suave
function setupSmoothScroll() {
    const links = document.querySelectorAll('a[href^="#"]');
    
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Configurar animaciones al hacer scroll
function setupScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-in');
            }
        });
    }, observerOptions);
    
    // Observar elementos para animación
    const animatedElements = document.querySelectorAll('.feature-card, .benefit-card, .testimonial');
    animatedElements.forEach(el => {
        observer.observe(el);
    });
}

// Mostrar alertas
function showAlert(message, type = 'info') {
    // Remover alertas existentes
    const existingAlerts = document.querySelectorAll('.custom-alert');
    existingAlerts.forEach(alert => alert.remove());
    
    const alertClass = {
        'success': 'alert-success',
        'error': 'alert-danger',
        'warning': 'alert-warning',
        'info': 'alert-info'
    }[type] || 'alert-info';
    
    const alertHtml = `
        <div class="custom-alert alert ${alertClass} alert-dismissible fade show position-fixed" 
             style="top: 20px; right: 20px; z-index: 9999; min-width: 300px;">
            <div class="d-flex align-items-center">
                <i class="fas ${getAlertIcon(type)} me-2"></i>
                <span>${message}</span>
                <button type="button" class="btn-close ms-auto" data-bs-dismiss="alert"></button>
            </div>
        </div>
    `;
    
    document.body.insertAdjacentHTML('beforeend', alertHtml);
    
    // Auto-remover después de 5 segundos
    setTimeout(() => {
        const alert = document.querySelector('.custom-alert');
        if (alert) {
            alert.remove();
        }
    }, 5000);
}

// Obtener ícono para alerta
function getAlertIcon(type) {
    const icons = {
        'success': 'fa-check-circle',
        'error': 'fa-exclamation-triangle',
        'warning': 'fa-exclamation-circle',
        'info': 'fa-info-circle'
    };
    
    return icons[type] || 'fa-info-circle';
}

// Simular llamada API (para desarrollo)
function simulateApiCall(data) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            // Simular éxito 90% del tiempo
            if (Math.random() > 0.1) {
                resolve({
                    success: true,
                    message: 'Solicitud procesada exitosamente',
                    data: data
                });
            } else {
                reject(new Error('Error simulado del servidor'));
            }
        }, 1500);
    });
}

// Manejar geolocalización
function handleGeoLocation() {
    const locationButton = document.querySelector('[onclick="getCurrentLocation()"]');
    if (locationButton) {
        // Verificar si el navegador soporta geolocalización
        if (!navigator.geolocation) {
            locationButton.style.display = 'none';
        }
    }
}

// Utilidad: Formatear fecha
function formatDate(date) {
    return new Date(date).toLocaleDateString('es-PE', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Utilidad: Formatear hora
function formatTime(date) {
    return new Date(date).toLocaleTimeString('es-PE', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Exportar funciones para uso global
window.getCurrentLocation = getCurrentLocation;
window.showAlert = showAlert;