package com.ncorti.kotlin.template.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

data class AiModelItem(
    val id: String,
    val name: String,
    val sizeText: String,
    val description: String,
    val downloadUrl: String,
    val isRecommended: Boolean = false
)

object ModelDownloader {
    private val client = OkHttpClient()

    suspend fun download(
        url: String,
        targetFile: File,
        onProgress: (Int) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext false
                val body = response.body ?: return@withContext false
                val total = body.contentLength()
                var downloaded = 0L

                body.byteStream().use { input ->
                    FileOutputStream(targetFile).use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var bytes: Int
                        while (input.read(buffer).also { bytes = it } != -1) {
                            output.write(buffer, 0, bytes)
                            downloaded += bytes
                            if (total > 0) {
                                val pct = ((downloaded * 100) / total).toInt()
                                withContext(Dispatchers.Main) { onProgress(pct) }
                            }
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            if (targetFile.exists()) targetFile.delete()
            false
        }
    }
}
