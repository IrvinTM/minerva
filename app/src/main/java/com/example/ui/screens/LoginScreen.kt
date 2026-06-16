package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MinervaBlue
import com.example.ui.theme.MinervaBlueDark
import com.example.ui.theme.MinervaBlueLight
import com.example.ui.theme.MinervaGrayDark
import com.example.ui.theme.MinervaGrayMedium
import com.example.ui.theme.MinervaGold
import androidx.compose.ui.graphics.Path
import com.example.ui.viewmodel.PortalViewModel

@Composable
fun LoginScreen(
  viewModel: PortalViewModel,
  modifier: Modifier = Modifier
) {
  val username by viewModel.username.collectAsState()
  val password by viewModel.password.collectAsState()
  val isAuthenticating by viewModel.isAuthenticating.collectAsState()
  val authError by viewModel.authError.collectAsState()

  val focusManager = LocalFocusManager.current
  var passwordVisible by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    // Subtle background ambient brand graphic
    Box(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 480.dp)
          .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        
        Spacer(modifier = Modifier.height(24.dp))

        // Programmatic academic logo
        MinervaCrestLogo(
          modifier = Modifier
            .size(110.dp)
            .padding(bottom = 12.dp)
        )

        // University Branded Header
        Text(
          text = "MINERVA",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
            color = MinervaBlue
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("app_brand_title")
        )

        Text(
          text = "PORTAL ACADÉMICO UNIVERSITARIO",
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            color = MinervaGrayMedium
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // Login Card Card Container
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
          )
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Iniciar Sesión",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MinervaGrayDark
              ),
              modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 6.dp)
            )

            Text(
              text = "Ingresa tus credenciales oficiales de estudiante para acceder al portal.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MinervaGrayMedium
              ),
              modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp)
            )

            // Dynamic Error Alert
            AnimatedVisibility(
              visible = authError != null,
              enter = fadeIn(),
              exit = fadeOut()
            ) {
              authError?.let { msg ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(
                      color = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                      shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Error notification",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.Medium,
                      color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }

            // Username input
            OutlinedTextField(
              value = username,
              onValueChange = { viewModel.onUsernameChange(it) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("username_input"),
              label = { Text("Usuario o Correo Institucional") },
              placeholder = { Text("ej. s123456 o estudiante@minerva.edu") },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = "Icono de campo de usuario",
                  tint = if (username.isNotEmpty()) MinervaBlue else MinervaGrayMedium
                )
              },
              singleLine = true,
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
              ),
              keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
              ),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinervaBlue,
                focusedLabelColor = MinervaBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                cursorColor = MinervaBlue
              ),
              shape = RoundedCornerShape(12.dp)
            )

            // Password input
            OutlinedTextField(
              value = password,
              onValueChange = { viewModel.onPasswordChange(it) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("password_input"),
              label = { Text("Contraseña del Portal") },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = "Icono de campo de contraseña",
                  tint = if (password.isNotEmpty()) MinervaBlue else MinervaGrayMedium
                )
              },
              trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                  Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = MinervaGrayMedium
                  )
                }
              },
              visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              singleLine = true,
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(
                onDone = {
                  focusManager.clearFocus()
                  viewModel.handleSignIn()
                }
              ),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinervaBlue,
                focusedLabelColor = MinervaBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                cursorColor = MinervaBlue
              ),
              shape = RoundedCornerShape(12.dp)
            )

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
              horizontalArrangement = Arrangement.End
            ) {
              Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = MinervaBlue
                ),
                modifier = Modifier.padding(vertical = 4.dp)
              )
            }

            // Prominent sign in button
            Button(
              onClick = {
                focusManager.clearFocus()
                viewModel.handleSignIn()
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = MinervaBlue,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(12.dp),
              elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
              ),
              enabled = !isAuthenticating
            ) {
              if (isAuthenticating) {
                CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = Color.White,
                  strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = "Conectando al Portal...",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                  )
                )
              } else {
                Text(
                  text = "Ingresar",
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security / Academic Handshake Info Box
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .background(MinervaBlueLight, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Emblema de conexión segura SSL",
            tint = MinervaBlueDark,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "El Portal MINERVA utiliza autenticación cifrada de doble factor para garantizar la privacidad académica bajo una conexión segura.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MinervaBlueDark,
              lineHeight = 16.sp
            ),
            modifier = Modifier.weight(1f)
          )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Portal quick-demo advisory note
        Text(
          text = "Modo de Demostración Rápida: Ingresa cualquier credencial (ej. estudiante@minerva.edu / 12345) para probar.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MinervaGrayMedium,
            textAlign = TextAlign.Center
          ),
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
      }
    }
  }
}

// Beautiful customized vector badge logo depicting goddess Minerva of El Salvador
@Composable
fun MinervaCrestLogo(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val sizePx = size.width
    val center = Offset(sizePx / 2f, sizePx / 2f)
    
    // Radial gradient brush for the background shield disk
    val brush = Brush.radialGradient(
      colors = listOf(MinervaBlue, MinervaBlueDark),
      center = center,
      radius = sizePx * 0.7f
    )

    // Draw background outer circle/oval seal base
    drawCircle(
      brush = brush,
      radius = sizePx * 0.48f,
      center = center
    )

    // Concentric gold outer border
    drawCircle(
      color = MinervaGold,
      radius = sizePx * 0.45f,
      center = center,
      style = Stroke(width = sizePx * 0.025f)
    )

    // Concentric gold inner border
    drawCircle(
      color = MinervaGold,
      radius = sizePx * 0.40f,
      center = center,
      style = Stroke(width = sizePx * 0.01f)
    )

    // Draw stylized Greek Temple / Minerva classical emblem in White
    // Drawing classical Minerva profile with simple elegant overlapping paths
    val p = Path().apply {
      // Crown cap
      moveTo(sizePx * 0.48f, sizePx * 0.28f)
      quadraticTo(sizePx * 0.38f, sizePx * 0.28f, sizePx * 0.36f, sizePx * 0.38f)
      quadraticTo(sizePx * 0.36f, sizePx * 0.44f, sizePx * 0.42f, sizePx * 0.44f)
      quadraticTo(sizePx * 0.48f, sizePx * 0.40f, sizePx * 0.54f, sizePx * 0.38f)
      quadraticTo(sizePx * 0.58f, sizePx * 0.32f, sizePx * 0.48f, sizePx * 0.28f)
    }
    drawPath(p, color = Color.White)

    // Nose, lips and head profile
    val profilePath = Path().apply {
      moveTo(sizePx * 0.44f, sizePx * 0.36f)
      lineTo(sizePx * 0.40f, sizePx * 0.44f) // forehead line
      quadraticTo(sizePx * 0.38f, sizePx * 0.48f, sizePx * 0.35f, sizePx * 0.52f) // Nose tip
      lineTo(sizePx * 0.40f, sizePx * 0.53f) // Nose base
      quadraticTo(sizePx * 0.37f, sizePx * 0.55f, sizePx * 0.36f, sizePx * 0.57f) // Lips
      lineTo(sizePx * 0.41f, sizePx * 0.58f)
      quadraticTo(sizePx * 0.38f, sizePx * 0.62f, sizePx * 0.42f, sizePx * 0.65f) // Chin
      quadraticTo(sizePx * 0.46f, sizePx * 0.65f, sizePx * 0.48f, sizePx * 0.60f) // Neck
      lineTo(sizePx * 0.58f, sizePx * 0.68f) // Shoulder
      lineTo(sizePx * 0.58f, sizePx * 0.46f)
      lineTo(sizePx * 0.48f, sizePx * 0.36f)
      close()
    }
    drawPath(profilePath, color = Color.White)

    // Classic hair roll back bun
    drawCircle(
      color = Color.White,
      radius = sizePx * 0.08f,
      center = Offset(sizePx * 0.58f, sizePx * 0.48f)
    )
    drawCircle(
      color = Color.White,
      radius = sizePx * 0.05f,
      center = Offset(sizePx * 0.64f, sizePx * 0.52f)
    )

    // Shoulder/Toga base plate
    val togaPath = Path().apply {
      moveTo(sizePx * 0.40f, sizePx * 0.64f)
      quadraticTo(sizePx * 0.38f, sizePx * 0.72f, sizePx * 0.46f, sizePx * 0.76f)
      quadraticTo(sizePx * 0.54f, sizePx * 0.76f, sizePx * 0.64f, sizePx * 0.70f)
      quadraticTo(sizePx * 0.62f, sizePx * 0.60f, sizePx * 0.48f, sizePx * 0.62f)
      close()
    }
    drawPath(togaPath, color = Color.White)

    // Surrounding Laurel Leaf Wreaths in Gold
    val laurelPathLeft = Path().apply {
      moveTo(sizePx * 0.20f, sizePx * 0.50f)
      quadraticTo(sizePx * 0.20f, sizePx * 0.72f, sizePx * 0.50f, sizePx * 0.82f)
    }
    val laurelPathRight = Path().apply {
      moveTo(sizePx * 0.80f, sizePx * 0.50f)
      quadraticTo(sizePx * 0.80f, sizePx * 0.72f, sizePx * 0.50f, sizePx * 0.82f)
    }
    drawPath(
      path = laurelPathLeft,
      color = MinervaGold,
      style = Stroke(width = sizePx * 0.015f, cap = StrokeCap.Round)
    )
    drawPath(
      path = laurelPathRight,
      color = MinervaGold,
      style = Stroke(width = sizePx * 0.015f, cap = StrokeCap.Round)
    )

    // Tiny decorative leaves
    drawCircle(color = MinervaGold, radius = sizePx * 0.025f, center = Offset(sizePx * 0.22f, sizePx * 0.55f))
    drawCircle(color = MinervaGold, radius = sizePx * 0.025f, center = Offset(sizePx * 0.26f, sizePx * 0.65f))
    drawCircle(color = MinervaGold, radius = sizePx * 0.025f, center = Offset(sizePx * 0.34f, sizePx * 0.73f))

    drawCircle(color = MinervaGold, radius = sizePx * 0.025f, center = Offset(sizePx * 0.78f, sizePx * 0.55f))
    drawCircle(color = MinervaGold, radius = sizePx * 0.025f, center = Offset(sizePx * 0.74f, sizePx * 0.65f))
    drawCircle(color = MinervaGold, radius = sizePx * 0.026f, center = Offset(sizePx * 0.66f, sizePx * 0.73f))
  }
}
