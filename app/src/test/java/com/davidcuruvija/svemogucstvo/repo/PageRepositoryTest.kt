package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WordPressApi
import com.davidcuruvija.svemogucstvo.model.page.PageContentDto
import com.davidcuruvija.svemogucstvo.model.page.PageDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PageRepositoryTest {

    private class FakeWordPressApi(private val renderedHtml : String) : WordPressApi {
        override suspend fun getPage(id : Int) : PageDto {
            return PageDto(id = id, content = PageContentDto(rendered = renderedHtml))
        }
    }

    @Test
    fun getPageContent_extractsOnlyLeadClassedTitleAndParagraphs() = runBlocking {
        val html = """
            <div class="banner has-parallax">
                <h1 class="lead">About Us</h1>
                <p>Not a lead paragraph, should be ignored.</p>
            </div>
            <div class="row"><div class="col">
                <p class="lead">First real paragraph.</p>
                <p class="lead">Second real paragraph.</p>
            </div></div>
        """.trimIndent()

        val repository = PageRepository(FakeWordPressApi(html))
        val content = repository.getPageContent(95)

        assertEquals("About Us", content.title)
        assertEquals(
            listOf("First real paragraph.", "Second real paragraph."),
            content.paragraphs
        )
    }

    @Test
    fun getPageContent_decodesHtmlEntitiesInExtractedText() = runBlocking {
        val html = """<h1 class="lead">Get In Touch</h1><p class="lead">It&#8217;s here &#8220;quoted&#8221;.</p>"""

        val repository = PageRepository(FakeWordPressApi(html))
        val content = repository.getPageContent(93)

        assertEquals("It’s here “quoted”.", content.paragraphs.first())
    }

    @Test
    fun getPageContent_returnsEmptyTitleWhenNoLeadHeadingPresent() = runBlocking {
        val html = """<p class="lead">Only a paragraph, no heading.</p>"""

        val repository = PageRepository(FakeWordPressApi(html))
        val content = repository.getPageContent(1)

        assertEquals("", content.title)
        assertEquals(listOf("Only a paragraph, no heading."), content.paragraphs)
    }

    @Test
    fun getPageContent_ignoresBlankLeadParagraphs() = runBlocking {
        val html = """
            <h1 class="lead">Title</h1>
            <p class="lead">   </p>
            <p class="lead">Real content.</p>
        """.trimIndent()

        val repository = PageRepository(FakeWordPressApi(html))
        val content = repository.getPageContent(1)

        assertEquals(listOf("Real content."), content.paragraphs)
    }
}
