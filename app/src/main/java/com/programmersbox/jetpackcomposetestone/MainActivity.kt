package com.programmersbox.jetpackcomposetestone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animate
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.ExperimentalKeyInput
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.ui.tooling.preview.Preview
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.koduok.compose.glideimage.GlideImage
import com.programmersbox.jetpackcomposetestone.ui.JetpackComposeTestOneTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.programmersbox.jetpackcomposetestone.anime.Sources as ASources
import com.programmersbox.jetpackcomposetestone.manga.Sources as MSources

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalKeyInput
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTestOneTheme {
                val appState = remember { AppState() }
                val info = mutableStateOf<GenericInfo?>(null)
                val data = mutableStateOf<GenericInformation?>(null)
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    //Greeting("Android")
                    //Previewing()
                    when (appState.currentScreen) {
                        CurrentScreen.HOME -> Previewing(appState, info)
                        CurrentScreen.LIST -> info.value?.let { uiViewer(info = it, appState = appState, data = data) }
                        CurrentScreen.INFO -> data.value?.let { InfoLayout(it, appState) }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTestOneTheme {
        Greeting("Android")
    }
}

class AppState {
    var currentScreen by mutableStateOf(CurrentScreen.HOME)
}

enum class CurrentScreen {
    HOME,
    LIST,
    INFO
}

@ExperimentalKeyInput
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun PreviewPreview() {
    JetpackComposeTestOneTheme {
        Previewing(appState = AppState(), info = mutableStateOf(null))
    }
}

@ExperimentalKeyInput
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
fun UIViewPreview() {
    JetpackComposeTestOneTheme {
        uiViewer(info = MSources.NINE_ANIME, appState = AppState(), data = mutableStateOf(null))
    }
}

data class DialogShowing(val show: Boolean = false, val list: List<GenericInfo> = emptyList(), val title: String = "")

val checked = mutableStateOf(false)

@ExperimentalKeyInput
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Previewing(appState: AppState, info: MutableState<GenericInfo?>) {
    //val theme = remember { mutableStateOf(darkColors()) }
    //var checked by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(DialogShowing()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    if (showDialog.value.show) {
        SourceDialog(showDialog)
    }
    MaterialTheme {
        Scaffold(
            topBar = {
                Column(Modifier.background(MaterialTheme.colors.background).fillMaxWidth().padding(top = 5.dp)) {
                    Image(
                        asset = Icons.Outlined.Settings,
                        modifier = Modifier
                            .clickable { drawerState.open() }
                            .align(Alignment.End)
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                }
            },
            bottomBar = {
                Row(Modifier.background(MaterialTheme.colors.background).fillMaxWidth().padding(5.dp)) {
                    Text("By Jacob", textAlign = TextAlign.End, style = MaterialTheme.typography.caption)
                    /*AndroidView(viewBlock = ::DiamondLoader, modifier = Modifier.size(50.dp)) { it.animateTo(100) }
                    var sliderProgress by remember { mutableStateOf(10f) }
                    var loaderProgress by remember { mutableStateOf(50f) }

                    val modelListener by remember {
                        mutableStateOf(DiamondModel(50, null, 10.dp, Color.Red, Color.DarkGray))
                    }

                    Column(Modifier.padding(horizontal = 5.dp)) {

                        DiamondLoaderCompose(
                            model = modelListener,
                            Modifier.size(75.dp).padding(5.dp)
                        )

                        Slider(value = sliderProgress, onValueChange = {
                            sliderProgress = it
                            modelListener.loadingWidth = it.dp
                        }, valueRange = 1f..100f)

                        Slider(value = loaderProgress, onValueChange = {
                            loaderProgress = it
                            modelListener.progress = it.toInt()
                        }, valueRange = 0f..100f)

                        Row {

                            Button(onClick = {
                                modelListener.progressColor = Color(Random.nextColor(255))
                                modelListener.emptyColor = Color(Random.nextColor(255))
                            }) {
                                Text("Random Color")
                            }
                        }
                    }*/
                }
            }
        ) {
            DrawerLayout(checked, drawerState) {
                Row(Modifier.background(MaterialTheme.colors.background).wrapContentSize().padding(it)) {
                    //val activity = (LifecycleOwnerAmbient.current as? ComponentActivity)
                    fun buttonOnClick(items: List<GenericInfo>, title: String): () -> Unit = {
                        if (items.size == 1) {
                            info.value = items.first()
                            appState.currentScreen = CurrentScreen.LIST
                        } else {
                            showDialog.value = DialogShowing(true, items, title)
                        }

                        /*activity?.startActivity(
                            Intent(activity, ShowActivity::class.java).apply { putExtra("video_url", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
                        )*/
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background, shape = RoundedCornerShape(5.dp))
                            .padding(5.dp)
                            .fillMaxHeight()
                            .weight(1f, true),
                        onClick = buttonOnClick(MSources.values().toList(), "Manga"),
                        colors = ButtonConstants.defaultButtonColors(backgroundColor = MaterialTheme.colors.surface)
                    ) { Text("Manga", style = MaterialTheme.typography.h3) }
                    OutlinedButton(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background, shape = RoundedCornerShape(5.dp))
                            .padding(5.dp)
                            .fillMaxHeight()
                            .weight(1f, true),
                        onClick = buttonOnClick(ASources.values().toList(), "Anime"),
                        colors = ButtonConstants.defaultButtonColors(backgroundColor = MaterialTheme.colors.surface)
                    ) { Text("Anime", style = MaterialTheme.typography.h3) }
                }
            }
        }
    }
    val activity = (LifecycleOwnerAmbient.current as? ComponentActivity)
    BackButtonHandler {
        activity?.finish()
    }
}

@ExperimentalMaterialApi
@ExperimentalKeyInput
@ExperimentalFoundationApi
@Composable
fun SourceDialog(showDialog: MutableState<DialogShowing>) =
    Dialog({ showDialog.value = DialogShowing() }) {
        MaterialTheme {
            LazyColumnFor(showDialog.value.list, modifier = Modifier.fillMaxHeight().background(MaterialTheme.colors.background)) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    border = cardBorder(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            //uiViewer(it, showDialog.value.title)
                            showDialog.value = DialogShowing()
                        }
                ) {
                    Text(
                        it.toString(),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme
                            .typography
                            .h6
                            .copy(textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }

@Suppress("RemoveRedundantQualifierName")
@ExperimentalFoundationApi
@ExperimentalKeyInput
@ExperimentalMaterialApi
@Composable
fun uiViewer(info: GenericInfo, appState: AppState, data: MutableState<GenericInformation?>) {
    var textValue by mutableStateOf(TextFieldValue(""))
    var page = 1
    var progressAlpha by remember { mutableStateOf(1f) }
    val currentList = mutableStateListOf<GenericData>()
    onActive {
        GlobalScope.launch {
            //delay(1000)
            //val f = info.getItems(page).toMutableList()
            currentList.addAll(info.getItems(page))// = currentList.apply { addAll(f) }
            //Loged.fa(currentList)
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    MaterialTheme {//info.getItems(page).toMutableList()
        Scaffold(
            topBar = {
                Row(Modifier.background(MaterialTheme.colors.background).fillMaxWidth().padding(top = 5.dp)) {
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { t: TextFieldValue -> textValue = t },
                        trailingIcon = { androidx.compose.material.Icon(Icons.Filled.Search) },
                        label = { Text("${currentList.size} Search") },
                        placeholder = { Text("Search") },
                        modifier = Modifier.padding(16.dp).weight(9f),
                        textStyle = androidx.compose.material.AmbientTextStyle.current
                            .copy(color = MaterialTheme.colors.onBackground).merge(TextStyle()),
                    )
                    Image(
                        asset = Icons.Outlined.Settings,
                        modifier = Modifier
                            .clickable { drawerState.open() }
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                }
            }
        ) {
            DrawerLayout(checked, drawerState) {
                Column(Modifier.background(MaterialTheme.colors.background).padding(5.dp)) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).drawOpacity(progressAlpha))
                    progressAlpha = 0f
                    Box(alignment = Alignment.TopCenter) {
                        val listState = rememberLazyListState()
                        LazyColumnForIndexed(
                            currentList.filter { it.title.contains(textValue.text, true) },
                            state = listState,
                            modifier = Modifier.fillMaxHeight()
                        ) { index, item ->
                            if (currentList.lastIndex == index && textValue.text.isEmpty()) {
                                onActive {
                                    //fetch more items here
                                    progressAlpha = 1f
                                    GlobalScope.launch {
                                        //currentList = currentList.apply { addAll(info.getItems(++page)) }
                                        currentList.addAll(info.getItems(++page))
                                    }
                                    println("New items - $page - ${currentList.size}")
                                    progressAlpha = 0f
                                }
                            }
                            RowItem(item, appState, data)
                            Divider()
                        }
                    }
                }
            }
        }

    }

    BackButtonHandler {
        appState.currentScreen = CurrentScreen.HOME
    }

    /*GlobalScope.launch {
        currentList = currentList.apply { addAll(info.getItems(page).toMutableList()) }
    }*/
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RowItem(item: GenericData, appState: AppState, data: MutableState<GenericInformation?>) = Card(
    shape = RoundedCornerShape(4.dp),
    border = cardBorder(),
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            GlobalScope.launch {
                item.listOfData
                    ?.let {
                        data.value = it
                        appState.currentScreen = CurrentScreen.INFO
                    }
            }
        }
) {
    Text(
        item.title,
        style = MaterialTheme
            .typography
            .h6
            .copy(textAlign = TextAlign.Center),
        modifier = Modifier.padding(16.dp)
    )
}

const val WIDTH_DEFAULT = 360 / 2
const val HEIGHT_DEFAULT = 480 / 2

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun InfoLayout(item: GenericInformation, appState: AppState) {
    var favorite by remember { mutableStateOf(false) }
    val moreInfo = remember { mutableStateOf(false) }
    JetpackComposeTestOneTheme {
        DrawerLayout(checked) {
            Scaffold(
                topBar = {
                    Row(
                        Modifier.background(MaterialTheme.colors.background).fillMaxWidth().padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable { favorite = !favorite }
                        ) {
                            Image(
                                asset = if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                modifier = Modifier.padding(5.dp),
                                colorFilter = ColorFilter.tint(animate(if(favorite) Color.Red else MaterialTheme.colors.onBackground))
                            )
                            Text(
                                if (favorite) "Unfavorite" else "Favorite",
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                },
                bottomBar = {
                    OutlinedButton(
                        onClick = { moreInfo.value = !moreInfo.value },
                        border = cardBorder(),
                        modifier = Modifier.fillMaxWidth().padding(5.dp)
                    ) { Text("More Info", color = MaterialTheme.colors.onBackground) }
                }
            ) {
                Column(Modifier.background(MaterialTheme.colors.background).padding(it)) {
                    TitleArea(
                        item,
                        Modifier.animateContentSize(),
                        moreInfo = moreInfo
                    )
                    ItemRows(item, Modifier.weight(animate(target = if (moreInfo.value) 0.0001f else 5f)))

                }
            }
        }
    }
    BackButtonHandler {
        appState.currentScreen = CurrentScreen.LIST
    }
}

@Composable
fun TitleArea(item: GenericInformation, modifier: Modifier = Modifier, moreInfo: MutableState<Boolean>) =
    Card(modifier = Modifier.padding(5.dp).then(modifier), border = cardBorder()) {
        Row(modifier = Modifier.padding(5.dp)) {
            GlideImage(
                model = item.imageUrl as Any,
                modifier = Modifier
                    .size(WIDTH_DEFAULT.dp, HEIGHT_DEFAULT.dp)
                    .padding(top = 5.dp)
            ) { transform(RoundedCorners(30)) }
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .let { if (moreInfo.value) it.fillMaxHeight() else it.height(HEIGHT_DEFAULT.dp) }
            ) {
                Text(
                    item.title,
                    style = MaterialTheme
                        .typography
                        .h4
                        .copy(textAlign = TextAlign.Center, fontSize = 20.sp)
                )
                Text(
                    item.url,
                    modifier = Modifier.clickable { },
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme
                        .typography
                        .caption
                        .copy(textAlign = TextAlign.Center, color = Color.Cyan)
                )
                LazyRowFor(item.genres, modifier = Modifier.padding(5.dp)) {
                    Text(it, modifier = Modifier.padding(5.dp), style = MaterialTheme.typography.subtitle2)
                }
                ScrollableColumn { Text(item.description.orEmpty(), style = MaterialTheme.typography.body1) }
            }
        }
    }

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ItemRows(item: GenericInformation, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val items = item.rowData()
    val activity = (LifecycleOwnerAmbient.current as? ComponentActivity)
    LazyColumnFor(
        items,
        state = listState,
        modifier = Modifier.padding(5.dp).then(modifier)
    ) {
        Card(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .clickable { item.playUi(activity, it, it.name) },
            border = cardBorder()
        ) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(it.name)
                Text(it.uploaded, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
fun cardBorder() = BorderStroke(1.dp, MaterialTheme.colors.onBackground)

@Composable
fun DrawerLayout(
    checked: MutableState<Boolean>,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    otherSettings: @Composable ColumnScope.() -> Unit = {},
    bodyContent: @Composable () -> Unit
) = ModalDrawerLayout(
    drawerState = drawerState,
    drawerContent = {
        ScrollableColumn(
            Modifier.background(MaterialTheme.colors.background).padding(5.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp)//SpaceEvenly
        ) {
            ThemeSettingRow(checked)
            Divider()
            DarkToggleButton(modifier = Modifier.align(Alignment.End).wrapContentWidth().size(30.dp))
            Divider()
            otherSettings()
        }
    },
    bodyContent = bodyContent
)

@Composable
fun ThemeSettingRow(checked: MutableState<Boolean>) =
    Row(Modifier.background(MaterialTheme.colors.background).wrapContentWidth().padding(5.dp)) {
        //Spacer(Modifier.weight(8f))
        Text(
            if (checked.value) "Light" else "Dark",
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.End,
            style = MaterialTheme
                .typography
                .h6
        )
        Switch(
            checked.value,
            onCheckedChange = {
                //theme.value = if (it) lightColors() else darkColors()
                checked.value = it
            },
            modifier = Modifier.padding(horizontal = 5.dp).align(Alignment.CenterVertically).weight(1f)
        )
    }

//fun imageFromFile(file: File): ImageBitmap = org.jetbrains.skija.Image.makeFromEncoded(file.readBytes()).asImageBitmap()