package com.sam.flickr.presentation.view.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.Locale

object TextUtils {
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            inputFormat.parse(dateString)?.let { date ->
                outputFormat.format(date)
            } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
        val spannable = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        return buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            ) {
                append(spannable.toString())
            }
        }
    }

    fun parseImageDimensions(description: String): Pair<String, String> {
        val widthRegex = "width=\"(\\d+)\"".toRegex()
        val heightRegex = "height=\"(\\d+)\"".toRegex()

        val width = widthRegex.find(description)?.groupValues?.get(1) ?: "Unknown"
        val height = heightRegex.find(description)?.groupValues?.get(1) ?: "Unknown"

        return Pair(width, height)
    }
} 