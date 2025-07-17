const API_GATEWAY_URL = 'http://localhost:8080/api/v1/tareas'; // URL de tu API Gateway para el servicio de tareas

let tasks = []; // Almacenará las tareas obtenidas del backend
let currentEditId = null; // Para saber si estamos creando o editando
// Este orden es CRÍTICO y debe coincidir con el orden de las columnas y transiciones de tu backend
const statusOrder = ['PENDIENTE', 'EN_PROGRESO', 'CITA_CONFIRMADA', 'COMPLETADA', 'ENVIADA'];

// --- Funciones para manejar el Modal ---
function openModal(id = null) {
    const modal = document.getElementById('taskModal');
    const form = document.getElementById('taskForm');
    document.getElementById('modalTitle').textContent = 'Añadir Nueva Tarea';
    form.reset(); // Limpiar el formulario

    currentEditId = id;
    if (id) {
        document.getElementById('modalTitle').textContent = 'Editar Tarea';
        const task = tasks.find(t => t.id === id);
        if (task) {
            document.getElementById('taskType').value = task.tipo.toUpperCase();
            document.getElementById('taskPriority').value = task.prioridad.toUpperCase();
            document.getElementById('taskPatient').value = task.paciente;
            document.getElementById('taskEPS').value = task.eps || '';
            document.getElementById('taskPhone').value = task.phone || '';
            document.getElementById('taskDoctor').value = task.doctor || '';
            document.getElementById('taskLocation').value = task.location || '';
            document.getElementById('taskObservations').value = task.observaciones || '';
            document.getElementById('taskDate').value = task.date || '';
            document.getElementById('taskTime').value = task.time || '';
        }
    }
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('taskModal').classList.remove('active');
    currentEditId = null;
    document.getElementById('taskForm').reset();
}

// --- Funciones de Notificación (Toast) ---
let notificationTimeout;
function showNotification(message, type = 'success') {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification show ${type}`; // Add type class for styling

    clearTimeout(notificationTimeout);
    notificationTimeout = setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

// --- Interacción con el Backend (API Gateway) ---

async function fetchTasksFromBackend() {
    const columnContentDivs = document.querySelectorAll('.column-content');
    columnContentDivs.forEach(div => div.innerHTML = '<div class="empty-state">Cargando tareas...</div>');

    try {
        const response = await fetch(API_GATEWAY_URL);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
        }
        const data = await response.json();
        // Mapea los datos del backend a la estructura que espera tu frontend
        tasks = data.map(task => ({
            id: task.id,
            tipo: task.tipo.toLowerCase(), // Convertir a minúsculas para clases CSS
            paciente: task.paciente,
            prioridad: task.prioridad.toLowerCase(), // Convertir a minúsculas para clases CSS
            eps: task.eps || '',
            phone: task.telefonoContacto || '', // Asegura que el nombre del campo coincida con el DTO de Spring
            observaciones: task.observaciones || '',
            status: task.estado, // El estado del backend ya viene en mayúsculas
            date: task.fechaRecordatorio ? task.fechaRecordatorio.split('T')[0] : '', // Extraer solo la fecha
            time: task.fechaRecordatorio ? task.fechaRecordatorio.split('T')[1].substring(0, 5) : '', // Extraer solo la hora (HH:mm)
            doctor: task.medicoAsignado || '', // Asegura que el nombre del campo coincida con el DTO
            location: task.lugarCita || '', // Asegura que el nombre del campo coincida con el DTO
            fechaCreacion: task.fechaCreacion,
            fechaActualizacion: task.fechaActualizacion
        }));
        renderTasks(); // Renderiza las tareas obtenidas
    } catch (error) {
        console.error('Error al obtener las tareas:', error);
        columnContentDivs.forEach(div => div.innerHTML = `<div class="empty-state" style="color: red;">Error al cargar las tareas: ${error.message}</div>`);
        showNotification(`Error al cargar las tareas: ${error.message}`, 'error');
    }
}

async function saveTask(event) {
    event.preventDefault(); // Evita que el formulario se envíe de forma predeterminada

    const form = document.getElementById('taskForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const taskData = {
        tipo: document.getElementById('taskType').value.toUpperCase(),
        prioridad: document.getElementById('taskPriority').value.toUpperCase(),
        paciente: document.getElementById('taskPatient').value,
        eps: document.getElementById('taskEPS').value || null,
        telefonoContacto: document.getElementById('taskPhone').value || null,
        medicoAsignado: document.getElementById('taskDoctor').value || null,
        lugarCita: document.getElementById('taskLocation').value || null,
        observaciones: document.getElementById('taskObservations').value || null,
        fechaRecordatorio: (document.getElementById('taskDate').value && document.getElementById('taskTime').value) ?
            `${document.getElementById('taskDate').value}T${document.getElementById('taskTime').value}:00` : null
    };

    try {
        let response;
        let method;
        let url;

        if (currentEditId) {
            // Actualizar tarea existente (PUT)
            method = 'PUT';
            url = `${API_GATEWAY_URL}/${currentEditId}`;
            // Para PUT, necesitamos enviar el objeto completo, incluido el estado actual que viene del backend
            const existingTask = tasks.find(t => t.id === currentEditId);
            const updatedTaskData = { ...taskData, estado: existingTask.status }; // Mantener el estado actual
            response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedTaskData)
            });
        } else {
            // Crear nueva tarea (POST)
            method = 'POST';
            url = API_GATEWAY_URL;
            response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(taskData)
            });
        }

        if (!response.ok) {
            const errorData = await response.json();
            const errorMessage = errorData.message || JSON.stringify(errorData);
            throw new Error(`Error al ${currentEditId ? 'actualizar' : 'crear'} la tarea: ${errorMessage}`);
        }

        showNotification(`Tarea ${currentEditId ? 'actualizada' : 'creada'} exitosamente!`);
        closeModal();
        fetchTasksFromBackend(); // Recargar las tareas desde el backend
    } catch (error) {
        console.error('Error:', error);
        showNotification(`Error al ${currentEditId ? 'actualizar' : 'crear'} la tarea: ${error.message}`, 'error');
    }
}

async function moveToNextStage(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (!task) return;

    const currentIndex = statusOrder.indexOf(task.status);
    if (currentIndex < statusOrder.length - 1) {
        const newStatus = statusOrder[currentIndex + 1];
        try {
            const response = await fetch(`${API_GATEWAY_URL}/${taskId}/estado/${newStatus}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Error al cambiar el estado: ${response.status} - ${errorText}`);
            }

            showNotification(`Tarea de "${task.paciente}" movida a "${newStatus.replace(/_/g, ' ')}."`);
            fetchTasksFromBackend(); // Recargar las tareas desde el backend
        } catch (error) {
            console.error('Error al cambiar el estado:', error);
            showNotification(`Error al cambiar el estado: ${error.message}`, 'error');
        }
    } else {
        showNotification('La tarea ya está en la última etapa.', 'warning');
    }
}

async function deleteTask(id) {
    if (!confirm(`¿Estás seguro de que quieres eliminar la tarea con ID ${id}?`)) {
        return;
    }
    try {
        const response = await fetch(`${API_GATEWAY_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error al eliminar la tarea: ${response.status} - ${errorText}`);
        }

        showNotification(`Tarea con ID ${id} eliminada exitosamente.`);
        fetchTasksFromBackend(); // Recargar la lista
    } catch (error) {
        console.error('Error al eliminar la tarea:', error);
        showNotification(`Error al eliminar la tarea: ${error.message}`, 'error');
    }
}

// --- Funciones para Renderizar y Filtrar ---
function createTaskCard(task) {
    const card = document.createElement('div');
    card.className = 'task-card';
    card.dataset.taskId = task.id; // Guarda el ID de la tarea en el dataset

    card.innerHTML = `
        <div class="task-header">
            <span class="task-type ${task.tipo}">${task.tipo.charAt(0).toUpperCase() + task.tipo.slice(1)}</span>
            <span class="task-priority priority-${task.prioridad}">${task.prioridad.charAt(0).toUpperCase() + task.prioridad.slice(1)}</span>
        </div>
        <div class="task-patient">${task.paciente}</div>
        <div class="task-details">
            ${task.phone ? `<div class="task-detail">📱 ${task.phone}</div>` : ''}
            ${task.eps ? `<div class="task-detail">🏥 ${task.eps}</div>` : ''}
            ${task.doctor ? `<div class="task-detail">👨‍⚕️ ${task.doctor}</div>` : ''}
            ${task.location ? `<div class="task-detail">📍 ${task.location}</div>` : ''}
            ${task.date ? `<div class="task-detail">📅 ${task.date} ${task.time || ''}</div>` : ''}
            <div class="task-detail">Estado: <span class="status status-${task.status.replace(/_/g, '')}">${task.status.replace(/_/g, ' ')}</span></div>
        </div>
        <div class="task-actions">
            <button class="task-action whatsapp" onclick="sendWhatsApp(${task.id})">WhatsApp</button>
            <button class="task-action edit" onclick="openModal(${task.id})">Editar</button>
            <button class="task-action next-stage" onclick="moveToNextStage(${task.id})">Siguiente Etapa</button>
            <button class="task-action delete" onclick="deleteTask(${task.id})">Eliminar</button>
        </div>
    `;
    return card;
}

function renderTasks() {
    statusOrder.forEach(status => {
        const column = document.getElementById(`column-${status.toLowerCase()}`); // El ID de la columna en HTML es en minúsculas
        if (column) {
            column.innerHTML = ''; // Limpiar la columna

            const filteredTasks = getFilteredTasks().filter(task => task.status === status);

            if (filteredTasks.length === 0) {
                column.innerHTML = '<div class="empty-state">No hay tareas en esta columna</div>';
            } else {
                filteredTasks.forEach(task => {
                    column.appendChild(createTaskCard(task));
                });
            }
        }
    });
    updateCounts(); // Actualiza los contadores después de renderizar
}

function getFilteredTasks() {
    const searchText = document.getElementById('filterSearch').value.toLowerCase();
    const statusFilter = document.getElementById('filterStatus').value; // Ya está en mayúsculas si se selecciona una opción
    const typeFilter = document.getElementById('filterType').value.toLowerCase();
    const priorityFilter = document.getElementById('filterPriority').value; // Ya está en mayúsculas

    return tasks.filter(task => {
        const matchesSearch = searchText ?
            (task.paciente.toLowerCase().includes(searchText) ||
                task.eps.toLowerCase().includes(searchText) ||
                task.tipo.toLowerCase().includes(searchText) ||
                task.observaciones.toLowerCase().includes(searchText)) : true;

        const matchesStatus = statusFilter ? task.status === statusFilter : true;
        const matchesType = typeFilter ? task.tipo === typeFilter : true;
        const matchesPriority = priorityFilter ? task.prioridad === priorityFilter : true;

        return matchesSearch && matchesStatus && matchesType && matchesPriority;
    });
}

function updateCounts() {
    statusOrder.forEach(status => {
        const count = tasks.filter(task => task.status === status).length;
        const countElement = document.getElementById(`count-${status.toLowerCase()}`);
        if (countElement) {
            countElement.textContent = count;
        }
    });
}

// --- Funciones Específicas de Acciones (WhatsApp, Recordatorios) ---
function sendWhatsApp(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (task && task.phone) {
        // Asegúrate de que el número de teléfono comience con el código del país (ej. +57)
        // Si tu backend no guarda el '+', deberías añadirlo aquí o asegurarte de que el usuario lo ingrese.
        // Asumimos que el backend ya guarda el número en formato internacional completo.
        const cleanedPhoneNumber = task.phone.replace(/[\s-()]/g, ''); // Limpiar espacios, guiones, paréntesis
        const finalPhoneNumber = cleanedPhoneNumber.startsWith('+') ? cleanedPhoneNumber : `+${cleanedPhoneNumber}`; // Añadir '+' si falta

        const message = `Hola señor/a ${task.paciente},\nLe saluda Biviana de la Central de Citas de Serviconli. 🌿\nMe permito recordarle que tiene una cita programada el día ${task.date || '[Fecha]'} a las ${task.time || '[Hora]'}, en ${task.location || '[Lugar o sede]'}, con el doctor/a ${task.doctor || '[Nombre del profesional]'}.\nEn Serviconli estamos comprometidos con su bienestar y el de su familia. ¡Gracias por confiar en nosotros! 💙\n— Equipo Serviconli`;

        const url = `https://wa.me/${finalPhoneNumber}?text=${encodeURIComponent(message)}`;
        window.open(url, '_blank');
        showNotification('Abriendo chat de WhatsApp...');
    } else {
        showNotification('No se encontró número de teléfono para este cliente o la tarea no existe.', 'error');
    }
}

function checkReminders() {
    const now = new Date();

    tasks.forEach(task => {
        // Recordar solo tareas 'PENDIENTE' o 'EN_PROGRESO' que tienen fecha y hora
        if (['PENDIENTE', 'EN_PROGRESO'].includes(task.status) && task.date && task.time) {
            const taskDateTime = new Date(`${task.date}T${task.time}`);
            // Activar recordatorio 5 minutos antes de la cita
            const reminderTime = new Date(taskDateTime.getTime() - 5 * 60 * 1000);

            // Asegurarse de que el recordatorio solo se muestre una vez por cada evento
            // Esto podría requerir un campo 'reminded' en la tarea o un set de IDs recordados.
            // Para simplicidad, solo mostramos si está dentro de la ventana de 5 minutos antes.
            if (now >= reminderTime && now < taskDateTime) {
                showNotification(`Recordatorio: ${task.paciente} - ${task.tipo.charAt(0).toUpperCase() + task.tipo.slice(1)} en 5 minutos (${task.doctor || 'Sin doctor'}) en ${task.location || 'sin ubicación'}`, 'warning');
            }
        }
    });
}

// --- Setup Inicial ---
function setupEventListeners() {
    document.getElementById('taskForm').addEventListener('submit', saveTask);
    document.getElementById('filterSearch').addEventListener('keyup', renderTasks); // Filtrar al escribir
}

function init() {
    fetchTasksFromBackend(); // Carga inicial de tareas
    setupEventListeners();

    // Verificar recordatorios cada minuto
    setInterval(checkReminders, 60000);

    // Cerrar modal con Escape
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeModal();
        }
    });

    // Cerrar modal al hacer clic fuera
    document.getElementById('taskModal').addEventListener('click', (e) => {
        if (e.target === e.currentTarget) {
            closeModal();
        }
    });
}

// Inicializar la aplicación cuando el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', init);