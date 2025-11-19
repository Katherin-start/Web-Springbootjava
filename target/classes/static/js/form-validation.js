// form-validation.js - Validaciones específicas de formularios

// Inicializar validaciones
document.addEventListener('DOMContentLoaded', function() {
    initializeFormValidations();
});

function initializeFormValidations() {
    // Validación en tiempo real para formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        setupRealTimeValidation(form);
    });
    
    // Validación específica para email
    const emailFields = document.querySelectorAll('input[type="email"]');
    emailFields.forEach(field => {
        field.addEventListener('blur', validateEmail);
    });
    
    // Validación de teléfono
    const phoneFields = document.querySelectorAll('input[type="tel"]');
    phoneFields.forEach(field => {
        field.addEventListener('blur', validatePhone);
        field.addEventListener('input', formatPhone);
    });
    
    // Validación de coordenadas
    const coordFields = document.querySelectorAll('input[type="number"][id*="lat"], input[type="number"][id*="lng"]');
    coordFields.forEach(field => {
        field.addEventListener('blur', validateCoordinates);
    });
}

// Configurar validación en tiempo real
function setupRealTimeValidation(form) {
    const fields = form.querySelectorAll('input, select, textarea');
    
    fields.forEach(field => {
        field.addEventListener('blur', function() {
            validateField(this);
        });
        
        field.addEventListener('input', function() {
            if (this.classList.contains('is-invalid')) {
                validateField(this);
            }
        });
    });
}

// Validar campo individual
function validateField(field) {
    const value = field.value.trim();
    const fieldType = field.type;
    const fieldName = field.name || field.id;
    
    // Remover estados previos
    field.classList.remove('is-valid', 'is-invalid');
    
    // Validar campo requerido
    if (field.hasAttribute('required') && !value) {
        field.classList.add('is-invalid');
        return false;
    }
    
    // Validaciones específicas por tipo
    let isValid = true;
    
    switch (fieldType) {
        case 'email':
            isValid = validateEmailField(field);
            break;
        case 'tel':
            isValid = validatePhoneField(field);
            break;
        case 'number':
            if (fieldName.includes('lat') || fieldName.includes('lng')) {
                isValid = validateCoordinateField(field);
            }
            break;
        case 'text':
            if (fieldName === 'nombre') {
                isValid = validateNameField(field);
            }
            break;
    }
    
    if (isValid && value) {
        field.classList.add('is-valid');
    }
    
    return isValid;
}

// Validar email
function validateEmailField(field) {
    const email = field.value.trim();
    if (!email) return true; // No validar si está vacío y no es requerido
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(email);
    
    if (!isValid) {
        showFieldError(field, 'Por favor ingresa un email válido');
    }
    
    return isValid;
}

// Validar teléfono
function validatePhoneField(field) {
    const phone = field.value.replace(/\D/g, ''); // Solo números
    if (!phone) return true;
    
    // Validar formato peruano (9 dígitos) o internacional
    const isValid = phone.length >= 9 && phone.length <= 15;
    
    if (!isValid) {
        showFieldError(field, 'Ingresa un número de teléfono válido (9-15 dígitos)');
    }
    
    return isValid;
}

// Formatear teléfono
function formatPhone(event) {
    const field = event.target;
    let value = field.value.replace(/\D/g, '');
    
    if (value.length > 0) {
        // Formato peruano: +51 XXX XXX XXX
        if (value.startsWith('51') && value.length > 2) {
            value = '+51 ' + value.substring(2);
        }
        
        // Agrupar en bloques de 3
        if (value.length > 3) {
            value = value.substring(0, 3) + ' ' + value.substring(3);
        }
        if (value.length > 7) {
            value = value.substring(0, 7) + ' ' + value.substring(7);
        }
        if (value.length > 11) {
            value = value.substring(0, 11) + ' ' + value.substring(11);
        }
    }
    
    field.value = value;
}

// Validar coordenadas
function validateCoordinateField(field) {
    const value = parseFloat(field.value);
    const fieldName = field.name || field.id;
    
    if (isNaN(value)) return true; // No validar si está vacío
    
    let isValid = true;
    let errorMessage = '';
    
    if (fieldName.includes('lat')) {
        isValid = value >= -90 && value <= 90;
        errorMessage = 'La latitud debe estar entre -90 y 90';
    } else if (fieldName.includes('lng')) {
        isValid = value >= -180 && value <= 180;
        errorMessage = 'La longitud debe estar entre -180 y 180';
    }
    
    if (!isValid) {
        showFieldError(field, errorMessage);
    }
    
    return isValid;
}

// Validar nombre
function validateNameField(field) {
    const name = field.value.trim();
    if (!name) return true;
    
    const isValid = name.length >= 2 && name.length <= 100;
    
    if (!isValid) {
        showFieldError(field, 'El nombre debe tener entre 2 y 100 caracteres');
    }
    
    return isValid;
}

// Mostrar error de campo
function showFieldError(field, message) {
    field.classList.add('is-invalid');
    
    // Remover feedback existente
    const existingFeedback = field.parentNode.querySelector('.invalid-feedback');
    if (existingFeedback) {
        existingFeedback.remove();
    }
    
    // Crear elemento de feedback
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = message;
    
    field.parentNode.appendChild(feedback);
}

// Validar formulario completo
function validateForm(form) {
    const fields = form.querySelectorAll('input, select, textarea');
    let isValid = true;
    
    fields.forEach(field => {
        if (!validateField(field)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// Validar checkboxes requeridos
function validateCheckboxGroup(groupName) {
    const checkboxes = document.querySelectorAll(`input[name="${groupName}"]`);
    const checked = Array.from(checkboxes).some(checkbox => checkbox.checked);
    
    if (!checked) {
        showAlert('Por favor selecciona al menos una opción', 'warning');
        return false;
    }
    
    return true;
}

// Limpiar validaciones
function clearValidations(form) {
    const fields = form.querySelectorAll('input, select, textarea');
    
    fields.forEach(field => {
        field.classList.remove('is-valid', 'is-invalid');
        
        const feedback = field.parentNode.querySelector('.invalid-feedback');
        if (feedback) {
            feedback.remove();
        }
    });
}

// Event listeners globales
function validateEmail(event) {
    validateEmailField(event.target);
}

function validatePhone(event) {
    validatePhoneField(event.target);
}

function validateCoordinates(event) {
    validateCoordinateField(event.target);
}

// Exportar funciones
window.validateForm = validateForm;
window.clearValidations = clearValidations;
window.validateCheckboxGroup = validateCheckboxGroup;