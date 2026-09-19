package com.example.data

import com.example.model.QuestionAnswer
import com.example.model.StudyItem

object SampleDataProvider {
    fun getInitialStudyItems(): List<StudyItem> {
        return listOf(
            StudyItem(
                id = "phy_light_refraction_2026",
                title = "আলোর প্রতিসরণ ও স্নেলের সূত্র — Suggestion 2026",
                classLevel = "Madhyamik 10",
                subject = "Physical Science",
                chapterName = "আলো (Light)",
                type = "Suggestion 2026",
                marks = 5,
                isExclusive = true,
                isSuggestion2026 = true,
                pyqYear = "",
                question = "আলোর প্রতিসরণ কাকে বলে? স্নেলের সূত্রটি লেখো।",
                answer = "আলোকরশ্মি যখন এক স্বচ্ছ মাধ্যম থেকে অন্য স্বচ্চ মাধ্যমে তির্যকভাবে প্রবেশ করে এবং দিক পরিবর্তন করে, তাকে আলোর প্রতিসরণ বলে। স্নেলের সূত্র: sin i / sin r = ধ্রুবক = μ।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 2,
                        question = "আলোর প্রতিসরণ কাকে বলে?",
                        answer = "আলোকরশ্মি এক স্বচ্ছ মাধ্যম থেকে অন্য স্বচ্ছ মাধ্যমে তির্যকভাবে প্রবেশ করলে তার দিক পরিবর্তনকে আলোর প্রতিসরণ বলে।"
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 3,
                        question = "স্নেলের সূত্রটি বিবৃত করো এবং প্রতিসরাঙ্কের সংজ্ঞা দাও।",
                        answer = "দুটি নির্দিষ্ট মাধ্যমের জন্য পতনকোণের সাইন ও প্রতিসরণকোণের সাইনের অনুপাত ধ্রুবক। এই ধ্রুবককে দ্বিতীয় মাধ্যমের প্রথম মাধ্যমের সাপেক্ষে প্রতিসরাঙ্ক (μ) বলে। μ = sin i / sin r।"
                    ),
                    QuestionAnswer(
                        qNo = "Q3",
                        marks = 5,
                        question = "লেন্সের সূত্রটি লেখো এবং অবতল লেন্সের একটি ব্যবহার উল্লেখ করো।",
                        answer = "লেন্সের সূত্র: 1/f = 1/v − 1/u। অবতল লেন্স স্বল্পদৃষ্টির চশমায় ব্যবহৃত হয়।"
                    )
                ),
                summaryNotes = "• μ = sin i / sin r\n• 1/f = 1/v − 1/u\n• উত্তল লেন্স = অভিসারী, অবতল = অপসারী",
                dateAdded = "12 Sep 2026",
                fileSizeKb = 286,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "life_auxin_hormone_2026",
                title = "অক্সিন হরমোন ও উদ্ভিদ বৃদ্ধি — 2026 Suggestion",
                classLevel = "Madhyamik 10",
                subject = "Life Science",
                chapterName = "জীবনের নিয়ন্ত্রণ ও সমন্বয়",
                type = "Suggestion 2026",
                marks = 4,
                isExclusive = true,
                isSuggestion2026 = true,
                pyqYear = "",
                question = "অক্সিন হরমোন কী? এর দুটি কাজ লেখো।",
                answer = "অক্সিন একটি উদ্ভিদ হরমোন যা অগ্রস্থ মেরিস্টেমে উৎপন্ন হয় এবং কোষের দৈর্ঘ্য বৃদ্ধি ঘটায়।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 2,
                        question = "অক্সিন হরমোন কোথায় উৎপন্ন হয়?",
                        answer = "প্রধানত কাণ্ড ও মূলের অগ্রস্থ মেরিস্টেমে অক্সিন উৎপন্ন হয়।"
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 4,
                        question = "অক্সিনের চারটি গুরুত্বপূর্ণ কাজ লেখো।",
                        answer = "১) অগ্র কুঁড়ির প্রাধান্য রক্ষা ২) কোষের দৈর্ঘ্য বৃদ্ধি ৩) মূলোদ্গম উদ্দীপনা ৪) ফলের বিকাশ সহায়তা।"
                    )
                ),
                summaryNotes = "অক্সিন = IAA। আলোকের বিপরীত দিকে বেশি জমা হয়ে আলোকানুবর্তন ঘটায়।",
                dateAdded = "10 Sep 2026",
                fileSizeKb = 214,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "math_quadratic_notes",
                title = "দ্বিঘাত সমীকরণ — সূত্র ও সমাধান নোট",
                classLevel = "Madhyamik 10",
                subject = "Mathematics",
                chapterName = "দ্বিঘাত সমীকরণ",
                type = "Notes & Chapter",
                marks = 5,
                isExclusive = true,
                isSuggestion2026 = false,
                pyqYear = "",
                question = "দ্বিঘাত সমীকরণ ax² + bx + c = 0 এর সমাধান সূত্রটি লেখো।",
                answer = "x = [-b ± √(b² − 4ac)] / 2a, যেখানে a ≠ 0।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 2,
                        question = "দ্বিঘাত সমীকরণের বিচক্ষক (D) কী?",
                        answer = "D = b² − 4ac। D > 0 হলে দুটি বাস্তব ও অসম মূল, D = 0 হলে সমমূল, D < 0 হলে অবাস্তব মূল।"
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 5,
                        question = "x² − 5x + 6 = 0 সমাধান করো।",
                        answer = "D = 25 − 24 = 1। x = [5 ± 1]/2। অতএব x = 3 অথবা x = 2।"
                    )
                ),
                summaryNotes = "মূলের যোগফল = −b/a, গুণফল = c/a।",
                dateAdded = "08 Sep 2026",
                fileSizeKb = 198,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "hist_1857_pyq_2024",
                title = "মহাবিদ্রোহ ১৮৫৭ — PYQ 2024 Solved",
                classLevel = "Madhyamik 10",
                subject = "History",
                chapterName = "প্রতিরোধ ও বিদ্রোহ",
                type = "PYQ Solved",
                marks = 8,
                isExclusive = true,
                isSuggestion2026 = false,
                pyqYear = "2024",
                question = "১৮৫৭ সালের মহাবিদ্রোহের প্রধান কারণগুলি আলোচনা করো।",
                answer = "রাজনৈতিক, অর্থনৈতিক, সামাজিক ও সামরিক কারণে ১৮৫৭-এর বিদ্রোহ ঘটে।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 2,
                        question = "মহাবিদ্রোহ কবে শুরু হয় এবং কোথায়?",
                        answer = "১৮৫৭ সালের ১০ মে মিরাটে সিপাহি বিদ্রোহের মধ্য দিয়ে শুরু হয়।"
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 8,
                        question = "১৮৫৭ সালের মহাবিদ্রোহের প্রধান কারণ ও ফলাফল লেখো।",
                        answer = "কারণ: চরমপন্থী অধিগ্রহণ নীতি, তালুকদারদের ক্ষোভ, চর্বি মাখা কার্তুজ। ফলাফল: ইস্ট ইন্ডিয়া কোম্পানির শাসনের অবসান এবং ব্রিটিশ ক্রাউনের প্রত্যক্ষ শাসন।"
                    )
                ),
                summaryNotes = "নেতৃত্ব: মঙ্গল পান্ডে, রানি লক্ষ্মীবাঈ, নানা সাহেব, বাহাদুর শাহ জাফর।",
                dateAdded = "02 Mar 2024",
                fileSizeKb = 312,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "geo_atmosphere_pyq_2025",
                title = "বায়ুমণ্ডলের স্তরসমূহ — PYQ 2025",
                classLevel = "Madhyamik 10",
                subject = "Geography",
                chapterName = "বায়ুমণ্ডল",
                type = "PYQ Solved",
                marks = 5,
                isExclusive = true,
                isSuggestion2026 = false,
                pyqYear = "2025",
                question = "বায়ুমণ্ডলের প্রধান স্তরগুলির নাম ও বৈশিষ্ট্য লেখো।",
                answer = "ট্রপোস্ফিয়ার, স্ট্র্যাটোস্ফিয়ার, মেসোস্ফিয়ার, থার্মোস্ফিয়ার।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 3,
                        question = "ট্রপোস্ফিয়ারের দুটি বৈশিষ্ট্য লেখো।",
                        answer = "আবহাওয়ার সব পরিবর্তন এখানে ঘটে এবং উচ্চতা বাড়লে তাপমাত্রা কমে।"
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 5,
                        question = "ওজোন স্তর কোথায় অবস্থিত এবং কেন গুরুত্বপূর্ণ?",
                        answer = "স্ট্র্যাটোস্ফিয়ারে ওজোন স্তর সূর্যের ক্ষতিকর অতিবেগুনি রশ্মি শোষণ করে জীবজগৎকে রক্ষা করে।"
                    )
                ),
                summaryNotes = "ট্রপোস্ফিয়ার ≈ 13 কিমি। ওজোন = O₃।",
                dateAdded = "18 Feb 2025",
                fileSizeKb = 241,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "eng_voice_change_notes",
                title = "Voice Change — Madhyamik Quick Notes",
                classLevel = "Madhyamik 10",
                subject = "English",
                chapterName = "Grammar",
                type = "Notes & Chapter",
                marks = 2,
                isExclusive = true,
                isSuggestion2026 = false,
                pyqYear = "",
                question = "Change the voice: The teacher explained the lesson.",
                answer = "The lesson was explained by the teacher.",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 1,
                        question = "Change the voice: She writes a letter.",
                        answer = "A letter is written by her."
                    ),
                    QuestionAnswer(
                        qNo = "Q2",
                        marks = 2,
                        question = "Change the voice: They have completed the project.",
                        answer = "The project has been completed by them."
                    )
                ),
                summaryNotes = "Object → Subject. Use correct be-verb + past participle. Add 'by' + agent if needed.",
                dateAdded = "05 Sep 2026",
                fileSizeKb = 164,
                author = "Towfik Sir"
            ),
            StudyItem(
                id = "ben_gyanchakshu_2026",
                title = "জ্ঞানচক্ষু — গুরুত্বপূর্ণ প্রশ্নোত্তর",
                classLevel = "Madhyamik 10",
                subject = "Bengali",
                chapterName = "গদ্য",
                type = "Important Q&A",
                marks = 5,
                isExclusive = true,
                isSuggestion2026 = true,
                pyqYear = "",
                question = "জ্ঞানচক্ষু প্রবন্ধের মূলভাব সংক্ষেপে লেখো।",
                answer = "প্রকৃত জ্ঞান কেবল পুঁথিগত বিদ্যা নয়; অন্তর্দৃষ্টি ও মানবিক বোধই প্রকৃত জ্ঞানচক্ষু।",
                qaList = listOf(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 5,
                        question = "জ্ঞানচক্ষু কীভাবে মানুষকে পথ দেখায়? আলোচনা করো।",
                        answer = "জ্ঞানচক্ষু কুসংস্কার দূর করে, সত্যকে চিনতে সাহায্য করে এবং সমাজের কল্যাণে মানুষকে সচেতন করে তোলে।"
                    )
                ),
                summaryNotes = "মূল ভাব: অন্তর্দৃষ্টি + নৈতিক জ্ঞান = প্রকৃত শিক্ষা।",
                dateAdded = "01 Sep 2026",
                fileSizeKb = 188,
                author = "Towfik Sir"
            )
        )
    }
}
