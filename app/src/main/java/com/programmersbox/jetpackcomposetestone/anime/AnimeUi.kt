package com.programmersbox.jetpackcomposetestone.anime

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.halilibo.composevideoplayer.MediaPlaybackControls
import com.halilibo.composevideoplayer.VideoPlayer
import com.halilibo.composevideoplayer.VideoPlayerSource
import com.programmersbox.helpfulutils.enableImmersiveMode
import com.programmersbox.helpfulutils.requestPermissions
import com.programmersbox.helpfulutils.setEnumItems
import com.programmersbox.jetpackcomposetestone.GenericUi
import com.programmersbox.jetpackcomposetestone.RowData
import com.programmersbox.jetpackcomposetestone.ui.JetpackComposeTestOneTheme
import com.programmersbox.loggingutils.Loged
import com.programmersbox.loggingutils.fd
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AnimeUi : GenericUi {
    override fun playUi(activity: ComponentActivity?, list: RowData, title: String) {
        GlobalScope.launch {
            val items = list.getItem()
            Loged.fd(items)
            activity?.runOnUiThread {
                activity.apply {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Download or Stream?")
                        .setEnumItems(items = ShowOption.values().map { it.name }.toTypedArray()) { s: ShowOption, d ->
                            when (s) {
                                ShowOption.DOWNLOAD -> requestPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) {
                                    if (it.isGranted) {
                                        items.firstOrNull()?.let { url ->

                                            fun getNameFromUrl(url: String): String {
                                                return Uri.parse(url).lastPathSegment?.let { if (it.isNotEmpty()) it else null } ?: title
                                            }

                                            val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                                                .toString() + "/ComposeWorld/" + getNameFromUrl(url) + "$title.mp4"

                                            Fetch.getDefaultInstance().enqueue(
                                                request = Request(url, filePath).apply {
                                                    priority = Priority.HIGH
                                                    networkType = NetworkType.ALL
                                                    addHeader("Accept-Language", "en-US,en;q=0.5")
                                                    addHeader(
                                                        "User-Agent",
                                                        "\"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0\""
                                                    )
                                                    addHeader("Accept", "text/html,video/mp4,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                                    addHeader("Access-Control-Allow-Origin", "*")
                                                    addHeader("Referer", "http://thewebsite.com")
                                                    addHeader("Connection", "keep-alive")
                                                }
                                            )
                                        }
                                    }
                                }
                                ShowOption.STREAM -> requestPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) {
                                    if (it.isGranted) {
                                        activity.startActivity(
                                            Intent(activity, ShowActivity::class.java).apply { putExtra("video_url", items.firstOrNull()) }
                                        )
                                    }
                                }
                            }
                            d.dismiss()
                        }
                        .show()
                }
            }

        }
    }
}

enum class ShowOption {
    DOWNLOAD, STREAM
}

class ShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoUrl = intent.getStringExtra("video_url").orEmpty()
        enableImmersiveMode()
        setContent {
            val selectedVideoState by savedInstanceState { videoUrl }
            JetpackComposeTestOneTheme {
                //Text(videoUrl)

                //var selectedVideoState by savedInstanceState { videoUrl }

                //var mediaPlaybackControls by remember { mutableStateOf(MediaPlaybackControls()) }
                //val context = ContextAmbient.current

                /*onActive {
                    (context as? FragmentActivity)?.lifecycle?.addObserver(object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                        fun onPause() {
                             //mediaPlaybackControls.pause()
                        }
                    })
                }*/

                /*mediaPlaybackControls =*/
                PlayVideo(selectedVideoState = selectedVideoState)
            }
        }
    }
}

@Composable
fun PlayVideo(selectedVideoState: String) {
    Box {
        VideoPlayer(
            source = VideoPlayerSource.Network(selectedVideoState),
            backgroundColor = Color.Transparent,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVideo() {
    PlayVideo("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
}