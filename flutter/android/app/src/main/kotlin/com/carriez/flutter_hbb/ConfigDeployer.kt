package com.carriez.flutter_hbb

import android.content.Context
import android.util.Log
import java.io.File

/**
 * 配置部署工具
 * 在应用首次启动时自动将预配置的TOML文件复制到RustDesk配置目录
 */
object ConfigDeployer {
    private const val TAG = "ConfigDeployer"
    
    // 预配置文件名
    private const val RUSTDESK_TOML = "RustDesk.toml"
    private const val RUSTDESK2_TOML = "RustDesk2.toml"
    
    /**
     * 部署配置文件到目标目录
     * @param context Android上下文
     * @param configDir 配置目录路径（如果为空则使用默认目录）
     */
    fun deployConfigFiles(context: Context, configDir: String) {
        // 如果configDir为空，使用默认的应用数据目录
        val targetDirPath = if (configDir.isNotEmpty()) {
            configDir
        } else {
            // 使用Android默认的应用数据目录
            context.filesDir.absolutePath
        }
        
        val targetDir = File(targetDirPath)
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        
        Log.d(TAG, "Deploying config files to: $targetDirPath")
        
        // 检查是否已经部署过（通过检查标记文件）
        val deployMarker = File(targetDir, ".config_deployed")
        if (deployMarker.exists()) {
            Log.d(TAG, "Config files already deployed, skipping")
            return
        }
        
        try {
            // 从assets目录复制配置文件
            val assets = context.assets
            
            // 复制 RustDesk.toml
            val tomlFile = File(targetDir, RUSTDESK_TOML)
            if (!tomlFile.exists()) {
                assets.open(RUSTDESK_TOML).use { input ->
                    tomlFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d(TAG, "Deployed $RUSTDESK_TOML to $targetDirPath")
            } else {
                Log.d(TAG, "$RUSTDESK_TOML already exists, skipping")
            }
            
            // 复制 RustDesk2.toml
            val toml2File = File(targetDir, RUSTDESK2_TOML)
            if (!toml2File.exists()) {
                assets.open(RUSTDESK2_TOML).use { input ->
                    toml2File.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d(TAG, "Deployed $RUSTDESK2_TOML to $targetDirPath")
            } else {
                Log.d(TAG, "$RUSTDESK2_TOML already exists, skipping")
            }
            
            // 创建部署标记文件
            deployMarker.createNewFile()
            Log.d(TAG, "Config deployment completed")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to deploy config files to $targetDirPath", e)
        }
    }
}
