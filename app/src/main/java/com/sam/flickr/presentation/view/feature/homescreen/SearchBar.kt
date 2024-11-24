package com.sam.flickr.presentation.view.feature.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.flickr.presentation.viewmodel.ImageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    imageViewModel: ImageViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val searchQuery by imageViewModel.query.collectAsState()

    Box(
        modifier = modifier.padding(
            horizontal = Constants.GRID_SPACING.dp,
            vertical = 8.dp
        )
    ) {
        SearchTextField(
            query = searchQuery,
            onQueryChange = { newQuery ->
                imageViewModel.query(newQuery)
            },
            onClearClick = {
                imageViewModel.query("")
            },
            focusManager = focusManager
        )
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    focusManager: FocusManager
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(Constants.CORNER_RADIUS.dp)
            ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            letterSpacing = 0.15.sp
        ),
        placeholder = {
            Text(
                "Search...",
                color = Color(Constants.GRAY_COLOR),
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(Constants.GRAY_COLOR),
                modifier = Modifier.size(Constants.ICON_SIZE.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClearClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = Color(Constants.GRAY_COLOR),
                        modifier = Modifier.size(Constants.ICON_SIZE.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(Constants.CORNER_RADIUS.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(Constants.GRAY_COLOR),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            }
        )
    )
}



