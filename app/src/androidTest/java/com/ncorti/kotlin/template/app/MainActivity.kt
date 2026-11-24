package com.ncorti.kotlin.template.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.io.File

val PurplePrimary = Color(0xFF5B4BDB)
val BgLight = Color(0xFFF6F7FB)
val LiveRed = Color(0xFFE53935)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = PurplePrimary)) {
                AniClipsApp()
            }
        }
    }
}

@Composable
fun AniClipsApp() {
    var showModelHub by remember { mutableStateOf(false) }

    if (showModelHub) {
        ModelHubScreen(onBack = { showModelHub = false })
    } else {
        MainHomeScreen(onOpenModelHub = { showModelHub = true })
    }
}

@Composable
fun MainHomeScreen(onOpenModelHub: () -> Unit) {
    var isLiveTab by remember { mutableStateOf(false) }
    var videoUrl by remember { mutableStateOf("") }
    var aiAnalysisEnabled by remember { mutableStateOf(true) }
    var autoCaptionsEnabled by remember { mutableStateOf(true) }
    var selectedCaptionStyle by remember { mutableStateOf("Viral Yellow") }
    var hookEnabled by remember { mutableStateOf(true) }
    var hookText by remember { mutableStateOf("") }
    var clipCount by remember { mutableIntStateOf(3) }
    var clipLength by remember { mutableStateOf("20s") }
    var isVertical by remember { mutableStateOf(true) }
    var autoClipHype by remember { mutableStateOf(true) }
    var statusMsg by remember { mutableStateOf("Ready - 100% Free and Unlimited") }

    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(LiveRed, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("ANICLIPS", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    Text("UNLIMITED AI CLIPPER", fontSize = 10.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                }
            }
            Row {
                IconButton(onClick = onOpenModelHub) {
                    Icon(Icons.Default.Psychology, contentDescription = "AI Models", tint = PurplePrimary)
                }
                IconButton(onClick = { statusMsg = "Saved clips are in Movies/AniClips" }) {
                    Icon(Icons.Default.VideoLibrary, contentDescription = "Saved Clips")
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9ECF2), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            TabChip("Normal Video", !isLiveTab, Modifier.weight(1f)) { isLiveTab = false }
            TabChip("Live Stream", isLiveTab, Modifier.weight(1f), activeColor = LiveRed) { isLiveTab = true }
        }

        Spacer(Modifier.height(14.dp))

        if (isLiveTab) {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = PurplePrimary)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Live Chat Velocity Engine", fontWeight = FontWeight.Bold)
                        Text(
                            "No LLM needed. Auto-detects viral moments from live comment spikes in real-time.",
                            fontSize = 12.sp, color = Color.Gray
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        SectionCard {
            Text(
                if (isLiveTab) "Live Stream Link" else "Video Stream URL",
                fontWeight = FontWeight.Bold, fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                placeholder = { Text(if (isLiveTab) "Paste live stream link..." else "Paste YouTube or Twitch link...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { videoUrl = clipboard.getText()?.text ?: "" }) {
                        Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = PurplePrimary)
                    }
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        if (!isLiveTab) {
            SectionCard {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("AI Viral Analysis", fontWeight = FontWeight.Bold)
                        Text("Use on-device LLM model", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(checked = aiAnalysisEnabled, onCheckedChange = { aiAnalysisEnabled = it })
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onOpenModelHub,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Download AI & Vosk Models")
                }
            }
            Spacer(Modifier.height(12.dp))
        } else {
            SectionCard {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Auto-Clip on Hype Spikes", fontWeight = FontWeight.Bold)
                        Text("Starts 10s before peak chat explosion", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(checked = autoClipHype, onCheckedChange = { autoClipHype = it })
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        SectionCard {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Dynamic Auto-Captions", fontWeight = FontWeight.Bold)
                    Text("Burned-in viral subtitles", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = autoCaptionsEnabled, onCheckedChange = { autoCaptionsEnabled = it })
            }
            if (autoCaptionsEnabled) {
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Viral Yellow", "Neon Cyan", "Classic White").forEach { style ->
                        val selected = selectedCaptionStyle == style
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCaptionStyle = style },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) PurplePrimary else Color.LightGray),
                            color = if (selected) Color(0xFFF0EEFF) else Color.White
                        ) {
                            Text(
                                style, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        SectionCard {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hook Title Header", fontWeight = FontWeight.Bold)
                    Text("Viral text on top blurred area", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = hookEnabled, onCheckedChange = { hookEnabled = it })
            }
            if (hookEnabled) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { hookText = "WAIT FOR IT!" }, label = { Text("WAIT FOR IT!", fontSize = 11.sp) })
                    AssistChip(onClick = { hookText = "UNEXPECTED!" }, label = { Text("UNEXPECTED!", fontSize = 11.sp) })
                }
                OutlinedTextField(
                    value = hookText,
                    onValueChange = { hookText = it },
                    placeholder = { Text("Auto-hook or type custom text") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (!isLiveTab) {
            SectionCard {
                Text("Number of Viral Clips", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..5).forEach { num ->
                        val sel = clipCount == num
                        Button(
                            onClick = { clipCount = num },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (sel) PurplePrimary else Color(0xFFECEEF4),
                                contentColor = if (sel) Color.White else Color.Black
                            )
                        ) { Text("$num") }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Clip Length", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("20s", "30s", "40s", "60s").forEach { len ->
                        val sel = clipLength == len
                        Button(
                            onClick = { clipLength = len },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (sel) PurplePrimary else Color(0xFFECEEF4),
                                contentColor = if (sel) Color.White else Color.Black
                            )
                        ) { Text(len) }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        SectionCard {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Vertical Shorts / Reels (9:16)", fontWeight = FontWeight.Bold)
                    Text("720x1280 with blurred background (Low RAM)", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = isVertical, onCheckedChange = { isVertical = it })
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                statusMsg = if (videoUrl.isBlank()) "Please paste a link first!"
                else if (isLiveTab) "Monitoring Live Chat for Hype Spikes..."
                else "Generating $clipCount Unlimited Clips ($clipLength)..."
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isLiveTab) LiveRed else PurplePrimary)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                if (isLiveTab) "Start Monitoring Live Chat" else "Generate $clipCount Clips ($clipLength) - Free",
                fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(statusMsg, fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ModelHubScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val progressMap = remember { mutableStateMapOf<String, Int>() }

    val llmModels = listOf(
        AiModelItem("phi3", "Phi-3 Mini (Fast)", "~400 MB", "Compact model. Best for Low-RAM & mid-range phones.", "https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_k_m.gguf"),
        AiModelItem("gemma2", "Gemma 2 2B (Recommended)", "~1.5 GB", "Excellent quality viral moment analysis.", "https://huggingface.co/bartowski/gemma-2-2b-it-GGUF/resolve/main/gemma-2-2b-it-Q4_K_M.gguf", true),
        AiModelItem("llama32", "Llama 3.2 3B (High Quality)", "~2 GB", "Highest quality analysis for 8GB+ RAM phones.", "https://huggingface.co/bartowski/Llama-3.2-3B-Instruct-GGUF/resolve/main/Llama-3.2-3B-Instruct-Q4_K_M.gguf")
    )

    val speechModels = listOf(
        AiModelItem("vosk_in", "Vosk Indian English & Hinglish", "~36 MB", "Fast, lightweight & 100% offline for Indian accents.", "https://alphacephei.com/vosk/models/vosk-model-small-en-in-0.4.zip", true),
        AiModelItem("vosk_hi", "Vosk Hindi & Regional", "~42 MB", "Offline speech recognition for Hindi & Hinglish.", "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip"),
        AiModelItem("vosk_en", "Vosk Global English", "~40 MB", "Universal English offline captions.", "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip")
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(BgLight)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("AI Models & Tools", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        TabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Viral LLM (AI)") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Speech Captions") })
        }

        Spacer(Modifier.height(12.dp))

        val list = if (selectedTab == 0) llmModels else speechModels
        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            list.forEach { item ->
                val prog = progressMap[item.id]
                val file = File(context.filesDir, "${item.id}.model")
                SectionCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        if (item.isRecommended) {
                            Text("RECOMMENDED", fontSize = 10.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("Download Size: ${item.sizeText}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text(item.description, fontSize = 13.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                progressMap[item.id] = 1
                                val ok = ModelDownloader.download(item.downloadUrl, file) { p -> progressMap[item.id] = p }
                                progressMap[item.id] = if (ok) 100 else -1
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Text(
                            when {
                                file.exists() && prog == null -> "Downloaded"
                                prog == 100 -> "Downloaded"
                                prog != null && prog > 0 -> "Downloading $prog%..."
                                prog == -1 -> "Retry Download"
                                else -> "Download (${item.sizeText})"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun TabChip(title: String, selected: Boolean, modifier: Modifier, activeColor: Color = PurplePrimary, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color.White else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Box(Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
            Text(title, fontWeight = FontWeight.Bold, color = if (selected) activeColor else Color.Gray, fontSize = 14.sp)
        }
    }
}
