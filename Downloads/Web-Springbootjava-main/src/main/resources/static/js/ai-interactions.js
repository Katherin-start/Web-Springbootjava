// ai-interactions.js - Funcionalidades para la IA

// Generar contenido con IA
async function generateAIContent() {
    const topicSelect = document.getElementById('ai-topic-select');
    const customTopic = document.getElementById('ai-custom-topic');
    const loadingElement = document.getElementById('ai-loading');
    const resultElement = document.getElementById('ai-result');
    const errorElement = document.getElementById('ai-error');
    const contentElement = document.getElementById('ai-content');
    
    // Obtener tema
    let topic = topicSelect.value;
    let prompt = '';
    
    if (topic === 'personalizado' && customTopic.value.trim()) {
        prompt = customTopic.value.trim();
        topic = 'personalizado';
    } else {
        // Usar tema predefinido
        prompt = getPredefinedPrompt(topic);
    }
    
    if (!prompt) {
        showAlert('Por favor selecciona un tema o escribe uno personalizado.', 'warning');
        return;
    }
    
    // Mostrar loading, ocultar resultados y errores
    loadingElement.classList.remove('d-none');
    resultElement.classList.add('d-none');
    errorElement.classList.add('d-none');
    
    try {
        // Llamar al endpoint de IA
        const response = await fetch('/api/ai/generar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                prompt: prompt,
                tipo: topic
            })
        });
        
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        
        if (data.success) {
            // Mostrar resultado
            contentElement.innerHTML = formatAIContent(data.contenido);
            resultElement.classList.remove('d-none');
            
            // Track éxito
            trackAIGeneration(topic, true);
        } else {
            throw new Error(data.error || 'Error desconocido al generar contenido');
        }
        
    } catch (error) {
        console.error('Error generando contenido IA:', error);
        
        // Mostrar error
        document.getElementById('ai-error-message').textContent = error.message;
        errorElement.classList.remove('d-none');
        
        // Mostrar contenido de fallback
        contentElement.innerHTML = getFallbackContent(topic);
        resultElement.classList.remove('d-none');
        
        // Track error
        trackAIGeneration(topic, false);
    } finally {
        loadingElement.classList.add('d-none');
    }
}

// Obtener prompt predefinido
function getPredefinedPrompt(topic) {
    const prompts = {
        'vision': `Como experto en movilidad urbana sostenible, genera una visión inteligente 
sobre el futuro de la movilidad urbana. Enfócate en cómo sistemas de estacionamiento 
inteligente como Parkea Ya pueden mejorar el tráfico, reducir emisiones y predecir demanda. 
Máximo 250 palabras, estilo profesional pero inspirador. Responde en español.`,

        'beneficios': `Enumera los principales beneficios de un sistema de estacionamiento inteligente 
para ciudades modernas. Incluye beneficios económicos, ambientales y sociales. 
Formato: lista con puntos clave, máximo 150 palabras. Responde en español.`,

        'sostenibilidad': `Analiza cómo los estacionamientos inteligentes pueden contribuir a la 
sostenibilidad urbana. Considera reducción de emisiones, optimización de espacios, 
integración con transporte sostenible e impacto en calidad de vida. 
Máximo 200 palabras. Responde en español.`,

        'prediccion': `Como IA analítica, genera una predicción inteligente sobre la demanda de 
estacionamientos en una zona urbana típica. Considera factores como horas pico, 
días de la semana, eventos especiales y estacionalidad. 
Formato: análisis breve con recomendaciones, máximo 200 palabras. Responde en español.`,

        'analisis': `Genera un análisis ejecutivo sobre la situación actual de estacionamientos 
en ciudades latinoamericanas. Incluye desafíos, oportunidades y recomendaciones 
para implementar sistemas inteligentes. Máximo 300 palabras. Responde en español.`
    };
    
    return prompts[topic] || '';
}

// Formatear contenido de IA
function formatAIContent(content) {
    if (!content) return '<p class="text-muted">No se pudo generar contenido.</p>';
    
    // Convertir saltos de línea en párrafos
    let formattedContent = content
        .split('\n')
        .filter(line => line.trim())
        .map(line => {
            line = line.trim();
            
            // Si la línea parece un título
            if (line.endsWith(':') || line.length < 50 && !line.includes('.') && !line.includes(',')) {
                return `<h6 class="text-primary mt-3 mb-2">${line}</h6>`;
            }
            
            // Si la línea parece un item de lista
            if (line.startsWith('-') || line.startsWith('•') || line.match(/^\d+\./)) {
                return `<div class="d-flex align-items-start mb-1">
                    <i class="fas fa-check text-success mt-1 me-2" style="font-size: 0.8em;"></i>
                    <span>${line.replace(/^[-•\d\.\s]+/, '').trim()}</span>
                </div>`;
            }
            
            // Párrafo normal
            return `<p class="mb-2">${line}</p>`;
        })
        .join('');
    
    return formattedContent || `<p>${content}</p>`;
}

// Contenido de fallback
function getFallbackContent(topic) {
    const fallbackContent = {
        'vision': `<p>Los estacionamientos inteligentes representan el futuro de la movilidad urbana. 
Al optimizar el uso de espacios y reducir el tiempo de búsqueda, contribuyen significativamente 
a disminuir la congestión vehicular y las emisiones de CO₂.</p>
<p>Sistemas como Parkea Ya permiten una gestión más eficiente de los recursos urbanos, 
integrando tecnología de punta con análisis predictivo para anticipar la demanda y 
optimizar la distribución de vehículos.</p>`,

        'beneficios': `<div class="d-flex align-items-start mb-2">
            <i class="fas fa-check text-success mt-1 me-2"></i>
            <span><strong>Reducción del tráfico:</strong> Menos vehículos circulando en busca de estacionamiento</span>
        </div>
        <div class="d-flex align-items-start mb-2">
            <i class="fas fa-check text-success mt-1 me-2"></i>
            <span><strong>Ahorro de tiempo:</strong> Los conductores encuentran estacionamiento más rápido</span>
        </div>
        <div class="d-flex align-items-start mb-2">
            <i class="fas fa-check text-success mt-1 me-2"></i>
            <span><strong>Sostenibilidad ambiental:</strong> Reducción significativa de emisiones de CO₂</span>
        </div>
        <div class="d-flex align-items-start mb-2">
            <i class="fas fa-check text-success mt-1 me-2"></i>
            <span><strong>Optimización de espacios:</strong> Mejor uso de la infraestructura existente</span>
        </div>`,

        'sostenibilidad': `<p>Los estacionamientos inteligentes son clave para ciudades más sostenibles. 
Al reducir el tiempo de búsqueda de estacionamiento, disminuyen las emisiones de gases 
de efecto invernadero y mejoran la calidad del aire urbano.</p>
<p>Además, permiten una mejor planificación del uso del suelo y promueven la integración 
con otros modos de transporte sostenible, creando ecosistemas de movilidad más eficientes 
y amigables con el medio ambiente.</p>`,

        'prediccion': `<p>La demanda de estacionamiento sigue patrones predecibles basados en:</p>
        <div class="d-flex align-items-start mb-1">
            <i class="fas fa-clock text-info mt-1 me-2"></i>
            <span><strong>Horas pico:</strong> Mayor demanda entre 8-10 AM y 5-7 PM</span>
        </div>
        <div class="d-flex align-items-start mb-1">
            <i class="fas fa-calendar text-info mt-1 me-2"></i>
            <span><strong>Días de semana:</strong> Alta demanda los días laborables</span>
        </div>
        <div class="d-flex align-items-start mb-2">
            <i class="fas fa-star text-info mt-1 me-2"></i>
            <span><strong>Eventos especiales:</strong> Picos durante eventos y fines de semana</span>
        </div>
        <p><strong>Recomendación:</strong> Implementar tarifas dinámicas y reservas anticipadas.</p>`,

        'analisis': `<p>Las ciudades latinoamericanas enfrentan desafíos únicos en gestión de estacionamiento, 
incluyendo alta densidad vehicular y infraestructura limitada.</p>
<p>Las oportunidades incluyen la adopción de tecnologías móviles, integración con transporte 
público y desarrollo de políticas urbanas que promuevan la movilidad sostenible.</p>`
    };
    
    return fallbackContent[topic] || `<p>Contenido sobre ${topic} generado por nuestra IA.</p>`;
}

// Track generaciones de IA
function trackAIGeneration(topic, success) {
    // En una implementación real, enviarías esto a analytics
    console.log(`IA Generation - Topic: ${topic}, Success: ${success}`);
    
    // Guardar en localStorage para estadísticas
    const stats = JSON.parse(localStorage.getItem('ai_generation_stats') || '{}');
    stats[topic] = (stats[topic] || 0) + 1;
    stats.total = (stats.total || 0) + 1;
    stats[`${topic}_success`] = (stats[`${topic}_success`] || 0) + (success ? 1 : 0);
    
    localStorage.setItem('ai_generation_stats', JSON.stringify(stats));
}

// Cargar predicción rápida
async function loadQuickPrediction() {
    try {
        const response = await fetch('/api/ai/prediccion?zona=centro+urbano');
        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                updatePredictionDisplay(data.contenido);
            }
        }
    } catch (error) {
        console.error('Error cargando predicción:', error);
    }
}

// Actualizar display de predicción
function updatePredictionDisplay(content) {
    const predictionElement = document.getElementById('ai-prediction');
    if (predictionElement && content) {
        predictionElement.innerHTML = content;
    }
}

// Generar idea personalizada
async function generateCustomIdea(tema) {
    const customTopic = document.getElementById('ai-custom-topic');
    if (customTopic) {
        customTopic.value = tema;
    }
    
    const topicSelect = document.getElementById('ai-topic-select');
    if (topicSelect) {
        topicSelect.value = 'personalizado';
    }
    
    await generateAIContent();
}

// Exportar funciones para uso global
window.generateAIContent = generateAIContent;
window.generateCustomIdea = generateCustomIdea;

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Cargar predicción rápida si existe el elemento
    const predictionElement = document.getElementById('ai-prediction');
    if (predictionElement) {
        loadQuickPrediction();
    }
    
    // Configurar event listeners para IA
    const generateButton = document.querySelector('[onclick="generateAIContent()"]');
    if (generateButton) {
        // También permitir Enter en el campo personalizado
        const customTopic = document.getElementById('ai-custom-topic');
        if (customTopic) {
            customTopic.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    generateAIContent();
                }
            });
        }
    }

    // Generación rápida de contenido
function quickGenerate(topic) {
    const topicSelect = document.getElementById('ai-topic-select');
    const customTopic = document.getElementById('ai-custom-topic');
    
    if (topicSelect && customTopic) {
        // Limpiar tema personalizado
        customTopic.value = '';
        
        // Establecer el tema seleccionado
        topicSelect.value = topic;
        
        // Generar contenido
        generateAIContent();
    } else {
        console.error('Elementos del formulario no encontrados');
        showAlert('Error: No se pudo encontrar el formulario de IA', 'error');
    }
}

// También asegúrate de tener la función showAlert
function showAlert(message, type = 'info') {
    // Crear alerta temporal
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Insertar al inicio del contenedor principal
    const container = document.querySelector('.container') || document.body;
    container.insertBefore(alertDiv, container.firstChild);
    
    // Auto-eliminar después de 5 segundos
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);
}

// Agregar al objeto global
window.quickGenerate = quickGenerate;
window.showAlert = showAlert;
});