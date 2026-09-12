package com.davidcuruvija.svemogucstvo.repo

import com.davidcuruvija.svemogucstvo.data.remote.WordPressApi
import com.davidcuruvija.svemogucstvo.model.page.PageContent
import org.jsoup.Jsoup
import javax.inject.Inject

class PageRepository @Inject constructor(private val api : WordPressApi) {

    // Pages are built with Flatsome's page builder, which wraps content in a lot of
    // layout/banner markup. Rather than rendering that raw HTML, only the elements
    // explicitly marked class="lead" (the title and body paragraphs) are pulled out,
    // so the app stays immune to unrelated layout changes made in the page builder.
    suspend fun getPageContent(pageId : Int) : PageContent {
        val page = api.getPage(pageId)
        val document = Jsoup.parse(page.content.rendered)

        val title = document.select("h1.lead, h2.lead").firstOrNull()?.text().orEmpty()
        val paragraphs = document.select("p.lead")
            .map { it.text() }
            .filter { it.isNotBlank() }

        return PageContent(
            title = title,
            paragraphs = paragraphs
        )
    }
}
