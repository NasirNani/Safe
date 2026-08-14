package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiriesChatScreen(
    language: AppLanguage,
    onCallHotline: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Support Desk, 1: Lawyer Chats
    var chatMessages by remember { mutableStateOf(MockData.initialChatMessages) }
    var inputMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = ChatSender.USER,
            senderNameAr = "أنت",
            senderNameEn = "You",
            messageAr = text,
            messageEn = text,
            timestamp = "الآن",
            isRead = true
        )
        chatMessages = chatMessages + userMsg
        inputMessage = ""

        // Check if there is an exact or related FAQ answer
        val answerAr = MockData.sampleFaqAnswers[text] ?: run {
            if (language == AppLanguage.ARABIC) {
                "شكراً لاستفسارك القانوني. تم استلام تفاصيل الموضوع وجارٍ فحصه طبقاً لأحكام القانون المصري والمبادئ القضائية المستقرة لمحكمة النقض. يمكنك أيضاً حجز استشارة متخصصة مع أحد مستشارينا المعتمدين في قسم المحامين."
            } else {
                "Thank you for your legal inquiry. Your question has been received and is being reviewed under Egyptian statutory provisions and Court of Cassation principles. You can also book a specialized consult with our verified attorneys."
            }
        }

        val advisorReply = ChatMessage(
            id = "reply_${System.currentTimeMillis() + 1}",
            sender = ChatSender.ADVISOR,
            senderNameAr = "المستشار / كريم ممدوح (مستشار الدعم)",
            senderNameEn = "Adv. Karim Mamdouh (Legal Support)",
            messageAr = answerAr,
            messageEn = answerAr,
            timestamp = "الآن",
            isRead = true
        )
        chatMessages = chatMessages + advisorReply

        coroutineScope.launch {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = SurfaceCard,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Header row
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Navy850)
                                    .border(1.5.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SupportAgent,
                                    contentDescription = "Support Agent",
                                    tint = GoldLight,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = AppStrings.chatTitle.get(language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldSuccess)
                                    )
                                    Text(
                                        text = AppStrings.onlineAdvisor.get(language),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }

                        // Direct Call Button
                        IconButton(
                            onClick = onCallHotline,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(CrimsonLight)
                                .testTag("chat_hotline_call_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneInTalk,
                                contentDescription = "Call",
                                tint = CrimsonEmergency,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tab selector: Support desk vs Lawyer chats
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Navy50,
                        contentColor = Navy850,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    text = AppStrings.tabSupportDesk.get(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    text = AppStrings.tabLawyerChats.get(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceCard,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Quick FAQ topics chips
                    Text(
                        text = AppStrings.quickFaqQuestions.get(language),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextMuted,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        items(MockData.sampleFaqInquiries) { faq ->
                            Surface(
                                onClick = { sendMessage(faq) },
                                shape = RoundedCornerShape(16.dp),
                                color = Navy50,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Navy100)
                            ) {
                                Text(
                                    text = faq,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = Navy850,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Message input row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputMessage,
                            onValueChange = { inputMessage = it },
                            placeholder = {
                                Text(
                                    text = AppStrings.chatPlaceholder.get(language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Navy800,
                                unfocusedBorderColor = BorderSubtle,
                                focusedContainerColor = OffWhite,
                                unfocusedContainerColor = OffWhite
                            ),
                            maxLines = 3,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field")
                        )

                        IconButton(
                            onClick = { sendMessage(inputMessage) },
                            enabled = inputMessage.isNotBlank(),
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (inputMessage.isNotBlank()) Navy850 else BorderSubtle)
                                .testTag("chat_send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (inputMessage.isNotBlank()) GoldLight else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("inquiries_chat_screen")
    ) { innerPadding ->
        if (selectedTab == 0) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(OffWhite)
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                                color = GoldPale,
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = message.getMessage(language),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = Gold900,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Navy850)
                                        .border(1.dp, GoldPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Gavel,
                                        contentDescription = null,
                                        tint = GoldLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(
                                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                                modifier = Modifier.widthIn(max = 290.dp)
                            ) {
                                if (!isUser) {
                                    Text(
                                        text = message.getSenderName(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy800,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 4.dp,
                                        bottomEnd = if (isUser) 4.dp else 16.dp
                                    ),
                                    color = if (isUser) Navy850 else SurfaceCard,
                                    border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                    tonalElevation = 2.dp
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = message.getMessage(language),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                lineHeight = 20.sp
                                            ),
                                            color = if (isUser) Color.White else TextPrimary
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = message.timestamp,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = if (isUser) GoldLight.copy(alpha = 0.8f) else TextMuted,
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Tab 1: Lawyer Direct Chats List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OffWhite)
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MockData.lawyers.take(2)) { lawyer ->
                    Card(
                        onClick = { /* open lawyer chat */ },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(lawyer.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lawyer.getName(language).take(2),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GoldLight
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = lawyer.getName(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "أمس",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "تم إرسال مسودة صحيفة الدعوى لمراجعتك..." else "Sent the lawsuit draft for your review...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
