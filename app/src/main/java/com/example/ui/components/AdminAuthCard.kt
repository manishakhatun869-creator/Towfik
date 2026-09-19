package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.PremiumGradientButton
import com.example.ui.theme.PremiumRose600
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextTertiary

@Composable
fun AdminAuthCard(
  onLoginSuccess: () -> Unit,
  onLoginAttempt: (email: String, pass: String) -> Boolean,
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue

  fun attemptLogin(onDone: () -> Unit) {
    if (onLoginAttempt(email, password)) {
      onDone()
    } else {
      errorMessage = "Invalid credentials. Please check admin email and passcode."
    }
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .premiumShadow(elevation = 10.dp, shape = RoundedCornerShape(26.dp), alpha = 0.12f)
      .testTag("admin_auth_card"),
    shape = RoundedCornerShape(26.dp),
    colors = CardDefaults.cardColors(containerColor = premiumCard()),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Premium gradient banner header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(BrandGradient)
          .padding(horizontal = 22.dp, vertical = 20.dp),
      ) {
        // Glass circle decoration
        Box(
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(120.dp)
            .background(Color.White.copy(alpha = 0.08f), CircleShape),
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          GradientIconTile(
            icon = Icons.Default.Shield,
            gradient = androidx.compose.ui.graphics.Brush.linearGradient(
              listOf(Color.White.copy(alpha = 0.30f), Color.White.copy(alpha = 0.12f)),
            ),
            contentDescription = "Admin Security",
            size = 52.dp,
            cornerRadius = 16.dp,
            iconSize = 27.dp,
          )
          Column {
            Text(
              text = "Admin Authentication",
              fontSize = 19.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Secure console access for Towfik Sir",
              fontSize = 12.5.sp,
              color = Color(0xFFD6E4FF),
              fontWeight = FontWeight.Medium,
            )
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp),
      ) {
        // Email Input
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it
            errorMessage = null
          },
          label = { Text("Admin Email") },
          placeholder = { Text("Enter admin email") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Email,
              contentDescription = "Email",
              tint = accent,
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_email_input"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accent,
            unfocusedBorderColor = premiumBorder(),
            focusedLabelColor = accent,
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
          ),
          singleLine = true,
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Password Input
        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            errorMessage = null
          },
          label = { Text("Admin Password") },
          placeholder = { Text("Enter passcode") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = "Password",
              tint = accent,
            )
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                tint = premiumTextTertiary(),
              )
            }
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_password_input"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accent,
            unfocusedBorderColor = premiumBorder(),
            focusedLabelColor = accent,
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
          ),
          keyboardActions = KeyboardActions(
            onDone = { attemptLogin(onLoginSuccess) },
          ),
          singleLine = true,
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(if (dark) Color(0xFF3B1420) else Color(0xFFFFF1F2))
              .border(
                1.dp,
                if (dark) Color(0xFF6E2438) else Color(0xFFFECDD3),
                RoundedCornerShape(12.dp),
              )
              .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Icon(
              imageVector = Icons.Default.ErrorOutline,
              contentDescription = null,
              tint = PremiumRose600,
              modifier = Modifier.size(18.dp),
            )
            Text(
              text = errorMessage ?: "",
              color = if (dark) Color(0xFFFDA4AF) else Color(0xFFBE123C),
              fontSize = 12.5.sp,
              fontWeight = FontWeight.SemiBold,
              lineHeight = 17.sp,
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Unlock Button — premium gradient CTA
        Box(modifier = Modifier.testTag("unlock_admin_button")) {
          PremiumGradientButton(
            text = "Unlock Admin Dashboard",
            icon = Icons.Default.Lock,
            onClick = { attemptLogin(onLoginSuccess) },
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Protected by Firestore security rules • All uploads are synced to the cloud",
          fontSize = 11.sp,
          color = premiumTextTertiary(),
          modifier = Modifier.align(Alignment.CenterHorizontally),
        )
      }
    }
  }
}
