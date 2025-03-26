package com.example.chatapp.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.general.Constants
import com.example.chatapp.general.selectFromTheme
import com.example.chatapp.R

import com.example.chatapp.ui.theme.ChatAppTheme

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
) {
    var input by rememberSaveable {
        mutableStateOf("")
    }
    input = value
    var inputEntered by rememberSaveable {
        mutableStateOf(false)
    }
    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    TextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && isFocused) {
                    onFocusLost()
                }
                isFocused = focusState.isFocused
            }
            .let {
                selectFromTheme(
                    lightThemeValue = it.border(
                        border = BorderStroke(
                            1.dp,
                            Color.Black
                        ), shape = RoundedCornerShape(12.dp)
                    ), darkThemeValue = it
                )
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
                    }, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.Black, // todo watch out for deprecation
            containerColor = Color.White,
            unfocusedLabelColor = Color.Black.copy(alpha = 0.75f),
            focusedIndicatorColor = Color.Transparent, // the line at the bottom (barely visible because of background)
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Color.Gray,
            errorLabelColor = Color.Red,
            cursorColor = Color.Black,
        ),
        singleLine = true
    )
}

@Preview
@Composable

fun Preview() {
    ChatAppTheme {
        InputField(
            modifier = Modifier,
            value = "",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(),
            regex = Constants.Regex.ONLY_NUMBERS,
            labelText = "TEXT",
            errorLabel = "ERROR"
        )
    }
}