package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MockData
import com.example.data.gemini.*
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class AiHubTab(val id: String) {
    CHAT("ai_chat"),
    IMAGE_AUDIT("image_audit"),
    VIDEO_FORENSICS("video_forensics")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiriesChatScreen(
    language: AppLanguage,
    onCallHotline: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = AppTheme.colors
    val coroutineScope = rememberCoroutineScope()

    var activeTab by remember { mutableStateOf(AiHubTab.CHAT) }
    var selectedModel by remember { mutableStateOf(GeminiModels.GEMINI_3_5_FLASH) }
    var enableHighThinking by remember { mutableStateOf(false) }

    // Multi-turn chat state
    var chatMessages by remember { mutableStateOf(MockData.initialChatMessages) }
    var conversationTurns by remember { mutableStateOf<List<GeminiChatTurn>>(emptyList()) }
    var inputMessage by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val chatListState = rememberLazyListState()

    // Image Audit State
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedSampleDoc by remember { mutableStateOf<LegalDocumentSample?>(LegalSamples.documentSamples[0]) }
    var imageCustomPrompt by remember { mutableStateOf("") }
    var imageAnalysisResult by remember { mutableStateOf<GeminiResponse?>(null) }
    var isAnalyzingImage by remember { mutableStateOf(false) }

    // Video Forensics State
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedSampleVideo by remember { mutableStateOf<LegalVideoSample?>(LegalSamples.videoSamples[0]) }
    var videoCustomPrompt by remember { mutableStateOf("") }
    var videoAnalysisResult by remember { mutableStateOf<GeminiResponse?>(null) }
    var isAnalyzingVideo by remember { mutableStateOf(false) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            selectedSampleDoc = null
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                selectedImageBitmap = bitmap
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            selectedSampleVideo = null
            Toast.makeText(context, "Video selected: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Legal Advice", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, AppStrings.copiedToast.get(language), Toast.LENGTH_SHORT).show()
    }

    // Multi-turn send handler
    fun sendUserMessage(text: String) {
        if (text.isBlank() || isGenerating) return
        val userPrompt = text.trim()
        inputMessage = ""

        val userMessage = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = ChatSender.USER,
            senderNameAr = "أنت",
            senderNameEn = "You",
            messageAr = userPrompt,
            messageEn = userPrompt,
            timestamp = "الآن",
            isRead = true
        )
        chatMessages = chatMessages + userMessage

        val newTurns = conversationTurns + GeminiChatTurn(role = "user", text = userPrompt)
        conversationTurns = newTurns

        isGenerating = true

        coroutineScope.launch {
            chatListState.animateScrollToItem(chatMessages.size - 1)

            val modelToUse = if (enableHighThinking) GeminiModels.GEMINI_3_1_PRO else selectedModel
            val response = GeminiClient.generateChatResponse(
                history = newTurns,
                model = modelToUse,
                enableHighThinking = enableHighThinking
            )

            isGenerating = false

            val advisorMessage = ChatMessage(
                id = "ai_reply_${System.currentTimeMillis()}",
                sender = ChatSender.ADVISOR,
                senderNameAr = "المستشار الذكي (${response.modelUsed})",
                senderNameEn = "AI Counsel (${response.modelUsed})",
                messageAr = response.text,
                messageEn = response.text,
                timestamp = "الآن",
                isRead = true
            )
            chatMessages = chatMessages + advisorMessage
            conversationTurns = conversationTurns + GeminiChatTurn(role = "model", text = response.text)

            chatListState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = colors.surface,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Navy850)
                                    .border(1.5.dp, colors.goldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Gemini AI Advisor",
                                    tint = colors.goldLight,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = AppStrings.geminiAssistantTitle.get(language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.textPrimary
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldSuccess)
                                    )
                                    Text(
                                        text = if (enableHighThinking) "High Thinking (Gemini 3.1 Pro)" else "Gemini Active",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                                        color = if (enableHighThinking) colors.goldPrimary else EmeraldSuccess
                                    )
                                }
                            }
                        }

                        // Direct Hotline Call
                        IconButton(
                            onClick = onCallHotline,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(CrimsonLight)
                                .testTag("chat_hotline_call_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneInTalk,
                                contentDescription = "Emergency Hotline",
                                tint = CrimsonEmergency,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3 Feature Tabs
                    TabRow(
                        selectedTabIndex = activeTab.ordinal,
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = activeTab == AiHubTab.CHAT,
                            onClick = { activeTab = AiHubTab.CHAT },
                            text = {
                                Text(
                                    text = AppStrings.tabAiChat.get(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (activeTab == AiHubTab.CHAT) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            icon = {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                        Tab(
                            selected = activeTab == AiHubTab.IMAGE_AUDIT,
                            onClick = { activeTab = AiHubTab.IMAGE_AUDIT },
                            text = {
                                Text(
                                    text = AppStrings.tabImageAnalysis.get(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (activeTab == AiHubTab.IMAGE_AUDIT) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            icon = {
                                Icon(Icons.Default.DocumentScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                        Tab(
                            selected = activeTab == AiHubTab.VIDEO_FORENSICS,
                            onClick = { activeTab = AiHubTab.VIDEO_FORENSICS },
                            text = {
                                Text(
                                    text = AppStrings.tabVideoAnalysis.get(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (activeTab == AiHubTab.VIDEO_FORENSICS) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            icon = {
                                Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("inquiries_chat_screen")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding)
        ) {
            when (activeTab) {
                AiHubTab.CHAT -> {
                    AiChatTabContent(
                        language = language,
                        colors = colors,
                        selectedModel = selectedModel,
                        onModelSelected = { selectedModel = it },
                        enableHighThinking = enableHighThinking,
                        onToggleHighThinking = { enableHighThinking = it },
                        chatMessages = chatMessages,
                        isGenerating = isGenerating,
                        inputMessage = inputMessage,
                        onInputChange = { inputMessage = it },
                        onSendMessage = { sendUserMessage(it) },
                        onClearChat = {
                            chatMessages = MockData.initialChatMessages.take(1)
                            conversationTurns = emptyList()
                        },
                        onCopyAdvice = { copyToClipboard(it) },
                        listState = chatListState
                    )
                }
                AiHubTab.IMAGE_AUDIT -> {
                    ImageAuditTabContent(
                        language = language,
                        colors = colors,
                        enableHighThinking = enableHighThinking,
                        onToggleHighThinking = { enableHighThinking = it },
                        selectedImageUri = selectedImageUri,
                        selectedSampleDoc = selectedSampleDoc,
                        onSelectSampleDoc = {
                            selectedSampleDoc = it
                            selectedImageUri = null
                            selectedImageBitmap = null
                            imageCustomPrompt = it.getDefaultPrompt(language)
                        },
                        onPickImage = { imagePickerLauncher.launch("image/*") },
                        customPrompt = imageCustomPrompt,
                        onPromptChange = { imageCustomPrompt = it },
                        isAnalyzing = isAnalyzingImage,
                        analysisResult = imageAnalysisResult,
                        onRunAnalysis = {
                            coroutineScope.launch {
                                isAnalyzingImage = true
                                val prompt = if (imageCustomPrompt.isNotBlank()) imageCustomPrompt else {
                                    selectedSampleDoc?.getDefaultPrompt(language) ?: "حلل هذا المستند القانوني بالكامل."
                                }
                                val docContext = selectedSampleDoc?.simulatedTextAr ?: "مستند مصور مقدم من المستخدم."
                                val fullPrompt = "$prompt\n\nنص المستند المعاين:\n$docContext"
                                
                                val turn = GeminiChatTurn(
                                    role = "user",
                                    text = fullPrompt,
                                    imageBase64 = selectedImageBitmap?.let { GeminiClient.bitmapToBase64(it) }
                                )
                                imageAnalysisResult = GeminiClient.generateChatResponse(
                                    history = listOf(turn),
                                    model = GeminiModels.GEMINI_3_1_PRO,
                                    enableHighThinking = enableHighThinking,
                                    systemInstruction = GeminiModels.ROLE_SYSTEM_LEGAL
                                )
                                isAnalyzingImage = false
                            }
                        },
                        onCopyResult = { copyToClipboard(it) }
                    )
                }
                AiHubTab.VIDEO_FORENSICS -> {
                    VideoForensicsTabContent(
                        language = language,
                        colors = colors,
                        enableHighThinking = enableHighThinking,
                        onToggleHighThinking = { enableHighThinking = it },
                        selectedVideoUri = selectedVideoUri,
                        selectedSampleVideo = selectedSampleVideo,
                        onSelectSampleVideo = {
                            selectedSampleVideo = it
                            selectedVideoUri = null
                            videoCustomPrompt = it.getDefaultPrompt(language)
                        },
                        onPickVideo = { videoPickerLauncher.launch("video/*") },
                        customPrompt = videoCustomPrompt,
                        onPromptChange = { videoCustomPrompt = it },
                        isAnalyzing = isAnalyzingVideo,
                        analysisResult = videoAnalysisResult,
                        onRunAnalysis = {
                            coroutineScope.launch {
                                isAnalyzingVideo = true
                                val prompt = if (videoCustomPrompt.isNotBlank()) videoCustomPrompt else {
                                    selectedSampleVideo?.getDefaultPrompt(language) ?: "حلل هذا التسجيل المرئي كدليل جنائي ومدني."
                                }
                                val videoDesc = selectedSampleVideo?.videoSimulatedDescription ?: "تسجيل فيديو مرفوع من المستخدم."
                                val fullPrompt = "$prompt\n\nوصف وبيانات تفريغ المقطع المرئي:\n$videoDesc"

                                val turn = GeminiChatTurn(
                                    role = "user",
                                    text = fullPrompt
                                )
                                videoAnalysisResult = GeminiClient.generateChatResponse(
                                    history = listOf(turn),
                                    model = GeminiModels.GEMINI_3_1_PRO,
                                    enableHighThinking = enableHighThinking,
                                    systemInstruction = """
You are the Chief Digital Evidence & Video Forensics Consultant for the Egyptian Judicial System (خبير الأدلة الرقمية والتسجيلات المرئية الجنائية والمدنية).
Analyze video evidence thoroughly under Egyptian Law:
1. Event Timeline & Frame Sequence (التسلسل الزمني للواقعة).
2. Determination of Fault & Statutory Liability (تحديد المسؤولية التقصيرية والجنائية وفق نصوص قانون العقوبات والقانون المدني).
3. Digital Evidence Admissibility (حجية الدليل الرقمي وفق قانون مكافحة جرائم تقنية المعلومات ١٧٥ لسنة ٢٠١٨).
4. Procedural Recommendations for Public Prosecution & Police Station reports (إجراءات تقديم التسجيل للنيابة العامة وتفريغه رسمياً).
"""
                                )
                                isAnalyzingVideo = false
                            }
                        },
                        onCopyResult = { copyToClipboard(it) }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 1: AI CHAT (Multi-turn + Model Selector + High Thinking)
// ----------------------------------------------------
@Composable
fun AiChatTabContent(
    language: AppLanguage,
    colors: AppColors,
    selectedModel: String,
    onModelSelected: (String) -> Unit,
    enableHighThinking: Boolean,
    onToggleHighThinking: (Boolean) -> Unit,
    chatMessages: List<ChatMessage>,
    isGenerating: Boolean,
    inputMessage: String,
    onInputChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onCopyAdvice: (String) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // AI Controls Bar (Model selection & High Thinking switch)
        Surface(
            color = colors.surface,
            border = BorderStroke(1.dp, colors.border),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // Row 1: Model Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.selectModel.get(language),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = colors.textSecondary
                    )

                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear Chat",
                            tint = colors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedModel == GeminiModels.GEMINI_3_5_FLASH && !enableHighThinking,
                            onClick = {
                                onToggleHighThinking(false)
                                onModelSelected(GeminiModels.GEMINI_3_5_FLASH)
                            },
                            label = { Text("Gemini 3.5 Flash (عام)", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.goldPale,
                                selectedLabelColor = colors.goldText
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedModel == GeminiModels.GEMINI_3_1_PRO && !enableHighThinking,
                            onClick = {
                                onToggleHighThinking(false)
                                onModelSelected(GeminiModels.GEMINI_3_1_PRO)
                            },
                            label = { Text("Gemini 3.1 Pro (معقد)", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.goldPale,
                                selectedLabelColor = colors.goldText
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedModel == GeminiModels.GEMINI_3_1_FLASH_LITE && !enableHighThinking,
                            onClick = {
                                onToggleHighThinking(false)
                                onModelSelected(GeminiModels.GEMINI_3_1_FLASH_LITE)
                            },
                            label = { Text("Gemini 3.1 Lite (فائق السرعة)", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.goldPale,
                                selectedLabelColor = colors.goldText
                            )
                        )
                    }
                }

                // Row 2: High Thinking Toggle Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (enableHighThinking) colors.goldPale else colors.surfaceVariant,
                    border = BorderStroke(1.dp, if (enableHighThinking) colors.goldBorder else colors.border),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .clickable { onToggleHighThinking(!enableHighThinking) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = if (enableHighThinking) colors.goldPrimary else colors.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = AppStrings.highThinkingMode.get(language),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (enableHighThinking) colors.goldText else colors.textPrimary
                                )
                                Text(
                                    text = if (enableHighThinking) "Gemini 3.1 Pro (Thinking: HIGH)" else "تفكير استراتيجي بالسوابق القضائية",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = colors.textSecondary
                                )
                            }
                        }

                        Switch(
                            checked = enableHighThinking,
                            onCheckedChange = { onToggleHighThinking(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.goldPrimary,
                                checkedTrackColor = colors.goldPale
                            ),
                            modifier = Modifier.testTag("chat_high_thinking_switch")
                        )
                    }
                }
            }
        }

        // Chat Message Thread (Scrollable)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages) { message ->
                val isUser = message.sender == ChatSender.USER
                val isSystem = message.sender == ChatSender.SYSTEM

                if (isSystem) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.goldPale,
                            border = BorderStroke(1.dp, colors.goldBorder)
                        ) {
                            Text(
                                text = message.getMessage(language),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = colors.goldText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isUser) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Navy850)
                                    .border(1.dp, colors.goldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = colors.goldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Column(
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                            modifier = Modifier.widthIn(max = 310.dp)
                        ) {
                            if (!isUser) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = message.getSenderName(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (colors.isDark) colors.goldLight else Navy800
                                    )
                                    if (enableHighThinking) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = colors.goldPale,
                                            border = BorderStroke(0.5.dp, colors.goldBorder)
                                        ) {
                                            Text(
                                                text = "HIGH THINKING",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                                                color = colors.goldText,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                ),
                                color = if (isUser) Navy850 else colors.surface,
                                border = if (isUser) null else BorderStroke(1.dp, colors.border),
                                tonalElevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = message.getMessage(language),
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                        color = if (isUser) Color.White else colors.textPrimary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (!isUser) {
                                            IconButton(
                                                onClick = { onCopyAdvice(message.getMessage(language)) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy",
                                                    tint = colors.textSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        } else {
                                            Spacer(modifier = Modifier.width(1.dp))
                                        }

                                        Text(
                                            text = message.timestamp,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = if (isUser) colors.goldLight.copy(alpha = 0.8f) else colors.textSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.goldPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (enableHighThinking) "Gemini 3.1 Pro يفكر بعمق في السوابق والمواد القانونية..." else AppStrings.analyzingWithGemini.get(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.goldPrimary
                        )
                    }
                }
            }
        }

        // Bottom Input Row & Quick Prompt Chips
        Surface(
            color = colors.surface,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Quick Legal FAQ Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                ) {
                    items(MockData.sampleFaqInquiries) { faq ->
                        Surface(
                            onClick = { onSendMessage(faq) },
                            shape = RoundedCornerShape(14.dp),
                            color = colors.surfaceVariant,
                            border = BorderStroke(1.dp, colors.border)
                        ) {
                            Text(
                                text = faq,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = colors.textPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Message Text Field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputMessage,
                        onValueChange = onInputChange,
                        placeholder = {
                            Text(
                                text = AppStrings.chatPlaceholder.get(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textSecondary
                            )
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.goldPrimary,
                            unfocusedBorderColor = colors.border,
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant
                        ),
                        maxLines = 3,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                    )

                    IconButton(
                        onClick = { onSendMessage(inputMessage) },
                        enabled = inputMessage.isNotBlank() && !isGenerating,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (inputMessage.isNotBlank() && !isGenerating) Navy850 else colors.border)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputMessage.isNotBlank() && !isGenerating) colors.goldLight else colors.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 2: IMAGE AUDIT (Gemini 3.1 Pro + High Thinking)
// ----------------------------------------------------
@Composable
fun ImageAuditTabContent(
    language: AppLanguage,
    colors: AppColors,
    enableHighThinking: Boolean,
    onToggleHighThinking: (Boolean) -> Unit,
    selectedImageUri: Uri?,
    selectedSampleDoc: LegalDocumentSample?,
    onSelectSampleDoc: (LegalDocumentSample) -> Unit,
    onPickImage: () -> Unit,
    customPrompt: String,
    onPromptChange: (String) -> Unit,
    isAnalyzing: Boolean,
    analysisResult: GeminiResponse?,
    onRunAnalysis: () -> Unit,
    onCopyResult: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Hero Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Navy850),
                border = BorderStroke(1.dp, colors.goldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Navy700)
                            .border(1.dp, colors.goldPrimary, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = colors.goldLight,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.imageAnalysisTitle.get(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AppStrings.imageAnalysisSubtitle.get(language),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextOnNavySecondary
                        )
                    }
                }
            }
        }

        // Upload or Snap Photo Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onPickImage,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceVariant),
                    border = BorderStroke(1.dp, colors.goldBorder),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("upload_doc_image_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = colors.goldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedImageUri != null) "تم اختيار صورة" else AppStrings.uploadDocImage.get(language),
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // Pre-loaded Realistic Legal Samples Selector
        item {
            Text(
                text = AppStrings.selectSampleDoc.get(language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LegalSamples.documentSamples.forEach { sample ->
                    val isSelected = selectedSampleDoc?.id == sample.id
                    Surface(
                        onClick = { onSelectSampleDoc(sample) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.goldPale else colors.surface,
                        border = BorderStroke(1.dp, if (isSelected) colors.goldPrimary else colors.border),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectSampleDoc(sample) },
                                colors = RadioButtonDefaults.colors(selectedColor = colors.goldPrimary)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sample.getTitle(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = sample.getSummary(language),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // High Thinking Toggle
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (enableHighThinking) colors.goldPale else colors.surfaceVariant,
                border = BorderStroke(1.dp, if (enableHighThinking) colors.goldBorder else colors.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleHighThinking(!enableHighThinking) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = if (enableHighThinking) colors.goldPrimary else colors.textSecondary
                        )
                        Column {
                            Text(
                                text = "تفعيل التفكير المعمق (High Thinking)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (enableHighThinking) colors.goldText else colors.textPrimary
                            )
                            Text(
                                text = "تدقيق عميق في الثغرات ونصوص القانون المدني بموديل Gemini 3.1 Pro",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = colors.textSecondary
                            )
                        }
                    }
                    Switch(
                        checked = enableHighThinking,
                        onCheckedChange = { onToggleHighThinking(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colors.goldPrimary,
                            checkedTrackColor = colors.goldPale
                        )
                    )
                }
            }
        }

        // Custom Prompt field
        item {
            OutlinedTextField(
                value = customPrompt,
                onValueChange = onPromptChange,
                label = { Text("تعليمات الفحص والتدقيق القانوني") },
                placeholder = { Text(AppStrings.docAuditPromptHint.get(language)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.goldPrimary,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Run Analysis CTA
        item {
            Button(
                onClick = onRunAnalysis,
                enabled = !isAnalyzing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Navy850),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("run_doc_audit_button")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colors.goldLight, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("جارٍ فحص المستند بواسطة Gemini 3.1 Pro...", color = colors.goldLight)
                } else {
                    Icon(imageVector = Icons.Default.Gavel, contentDescription = null, tint = colors.goldLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.runDocAudit.get(language), color = colors.goldLight, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Result Card
        analysisResult?.let { result ->
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.goldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSuccess)
                                Text(
                                    text = "تقرير الفحص القانوني (Gemini 3.1 Pro)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.textPrimary
                                )
                            }
                            IconButton(onClick = { onCopyResult(result.text) }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = colors.goldPrimary)
                            }
                        }

                        if (result.thinkingProcess != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.goldPale,
                                border = BorderStroke(1.dp, colors.goldBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = AppStrings.thoughtsProcess.get(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = colors.goldText
                                    )
                                    Text(
                                        text = result.thinkingProcess,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = result.text,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 3: VIDEO FORENSICS (Gemini 3.1 Pro + High Thinking)
// ----------------------------------------------------
@Composable
fun VideoForensicsTabContent(
    language: AppLanguage,
    colors: AppColors,
    enableHighThinking: Boolean,
    onToggleHighThinking: (Boolean) -> Unit,
    selectedVideoUri: Uri?,
    selectedSampleVideo: LegalVideoSample?,
    onSelectSampleVideo: (LegalVideoSample) -> Unit,
    onPickVideo: () -> Unit,
    customPrompt: String,
    onPromptChange: (String) -> Unit,
    isAnalyzing: Boolean,
    analysisResult: GeminiResponse?,
    onRunAnalysis: () -> Unit,
    onCopyResult: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Hero Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Navy850),
                border = BorderStroke(1.dp, colors.goldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Navy700)
                            .border(1.dp, colors.goldPrimary, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = colors.goldLight,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.videoAnalysisTitle.get(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AppStrings.videoAnalysisSubtitle.get(language),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextOnNavySecondary
                        )
                    }
                }
            }
        }

        // Upload Video Button
        item {
            Button(
                onClick = onPickVideo,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceVariant),
                border = BorderStroke(1.dp, colors.goldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_video_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.VideoFile,
                    contentDescription = null,
                    tint = colors.goldPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedVideoUri != null) "تم اختيار ملف فيديو" else AppStrings.uploadVideo.get(language),
                    color = colors.textPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        // Pre-loaded Realistic Legal Video Scenarios
        item {
            Text(
                text = AppStrings.selectSampleVideo.get(language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LegalSamples.videoSamples.forEach { sample ->
                    val isSelected = selectedSampleVideo?.id == sample.id
                    Surface(
                        onClick = { onSelectSampleVideo(sample) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.goldPale else colors.surface,
                        border = BorderStroke(1.dp, if (isSelected) colors.goldPrimary else colors.border),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectSampleVideo(sample) },
                                colors = RadioButtonDefaults.colors(selectedColor = colors.goldPrimary)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sample.getTitle(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = colors.textPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Navy850
                                    ) {
                                        Text(
                                            text = sample.durationText,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = colors.goldLight,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = sample.getSummary(language),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // High Thinking Toggle
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (enableHighThinking) colors.goldPale else colors.surfaceVariant,
                border = BorderStroke(1.dp, if (enableHighThinking) colors.goldBorder else colors.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleHighThinking(!enableHighThinking) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = if (enableHighThinking) colors.goldPrimary else colors.textSecondary
                        )
                        Column {
                            Text(
                                text = "تفعيل التفكير المعمق (High Thinking)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (enableHighThinking) colors.goldText else colors.textPrimary
                            )
                            Text(
                                text = "تحليل جنائي ومدني متقدم لمسؤولية أطراف الواقعة بنموذج Gemini 3.1 Pro",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = colors.textSecondary
                            )
                        }
                    }
                    Switch(
                        checked = enableHighThinking,
                        onCheckedChange = { onToggleHighThinking(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colors.goldPrimary,
                            checkedTrackColor = colors.goldPale
                        )
                    )
                }
            }
        }

        // Custom Prompt
        item {
            OutlinedTextField(
                value = customPrompt,
                onValueChange = onPromptChange,
                label = { Text("طلب التحليل الجنائي والقانوني للفيديو") },
                placeholder = { Text(AppStrings.videoAnalysisPromptHint.get(language)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.goldPrimary,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Run Analysis CTA
        item {
            Button(
                onClick = onRunAnalysis,
                enabled = !isAnalyzing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Navy850),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("run_video_analysis_button")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colors.goldLight, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("جارٍ تحليل الفيديو واستخراج الأدلة بواسطة Gemini Pro...", color = colors.goldLight)
                } else {
                    Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = colors.goldLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.runVideoAnalysis.get(language), color = colors.goldLight, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Result Card
        analysisResult?.let { result ->
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.goldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = colors.goldPrimary)
                                Text(
                                    text = "تقرير تفريغ الأدلة والمسؤوليات (Gemini 3.1 Pro)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.textPrimary
                                )
                            }
                            IconButton(onClick = { onCopyResult(result.text) }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = colors.goldPrimary)
                            }
                        }

                        if (result.thinkingProcess != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.goldPale,
                                border = BorderStroke(1.dp, colors.goldBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = AppStrings.thoughtsProcess.get(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = colors.goldText
                                    )
                                    Text(
                                        text = result.thinkingProcess,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = result.text,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }
    }
}
