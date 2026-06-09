package com.campus.teamup.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object TextFieldStyle {

    @Composable
    fun myTextFieldColor() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF667eea),
        unfocusedBorderColor = Color(0xFFE2E8F0),
        focusedLabelColor = Color(0xFF667eea),
        cursorColor = Color(0xFF667eea)
    )

    val defaultShape = RoundedCornerShape(20.dp)
}
