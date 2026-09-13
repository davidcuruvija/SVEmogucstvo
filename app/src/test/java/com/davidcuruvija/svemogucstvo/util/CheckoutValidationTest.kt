package com.davidcuruvija.svemogucstvo.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CheckoutValidationTest {

    @Test
    fun isValidName_acceptsLettersSpacesHyphensAndApostrophes() {
        assertTrue(isValidName("Marina Ćuruvija"))
        assertTrue(isValidName("Anne-Marie"))
        assertTrue(isValidName("O'Brien"))
    }

    @Test
    fun isValidName_rejectsBlankOrNonLetterCharacters() {
        assertFalse(isValidName(""))
        assertFalse(isValidName("   "))
        assertFalse(isValidName("John3"))
        assertFalse(isValidName("John@Doe"))
    }

    @Test
    fun isValidEmail_acceptsWellFormedAddress() {
        assertTrue(isValidEmail("test@example.com"))
    }

    @Test
    fun isValidEmail_rejectsMissingAtSignOrBlank() {
        assertFalse(isValidEmail("testexample.com"))
        assertFalse(isValidEmail(""))
    }

    @Test
    fun isValidAddress_acceptsStreetNameAndNumber() {
        assertTrue(isValidAddress("Pere Perica 323b"))
    }

    @Test
    fun isValidAddress_rejectsSingleWordOrBlank() {
        assertFalse(isValidAddress("Belgrade"))
        assertFalse(isValidAddress(""))
    }

    @Test
    fun isValidAddress_rejectsMultipleWordsWithNoLetters() {
        assertFalse(isValidAddress("123 456"))
    }

    @Test
    fun isValidCity_acceptsLettersSpacesHyphensAndUnicodeLetters() {
        assertTrue(isValidCity("Novi Sad"))
        assertTrue(isValidCity("Inđija"))
    }

    @Test
    fun isValidCity_rejectsDigitsOrBlank() {
        assertFalse(isValidCity("Belgrade2"))
        assertFalse(isValidCity(""))
    }

    @Test
    fun isValidPostalCode_acceptsExactlyFiveDigitsIgnoringWhitespace() {
        assertTrue(isValidPostalCode("11000"))
        assertTrue(isValidPostalCode(" 11000 "))
    }

    @Test
    fun isValidPostalCode_rejectsWrongLengthOrNonDigits() {
        assertFalse(isValidPostalCode("1100"))
        assertFalse(isValidPostalCode("110000"))
        assertFalse(isValidPostalCode("abcde"))
    }

    @Test
    fun isValidPhone_acceptsPlausibleInternationalNumber() {
        assertTrue(isValidPhone("+381 64 1234567"))
    }

    @Test
    fun isValidPhone_rejectsTooFewOrTooManyDigits() {
        assertFalse(isValidPhone("12345"))
        assertFalse(isValidPhone("1234567890123456"))
    }

    @Test
    fun isValidPhone_rejectsLetters() {
        assertFalse(isValidPhone("064-ABCDEFG"))
    }
}
