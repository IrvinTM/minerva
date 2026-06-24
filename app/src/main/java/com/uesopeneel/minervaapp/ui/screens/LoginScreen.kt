package com.uesopeneel.minervaapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import com.uesopeneel.minervaapp.ui.theme.MinervaBlue
import com.uesopeneel.minervaapp.ui.theme.MinervaBlueDark
import com.uesopeneel.minervaapp.ui.theme.MinervaBlueLight
import com.uesopeneel.minervaapp.ui.theme.MinervaGrayDark
import com.uesopeneel.minervaapp.ui.theme.MinervaGrayMedium
import com.uesopeneel.minervaapp.ui.theme.MinervaGold
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import com.uesopeneel.minervaapp.R
import com.uesopeneel.minervaapp.ui.viewmodel.PortalViewModel

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

        // University Academic Logo
        Image(
          painter = painterResource(id = R.drawable.ic_login_logo),
          contentDescription = "Minerva Logo",
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
          text = "EXPEDIENTE EN LINEA",
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
              text = "Ingresa tus credenciales",
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
              label = { Text("Usuario") },
              placeholder = { Text("ej. tt23023") },
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
              label = { Text("Contraseña") },
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
            text = "...",
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
          text = "Tener en cuenta que la app esta en desarrollo",
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
