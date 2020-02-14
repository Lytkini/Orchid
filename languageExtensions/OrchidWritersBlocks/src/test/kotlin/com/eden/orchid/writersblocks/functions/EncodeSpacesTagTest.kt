package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.ClogStringConverterHelper
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.p
import kotlinx.html.unsafe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class EncodeSpacesTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    companion object {
        const val input = "    dog    "
        const val expected = "&nbsp;&nbsp;&nbsp;&nbsp;dog&nbsp;&nbsp;&nbsp;&nbsp;"
    }

    @Test
    @DisplayName("Test pluralize tag.")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{{ input|encodeSpaces }}
            """.trimMargin(),
            mapOf(
                "input" to input
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        unsafe { +expected }
                    }
                }
            }
    }

    @Test
    fun test02() {
        val underTest = EncodeSpacesFunction()
        val context = mock(OrchidContext::class.java)
        `when`(context.resolve(StringConverter::class.java)).thenReturn(
            StringConverter(
                setOf(ClogStringConverterHelper())
            )
        )

        underTest.input = input

        val actual = underTest.apply(context, null)

        expectThat(actual.toByteArray()).isEqualTo(expected.toByteArray())
        expectThat(actual).isEqualTo(expected)
    }
}
