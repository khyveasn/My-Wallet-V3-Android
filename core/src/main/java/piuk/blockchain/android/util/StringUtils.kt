package piuk.blockchain.android.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ClickableSpan
import android.view.View

import javax.inject.Inject

class StringUtils @Inject
constructor(private val context: Context) {

    fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }

    @Deprecated("Hides warnings/errors about mismatched number of arguments TODO: Inline, AND-1297")
    fun getQuantityString(@PluralsRes pluralId: Int, size: Int): String {
        return context.resources.getQuantityString(pluralId, size, size)
    }

    @Deprecated("Hides warnings/errors about mismatched number of arguments TODO: Inline, AND-1297")
    fun getFormattedString(@StringRes stringId: Int, vararg formatArgs: Any): String {
        return context.resources.getString(stringId, *formatArgs)
    }

    fun getStringWithMappedLinks(@StringRes stringId: Int, linksMap: Map<String, Uri>): CharSequence {

        val rawText = context.getText(stringId) as SpannedString
        val out = SpannableString(rawText)

        for (annotation in rawText.getSpans(0, rawText.length, android.text.Annotation::class.java)) {
            if (annotation.key == "link") {
                linksMap[annotation.value]?.let {
                    out.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View?) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, it))
                        }
                    },
                    rawText.getSpanStart(annotation),
                    rawText.getSpanEnd(annotation),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return out
    }
}
