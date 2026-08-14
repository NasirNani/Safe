package com.example.data.gemini

import com.example.model.AppLanguage

data class LegalDocumentSample(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val category: String,
    val summaryAr: String,
    val summaryEn: String,
    val simulatedTextAr: String,
    val defaultPromptAr: String,
    val defaultPromptEn: String
) {
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getSummary(language: AppLanguage): String = if (language == AppLanguage.ARABIC) summaryAr else summaryEn
    fun getDefaultPrompt(language: AppLanguage): String = if (language == AppLanguage.ARABIC) defaultPromptAr else defaultPromptEn
}

data class LegalVideoSample(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val scenarioType: String,
    val durationText: String,
    val summaryAr: String,
    val summaryEn: String,
    val videoSimulatedDescription: String,
    val defaultPromptAr: String,
    val defaultPromptEn: String
) {
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getSummary(language: AppLanguage): String = if (language == AppLanguage.ARABIC) summaryAr else summaryEn
    fun getDefaultPrompt(language: AppLanguage): String = if (language == AppLanguage.ARABIC) defaultPromptAr else defaultPromptEn
}

object LegalSamples {

    val documentSamples = listOf(
        LegalDocumentSample(
            id = "doc_1_real_estate",
            titleAr = "عقد بيع ابتدائي لوحدة سكنية مع شرط جزائي",
            titleEn = "Apartment Preliminary Sale Contract with Penalty Clause",
            category = "عقود وعقارات",
            summaryAr = "عقد بيع شقة بمدينة الشيخ زايد بقيمة ٣,٥٠٠,٠٠٠ ج.م يشمل جدول أقساط وشرطاً فاسخاً صريحاً وتاريخ استلام محدد.",
            summaryEn = "Sale contract for an apartment in Sheikh Zayed (EGP 3.5M) with installment schedule, explicit rescission and handover date.",
            simulatedTextAr = """
عقد بيع شقة سكنية ابتدائي
إنه في يوم الإثنين الموافق ١٠ يناير ٢٠٢٦، تم الاتفاق بين:
الطرف الأول (البائع): السيد / أحمد محمود فؤاد - بطاقة رقم قومي: ٢٨٥٠٩١٢٠١٠٤٥٣٢
الطرف الثاني (المشتري): السيد / عبد الله حسن الطيبي - بطاقة رقم قومي: ٢٩٠١٢١٥٠١٠٢٢٣٤
البند الأول: باع وأسقط وتنازل الطرف الأول إلى الطرف الثاني الشقة رقم ٤ بالدور الثاني بالعقار رقم ١٢٠ بالحي الثامن بالشيخ زايد بمساحة ١٦٥ م٢.
البند الثاني: تم هذا البيع نظير ثمن إجمالي قدره ٣,٥٠٠,٠٠٠ ج.م (ثلاثة ملايين وخمسمائة ألف جنيه مصري) سدد المشتري منها دفعة مقدمة ٢,٠٠٠,٠٠٠ ج.م والمتبقي على أقساط ربع سنوية.
البند الثالث: يعتبر العقد مفسوخاً من تلقاء نفسه دون حاجة لإنذار أو حكم قضائي في حال تأخر المشتري عن سداد قسطين متتاليين (الشرط الفاسخ الصريح).
البند الرابع: يلتزم البائع بتسليم الوحدة في موعد غايته ٣٠ يونيو ٢٠٢٦ صالحة للسكن ومكتملة المرافق وإلا التزم بغرامة تأخير ٥٠,٠٠٠ ج.م شهرياً.
            """.trimIndent(),
            defaultPromptAr = "افحص هذا العقد وحدد مدى قانونية الشرط الفاسخ الصريح، وحماية المشتري في حال تأخر التسليم، والإجراءات اللازمة لرفع دعوى صحة توقيع أو تسجيل شهر عقاري وفق القانون ٩ لسنة ٢٠٢٢.",
            defaultPromptEn = "Audit this real estate contract, verify the legal validity of the rescission clause, handover penalty, and steps for registration under Law 9/2022."
        ),
        LegalDocumentSample(
            id = "doc_2_poa",
            titleAr = "توكيل رسمي عام في القضايا (شهر عقاري)",
            titleEn = "General Power of Attorney for Litigation (Notary Public)",
            category = "توكيلات وتوثيق",
            summaryAr = "صيغة توكيل رسمي موثق أمام مكتب توثيق الأهرام يبيح المرافعة والصلح والتنازل والإقرار والطعن بالنقض.",
            summaryEn = "Notarized general power of attorney allowing litigation, settlement, waiver, and cassation appeal.",
            simulatedTextAr = """
مصلحة الشهر العقاري والتوثيق - مكتب توثيق الأهرام
محضر توثيق رقم ٤٨١٢ حرف (أ) لسنة ٢٠٢٦
حضر لدينا نحن الموثق بمكتب توثيق الأهرام، السيد / محمد طارق عبد الرحمن، وقرر أنه وكل عنه الأستاذ المستشار / كريم ممدوح المحامي المقيد بالنقض.
في: الحضور نيابة عنه أمام جميع المحاكم على اختلاف درجاتها وتخصصاتها (مدني، جنائي، مجلس دولة، نقض، عمال، أسرة)، وله حق المرافعة والصلح والإبراء والطعن بالتزوير وتوجيه اليمين الحاسمة والرد وقبض المبالغ واستلام الصيغة التنفيذية.
            """.trimIndent(),
            defaultPromptAr = "بيّن ما إذا كان هذا التوكيل يمنح صلاحيات خطيرة كالإقرار والصلح والتنازل والتصالح الجنائي، وما هي الضوابط الواجب اشتراطها لتقييد صلاحيات الوكيل لحماية الموكل.",
            defaultPromptEn = "Explain whether this POA includes high-risk powers (waiver, settlement, receiving funds), and recommend boundary clauses to protect the principal."
        ),
        LegalDocumentSample(
            id = "doc_3_promissory_cheque",
            titleAr = "إيصال أمانة وشيك بنكي مسطر",
            titleEn = "Trust Receipt & Crossed Bank Cheque",
            category = "جنح وأموال",
            summaryAr = "إيصال أمانة محرر بمبلغ ٥٠٠,٠٠٠ ج.م لتسليمه لشخص ثالث، وشيك بنكي مسطر مستحق الدفع.",
            summaryEn = "EGP 500k trust receipt for third-party delivery and crossed check due for payment.",
            simulatedTextAr = """
إيصال أمانة
استلمت أنا / محمود سمير عبد العال - بطاقة رقم قومي: ٢٨٠٠٤١٢٠١٠٩٨٧٦
من السيد / عبد الله الطيبي
مبلغاً وقدره ٥٠٠,٠٠٠ ج.م (فقط خمسمائة ألف جنيه مصري لا غير)
وذلك لتوصيلها وتسليمها للسيد / إبراهيم خليل منصور المقيم بالقاهرة.
وإذا لم أقم بتوصيل هذا المبلغ أكون مبدداً وخائناً للأمانة وأتحمل المسؤولية الجنائية والمدنية طبقاً لنص المادة ٣٤١ من قانون العقوبات.
            """.trimIndent(),
            defaultPromptAr = "حلل شروط صحة إيصال الأمانة، الدفوع الجوهرية (انتفاء ركن التسليم - صورية المعاملة - المعاملة المدنية والتجارية)، وفرص البراءة أو الإدانة وفق قضاء النقض.",
            defaultPromptEn = "Analyze trust receipt elements under Article 341 Penal Code, defenses regarding delivery reality, and Cassation precedent."
        ),
        LegalDocumentSample(
            id = "doc_4_commercial_lease",
            titleAr = "عقد إيجار مقر تجاري خاضع للقانون ٤ لسنة ١٩٩٦",
            titleEn = "Commercial Headquarters Lease Agreement (Law 4/1996)",
            category = "إيجارات وتجاري",
            summaryAr = "عقد إيجار مقر شركة بالتجمع الخامس لمدة ٥ سنوات مع زيادة سنوية ١٠٪ وتأمين قدره ٣ أشهر.",
            summaryEn = "5-year commercial lease for an office in New Cairo with 10% annual escalation and 3 months security deposit.",
            simulatedTextAr = """
عقد إيجار مكتب إداري تجاري
خاضع لأحكام القانون رقم ٤ لسنة ١٩٩٦ والقانون المدني
المؤجر: شركة الإيمان للاستثمار العقاري
المستأجر: شركة الأفق للحلول البرمجية (ش.ذ.م.م)
مدة الإيجار: خمس سنوات تبدأ من ١/٢/٢٠٢٦ وتنتهي في ٣١/١/٢٠٣١ غير قابلة للتجديد إلا بموجب عقد جديد.
القيمة الإيجارية: ٤٥,٠٠٠ ج.م شهرياً مع زيادة سنوية مركبة بنسبة ١٠٪.
التأمين: مبلغ ١٣٥,٠٠٠ ج.م يُرد عند انتهاء العقد وتسليم العين بالحالة التي كانت عليها.
البند المضاف: يتم إثبات تاريخ العقد ووضع الصيغة التنفيذية وفقاً للقانون ١٣٧ لسنة ٢٠٠٦ لتمكين المؤجر من الإخلاء المباشر عند انتهاء المدة.
            """.trimIndent(),
            defaultPromptAr = "ما هي الفوائد القانونية لوضع الصيغة التنفيذية على عقد الإيجار بالشهر العقاري؟ وما هي حقوق المستأجر في استرداد التأمين؟",
            defaultPromptEn = "What are the legal benefits of notarizing the executive formula under Law 137/2006 for this lease agreement?"
        )
    )

    val videoSamples = listOf(
        LegalVideoSample(
            id = "video_1_accident",
            titleAr = "تسجيل كاميرا لوحة القيادة (Dashcam) لحادث تصادم مروري",
            titleEn = "Dashcam Traffic Collision Video Evidence",
            scenarioType = "حوادث ومرور",
            durationText = "0:45",
            summaryAr = "تسجيل عالي الدقة يوضح تجاوز سيارة النقل للإشارة الضوئية الحمراء بسرعة واصطدامها بسيارة الموكل بمحور المشير طنطاوي.",
            summaryEn = "HD dashcam footage showing a truck running a red light at speed and colliding with client's car on El-Mosheer Axis.",
            videoSimulatedDescription = """
تسجيل كاميرا لوحة القيادة (Dashcam) - محور المشير طنطاوي، القاهرة الجديدة:
- التوقيت 00:05: إشارة المرور الخاصة بالسيارة الشاكية خضراء وتتحرك بسرعة قانونية (٥٥ كم/س).
- التوقيت 00:18: سيارة نقل ثقيل (شاحنة نقل رقم ق ص ب ٤٨١) تقتحم التقاطع متجاوزة الإشارة الحمراء الواضحة.
- التوقيت 00:24: وقوع التصادم المباشر في الجانب الأيمن من سيارة الشاكي مما أدى لدورانها وتلفيات جسيمة.
- التوقيت 00:35: نزول سائق الشاحنة وإقراره الشفهي بضعف الفرامل.
            """.trimIndent(),
            defaultPromptAr = "حلل هذا الفيديو كدليل جنائي ومدني: حدد أركان المسؤولية التقصيرية عن حوادث السيارات (خطأ، ضرر، علاقة سببية)، والمطالبة بالتعويض المادي والأدبي وأتعاب إصلاح المركبة من شركة التأمين الإجباري والمسؤول عن الحقوق المدنية.",
            defaultPromptEn = "Analyze this traffic dashcam video: determine tort liability (fault, damage, causation), criminal liability for red-light violation, and claims against insurance companies."
        ),
        LegalVideoSample(
            id = "video_2_cctv_theft",
            titleAr = "تسجيل كاميرات مراقبة (CCTV) لمحل تجاري - نزاع خيانة أمانة / استيلاء",
            titleEn = "Commercial Store CCTV Footage - Breach of Trust / Misappropriation",
            scenarioType = "جنائي وأموال",
            durationText = "1:15",
            summaryAr = "كاميرا مراقبة داخلية توضح قيام أمين الخزينة بوضع مبالغ نقدية من مبيعات اليوم في حقيبته الخاصة دون إدراجها بدفتر اليومية.",
            summaryEn = "Indoor CCTV showing the cashier placing cash sales into his personal bag without recording in the ledger.",
            videoSimulatedDescription = """
تسجيل كاميرا مراقبة داخلية للمتجر (CCTV - متجر بالتجمع الخامس):
- التوقيت 00:10: استلام أمين الخزينة مبالغ نقدية من العملاء بإجمالي ٧٥,٠٠٠ ج.م.
- التوقيت 00:38: إطفاء شاشة الكاشير ووضع رزمة نقدية داخل حقيبة شخصية سوداء.
- التوقيت 00:55: مغادرة المتجر دون إتمام جرد الخزينة اليومي وإثبات عجز وهمي بالدفاتر.
            """.trimIndent(),
            defaultPromptAr = "كيف يُقدم هذا التسجيل كدليل إدانة أمام النيابة العامة؟ ما هي الإجراءات القانونية لطلب تفريغ الكاميرات بمعرفة إدارة المساعدات الفنية وقانونية كاميرات المراقبة كدليل معتمد؟",
            defaultPromptEn = "How is this CCTV presented to the Public Prosecution? What are the technical verification steps under cybercrime laws?"
        ),
        LegalVideoSample(
            id = "video_3_property_handover",
            titleAr = "فيديو معاينة وتوثيق تسليم عقار وعيوب خفية",
            titleEn = "Property Handover Inspection Video & Latent Defects",
            scenarioType = "مدني وعقارات",
            durationText = "1:30",
            summaryAr = "توثيق بالفيديو لحالة فيلا سكنية بعد تسلمها بوجود شروخ إنشائية وهبوط بالأرضيات وتسريبات مياه خفية خلافا للمواصفات.",
            summaryEn = "Video documentation of a newly handed-over villa revealing structural cracks, floor subsidence, and hidden leakages.",
            videoSimulatedDescription = """
تسجيل فيديو توثيقي لمعاينة فيلا بمجمع سكني:
- التوقيت 00:15: استعراض شروخ مائلة في الحوائط الحاملة بالدور الأرضي.
- التوقيت 00:45: وجود رطوبة شديدة وتصدع في سقف الحمام الرئيسي نتيجة تسريب داخلي مدفون.
- التوقيت 01:10: هبوط في بلاط الفناء الخارجي بعمق ٥ سم مما يهدد السلامة الإنشائية.
            """.trimIndent(),
            defaultPromptAr = "حدد المسؤولية العقدية والموجبة لضمان العيوب الخفية والضمان العشري (المادة ٦٥١ مدني) ضد المقاول والمهندس المعماري، وكيفية إثبات الحالة بدعوى مستعجلة.",
            defaultPromptEn = "Determine decennial liability (Article 651 Civil Code) and urgent expert inspection lawsuit procedures for these construction defects."
        ),
        LegalVideoSample(
            id = "video_4_oral_witness",
            titleAr = "تسجيل إقرار وتنازل شفهي أمام شهود (فيديو واقعة)",
            titleEn = "Oral Statement & Settlement Witness Video",
            scenarioType = "إثبات ومعاملات",
            durationText = "0:50",
            summaryAr = "تسجيل فيديو بمجلس عرفي يتضمن إقرار أحد الأطراف باستلام كامل حصته الميراثية وتنازله عن النزاع القائم.",
            summaryEn = "Video recording of a settlement session where a party acknowledges receiving their full inheritance share.",
            videoSimulatedDescription = """
تسجيل فيديو لجلسة صلح عرفي:
- التوقيت 00:05: حضور كبار العائلات والشهود وتلاوة بنود الاتفاق المالي.
- التوقيت 00:25: إقرار صريح من الطرف الشاكي باستلام مبلغ ٢,٠٠٠,٠٠٠ ج.م عداً ونقداً كتعويض نهائي.
- التوقيت 00:40: تصافح الأطراف والتعهد بالتنازل عن الجنحة رقم ٤٣١٢ لسنة ٢٠٢٥ أمام المحكمة.
            """.trimIndent(),
            defaultPromptAr = "ما هي القيمة القانونية للإقرار المصور بالفيديو في القانون المدني وقانون الإثبات؟ وهل يجوز تقديمه لإثبات التنازل والصلح أمام محكمة الجنح؟",
            defaultPromptEn = "What is the evidentiary value of a recorded video confession in Egyptian civil and criminal courts for settlement proof?"
        )
    )
}
