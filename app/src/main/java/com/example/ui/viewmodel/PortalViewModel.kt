package com.example.ui.viewmodel

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.UesAuthService
import com.example.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

// Screen navigation states
sealed class Screen {
  object Login : Screen()
  object Dashboard : Screen()
}

// Data Classes for Academic Portal
data class CourseGrade(
  val courseCode: String,
  val courseName: String,
  val instructor: String,
  val currentGrade: Double,
  val letterGrade: String,
  val credits: Int,
  val assignments: List<AssignmentGrade>,
  val colorHex: Long
)

data class AssignmentGrade(
  val name: String,
  val category: String, // Exam, Homework, Quiz, Project
  val score: Double,
  val maxScore: Double,
  val weight: Double // Percentage of final grade
)

data class ScheduleItem(
  val courseCode: String,
  val courseName: String,
  val room: String,
  val weekday: String, // "Mon", "Tue", etc.
  val timeSlot: String,
  val instructor: String
)

data class AcademicNotification(
  val id: Int,
  val title: String,
  val message: String,
  val date: String,
  val category: String // "Grade", "Alert", "Info"
)

class PortalViewModel(application: Application) : AndroidViewModel(application) {

  private val sessionManager = SessionManager(application)
  private val authService = UesAuthService.create()

  private val apiService = com.example.network.UesApiService.create()

  private val _facultad = MutableStateFlow<com.example.network.FacultadData?>(null)
  val facultad: StateFlow<com.example.network.FacultadData?> = _facultad.asStateFlow()

  private val _materias = MutableStateFlow<List<com.example.network.MateriaItem>>(emptyList())
  val materias: StateFlow<List<com.example.network.MateriaItem>> = _materias.asStateFlow()

  private val _isLoadingExpediente = MutableStateFlow(false)
  val isLoadingExpediente: StateFlow<Boolean> = _isLoadingExpediente.asStateFlow()

  private val _isExpedienteOpen = MutableStateFlow(false)
  val isExpedienteOpen: StateFlow<Boolean> = _isExpedienteOpen.asStateFlow()

  private val _selectedMateria = MutableStateFlow<com.example.network.MateriaItem?>(null)
  val selectedMateria: StateFlow<com.example.network.MateriaItem?> = _selectedMateria.asStateFlow()

  private val _evaluaciones = MutableStateFlow<List<com.example.network.EvaluacionDataItem>>(emptyList())
  val evaluaciones: StateFlow<List<com.example.network.EvaluacionDataItem>> = _evaluaciones.asStateFlow()

  private val _isLoadingEvaluaciones = MutableStateFlow(false)
  val isLoadingEvaluaciones: StateFlow<Boolean> = _isLoadingEvaluaciones.asStateFlow()

  // Current screen state
  private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // Authentication states
  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username.asStateFlow()

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password.asStateFlow()

  private val _isAuthenticating = MutableStateFlow(false)
  val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()

  private val _authError = MutableStateFlow<String?>(null)
  val authError: StateFlow<String?> = _authError.asStateFlow()

  // Logged in Student Details
  private val _studentName = MutableStateFlow("Irvin Toledo")
  val studentName: StateFlow<String> = _studentName.asStateFlow()

  private val _studentId = MutableStateFlow("MIN-2024-8992")
  val studentId: StateFlow<String> = _studentId.asStateFlow()

  private val _gpa = MutableStateFlow(3.84)
  val gpa: StateFlow<Double> = _gpa.asStateFlow()

  private val _creditsEarned = MutableStateFlow(114)
  val creditsEarned: StateFlow<Int> = _creditsEarned.asStateFlow()

  private val _termName = MutableStateFlow("Ciclo I - 2026")
  val termName: StateFlow<String> = _termName.asStateFlow()

  // Courses and Grade data
  private val _courses = MutableStateFlow<List<CourseGrade>>(emptyList())
  val courses: StateFlow<List<CourseGrade>> = _courses.asStateFlow()

  // Weekly Schedule Class items
  private val _schedule = MutableStateFlow<List<ScheduleItem>>(emptyList())
  val schedule: StateFlow<List<ScheduleItem>> = _schedule.asStateFlow()

  // Portal Notification list
  private val _notifications = MutableStateFlow<List<AcademicNotification>>(emptyList())
  val notifications: StateFlow<List<AcademicNotification>> = _notifications.asStateFlow()

  // Selected course for detailed view dialog
  private val _selectedCourse = MutableStateFlow<CourseGrade?>(null)
  val selectedCourse: StateFlow<CourseGrade?> = _selectedCourse.asStateFlow()

  init {
    loadMockData()
    checkExistingSession()
  }

  private fun checkExistingSession() {
    val savedToken = sessionManager.getAccessToken()
    val savedUsername = sessionManager.getUsername()
    if (savedToken != null && savedUsername != null) {
      _username.value = savedUsername
      processSuccessfulToken(savedToken)
      _currentScreen.value = Screen.Dashboard
    }
  }

  private fun processSuccessfulToken(token: String) {
    try {
      val parts = token.split(".")
      if (parts.size >= 2) {
        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val jsonString = String(decodedBytes, Charsets.UTF_8)
        val json = JSONObject(jsonString)
        
        val name = json.optString("name")
        val preferredUsername = json.optString("preferred_username")
        val email = json.optString("email")

        if (!name.isNullOrBlank()) {
          _studentName.value = name.trim()
        }
        if (!preferredUsername.isNullOrBlank()) {
          _studentId.value = preferredUsername.trim()
        } else if (!email.isNullOrBlank()) {
          _studentId.value = email.substringBefore("@")
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      val u = _username.value
      if (u.isNotEmpty()) {
        _studentName.value = if (u.contains("@")) u.substringBefore("@") else u
        _studentId.value = u
      }
    }
  }

  fun onUsernameChange(value: String) {
    _username.value = value
    _authError.value = null
  }

  fun onPasswordChange(value: String) {
    _password.value = value
    _authError.value = null
  }

  fun openExpediente() {
    _isExpedienteOpen.value = true
    if (_materias.value.isEmpty()) {
      loadExpediente()
    }
  }

  fun closeExpediente() {
    _isExpedienteOpen.value = false
    clearSelectedMateria()
  }

  fun selectMateria(materia: com.example.network.MateriaItem) {
    _selectedMateria.value = materia
    loadEvaluaciones(materia.id)
  }

  fun clearSelectedMateria() {
    _selectedMateria.value = null
    _evaluaciones.value = emptyList()
  }

  private fun loadEvaluaciones(idExpediente: Int) {
    viewModelScope.launch {
      val token = sessionManager.getAccessToken() ?: return@launch
      _isLoadingEvaluaciones.value = true
      try {
        val response = apiService.getEvaluaciones(token, token, idExpediente)
        if (response.isSuccessful) {
          _evaluaciones.value = response.body()?.data ?: emptyList()
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        _isLoadingEvaluaciones.value = false
      }
    }
  }

  private fun loadExpediente() {
    viewModelScope.launch {
      val token = sessionManager.getAccessToken() ?: return@launch
      _isLoadingExpediente.value = true
      try {
        val facRes = apiService.getFacultadPlanEstudio(token, token)
        if (facRes.isSuccessful) {
          _facultad.value = facRes.body()?.data
        }

        val ciclosRes = apiService.getCiclosEstudio(token, token)
        if (ciclosRes.isSuccessful) {
          val ciclos = ciclosRes.body()?.data ?: emptyList()
          // Pick the most recent ciclo
          val currentCiclo = ciclos.maxByOrNull { it.idPeriodo }
          
          if (currentCiclo != null) {
            val materiasRes = apiService.getMaterias(token, token, currentCiclo.idPeriodo)
            if (materiasRes.isSuccessful) {
              _materias.value = materiasRes.body()?.data ?: emptyList()
            }
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        _isLoadingExpediente.value = false
      }
    }
  }

  fun selectCourse(course: CourseGrade?) {
    _selectedCourse.value = course
  }

  fun handleSignIn() {
    val u = _username.value.trim()
    val p = _password.value

    if (u.isEmpty()) {
      _authError.value = "Por favor, ingresa tu usuario del portal o correo institucional."
      return
    }
    if (p.isEmpty()) {
      _authError.value = "Por favor, ingresa tu contraseña del portal."
      return
    }

    viewModelScope.launch {
      _isAuthenticating.value = true
      _authError.value = null

      try {
        val response = authService.authenticate(
          username = u,
          password = p
        )

        if (response.isSuccessful) {
          val body = response.body()
          if (body != null) {
            val token = body.accessToken
            sessionManager.saveSession(token, u)
            processSuccessfulToken(token)
            
            _isAuthenticating.value = false
            _currentScreen.value = Screen.Dashboard
          } else {
            _isAuthenticating.value = false
            _authError.value = "La respuesta del servidor no contiene datos de inicio de sesión."
          }
        } else {
          _isAuthenticating.value = false
          val errorBody = response.errorBody()?.string()
          if (response.code() == 401 || errorBody?.contains("invalid_grant") == true || errorBody?.contains("invalid_credentials") == true) {
            _authError.value = "Credenciales incorrectas. Verifica tu usuario y contraseña de la UES."
          } else {
            _authError.value = "Error al iniciar sesión (${response.code()}). Inténtalo de nuevo."
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
        _isAuthenticating.value = false
        _authError.value = "Error de red: No se pudo conectar al servidor de la UES."
      }
    }
  }

  fun handleSignOut() {
    sessionManager.clearSession()
    _password.value = ""
    _authError.value = null
    _currentScreen.value = Screen.Login
  }

  private fun loadMockData() {
    // 1. Course Grades & breakdowns
    _courses.value = listOf(
      CourseGrade(
        courseCode = "CSC 430",
        courseName = "Ingeniería de Software II",
        instructor = "Dra. Helen Vance",
        currentGrade = 94.8,
        letterGrade = "A",
        credits = 4,
        colorHex = 0xFF730E15, // Minerva Burgundy
        assignments = listOf(
          AssignmentGrade("Artefactos de Planificación de Sprint", "Activity", 10.0, 10.0, 0.10),
          AssignmentGrade("Prototipo de Simulación en Jetpack Compose", "Project", 95.0, 100.0, 0.25),
          AssignmentGrade("Examen Parcial Escrito", "Exam", 92.5, 100.0, 0.30),
          AssignmentGrade("Configuración de Integración Continua", "Quiz", 10.0, 10.0, 0.05),
          AssignmentGrade("Presentación de Arquitectura Final", "Project", 98.0, 100.0, 0.30)
        )
      ),
      CourseGrade(
        courseCode = "MAT 310",
        courseName = "Álgebra Lineal Aplicada",
        instructor = "Prof. Marcus Brody",
        currentGrade = 88.5,
        letterGrade = "B+",
        credits = 3,
        colorHex = 0xFF1E0305, // Deep Accent Charcoal
        assignments = listOf(
          AssignmentGrade("Corto 1: Espacios Vectoriales", "Quiz", 8.0, 10.0, 0.15),
          AssignmentGrade("Tarea de Transformaciones Matriciales", "Homework", 92.0, 100.0, 0.20),
          AssignmentGrade("Examen Parcial", "Exam", 84.0, 100.0, 0.35),
          AssignmentGrade("Práctica de Autovalores en Laboratorio", "Activity", 10.0, 10.0, 0.10),
          AssignmentGrade("Trabajo de Aplicación de SVD", "Project", 95.0, 100.0, 0.20)
        )
      ),
      CourseGrade(
        courseCode = "CSC 450",
        courseName = "Diseño de Bases de Datos Distribuidas",
        instructor = "Dr. Alistair Finch",
        currentGrade = 97.2,
        letterGrade = "A+",
        credits = 4,
        colorHex = 0xFF0F9D58, // Academic success green
        assignments = listOf(
          AssignmentGrade("Análisis de Consenso Raft", "Activity", 15.0, 15.0, 0.10),
          AssignmentGrade("Despliegue de Clúster NoSQL", "Project", 100.0, 100.0, 0.30),
          AssignmentGrade("Caso de Estudio Parcial", "Exam", 94.0, 100.0, 0.30),
          AssignmentGrade("Laboratorio de Aislamiento de Transacciones", "Homework", 10.0, 10.0, 0.10),
          AssignmentGrade("Informe de Rendimiento Final Cassandra", "Project", 98.0, 100.0, 0.20)
        )
      ),
      CourseGrade(
        courseCode = "PHY 220",
        courseName = "Física Universitaria II (Electromagnetismo)",
        instructor = "Dra. Roberta Klein",
        currentGrade = 85.1,
        letterGrade = "B",
        credits = 4,
        colorHex = 0xFFE67E22, // Borderline Warning Orange
        assignments = listOf(
          AssignmentGrade("Guía de Ley de Gauss", "Homework", 82.0, 100.0, 0.15),
          AssignmentGrade("Cálculo de Potencial Eléctrico", "Activity", 18.0, 20.0, 0.15),
          AssignmentGrade("Laboratorio 3: Circuitos de CA", "Activity", 88.0, 100.0, 0.20),
          AssignmentGrade("Examen Parcial de Física", "Exam", 81.0, 100.0, 0.30),
          AssignmentGrade("Ensayo de Inductores y Ondas de CA", "Homework", 94.0, 100.0, 0.20)
        )
      ),
      CourseGrade(
        courseCode = "LCI 102",
        courseName = "Composición y Discurso Literario",
        instructor = "Prof. Clara Higgins",
        currentGrade = 91.4,
        letterGrade = "A-",
        credits = 3,
        colorHex = 0xFFC5A059, // Gold Accent
        assignments = listOf(
          AssignmentGrade("Ensayo de Respuesta 1: Otelo", "Homework", 90.0, 100.0, 0.20),
          AssignmentGrade("Trabajo de Revisión por Pares", "Activity", 10.0, 10.0, 0.10),
          AssignmentGrade("Bibliografía Anotada", "Homework", 95.0, 100.0, 0.25),
          AssignmentGrade("Control de Lectura Parcial", "Quiz", 8.5, 10.0, 0.15),
          AssignmentGrade("Propuesta del Proyecto de Investigación", "Project", 91.0, 100.0, 0.30)
        )
      )
    )

    // 2. Schedule items
    _schedule.value = listOf(
      ScheduleItem("CSC 430", "Ing. de Software II", "Edificio de Ingeniería 302", "Lun", "09:00 AM - 10:30 AM", "Dra. Vance"),
      ScheduleItem("MAT 310", "Álgebra Lineal", "Edificio de Ciencias 105", "Lun", "11:00 AM - 12:15 PM", "Prof. Brody"),
      ScheduleItem("PHY 220", "Lab. de Física II", "Anexo de Ciencias 12B", "Mar", "02:00 PM - 04:50 PM", "Dra. Klein"),
      ScheduleItem("CSC 450", "Bases Dat. Distribuidas", "Centro de Cómputo 211", "Mié", "10:00 AM - 11:30 AM", "Dr. Finch"),
      ScheduleItem("CSC 430", "Ing. de Software II", "Edificio de Ingeniería 302", "Mié", "09:00 AM - 10:30 AM", "Dra. Vance"),
      ScheduleItem("MAT 310", "Álgebra Lineal", "Edificio de Ciencias 105", "Mié", "11:00 AM - 12:15 PM", "Prof. Brody"),
      ScheduleItem("LCI 102", "Discurso Literario", "Edificio de Humanidades 4", "Jue", "01:00 PM - 02:15 PM", "Prof. Higgins"),
      ScheduleItem("PHY 220", "Clase de Física II", "Auditorio de Ciencias A", "Jue", "03:00 PM - 04:30 PM", "Dra. Klein"),
      ScheduleItem("CSC 450", "Bases Dat. Distribuidas", "Centro de Cómputo 211", "Vie", "10:00 AM - 11:30 AM", "Dr. Finch")
    )

    // 3. Official Notifications
    _notifications.value = listOf(
      AcademicNotification(
        id = 101,
        title = "Nota de Parcial Publicada",
        message = "La Dra. Helen Vance ha publicado las notas para CSC 430 (Examen Parcial Escrito).",
        date = "Hoy, 10:15 AM",
        category = "Grade"
      ),
      AcademicNotification(
        id = 102,
        title = "Inscripción Ciclo II Abierta",
        message = "El período de inscripción para estudiantes de último año y destacados ya está activo.",
        date = "Ayer, 08:00 AM",
        category = "Alert"
      ),
      AcademicNotification(
        id = 103,
        title = "Horario de Biblioteca Extendido",
        message = "La Biblioteca Central Minerva permanecerá abierta las 24 horas del día durante la semana de exámenes finales a partir del 18 de junio.",
        date = "Jun 14, 2026",
        category = "Info"
      )
    )
  }
}
