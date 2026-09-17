package com.zincstate.manifest.core.common

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

object AttachmentManager {

    private const val ATTACHMENTS_DIR = "secure_attachments"

    suspend fun saveAttachment(
        context: Context,
        transactionId: String,
        fileName: String,
        inputStream: InputStream
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val dir = File(context.filesDir, ATTACHMENTS_DIR)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // File naming format: {transactionId}_{fileName}
            val safeFileName = "${transactionId}_${System.currentTimeMillis()}_$fileName"
            val file = File(dir, safeFileName)

            val encryptedFile = EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileOutput().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            Result.success(safeFileName)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun loadAttachment(
        context: Context,
        fileName: String
    ): Result<InputStream> = withContext(Dispatchers.IO) {
        try {
            val dir = File(context.filesDir, ATTACHMENTS_DIR)
            val file = File(dir, fileName)

            if (!file.exists()) {
                return@withContext Result.failure(Exception("File not found"))
            }

            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val encryptedFile = EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            Result.success(encryptedFile.openFileInput())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteAttachment(
        context: Context,
        fileName: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val dir = File(context.filesDir, ATTACHMENTS_DIR)
            val file = File(dir, fileName)
            if (file.exists()) {
                file.delete()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
