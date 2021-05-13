package one.nira.piu.data

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import one.nira.piu.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files


class BackupCSV {
    companion object {
        fun copyFile(ctx: Context) {
            val originDir: String = ctx.getExternalFilesDir(null)!!.absolutePath
            val originFile = File(originDir + File.separator + Const.filename)
            val destFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "PIUManager_Backup.csv")

            if(!originFile.exists()) {
                alertNoOrigin(ctx)
            }
            else {
                if (Build.VERSION.SDK_INT >= 26) {
                    if (destFile.exists()) {
                        destFile.delete()
                    }
                    Files.copy(originFile.toPath(), destFile.toPath())
                    alertComplete(ctx)
                } else {
                    if (!destFile.exists()) {
                        destFile.createNewFile()
                    }

                    FileInputStream(originFile).use { `in` ->
                        FileOutputStream(destFile).use { out ->
                            // Transfer bytes from in to out
                            val buf = ByteArray(1024)
                            var len: Int
                            while (`in`.read(buf).also { len = it } > 0) {
                                out.write(buf, 0, len)
                            }
                        }
                    }
                    alertComplete(ctx)
                }
            }
        }

        fun alertNoOrigin(ctx: Context) {
            val dlg = AlertDialog.Builder(ctx)
            dlg.setTitle(R.string.noorigin_title)
            dlg.setMessage(R.string.noorigin_body)
            dlg.setNeutralButton(R.string.btn_txt_ok,
                DialogInterface.OnClickListener { dlgInterface, i ->
                    dlgInterface.dismiss()
                }
            )
            dlg.create().show()
        }

        fun alertComplete(ctx: Context) {
            val dlg = AlertDialog.Builder(ctx)
            dlg.setTitle(R.string.complete_title)
            dlg.setMessage(R.string.complete_body)
            dlg.setNeutralButton(R.string.btn_txt_ok,
                DialogInterface.OnClickListener { dlgInterface, i ->
                    dlgInterface.dismiss()
                }
            )
            dlg.create().show()
        }

        fun alertPermission(ctx: Context) {
            val dlg = AlertDialog.Builder(ctx)
            dlg.setTitle(R.string.permission_title)
            dlg.setMessage(R.string.permission_body)
            dlg.setNeutralButton(R.string.btn_txt_ok,
                DialogInterface.OnClickListener { dlgInterface, i ->
                    // 권한 요청 수행
                    requestPermissions(
                        ctx as Activity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        0
                    )
                }
            )
            dlg.create().show()
        }
    }
}