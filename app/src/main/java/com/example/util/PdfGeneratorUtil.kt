package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import java.io.File
import java.io.FileOutputStream

object PdfGeneratorUtil {

    // Standard ISO 216 A4 Dimensions in Points (72 dpi: 595.28 x 841.89)
    const val PAGE_WIDTH = 595
    const val PAGE_HEIGHT = 842
    private const val MARGIN = 36f
    private const val BOTTOM_LIMIT = PAGE_HEIGHT - 54f // Safe cutoff before footer

    fun generatePdfFile(context: Context, item: StudyItem): File? {
        val document = PdfDocument()
        val pdfContext = PdfPageContext(document, item)

        try {
            pdfContext.startPage()
            renderDocumentContent(pdfContext, item)
            pdfContext.finishCurrentPage()

            // Save to cache directory
            val cacheDir = File(context.cacheDir, "shared_pdfs")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val sanitizedName = item.title
                .replace("[^a-zA-Z0-9\\s_]".toRegex(), "")
                .trim()
                .replace("\\s+".toRegex(), "_")
                .take(30)
                .ifBlank { "Towfik_Exclusive_Notes" }

            val file = File(cacheDir, "Towfik_Exclusive_${sanitizedName}_${item.marks}Marks.pdf")
            val outputStream = FileOutputStream(file)
            document.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            document.close()
        }
    }

    private fun renderDocumentContent(pdfContext: PdfPageContext, item: StudyItem) {
        val contentWidth = PAGE_WIDTH - (MARGIN * 2)

        // 1. Metadata Info Card
        pdfContext.ensureSpace(58f)
        val canvas = pdfContext.canvas ?: return
        val currentY = pdfContext.currentY

        val metaRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 48f)
        val metaBg = Paint().apply {
            color = Color.rgb(248, 250, 252)
            style = Paint.Style.FILL
        }
        val metaBorder = Paint().apply {
            color = Color.rgb(226, 232, 240)
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
        }
        canvas.drawRoundRect(metaRect, 8f, 8f, metaBg)
        canvas.drawRoundRect(metaRect, 8f, 8f, metaBorder)

        // Left accent bar
        val accentBar = Paint().apply {
            color = Color.rgb(37, 99, 235)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(MARGIN, currentY, MARGIN + 4f, currentY + 48f), 2f, 2f, accentBar)

        // Class & Subject line
        val classSubPaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 11.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("📚  ${item.classLevel}  •  ${item.subject}", MARGIN + 14f, currentY + 20f, classSubPaint)

        // Chapter & marks line
        val chapterText = if (item.chapterName.isNotBlank()) "অধ্যায়: ${item.chapterName}" else "Exam Prep Set"
        val chapPaint = Paint().apply {
            color = Color.rgb(71, 85, 105)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }
        canvas.drawText(chapterText, MARGIN + 14f, currentY + 38f, chapPaint)

        // Right side badge
        val badgeBg = Paint().apply {
            color = if (item.isSuggestion2026) Color.rgb(254, 243, 199) else Color.rgb(224, 231, 255)
            style = Paint.Style.FILL
        }
        val badgeRect = RectF(PAGE_WIDTH - MARGIN - 130f, currentY + 12f, PAGE_WIDTH - MARGIN - 12f, currentY + 36f)
        canvas.drawRoundRect(badgeRect, 6f, 6f, badgeBg)

        val badgeTextPaint = Paint().apply {
            color = if (item.isSuggestion2026) Color.rgb(180, 83, 9) else Color.rgb(30, 64, 175)
            textSize = 9.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val badgeLabel = if (item.isSuggestion2026) "★ 2026 Suggestion" else "${item.marks} Marks"
        canvas.drawText(badgeLabel, badgeRect.centerX(), currentY + 28f, badgeTextPaint)

        pdfContext.currentY += 58f

        // 2. Main Title Layout
        val textPaintBold = TextPaint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val titleLayout = StaticLayout.Builder.obtain(
            item.title,
            0,
            item.title.length,
            textPaintBold,
            contentWidth.toInt()
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        pdfContext.ensureSpace(titleLayout.height + 16f)
        val canvasTitle = pdfContext.canvas ?: return
        canvasTitle.save()
        canvasTitle.translate(MARGIN, pdfContext.currentY)
        titleLayout.draw(canvasTitle)
        canvasTitle.restore()
        pdfContext.currentY += titleLayout.height + 14f

        // 3. Render Question & Model Answer list
        val questionsToRender = if (item.qaList.isNotEmpty()) {
            item.qaList
        } else if (item.question.isNotBlank() || item.answer.isNotBlank()) {
            listOf(
                QuestionAnswer(
                    qNo = "Q1",
                    marks = item.marks,
                    question = item.question,
                    answer = item.answer
                )
            )
        } else {
            emptyList()
        }

        if (questionsToRender.isNotEmpty()) {
            for ((index, qa) in questionsToRender.withIndex()) {
                val qNumberStr = qa.qNo.ifBlank { "Q${index + 1}" }
                val qLabel = "$qNumberStr. প্রশ্ন (Question) [${qa.marks} Marks]:"
                val aLabel = "$qNumberStr. উত্তর ও পূর্ণাঙ্গ মডেল সমাধান (Model Answer):"

                if (qa.question.isNotBlank()) {
                    drawQuestionBox(
                        pdfContext = pdfContext,
                        label = qLabel,
                        text = qa.question,
                        contentWidth = contentWidth,
                        marks = qa.marks
                    )
                }

                if (qa.answer.isNotBlank()) {
                    drawAnswerBox(
                        pdfContext = pdfContext,
                        label = aLabel,
                        text = qa.answer,
                        contentWidth = contentWidth
                    )
                }
            }
        }

        // 4. Summary Notes & Exam Tips if present
        if (item.summaryNotes.isNotBlank()) {
            drawSummaryNotesBox(
                pdfContext = pdfContext,
                text = item.summaryNotes,
                contentWidth = contentWidth
            )
        }
    }

    private fun drawQuestionBox(
        pdfContext: PdfPageContext,
        label: String,
        text: String,
        contentWidth: Float,
        marks: Int
    ) {
        val qBodyPaint = TextPaint().apply {
            color = Color.rgb(30, 41, 59)
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val qLayout = StaticLayout.Builder.obtain(
            text,
            0,
            text.length,
            qBodyPaint,
            (contentWidth - 28).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        val boxHeight = qLayout.height + 36f

        // Check space before drawing
        pdfContext.ensureSpace(boxHeight + 12f)

        val canvas = pdfContext.canvas ?: return
        val currentY = pdfContext.currentY
        val qBoxRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + boxHeight)

        val qBoxBg = Paint().apply {
            color = Color.rgb(253, 242, 248) // Soft Pastel Pink
            style = Paint.Style.FILL
        }
        val qBoxBorder = Paint().apply {
            color = Color.rgb(244, 114, 182) // Pink Border
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        canvas.drawRoundRect(qBoxRect, 8f, 8f, qBoxBg)
        canvas.drawRoundRect(qBoxRect, 8f, 8f, qBoxBorder)

        // Left Pink Accent Strip
        val qStripPaint = Paint().apply {
            color = Color.rgb(219, 39, 119) // Vivid Rose / Pink
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(MARGIN, currentY, MARGIN + 4f, currentY + boxHeight), 2f, 2f, qStripPaint)

        val qHeaderPaint = Paint().apply {
            color = Color.rgb(157, 23, 77) // Deep Pink Heading
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(label, MARGIN + 14f, currentY + 18f, qHeaderPaint)

        // Question body text
        canvas.save()
        canvas.translate(MARGIN + 14f, currentY + 28f)
        qLayout.draw(canvas)
        canvas.restore()

        pdfContext.currentY += boxHeight + 12f
    }

    private fun drawAnswerBox(
        pdfContext: PdfPageContext,
        label: String,
        text: String,
        contentWidth: Float
    ) {
        val aBodyPaint = TextPaint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val aLayout = StaticLayout.Builder.obtain(
            text,
            0,
            text.length,
            aBodyPaint,
            (contentWidth - 28).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        val boxHeight = aLayout.height + 36f

        pdfContext.ensureSpace(boxHeight + 14f)

        val canvas = pdfContext.canvas ?: return
        val currentY = pdfContext.currentY
        val aBoxRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + boxHeight)

        val aBoxBg = Paint().apply {
            color = Color.rgb(240, 247, 255)
            style = Paint.Style.FILL
        }
        val aBoxBorder = Paint().apply {
            color = Color.rgb(191, 219, 254)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        canvas.drawRoundRect(aBoxRect, 8f, 8f, aBoxBg)
        canvas.drawRoundRect(aBoxRect, 8f, 8f, aBoxBorder)

        // Left accent strip
        val aStripPaint = Paint().apply {
            color = Color.rgb(37, 99, 235)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(MARGIN, currentY, MARGIN + 4f, currentY + boxHeight), 2f, 2f, aStripPaint)

        val aHeaderPaint = Paint().apply {
            color = Color.rgb(29, 78, 216)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(label, MARGIN + 14f, currentY + 18f, aHeaderPaint)

        // Answer body text
        canvas.save()
        canvas.translate(MARGIN + 14f, currentY + 28f)
        aLayout.draw(canvas)
        canvas.restore()

        pdfContext.currentY += boxHeight + 14f
    }

    private fun drawSummaryNotesBox(
        pdfContext: PdfPageContext,
        text: String,
        contentWidth: Float
    ) {
        val notesPaint = TextPaint().apply {
            color = Color.rgb(71, 85, 105)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val notesLayout = StaticLayout.Builder.obtain(
            text,
            0,
            text.length,
            notesPaint,
            (contentWidth - 28).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        val boxHeight = notesLayout.height + 34f

        pdfContext.ensureSpace(boxHeight + 12f)

        val canvas = pdfContext.canvas ?: return
        val currentY = pdfContext.currentY
        val boxRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + boxHeight)

        val bgPaint = Paint().apply {
            color = Color.rgb(254, 252, 232)
            style = Paint.Style.FILL
        }
        val borderPaint = Paint().apply {
            color = Color.rgb(254, 240, 138)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        canvas.drawRoundRect(boxRect, 8f, 8f, bgPaint)
        canvas.drawRoundRect(boxRect, 8f, 8f, borderPaint)

        // Amber left strip
        val stripPaint = Paint().apply {
            color = Color.rgb(217, 119, 6)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(MARGIN, currentY, MARGIN + 4f, currentY + boxHeight), 2f, 2f, stripPaint)

        val headerPaint = Paint().apply {
            color = Color.rgb(180, 83, 9)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("📌 বিশেষ দ্রষ্টব্য ও সূত্রাবলী (Key Exam Points & Tips):", MARGIN + 14f, currentY + 18f, headerPaint)

        canvas.save()
        canvas.translate(MARGIN + 14f, currentY + 26f)
        notesLayout.draw(canvas)
        canvas.restore()

        pdfContext.currentY += boxHeight + 14f
    }

    fun sharePdf(context: Context, item: StudyItem) {
        val pdfFile = generatePdfFile(context, item)
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(context, "Failed to generate PDF document", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "Towfik Exclusive: ${item.title}")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "📚 *${item.title}*\n${item.classLevel} • ${item.subject} • ${item.marks} Marks\n\nGenerated by Towfik Exclusive Educational App\nWeb App & Notes: https://ais-pre-5zfsassdynut36rj53le4n-696515565693.asia-southeast1.run.app"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share Study Note as PDF Document")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error sharing PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private class PdfPageContext(
        val document: PdfDocument,
        val item: StudyItem
    ) {
        var pageNumber = 1
        var currentPage: PdfDocument.Page? = null
        var canvas: Canvas? = null
        var currentY: Float = 0f

        fun startPage() {
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            currentPage = document.startPage(pageInfo)
            val c = currentPage!!.canvas
            canvas = c

            // Page Background
            val bgPaint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            c.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

            // Subtle Background Watermark
            drawWatermark(c)

            if (pageNumber == 1) {
                // Primary Beautiful Header Banner
                val headerRect = RectF(0f, 0f, PAGE_WIDTH.toFloat(), 72f)
                val headerPaint = Paint().apply {
                    color = Color.rgb(30, 58, 138) // Deep Royal Blue
                    style = Paint.Style.FILL
                }
                c.drawRect(headerRect, headerPaint)

                // Gold Accent Line
                val goldLinePaint = Paint().apply {
                    color = Color.rgb(245, 158, 11) // Golden amber
                    style = Paint.Style.FILL
                }
                c.drawRect(0f, 72f, PAGE_WIDTH.toFloat(), 75.5f, goldLinePaint)

                // Sub-pill badge at top
                val topBadgePaint = Paint().apply {
                    color = Color.rgb(245, 158, 11)
                    textSize = 8.5f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                c.drawText("★  MADHYAMIK & HIGHER SECONDARY 2026 BATCH  ★", PAGE_WIDTH / 2f, 20f, topBadgePaint)

                // Main Title
                val titlePaint = Paint().apply {
                    color = Color.WHITE
                    textSize = 15f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                c.drawText("TOWFIK EXCLUSIVE STUDY HUB", PAGE_WIDTH / 2f, 42f, titlePaint)

                // Subtitle
                val subHeaderPaint = Paint().apply {
                    color = Color.rgb(224, 231, 255)
                    textSize = 9f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                c.drawText("Official Question & Model Answer Guidance by Towfik Sir", PAGE_WIDTH / 2f, 58f, subHeaderPaint)

                currentY = 88f
            } else {
                // Subsequent Pages Sleek Branded Header
                val miniHeaderPaint = Paint().apply {
                    color = Color.rgb(30, 58, 138)
                    style = Paint.Style.FILL
                }
                c.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 34f, miniHeaderPaint)

                val miniGoldLine = Paint().apply {
                    color = Color.rgb(245, 158, 11)
                    style = Paint.Style.FILL
                }
                c.drawRect(0f, 34f, PAGE_WIDTH.toFloat(), 36f, miniGoldLine)

                val miniTitlePaint = Paint().apply {
                    color = Color.WHITE
                    textSize = 10f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.LEFT
                    isAntiAlias = true
                }
                val truncatedTitle = if (item.title.length > 38) item.title.take(36) + "..." else item.title
                c.drawText("TOWFIK EXCLUSIVE  •  $truncatedTitle", MARGIN, 21f, miniTitlePaint)

                val pageNumHeaderPaint = Paint().apply {
                    color = Color.rgb(245, 158, 11)
                    textSize = 9.5f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.RIGHT
                    isAntiAlias = true
                }
                c.drawText("Page $pageNumber", PAGE_WIDTH - MARGIN, 21f, pageNumHeaderPaint)

                currentY = 48f
            }
        }

        private fun drawWatermark(c: Canvas) {
            c.save()
            c.rotate(-35f, PAGE_WIDTH / 2f, PAGE_HEIGHT / 2f)
            val wmPaint = Paint().apply {
                color = Color.argb(16, 30, 58, 138) // Very subtle opacity
                textSize = 38f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            c.drawText("TOWFIK EXCLUSIVE", PAGE_WIDTH / 2f, PAGE_HEIGHT / 2f - 40f, wmPaint)
            c.drawText("2026 SUGGESTION SET", PAGE_WIDTH / 2f, PAGE_HEIGHT / 2f + 10f, wmPaint)
            c.restore()
        }

        fun finishCurrentPage() {
            val c = canvas ?: return
            val p = currentPage ?: return

            // Footer Section
            val footerY = PAGE_HEIGHT - 38f

            // Top divider
            val divPaint = Paint().apply {
                color = Color.rgb(226, 232, 240)
                strokeWidth = 1f
            }
            c.drawLine(MARGIN, footerY, PAGE_WIDTH - MARGIN, footerY, divPaint)

            // Prominent "TOWFIK EXCLUSIVE" Bottom Text
            val brandPaint = Paint().apply {
                color = Color.rgb(30, 58, 138)
                textSize = 9.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.LEFT
                isAntiAlias = true
            }
            c.drawText("★ TOWFIK EXCLUSIVE", MARGIN, footerY + 16f, brandPaint)

            val subFooterPaint = Paint().apply {
                color = Color.rgb(100, 116, 139)
                textSize = 8.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textAlign = Paint.Align.LEFT
                isAntiAlias = true
            }
            c.drawText(" • Educational Platform & Madhyamik Guidance", MARGIN + 122f, footerY + 16f, subFooterPaint)

            // Page Indicator on Bottom Right
            val pageNumPaint = Paint().apply {
                color = Color.rgb(71, 85, 105)
                textSize = 9f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
                isAntiAlias = true
            }
            c.drawText("Page $pageNumber", PAGE_WIDTH - MARGIN, footerY + 16f, pageNumPaint)

            document.finishPage(p)
            currentPage = null
            canvas = null
            pageNumber++
        }

        fun ensureSpace(requiredHeight: Float) {
            if (currentY + requiredHeight > BOTTOM_LIMIT) {
                finishCurrentPage()
                startPage()
            }
        }
    }
}
