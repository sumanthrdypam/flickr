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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.flickr.presentation.theme.TextPrimary
import com.sam.flickr.presentation.theme.TextSecondary
import com.sam.flickr.presentation.theme.Transparent
import com.sam.flickr.presentation.theme.White
import com.sam.flickr.presentation.viewmodel.ImageViewModel
import com.sam.flickr.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    imageViewModel: ImageViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val searchQuery by imageViewModel.searchQuery.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.grid_spacing),
            vertical = dimensionResource(R.dimen.spacing_small)
        )
    ) {
        SearchTextField(
            query = searchQuery,
            onQueryChange = { newQuery ->
                imageViewModel.updateSearchQuery(newQuery)
            },
            onClearClick = {
                imageViewModel.updateSearchQuery("")
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
                color = White,
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
            ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = TextPrimary,
            letterSpacing = 0.15.sp
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                color = TextSecondary,
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon),
                tint = TextSecondary,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.search_clear),
                        tint = TextSecondary,
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        colors = TextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = TextSecondary,
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent
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