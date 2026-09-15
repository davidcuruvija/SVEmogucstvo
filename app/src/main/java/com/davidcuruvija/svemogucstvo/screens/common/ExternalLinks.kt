package com.davidcuruvija.svemogucstvo.screens.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

const val INSTAGRAM_USERNAME = "sve.mogucstvo"
const val WEBSITE_URL = "https://svemogucstvo.com"

// Deep-links into the Instagram app when installed (opens the profile natively,
// not in a browser); falls back to the profile's web page otherwise.
fun openInstagramProfile(context: Context, username: String) {
    val appIntent = Intent(
        Intent.ACTION_VIEW,
        "http://instagram.com/_u/$username".toUri()
    ).apply {
        setPackage("com.instagram.android")
    }

    try {
        context.startActivity(appIntent)
    } catch (e: ActivityNotFoundException) {
        openUrl(context, "https://instagram.com/$username")
    }
}

fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
