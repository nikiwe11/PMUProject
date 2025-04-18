package com.example.chatapp.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.chatapp.general.Constants
import com.example.chatapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: (String) -> Boolean = { false },
    inputIsRequired: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    regex: String = Constants.Regex.NO_LIMIT,
    labelText: String? = null,
    errorLabel: String = stringResource(R.string.field_cannot_be_empty_warning),
    onFocusLost: () -> Unit = {},
    containerColor: Color = Color.White,
    textColor: Color = Color.Black,
    unfocusedIndicatorColor: Color = Color.Transparent,
    focusedIndicatorColor: Color = Color.Transparent,
    unfocusedLabelColor: Color = Color.Black.copy(alpha = 0.75f),
    focusedLabelColor: Color = Color.Gray,
    border: BorderStroke? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var input by rememberSaveable { mutableStateOf("") }
    input = value
    var inputEntered by rememberSaveable { mutableStateOf(false) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && isFocused) {
                    onFocusLost()
                }
                isFocused = focusState.isFocused
            }
            .let { originalModifier ->
                border?.let {
                    originalModifier.border(border = it, shape = RoundedCornerShape(12.dp))
                } ?: originalModifier
            },
        shape = RoundedCornerShape(12.dp),
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = {
            if (it.length <= 30 && (it.isEmpty() || it.matches(regex.toRegex()))) {
                input = it
                onValueChange(it)
            } else {
                input = it.dropLast(1)
                onValueChange(it.dropLast(1))
            }
            inputEntered = true
        },
        label = labelText?.let {
            {
                Text(
                    text = when {
                        input.isEmpty() && inputIsRequired && inputEntered -> stringResource(R.string.field_cannot_be_empty_warning)
                        isError(input) && inputEntered -> errorLabel
                        else -> labelText
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            containerColor = containerColor,
            unfocusedLabelColor = unfocusedLabelColor,
            focusedIndicatorColor = focusedIndicatorColor,
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            focusedLabelColor = focusedLabelColor,
            errorLabelColor = Color.Red,
            cursorColor = textColor,
        ),
        singleLine = true
    )
}