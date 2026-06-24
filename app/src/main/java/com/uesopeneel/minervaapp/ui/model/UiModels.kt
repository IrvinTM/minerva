package com.uesopeneel.minervaapp.ui.model

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
  val category: String,
  val score: Double,
  val maxScore: Double,
  val weight: Double
)

data class ScheduleItem(
  val courseCode: String,
  val courseName: String,
  val room: String,
  val weekday: String,
  val timeSlot: String,
  val instructor: String
)

data class AcademicNotification(
  val id: Int,
  val title: String,
  val message: String,
  val date: String,
  val category: String
)
