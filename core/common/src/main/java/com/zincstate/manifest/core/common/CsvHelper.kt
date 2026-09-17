package com.zincstate.manifest.core.common

import android.content.Context
import android.net.Uri
import com.zincstate.manifest.core.database.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.UUID

object CsvHelper {

    suspend fun exportTransactions(context: Context, uri: Uri, transactions: List<TransactionEntity>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    // Header
                    writer.write("id,type,amount,categoryId,accountId,date,note,description\n")
                    
                    // Chunked batching to avoid massive memory allocations
                    transactions.chunked(1000).forEach { chunk ->
                        chunk.forEach { t ->
                            val line = "${t.id},${t.type},${t.amount},${t.categoryId},${t.accountId},${t.date},\"${t.note.replace("\"", "\"\"")}\",\"${t.description.replace("\"", "\"\"")}\"\n"
                            writer.write(line)
                        }
                        writer.flush()
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun importTransactions(context: Context, uri: Uri, defaultAccountId: String, defaultCategoryId: String): Result<List<TransactionEntity>> = withContext(Dispatchers.IO) {
        try {
            val transactions = mutableListOf<TransactionEntity>()
            
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    // Skip header
                    reader.readLine()
                    
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        // Simple CSV parse. In a production app, use a proper CSV library or regex to handle commas inside quotes.
                        val tokens = line!!.split(",").map { it.trim().removeSurrounding("\"") }
                        if (tokens.size >= 8) {
                            val t = TransactionEntity(
                                id = UUID.randomUUID().toString(), // Ignore imported ID to avoid conflicts
                                type = tokens[1],
                                amount = tokens[2].toDoubleOrNull() ?: 0.0,
                                categoryId = if (tokens[3].isNotBlank()) tokens[3] else defaultCategoryId,
                                accountId = if (tokens[4].isNotBlank()) tokens[4] else defaultAccountId,
                                date = tokens[5],
                                note = tokens[6],
                                description = tokens[7],
                                createdAt = System.currentTimeMillis()
                            )
                            transactions.add(t)
                        }
                    }
                }
            }
            Result.success(transactions)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
