package com.programmersbox.jetpackcomposetestone.manga

import com.programmersbox.jetpackcomposetestone.GenericUi
import com.programmersbox.jetpackcomposetestone.RowData
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.koduok.compose.glideimage.GlideImage
import com.programmersbox.gsonutils.getObjectExtra
import com.programmersbox.gsonutils.putExtra
import com.programmersbox.jetpackcomposetestone.ui.JetpackComposeTestOneTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MangaUi : GenericUi {
    override fun playUi(activity: ComponentActivity?, list: RowData, title: String) {
        GlobalScope.launch {
            val items = list.getItem()
            activity?.runOnUiThread {
                activity.startActivity(Intent(activity, MangaActivity::class.java).apply { putExtra("pages", items) })
            }
        }
    }
}

class MangaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = intent.getObjectExtra<List<String>>("pages").orEmpty()
        setContent {
            JetpackComposeTestOneTheme {
                LazyColumnFor(items = list) {
                    GlideImage(model = it as Any, modifier = Modifier.padding(vertical = 5.dp))
                }
            }
        }
    }
}
