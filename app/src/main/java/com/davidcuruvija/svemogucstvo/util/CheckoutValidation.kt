package com.davidcuruvija.svemogucstvo.util

fun isValidName(value : String) : Boolean {
    return value.isNotBlank() &&
            value.all {
                it.isLetter() || it == ' ' || it == '-' || it == '\''
            }
}

fun isValidEmail(value : String) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS
        .matcher(value)
        .matches()
}

fun isValidAddress(value : String) : Boolean {
    val parts = value.trim().split(Regex("\\s+"))

    if (parts.size < 2) {
        return false
    }

    return value.any { it.isLetter() }
}

fun isValidCity(value : String) : Boolean {
    return value.isNotBlank() &&
            value.all {
                it.isLetter() || it == ' ' || it == '-'
            }
}

fun isValidPostalCode(value : String) : Boolean {
    return Regex("^\\d{5}$").matches(value.trim())
}

fun isValidPhone(value : String) : Boolean {
    val cleaned = value.filter {
        it.isDigit() || it == '+' || it == ' ' || it == '-' || it == '(' || it == ')'
    }

    val digitCount = value.count { it.isDigit() }

    return cleaned == value && digitCount in 8..15
}