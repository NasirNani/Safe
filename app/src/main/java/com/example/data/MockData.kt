package com.example.data

import com.example.model.*

object MockData {

    val lawyers = listOf(
        Lawyer(
            id = "lawyer_1",
            nameAr = "د. طارق عبد الرحمن المنشاوي",
            nameEn = "Dr. Tarek Abdel Rahman El-Menshawy",
            titleAr = "دكتوراه في القانون المدني - محامٍ بالنقض والدستورية العليا",
            titleEn = "PhD in Civil Law - Supreme Constitutional & Cassation Advocate",
            category = PracticeAreaCategory.CIVIL,
            barLevelAr = "مقيد بالنقض (أكثر من ٢٠ سنة)",
            barLevelEn = "Cassation Bar (20+ Years)",
            experienceYears = 22,
            rating = 4.9,
            reviewsCount = 186,
            consultationFeeEgp = 500,
            governorateAr = "القاهرة - مصر الجديدة",
            governorateEn = "Cairo - Heliopolis",
            officeAddressAr = "١٤ شارع النزهة، ميدان الحجاز، مصر الجديدة",
            officeAddressEn = "14 El Nozha St, Hegaz Sq, Heliopolis",
            bioAr = "خبير متخصص في النزاعات العقارية الكبرى، دعاوى صحة ونفاذ عقود البيع، فسخ الإيجارات، وصياغة العقود المركبة طبقاً لأحكام القانون المدني المصري.",
            bioEn = "Expert specialized in major real estate litigations, contract validation, lease disputes, and complex agreement drafting under Egyptian Civil Code.",
            phone = "+20 102 443 8901",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 310,
            avatarColorHex = 0xFF14294A
        ),
        Lawyer(
            id = "lawyer_2",
            nameAr = "المستشارة نهى سامي الشريف",
            nameEn = "Adv. Noha Samy El-Sherif",
            titleAr = "محامية بالنقض - متخصصة في الشركات والتحكيم التجاري الدولي",
            titleEn = "Cassation Advocate - Corporate Law & Commercial Arbitration",
            category = PracticeAreaCategory.COMMERCIAL,
            barLevelAr = "مقيدة بالنقض ومحكمة التحكيم الدولية",
            barLevelEn = "Cassation Bar & Int. Arbitration Court",
            experienceYears = 16,
            rating = 4.95,
            reviewsCount = 142,
            consultationFeeEgp = 650,
            governorateAr = "الجيزة - الدقي / المهندسين",
            governorateEn = "Giza - Dokki / Mohandessin",
            officeAddressAr = "٢٨ شارع مصدق، برج الأطباء، الدقي",
            officeAddressEn = "28 Mossadak St, Doctors Tower, Dokki",
            bioAr = "مستشارة قانونية لكبرى الشركات والكيانات الاستثمارية بمصر، متخصصة في تأسيس الشركات المساهمة، صياغة اتفاقيات الشركاء، النزاعات التجارية، والتحكيم المؤسسي.",
            bioEn = "Legal consultant for major corporate entities in Egypt, specialized in joint-stock companies, shareholder agreements, commercial disputes, and institutional arbitration.",
            phone = "+20 114 902 3311",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 240,
            avatarColorHex = 0xFF785B0D
        ),
        Lawyer(
            id = "lawyer_3",
            nameAr = "المستشار وائل الشناوي",
            nameEn = "Adv. Wael El-Shennawy",
            titleAr = "محامٍ بالجنايات والنقض الجنائي - قضايا الأموال العامة والشؤون الاقتصادية",
            titleEn = "Criminal & Cassation Defense Counsel - Public Funds & Financial Crimes",
            category = PracticeAreaCategory.CRIMINAL,
            barLevelAr = "محامٍ مقيد بالنقض الجنائي",
            barLevelEn = "Criminal Cassation Advocate",
            experienceYears = 19,
            rating = 4.85,
            reviewsCount = 205,
            consultationFeeEgp = 550,
            governorateAr = "القاهرة - المعادي",
            governorateEn = "Cairo - Maadi",
            officeAddressAr = "٧ شارع النصر، المعادي الجديدة",
            officeAddressEn = "7 El Nasr St, New Maadi",
            bioAr = "ترافع في العديد من القضايا الجنائية الكبرى وقضايا الشيكات، النصب وخيانة الأمانة، والأموال العامة، مع نسبة نقض وإلغاء أحكام استثنائية أمام محكمة النقض.",
            bioEn = "Pioneering criminal advocate who argued landmark cases in public funds, fraud, breach of trust, and criminal cassation appeals.",
            phone = "+20 100 882 1994",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 295,
            avatarColorHex = 0xFF1E3661
        ),
        Lawyer(
            id = "lawyer_4",
            nameAr = "المستشار حسام الدين فهمي",
            nameEn = "Adv. Hossam El-Din Fahmy",
            titleAr = "محامٍ بالاستئناف العالي ومجلس الدولة - القضاء الإداري والتراخيص",
            titleEn = "High Appeal & State Council Advocate - Administrative Justice & Permits",
            category = PracticeAreaCategory.ADMINISTRATIVE,
            barLevelAr = "استئناف عالي ومجلس الدولة (١٤ سنة)",
            barLevelEn = "High Appeal & State Council (14 Years)",
            experienceYears = 14,
            rating = 4.8,
            reviewsCount = 96,
            consultationFeeEgp = 400,
            governorateAr = "الإسكندرية - سموحة",
            governorateEn = "Alexandria - Smouha",
            officeAddressAr = "٥٥ طريق فوزي معاذ، سموحة، الإسكندرية",
            officeAddressEn = "55 Fawzy Moaz Way, Smouha, Alex",
            bioAr = "متخصص في دعاوى إلغاء القرارات الإدارية، تراخيص المنشآت الصناعية والعقارية، المحاكم التأديبية للموظفين، ونزاعات الهيئات الحكومية بمجلس الدولة.",
            bioEn = "Specialized in administrative annulment lawsuits, industrial/building permits, civil service disciplinary actions, and disputes with government authorities.",
            phone = "+20 122 551 0442",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 160,
            avatarColorHex = 0xFF0F1E36
        ),
        Lawyer(
            id = "lawyer_5",
            nameAr = "المستشارة داليا بدر الدين",
            nameEn = "Adv. Dalia Badr El-Din",
            titleAr = "محامية بالنقض - خبيرة قضايا التعويضات الكبرى والتأمين وأخطاء المهن",
            titleEn = "Cassation Advocate - Major Compensation Claims, Insurance & Malpractice",
            category = PracticeAreaCategory.COMPENSATION,
            barLevelAr = "مقيدة بالنقض (١٧ سنة خبرة)",
            barLevelEn = "Cassation Bar (17 Years Exp.)",
            experienceYears = 17,
            rating = 4.92,
            reviewsCount = 168,
            consultationFeeEgp = 450,
            governorateAr = "القاهرة - مدينة نصر",
            governorateEn = "Cairo - Nasr City",
            officeAddressAr = "٢٢ شارع عباس العقاد، المنطقة الأولى، مدينة نصر",
            officeAddressEn = "22 Abbas El Akkad St, Nasr City",
            bioAr = "حققت أعلى مبالغ تعويضات قضائية في حوادث السير، أخطاء العمل الجسيمة، الأخطاء الطبية، ومطالبات شركات التأمين المصرية والدولية.",
            bioEn = "Secured landmark compensation rulings in traffic accidents, severe industrial negligence, medical malpractice, and complex insurance policy claims.",
            phone = "+20 106 339 8812",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 275,
            avatarColorHex = 0xFFA67C1E
        ),
        Lawyer(
            id = "lawyer_6",
            nameAr = "المستشار شريف ممدوح علام",
            nameEn = "Adv. Sherif Mamdouh Allam",
            titleAr = "محامٍ بالاستئناف العالي - عقود استثمارية وملكية فكرية",
            titleEn = "High Appeal Advocate - Investment Contracts & IP Protection",
            category = PracticeAreaCategory.COMMERCIAL,
            barLevelAr = "محامٍ بالاستئناف العالي (١٢ سنة)",
            barLevelEn = "High Appeal Bar (12 Years)",
            experienceYears = 12,
            rating = 4.75,
            reviewsCount = 84,
            consultationFeeEgp = 380,
            governorateAr = "القاهرة - التجمع الخامس",
            governorateEn = "Cairo - 5th Settlement",
            officeAddressAr = "مجمع البنوك، شارع التسعين الجنوبي، التجمع الخامس",
            officeAddressEn = "Banks Complex, South 90th St, New Cairo",
            bioAr = "متخصص في حماية العلامات التجارية، براءات الاختراع، عقود الشركات الناشئة وجولات الاستثمار، والامتياز التجاري (الفرانشايز).",
            bioEn = "Specialized in trademark protection, patents, tech startup funding contracts, and franchise agreements.",
            phone = "+20 111 773 2090",
            isVerified = true,
            isAvailableOnline = true,
            casesWonCount = 130,
            avatarColorHex = 0xFF162B54
        )
    )

    val contractTemplates = listOf(
        ContractTemplate(
            id = "tmpl_1",
            titleAr = "عقد إيجار وحدة سكنية طبقاً للقانون ٤ لسنة ١٩٩٦",
            titleEn = "Residential Lease Agreement (Law 4 of 1996)",
            category = PracticeAreaCategory.CIVIL,
            descriptionAr = "نموذج عقد إيجار شقة سكنية متوازن وموثق بالشروط النموذجية مع حماية المالك والمستأجر والشرط الفاسخ الصريح.",
            descriptionEn = "Balanced residential lease agreement template complying with Egyptian Law No. 4/1996 with explicit termination clause.",
            legalBasisAr = "القانون المدني المصري والقانون رقم ٤ لسنة ١٩٩٦ المعدل بالقانون ١٣٧ لسنة ٢٠٠٦",
            legalBasisEn = "Egyptian Civil Code & Law No. 4/1996 amended by Law 137/2006",
            clausesCount = 16,
            downloadsCount = 1420,
            fileSize = "180 KB",
            clausesSummaryAr = listOf(
                "بيانات المؤجر والمستأجر والعين المؤجرة بالتفصيل",
                "مدة الإيجار وتاريخ البدء والانتهاء والتجديد",
                "القيمة الإيجارية الشهرية وميعاد السداد وغرامة التأخير",
                "مبلغ التأمين النقدي والتزامات الصيانة والمرافق",
                "الشرط الفاسخ الصريح والتنازل عن التنبيه والإنذار"
            ),
            clausesSummaryEn = listOf(
                "Parties & Leased Unit Full Identification",
                "Lease Term, Commencement, Expiration & Renewal",
                "Monthly Rent, Payment Due Date & Late Penalties",
                "Security Deposit, Utilities & Maintenance Obligations",
                "Explicit Dissolution Clause & Notice Waiver"
            ),
            sampleTextAr = "إنه في يوم .......... الموافق .../.../٢٠٢٦ م، تم الاتفاق بين كل من:\nأولاً: السيد / ......................... المقيم في ................... بطاقة رقم ................... (طرف أول - مؤجر)\nثانياً: السيد / ......................... المقيم في ................... بطاقة رقم ................... (طرف ثانٍ - مستأجر)\nبعد أن أقر الطرفان بأهليتهما للتعاقد اتفقا على الآتي:\nالبند الأول: أجر الطرف الأول للطرف الثاني الوحدة السكنية رقم (....) بالعقار الكائن في ..................... بقصد استعمالها سكناً خاصاً.\nالبند الثاني: مدة هذا العقد هي (....) تبدأ من .../.../٢٠٢٦ وتنتهي في .../.../٢٠٢٧ غير قابلة للتجديد إلا بعقد جديد.",
            sampleTextEn = "On this day .../.../2026, by and between:\nFirst: Mr./Mrs. ......................... residing at ................... National ID: ................... (First Party - Lessor)\nSecond: Mr./Mrs. ......................... residing at ................... National ID: ................... (Second Party - Lessee)\nClause 1: The First Party leases to the Second Party Apartment No. (....) located at ..................... for private residential use.\nClause 2: The duration is (....) commencing on .../.../2026 and expiring on .../.../2027, renewable only by explicit mutual agreement."
        ),
        ContractTemplate(
            id = "tmpl_2",
            titleAr = "عقد بيع ابتدائي لقطعة أرض / عقار مع إقرار صحة ونفاذ",
            titleEn = "Preliminary Real Estate / Land Sale Agreement",
            category = PracticeAreaCategory.CIVIL,
            descriptionAr = "صيغة عقد بيع عقاري شاملة لإجراءات التسجيل بالشهر العقاري، تسليم الحيازة، وبراءة الذمة من الحقوق العينية.",
            descriptionEn = "Comprehensive real estate sale draft covering notary registration, title transfer, and vacant possession delivery.",
            legalBasisAr = "المواد ٤١٨ وما بعدها من القانون المدني المصري وقانون الشهر العقاري رقم ٩ لسنة ٢٠٢٢",
            legalBasisEn = "Articles 418 et seq. Egyptian Civil Code & Real Estate Notary Law No. 9/2022",
            clausesCount = 18,
            downloadsCount = 980,
            fileSize = "220 KB",
            clausesSummaryAr = listOf(
                "تحديد المبيع تفصيلاً وحدوده الجغرافية الأربعة",
                "سند ملكية البائع وتسلسل الملكيات السابقة",
                "الثمن الإجمالي وطريقة السداد (نقداً أو دفعات)",
                "التزام البائع بالمثول أمام الشهر العقاري والتوقيع",
                "الشرط الجزائي وضمان عدم التعرض المادي والقانوني"
            ),
            clausesSummaryEn = listOf(
                "Detailed Property Description & 4 Boundaries",
                "Seller's Chain of Title & Prior Ownership Deeds",
                "Total Price & Payment Installment Schedule",
                "Seller's Obligation to Appear at Notary Office",
                "Penalty Clause & Legal Warranty of Non-Interference"
            ),
            sampleTextAr = "البند التمهيدي: يمتلك الطرف الأول العقار الكائن بـ ............. بموجب العقد المسجل رقم ..... لسنة ..... شهر عقاري .....\nالبند الأول: باع وأسقط وتنازل الطرف الأول بكافة الضمانات الفعلية والقانونية إلى الطرف الثاني القابل لذلك ما هو العقار الموضح بالتمهيد.\nالبند الثاني: تم هذا البيع نظير ثمن إجمالي قدره ........... جنيه مصري سدد منه الطرف الثاني ........... عند التوقيع.",
            sampleTextEn = "Preamble: First party owns the property pursuant to registered deed No. .....\nClause 1: The First Party sells and conveys to the Second Party the property described above.\nClause 2: The total agreed purchase price is ........... EGP, paid in full / installments."
        ),
        ContractTemplate(
            id = "tmpl_3",
            titleAr = "عقد تأسيس شركة ذات مسؤولية محدودة (LLC)",
            titleEn = "Articles of Incorporation - Limited Liability Co. (LLC)",
            category = PracticeAreaCategory.COMMERCIAL,
            descriptionAr = "النموذج المعتمد من الهيئة العامة للاستثمار والمناطق الحرة (GAFI) لتأسيس شركة ش.ذ.م.م طبقاً للقانون ١٥٩ لسنة ١٩٨١.",
            descriptionEn = "Standard GAFI-compliant draft for incorporating an Egyptian LLC under Law No. 159 of 1981.",
            legalBasisAr = "قانون شركات الأموال رقم ١٥٩ لسنة ١٩٨١ ولائحته التنفيذية وقانون الاستثمار رقم ٧٢ لسنة ٢٠١٧",
            legalBasisEn = "Companies Law No. 159/1981 and Investment Law No. 72/2017",
            clausesCount = 24,
            downloadsCount = 750,
            fileSize = "310 KB",
            clausesSummaryAr = listOf(
                "اسم الشركة، غرضها الاستثماري، ومقرها الرئيسي بمصر",
                "رأس مال الشركة وحصص الشركاء النقدية والعينية",
                "تعيين المديرين وصلاحيات الإدارة والتمثيل أمام البنوك",
                "سنة الشركة المالية وتوزيع الأرباح والخسائر",
                "أيلولة الحصص وحق الشفعة عند تنازل أي شريك"
            ),
            clausesSummaryEn = listOf(
                "Company Name, Purpose & Registered Egyptian Office",
                "Capital Structure & Partners' Cash/In-Kind Quotas",
                "Appointment of Managers & Bank Signatory Powers",
                "Financial Year & Distribution of Net Profits/Losses",
                "Transfer of Quotas & Partners' Preemption Rights"
            ),
            sampleTextAr = "مادة (١): تأسست بين الموقعين على هذا العقد شركة ذات مسؤولية محدودة مصرية الجنسية طبقاً لأحكام القانون ١٥٩ لسنة ١٩٨١.\nمادة (٢): اسم الشركة: .............................. (شركة ذات مسؤولية محدودة).\nمادة (٣): غرض الشركة: .....................................................",
            sampleTextEn = "Article 1: An Egyptian Limited Liability Company is hereby established pursuant to Law 159 of 1981.\nArticle 2: Company Name: .............................. (L.L.C).\nArticle 3: Company Objectives: ....................................................."
        ),
        ContractTemplate(
            id = "tmpl_4",
            titleAr = "صيغة دعوى تعويض عن حادث سيارة وإصابة خطأ",
            titleEn = "Lawsuit Petition: Compensation for Traffic Injury & Torts",
            category = PracticeAreaCategory.COMPENSATION,
            descriptionAr = "صحيفة دعوى نموذجية لرفع طلب تعويض مادي وأدبي وموروث ضد السائق المسؤول وشركة التأمين الإجباري.",
            descriptionEn = "Standard court petition claiming material, moral, and hereditary damages against at-fault driver and insurance carrier.",
            legalBasisAr = "المادتان ١٦٣ و ١٧٨ من القانون المدني والقانون رقم ٧٢ لسنة ٢٠٠٧ بشأن التأمين الإجباري",
            legalBasisEn = "Articles 163 & 178 Egyptian Civil Code & Mandatory Auto Insurance Law 72/2007",
            clausesCount = 12,
            downloadsCount = 1120,
            fileSize = "195 KB",
            clausesSummaryAr = listOf(
                "وقائع الحادث ورقم المحضر والجنحة الصادر فيها حكم الإدانة",
                "بيان أركان المسؤولية التقصيرية (الخطأ، الضرر، وعلاقة السببية)",
                "تفصيل عناصر الضرر المادي (علاج، فقد كسب) والأدبي (آلام نفسية)",
                "مسؤولية شركة التأمين الشاملة كضامن للوفاء بالتعويض",
                "الطلبات الختامية بالحكم بإلزام المعلن إليهم بالتضامن بمبلغ التعويض"
            ),
            clausesSummaryEn = listOf(
                "Accident Facts, Police Report & Final Criminal Conviction",
                "Elements of Tort Liability (Fault, Damage & Causation)",
                "Itemized Material Damages (Medical, Lost Wages) & Moral Harm",
                "Insurer's Joint & Several Liability under Compulsory Scheme",
                "Concluding Pleas Requesting Full Judgment with Legal Interest"
            ),
            sampleTextAr = "بناءً على طلب السيد / ................ المقيم في .............. ومحله المختار مكتب الأستاذ / ................. المحامي بالنقض.\nأنا ........... محضر محكمة ........... الجزئية انتقلت وأعلنت:\n١- السيد / ................. المقيم في ................. (المتسبب في الحادث)\n٢- السيد / الممثل القانوني لشركة ............. للتأمين بصفته.\nالموضوع: بتاريخ .../.../٢٠٢٥ تسبب المعلن إليه الأول بخطئه في إصابة الطالب...",
            sampleTextEn = "At the request of Mr. ................ residing at .............\nI, the Process Server of the Competent Court, hereby served:\n1. Mr. ................. (Driver at fault)\n2. Legal Representative of ............. Insurance Co.\nSubject: Claim for tort compensation arising out of vehicle accident on .../.../2025..."
        ),
        ContractTemplate(
            id = "tmpl_5",
            titleAr = "إنذار رسمي على يد محضر بسداد القيمة الإيجارية المتأخرة",
            titleEn = "Official Process Server Notice: Rent Arrears Demand",
            category = PracticeAreaCategory.CIVIL,
            descriptionAr = "صيغة إنذار تكليف بالوفاء كإجراء وجوبي سابق لرفع دعوى الإخلاء أو استصدار أمر الأداء.",
            descriptionEn = "Formal legal notice served via court bailiff to formally demand overdue rent before eviction proceedings.",
            legalBasisAr = "المادة ٥٦٣ من القانون المدني والمادة ١٨ من القانون ١٣٦ لسنة ١٩٨١",
            legalBasisEn = "Article 563 Egyptian Civil Code & Article 18 Law 136/1981",
            clausesCount = 8,
            downloadsCount = 890,
            fileSize = "140 KB",
            clausesSummaryAr = listOf(
                "بيانات عقد الإيجار ومقدار الأجرة الشهرية وتاريخ الاستحقاق",
                "حصر الشهور المتأخر فيها المستأجر وإجمالي المبلغ المطلوب",
                "منح مهلة قانونية (١٥ يوماً) للوفاء الفوري",
                "التنبيه باتخاذ الإجراءات القضائية وطلب الإخلاء والتعويض"
            ),
            clausesSummaryEn = listOf(
                "Lease Deed Identification & Monthly Due Amount",
                "Exact Unpaid Months & Cumulative Overdue Balance",
                "15-Day Statutory Cure Period for Immediate Settlement",
                "Formal Caution of Eviction Lawsuit & Damages on Default"
            ),
            sampleTextAr = "إنه في يوم ......... الموافق .../.../٢٠٢٦ م\nبناءً على طلب السيد / ...................\nأنا ........... محضر محكمة ........... قد أنذرت السيد / ...................\nبأن يدفع للطالب مبلغ وقدره (......... ج.م) قيمة إيجار الشقة عن المدة من .../.../٢٠٢٥ حتى .../.../٢٠٢٦ خلال ١٥ يوماً من تاريخ هذا الإنذار.",
            sampleTextEn = "On this day .../.../2026,\nAt the request of Mr. ...................\nI, Court Bailiff, served Mr. ...................\nDemanding payment of ........... EGP representing accrued rent for the period from .../.../2025 to .../.../2026 within 15 days hereof."
        )
    )

    val courtRulings = listOf(
        CourtRuling(
            id = "ruling_1",
            appealNumber = "١٢٨٤٥",
            judicialYear = "٩٢ ق",
            sessionDate = "جلسة ١٥ مارس ٢٠٢٣",
            chamberAr = "الدائرة المدنية - محكمة النقض",
            chamberEn = "Civil Chamber - Court of Cassation",
            category = PracticeAreaCategory.COMPENSATION,
            principleAr = "تقدير التعويض عن الضررين المادي والأدبي وسلطة محكمة الموضوع في جبر الضرر كاملاً",
            principleEn = "Assessment of Material & Moral Damages & Trial Court's Mandate to Award Full Restitution",
            fullRulingSummaryAr = "المقرر في قضاء محكمة النقض أن تقدير التعويض الجابر للضرر هو من إطلاقات محكمة الموضوع ما دامت قد بينت العناصر المكونة له من ضرر مادي كفوات كسب أو مصاريف علاج وضرر أدبي كالآلام النفسية، ولا يُلزم القانون القاضي باتباع معايير حسابية جامدة، بل يكفي أن يكون التعويض مكافئاً للضرر المباشر المحقق وغير مبالغ فيه بما يجاوز العدالة.",
            fullRulingSummaryEn = "It is well established in Court of Cassation jurisprudence that assessing compensation for tortious damage falls within the trial court's discretion, provided it specifies the component elements of both material damage (lost profits, medical expenditures) and moral harm (mental anguish). The law does not mandate rigid mathematical formulas, so long as the award provides equitable and full restitution.",
            keywordsAr = listOf("تعويض", "مسؤولية تقصيرية", "ضرر أدبي", "فوات كسب", "سلطة تقديرية"),
            keywordsEn = listOf("Compensation", "Tort Liability", "Moral Harm", "Lost Profits", "Judicial Discretion")
        ),
        CourtRuling(
            id = "ruling_2",
            appealNumber = "٣٤١٢",
            judicialYear = "٩٠ ق",
            sessionDate = "جلسة ٨ يناير ٢٠٢٢",
            chamberAr = "الدائرة الجنائية - محكمة النقض",
            chamberEn = "Criminal Chamber - Court of Cassation",
            category = PracticeAreaCategory.CRIMINAL,
            principleAr = "أركان جريمة خيانة الأمانة ووجوب تسليم المال على سبيل الأمانة بموجب أحد عقود الائتمان الخمسة",
            principleEn = "Elements of Breach of Trust: Mandatory Delivery under Statutory Fiduciary Contracts",
            fullRulingSummaryAr = "لا تقوم جريمة خيانة الأمانة المعاقب عليها بالمادة ٣٤١ من قانون العقوبات إلا إذا كان المال قد سُلم إلى المتهم بموجب أحد العقود الخمسة الواردة حصراً في النص (الوديعة، الإجارة، عارية الاستعمال، الرهن، الوكالة)، وإذا انتفى سبب التسليم أو ثبت أن المعاملة كانت بيعاً أو دينًا مدنيًا انحسرت الحماية الجنائية ووجب القضاء بالبراءة.",
            fullRulingSummaryEn = "The offense of breach of trust under Article 341 of the Penal Code is not established unless the funds were delivered to the accused under one of the five exclusive statutory fiduciary contracts (deposit, lease, loan for use, pledge, or agency). If the transaction is purely a civil debt or purchase, criminal liability is excluded.",
            keywordsAr = listOf("خيانة أمانة", "عقود الائتمان", "المادة ٣٤١ عقوبات", "تسليم المال", "براءة"),
            keywordsEn = listOf("Breach of Trust", "Fiduciary Contracts", "Article 341", "Delivery of Goods", "Acquittal")
        ),
        CourtRuling(
            id = "ruling_3",
            appealNumber = "٨٩٢١",
            judicialYear = "٨٩ ق",
            sessionDate = "جلسة ٢٢ نوفمبر ٢٠٢١",
            chamberAr = "الدائرة التجارية - محكمة النقض",
            chamberEn = "Commercial Chamber - Court of Cassation",
            category = PracticeAreaCategory.COMMERCIAL,
            principleAr = "حجية الشيك البنكي ومبدأ استقلال التوقيعات وتجريد الالتزام الصرفي عن العلاقة الأصلية",
            principleEn = "Irrebuttable Negotiability of Bank Checks & Autonomy of Commercial Commitments",
            fullRulingSummaryAr = "الشيك أداة وفاء تجري مجرى النقود في المعاملات، والأصل في الالتزام الصرفي الناشئ عن توقيع الشيك أنه التزام مجرد ومستقل عن العلاقة القانونية الأصلية التي كانت سبباً في تحريره، ولا يجوز للساحب أن يحتج في مواجهة الحامل حسن النية بالدفوع المستمدة من عقد المعاملة الأصلية.",
            fullRulingSummaryEn = "A check serves as a cash equivalent in commercial dealings. The bill liability arising from signing a check is autonomous and detached from the underlying transaction, and the drawer cannot invoke contract defects against a bona fide holder.",
            keywordsAr = listOf("شيك بنكي", "التزام صرفي", "قانون التجارة", "حامل حسن النية", "أوراق تجارية"),
            keywordsEn = listOf("Bank Check", "Commercial Paper", "Trade Code", "Bona Fide Holder", "Negotiable Instruments")
        ),
        CourtRuling(
            id = "ruling_4",
            appealNumber = "٤٥٠١",
            judicialYear = "٩١ ق",
            sessionDate = "جلسة ٤ مايو ٢٠٢٣",
            chamberAr = "دائرة مجلس الدولة والطعون الإدارية",
            chamberEn = "State Council & Administrative Appeals Chamber",
            category = PracticeAreaCategory.ADMINISTRATIVE,
            principleAr = "القرار الإداري السلبي بالامتناع عن إصدار الترخيص المستوفي للشروط والتعويض عن تعطيل المشروع",
            principleEn = "Negative Administrative Decision (Refusal of Compliant Permit) & Loss of Business Damages",
            fullRulingSummaryAr = "امتناع جهة الإدارة عن إصدار ترخيص البناء أو النشاط التجاري رغم استيفاء طالب الترخيص لكافة الشروط والمستندات المنصوص عليها قانوناً يُعد قراراً إدارياً سلبياً مخالفاً للقانون، ويوجب إلغاءه مع إلزام الإدارة بتعويض المستثمر عما لحقه من خسارة مادية وما فاته من كسب محقق.",
            fullRulingSummaryEn = "Administrative authority's refusal or withholding of a permit despite full statutory compliance constitutes an unlawful negative administrative decision, warranting annulment and state liability for investor's incurred loss and business delay.",
            keywordsAr = listOf("مجلس الدولة", "قرار إداري سلبي", "تراخيص بناء", "إلغاء وتعويض", "إساءة استعمال السلطة"),
            keywordsEn = listOf("State Council", "Negative Administrative Decision", "Building Permit", "Annulment", "Abuse of Power")
        ),
        CourtRuling(
            id = "ruling_5",
            appealNumber = "٧١٢٠",
            judicialYear = "٩٣ ق",
            sessionDate = "جلسة ١٠ أكتوبر ٢٠٢٤",
            chamberAr = "دائرة الإيجارات والطعون المدنية",
            chamberEn = "Tenancy & Civil Appeals Chamber",
            category = PracticeAreaCategory.CIVIL,
            principleAr = "إعمال الشرط الفاسخ الصريح في عقد الإيجار عند التأخر في سداد الأجرة وسقوط سلطة القاضي في التمهيل",
            principleEn = "Immediate Enforcement of Explicit Lease Cancellation Clause for Non-Payment without Judicial Grace",
            fullRulingSummaryAr = "إذا اتفق طرفا عقد الإيجار على اعتباره مفسوخاً من تلقاء نفسه دون حاجة لتنبيه أو إنذار أو حكم قضائي في حال تأخر المستأجر عن سداد الأجرة في ميعادها، فإن المحكمة تلتزم بإعمال هذا الشرط ولا تملك منح المستأجر مهلة للسداد متى تحقق التخلف غير المبرر.",
            fullRulingSummaryEn = "Where contracting parties agree that a lease terminates ipso jure without prior notice or court order upon rent default, the court must give effect to this explicit clause and lacks legal power to grant grace periods once default is proven.",
            keywordsAr = listOf("عقد إيجار", "شرط فاسخ صريح", "سداد الأجرة", "إخلاء العين", "القانون المدني"),
            keywordsEn = listOf("Lease Deed", "Explicit Dissolution", "Rent Default", "Eviction", "Civil Law")
        )
    )

    val sampleFaqInquiries = listOf(
        "ما هي شروط وإجراءات رفع دعوى تعويض عن حادث سير في مصر؟",
        "كيف أتحقق من قيد المحامي في جدول نقابة المحامين المصرية؟",
        "ما هي الرسوم والخطوات المطلوبة لتسجيل شقة بالشهر العقاري؟",
        "هل الشيك البنكي يسقط بالتقادم؟ وما مدة تقديم الشكوى الجنائية؟",
        "كيف أطعن على قرار إداري صادر من جهة حكومية أمام مجلس الدولة؟"
    )

    val sampleFaqAnswers = mapOf(
        "ما هي شروط وإجراءات رفع دعوى تعويض عن حادث سير في مصر؟" to 
            "لرفع دعوى تعويض عن حادث سير يلزم:\n١- استخراج صورة رسمية من محضر الشرطة وتقرير المعاينة الطبية.\n٢- استخراج شهادة بنهائية الحكم الجنائي الصادر بإدانة السائق المتسبب (جنحة الإصابة الخطأ).\n٣- توجيه إنذار رسمي أو اختصام شركة التأمين الإجباري المسؤولة عن السيارة.\n٤- إيداع صحيفة الدعوى أمام المحكمة المدنية المختصة للمطالبة بالتعويض المادي والأدبي والموروث.",
        "كيف أتحقق من قيد المحامي في جدول نقابة المحامين المصرية؟" to 
            "جميع المحامين في تطبيق 'مظلة' موثقون رسمياً بكارنيه النقابة ورقم القيد. يمكنك التحقق عبر موقع نقابة المحامين الإلكتروني أو الاطلاع على بطاقة العضوية ودرجة القيد (ابتدائي / استئناف / نقض) المعروضة في ملف كل محامٍ على التطبيق.",
        "ما هي الرسوم والخطوات المطلوبة لتسجيل شقة بالشهر العقاري؟" to 
            "وفقاً للقانون رقم ٩ لسنة ٢٠٢٢، تم تبسيط إجراءات الشهر العقاري:\n١- تقديم طلب الشهر إلكترونياً أو بمكتب التوثيق مع إرفاق عقد البيع وسند الملكية السابق.\n٢- رفع الإحداثيات المساحية عبر جهة معتمدة (بيان الرفع المساحي الرقمي).\n٣- سداد رسوم التوثيق بحد أقصى ٣٩٠٠ جنيه مصري مهما بلغت قيمة العقار.\n٤- يتم البت في الطلب وإصدار المحرر المشهر خلال مدة أقصاها ٣٠ يوماً.",
        "هل الشيك البنكي يسقط بالتقادم؟ وما مدة تقديم الشكوى الجنائية؟" to 
            "طبقاً لقانون التجارة رقم ١٧ لسنة ١٩٩٩:\n- دعوى الشيك الجنائية (جنحة إصدار شيك بدون رصيد) تسقط بمضي ٣ سنوات من تاريخ تقديم الشيك للبنك ورفضه.\n- الالتزام الصرفي المدني للشيك يتقادم بمضي سنة واحدة من تاريخ انقضاء ميعاد تقديمه للوفاء.\n- يُنصح دائماً بالرجوع على الساحب جنائياً ومدنياً في أقرب وقت للحفاظ على كافة الحقوق.",
        "كيف أطعن على قرار إداري صادر من جهة حكومية أمام مجلس الدولة؟" to 
            "إجراءات الطعن أمام محكمة القضاء الإداري بمجلس الدولة:\n١- التظلم من القرار أمام الجهة الإدارية المصدرة له خلال ٦٠ يوماً من تاريخ العلم به.\n٢- في حال رفض التظلم أو انقضاء ٦٠ يوماً دون رد، يتم رفع دعوى الإلغاء أمام مجلس الدولة.\n٣- إيداع صحيفة الدعوى وسداد الرسوم وإخطار هيئة مفوضي الدولة لإعداد التقرير القانوني."
    )

    val initialChatMessages = listOf(
        ChatMessage(
            id = "msg_1",
            sender = ChatSender.SYSTEM,
            senderNameAr = "نظام مظلة القانوني",
            senderNameEn = "UMBRELLA Legal System",
            messageAr = "مرحباً بك في مركز الاستشارات والدعم القانوني لمنصة 'مظلة'. مستشارونا المعتمدون جاهزون لمساعدتك وتقديم الرأي القانوني الموثوق طبقاً لأحكام القانون المصري.",
            messageEn = "Welcome to UMBRELLA Legal Inquiries & Support Desk. Our certified legal counsel is on standby to assist you under Egyptian law.",
            timestamp = "10:00 ص",
            isRead = true
        ),
        ChatMessage(
            id = "msg_2",
            sender = ChatSender.ADVISOR,
            senderNameAr = "المستشار / كريم ممدوح (مستشار الدعم)",
            senderNameEn = "Adv. Karim Mamdouh (Legal Support)",
            messageAr = "أهلاً بك يا أستاذ عبد الله! كيف يمكننا مساعدتك قانونياً اليوم؟ يمكنك طرح سؤالك مباشرة، أو اختيار أحد الإجراءات السريعة بالأسفل.",
            messageEn = "Hello Mr. Abdullah! How can we assist you legally today? You can type your inquiry directly or pick one of the quick legal topics below.",
            timestamp = "10:02 ص",
            isRead = true
        )
    )

    val lawyerReviewsMap = mapOf(
        "lawyer_1" to listOf(
            LawyerReview(
                id = "rev_1_1",
                reviewerNameAr = "م. حازم السعدني",
                reviewerNameEn = "Eng. Hazem El-Saadani",
                rating = 5.0,
                dateAr = "منذ ٤ أيام",
                dateEn = "4 days ago",
                caseCategoryAr = "نزاع عقاري وصحة تعاقد",
                caseCategoryEn = "Real Estate Dispute & Title",
                commentAr = "د. طارق قامة قانونية متميزة جداً. تم كسب دعوى صحة ونفاذ لعقار بالتجمع الخامس واسترداد حقوقنا كاملة في زمن قياسي مع صياغة مذكرات نقض محكمة ومبهرة.",
                commentEn = "Dr. Tarek is an outstanding legal mind. We successfully won a complex real estate validity lawsuit in record time with impeccably researched cassation briefs."
            ),
            LawyerReview(
                id = "rev_1_2",
                reviewerNameAr = "أ. منى الدمرداش",
                reviewerNameEn = "Mrs. Mona El-Demerdash",
                rating = 5.0,
                dateAr = "منذ أسبوعين",
                dateEn = "2 weeks ago",
                caseCategoryAr = "فسخ عقد إيجار وإخلاء",
                caseCategoryEn = "Lease Termination & Eviction",
                commentAr = "استشارة وافية ودقيقة جداً، وضّح لي الإجراءات القانونية وثغرات العقد وتم إنهاء النزاع ودياً بناءً على توجيهاته دون الحاجة للتقاضي الطويل.",
                commentEn = "Comprehensive and precise consultation. He pinpointed exact legal gaps in the lease agreement and settled the dispute amicably without lengthy litigation."
            ),
            LawyerReview(
                id = "rev_1_3",
                reviewerNameAr = "د. أشرف قنديل",
                reviewerNameEn = "Dr. Ashraf Qandil",
                rating = 4.8,
                dateAr = "منذ شهر",
                dateEn = "1 month ago",
                caseCategoryAr = "صياغة عقود شراكة وتطوير",
                caseCategoryEn = "Partnership & Development Contracts",
                commentAr = "أمانة واحترافية فائقة في مراجعة بنود الشراكة وحماية حقوق الملاك. المذكرات القانونية التي أعدها كانت مرجعاً متكاملاً.",
                commentEn = "Superb professionalism in drafting partnership clauses and safeguarding owner equity. His briefs were utterly thorough."
            )
        ),
        "lawyer_2" to listOf(
            LawyerReview(
                id = "rev_2_1",
                reviewerNameAr = "أحمد رضوان (رئيس مجلس إدارة)",
                reviewerNameEn = "Ahmed Radwan (CEO)",
                rating = 5.0,
                dateAr = "منذ يومين",
                dateEn = "2 days ago",
                caseCategoryAr = "تأسيس شركة مساهمة وتمويل",
                caseCategoryEn = "Joint Stock Incorporation & Funding",
                commentAr = "المستشارة نهى الشريف من أفضل خبراء قانون الشركات في مصر. أنهت إجراءات هيئة الاستثمار GAFI ووثقت اتفاقية المساهمين وحصص المستثمرين بمنتهى الحرفية.",
                commentEn = "Adv. Noha is truly one of the sharpest corporate law minds in Egypt. Executed GAFI incorporation and investor agreements with supreme elegance."
            ),
            LawyerReview(
                id = "rev_2_2",
                reviewerNameAr = "مروان شكري",
                reviewerNameEn = "Marwan Shoukry",
                rating = 4.9,
                dateAr = "منذ ٣ أسابيع",
                dateEn = "3 weeks ago",
                caseCategoryAr = "تحكيم تجاري دولي",
                caseCategoryEn = "International Arbitration",
                commentAr = "تمثيل قانوني رفيع المستوى في فض نزاع توريد تجاري أمام مركز القاهرة الإقليمي للتحكيم التجاري الدولي (CRCICA).",
                commentEn = "First-rate representation in settling a high-stakes commercial supply dispute at CRCICA."
            )
        ),
        "lawyer_3" to listOf(
            LawyerReview(
                id = "rev_3_1",
                reviewerNameAr = "طارق مهران",
                reviewerNameEn = "Tarek Mehran",
                rating = 5.0,
                dateAr = "منذ ٥ أيام",
                dateEn = "5 days ago",
                caseCategoryAr = "قضية أموال عامة وشيكات",
                caseCategoryEn = "Public Funds & Commercial Check",
                commentAr = "مرافعة تاريخية أمام محكمة الجنايات وحكم بالبراءة المستحقة بفضل الدفع بانتفاء أركان الجريمة وتسليم المال على سبيل الائتمان.",
                commentEn = "Historic courtroom defense leading to an emphatic acquittal by proving failure of fiduciary delivery elements."
            ),
            LawyerReview(
                id = "rev_3_2",
                reviewerNameAr = "عمر البحيري",
                reviewerNameEn = "Omar El-Beheiry",
                rating = 4.8,
                dateAr = "منذ شهر",
                dateEn = "1 month ago",
                caseCategoryAr = "نقض حكم جنائي",
                caseCategoryEn = "Criminal Cassation Appeal",
                commentAr = "تم قبول الطعن بالنقض وإعادة المحاكمة بعد مذكرات دفاع قانونية محكمة تكشف القصور في التسبيب والفساد في الاستدلال.",
                commentEn = "Cassation appeal successfully admitted and retrial ordered based on flawless reasoning deficiency arguments."
            )
        )
    )

    fun getReviewsForLawyer(lawyerId: String): List<LawyerReview> {
        return lawyerReviewsMap[lawyerId] ?: listOf(
            LawyerReview(
                id = "rev_def_1",
                reviewerNameAr = "أ. محمود عبد العال",
                reviewerNameEn = "Mahmoud Abdel Aal",
                rating = 5.0,
                dateAr = "منذ أسبوع",
                dateEn = "1 week ago",
                caseCategoryAr = "استشارة قانونية متخصصة",
                caseCategoryEn = "Specialized Legal Consultation",
                commentAr = "مستشار ممتاز ذو خبرة قانونية عميقة، التزام تام بالمواعيد وإيضاح كامل للحقوق والواجبات القضائية.",
                commentEn = "Exceptional counsel with deep statutory knowledge, punctuality, and clear explanation of all legal merits."
            ),
            LawyerReview(
                id = "rev_def_2",
                reviewerNameAr = "م. سارة المهدي",
                reviewerNameEn = "Eng. Sara El-Mahdy",
                rating = 4.9,
                dateAr = "منذ ٣ أسابيع",
                dateEn = "3 weeks ago",
                caseCategoryAr = "مراجعة عقود واتفاقيات",
                caseCategoryEn = "Contracts & Agreements Review",
                commentAr = "صياغة دقيقة تحمي الطرف المتعاقد من أي التزامات مجهولة أو شروط مجحفة. أنصح بالتعامل معه بشدة.",
                commentEn = "Rigorous drafting that protected our business from ambiguous liabilities. Highly recommended."
            )
        )
    }

    val lawyerCredentialsMap = mapOf(
        "lawyer_1" to listOf(
            LawyerEducationCredential("دكتوراه في القانون المدني والمسؤولية العقدية", "PhD in Civil Law & Contractual Liability", "جامعة القاهرة - كلية الحقوق", "Cairo University - Faculty of Law", "2008"),
            LawyerEducationCredential("ماجستير في القانون الخاص المقارن", "LL.M in Comparative Private Law", "جامعة عين شمس", "Ain Shams University", "2004"),
            LawyerEducationCredential("قيد جدول النقض والمحكمة الدستورية العليا", "Admitted to Cassation & Supreme Constitutional Bar", "نقابة المحامين المصرية", "Egyptian Bar Association", "2010")
        ),
        "lawyer_2" to listOf(
            LawyerEducationCredential("ماجستير في قانون التجارة والتحكيم الدولي", "LL.M in Commercial Law & Int. Arbitration", "جامعة السوربون - باريس ١", "Paris 1 Panthéon-Sorbonne", "2011"),
            LawyerEducationCredential("ليسانس الحقوق بمرتبة الشرف (القسم الفرنسي)", "LL.B with Honors (French Section)", "حقوق القاهرة", "Cairo University Law", "2007"),
            LawyerEducationCredential("عضوية مركز القاهرة الإقليمي للتحكيم الدولي CRCICA", "Fellow of CRCICA International Arbitration", "مركز التحكيم الدولي", "CRCICA Cairo", "2015")
        )
    )

    fun getCredentialsForLawyer(lawyerId: String): List<LawyerEducationCredential> {
        return lawyerCredentialsMap[lawyerId] ?: listOf(
            LawyerEducationCredential("ليسانس الحقوق ودبلوم الدراسات القضائية", "LL.B & Judicial Studies Diploma", "كلية الحقوق - جامعة القاهرة", "Faculty of Law - Cairo University", "2006"),
            LawyerEducationCredential("عضوية الجمعية المصرية للقانون الدولي", "Member of Egyptian Society of International Law", "نقابة المحامين العامة", "General Bar Syndicate", "2012")
        )
    }

    val userInquiriesHistory = listOf(
        UserInquiryHistoryItem(
            id = "inq_101",
            lawyerId = "lawyer_1",
            lawyerNameAr = "د. طارق عبد الرحمن المنشاوي",
            lawyerNameEn = "Dr. Tarek Abdel Rahman El-Menshawy",
            lawyerSpecialtyAr = "قانون مدني وعقود عقارية",
            lawyerSpecialtyEn = "Civil Law & Real Estate",
            topicAr = "مراجعة عقد شراء شقة سكنية بالتجمع الخامس والتأكد من صحة التوكيل والملكية",
            topicEn = "Review residential apartment purchase contract in 5th Settlement & Title deed",
            dateAr = "١٢ أغسطس ٢٠٢٦",
            dateEn = "Aug 12, 2026",
            statusAr = "مكتملة وموثقة",
            statusEn = "Resolved & Documented",
            statusColorHex = 0xFF10B981,
            messageCount = 8,
            lastMessagePreviewAr = "تمت مراجعة بنود العقد وإضافة الشرط الجزائي وبراءة الذمة المالية بنجاح.",
            lastMessagePreviewEn = "Contract clauses audited with penalty clause and full discharge clause added."
        ),
        UserInquiryHistoryItem(
            id = "inq_102",
            lawyerId = "lawyer_2",
            lawyerNameAr = "المستشارة نهى سامي الشريف",
            lawyerNameEn = "Adv. Noha Samy El-Sherif",
            lawyerSpecialtyAr = "شركات واستثمار (LLC)",
            lawyerSpecialtyEn = "Corporate & Investment (LLC)",
            topicAr = "صياغة اتفاقية توزيع أرباح وتخارج شريك في شركة برمجيات ناشئة",
            topicEn = "Draft profit distribution and founder exit agreement for software startup",
            dateAr = "٨ أغسطس ٢٠٢٦",
            dateEn = "Aug 8, 2026",
            statusAr = "قيد المتابعة",
            statusEn = "In Progress",
            statusColorHex = 0xFF0284C7,
            messageCount = 14,
            lastMessagePreviewAr = "أرجو إرسال السجل التجاري المعدل لاعتماد مسودة المحضر النهائي.",
            lastMessagePreviewEn = "Please forward the amended commercial register to finalize the draft."
        ),
        UserInquiryHistoryItem(
            id = "inq_103",
            lawyerId = "lawyer_5",
            lawyerNameAr = "المستشارة داليا بدر الدين",
            lawyerNameEn = "Adv. Dalia Badr El-Din",
            lawyerSpecialtyAr = "تعويضات وتأمين حوادث",
            lawyerSpecialtyEn = "Compensation & Insurance",
            topicAr = "مطالبة شركة التأمين الإجباري بتعويض عن تلفيات تصادم سيارة على الطريق الدائري",
            topicEn = "Claim against compulsory insurance for vehicle collision damages on Ring Road",
            dateAr = "٢٨ يوليو ٢٠٢٦",
            dateEn = "Jul 28, 2026",
            statusAr = "بانتظار تقرير الخبير",
            statusEn = "Awaiting Expert Report",
            statusColorHex = 0xFFF59E0B,
            messageCount = 6,
            lastMessagePreviewAr = "تم إيداع صحيفة الدعوى وقيدها برقم جلسة أول سبتمبر القادم.",
            lastMessagePreviewEn = "Lawsuit petition filed and docketed for early September hearing."
        )
    )

    val userDownloadedContracts = listOf(
        UserDownloadedContractItem(
            id = "dl_1",
            templateId = "tmpl_1",
            titleAr = "عقد إيجار وحدة سكنية طبقاً للقانون ٤ لسنة ١٩٩٦",
            titleEn = "Residential Lease Agreement (Law 4 of 1996)",
            downloadDateAr = "١٠ أغسطس ٢٠٢٦",
            downloadDateEn = "Aug 10, 2026",
            fileSize = "180 KB",
            fileFormat = "DOCX & PDF",
            category = PracticeAreaCategory.CIVIL,
            clausesCount = 16
        ),
        UserDownloadedContractItem(
            id = "dl_2",
            templateId = "tmpl_3",
            titleAr = "عقد تأسيس شركة ذات مسؤولية محدودة (LLC)",
            titleEn = "Articles of Incorporation - Limited Liability Co. (LLC)",
            downloadDateAr = "٥ أغسطس ٢٠٢٦",
            downloadDateEn = "Aug 5, 2026",
            fileSize = "310 KB",
            fileFormat = "DOCX / GAFI",
            category = PracticeAreaCategory.COMMERCIAL,
            clausesCount = 24
        ),
        UserDownloadedContractItem(
            id = "dl_3",
            templateId = "tmpl_2",
            titleAr = "عقد بيع ابتدائي لقطعة أرض / عقار مع إقرار صحة ونفاذ",
            titleEn = "Preliminary Real Estate / Land Sale Agreement",
            downloadDateAr = "٢٢ يوليو ٢٠٢٦",
            downloadDateEn = "Jul 22, 2026",
            fileSize = "220 KB",
            fileFormat = "DOCX & PDF",
            category = PracticeAreaCategory.CIVIL,
            clausesCount = 18
        ),
        UserDownloadedContractItem(
            id = "dl_4",
            templateId = "tmpl_5",
            titleAr = "إنذار رسمي على يد محضر بسداد القيمة الإيجارية المتأخرة",
            titleEn = "Official Process Server Notice: Rent Arrears Demand",
            downloadDateAr = "١٥ يوليو ٢٠٢٦",
            downloadDateEn = "Jul 15, 2026",
            fileSize = "140 KB",
            fileFormat = "DOCX",
            category = PracticeAreaCategory.CIVIL,
            clausesCount = 8
        )
    )

    val initialNotifications = listOf(
        AppNotification(
            id = "notif_1",
            type = NotificationType.CHAT_REPLY,
            titleAr = "رد جديد من المستشار القانوني",
            titleEn = "New Legal Counsel Reply",
            messageAr = "قام د. طارق المنشاوي بالرد على استفسارك بخصوص صحة ونفاذ عقد البيع الابتدائي وإجراءات التسجيل بالشهر العقاري.",
            messageEn = "Dr. Tarek El-Menshawy replied to your inquiry regarding sale contract validation and land registry procedures.",
            timeAgoAr = "منذ ٥ دقائق",
            timeAgoEn = "5m ago",
            isRead = false,
            lawyerId = "lawyer_1",
            lawyerNameAr = "د. طارق عبد الرحمن المنشاوي",
            lawyerNameEn = "Dr. Tarek Abdel Rahman El-Menshawy"
        ),
        AppNotification(
            id = "notif_2",
            type = NotificationType.CASE_STATUS_CHANGE,
            titleAr = "تحديث في حالة القضية المطروحة",
            titleEn = "Case Status Update",
            messageAr = "تم قبول قضيتك رقم (UMB-2026-8812) للمراجعة وتلقيت ٣ عروض أتعاب ومذكرات دفاع من محامين مقيدين بالنقض.",
            messageEn = "Your case (UMB-2026-8812) has been accepted for review with 3 fee proposals received from Cassation advocates.",
            timeAgoAr = "منذ ٤٥ دقيقة",
            timeAgoEn = "45m ago",
            isRead = false,
            caseNumber = "UMB-2026-8812"
        ),
        AppNotification(
            id = "notif_3",
            type = NotificationType.CONSULTATION_CONFIRMED,
            titleAr = "تأكيد موعد الاستشارة القانونية",
            titleEn = "Consultation Confirmed",
            messageAr = "تم تأكيد موعد الاستشارة العاجلة مع المستشارة نهى الشريف يوم الغد الساعة ٦:٠٠ مساءً (مكتب الدقي / أونلاين).",
            messageEn = "Urgent legal consultation confirmed with Adv. Noha El-Sherif tomorrow at 6:00 PM (Dokki Office / Online).",
            timeAgoAr = "منذ ساعتين",
            timeAgoEn = "2h ago",
            isRead = false,
            lawyerId = "lawyer_2",
            lawyerNameAr = "المستشارة نهى سامي الشريف",
            lawyerNameEn = "Adv. Noha Samy El-Sherif"
        ),
        AppNotification(
            id = "notif_4",
            type = NotificationType.LEGAL_ALERT,
            titleAr = "تنبيه قضائي - سريان مبدأ محكمة النقض",
            titleEn = "Cassation Court Alert",
            messageAr = "صدر حكم حديث من الدائرة المدنية بمحكمة النقض بشأن شروط بطلان شرط التحكيم في عقود الإيجار التجارية.",
            messageEn = "Recent Cassation Civil Chamber principle published regarding arbitration clause invalidity in commercial leases.",
            timeAgoAr = "أمس",
            timeAgoEn = "Yesterday",
            isRead = true
        ),
        AppNotification(
            id = "notif_5",
            type = NotificationType.CHAT_REPLY,
            titleAr = "رسالة صوتية من المستشار وائل الشناوي",
            titleEn = "Voice message from Adv. Wael",
            messageAr = "أرسل لك المستشار وائل الشناوي تسجيلاً صوتياً يوضح فيه الدفوع الجنائية لمذكرة المعارضة الاستئنافية.",
            messageEn = "Adv. Wael El-Shennawy sent a voice memo outlining criminal defense grounds for the appeal brief.",
            timeAgoAr = "منذ يومين",
            timeAgoEn = "2 days ago",
            isRead = true,
            lawyerId = "lawyer_3",
            lawyerNameAr = "المستشار وائل الشناوي",
            lawyerNameEn = "Adv. Wael El-Shennawy"
        )
    )
}

