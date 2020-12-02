package com.programmersbox.jetpackcomposetestone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
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
                        CurrentScreen.LIST -> info.value?.let { uiViewer(info = it, title = "Test", appState = appState, data = data) }
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
        uiViewer(info = MSources.NINE_ANIME, title = "Test", appState = AppState(), data = mutableStateOf(null))
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
                }
            }
        ) {
            DrawerLayout(checked, drawerState) {
                Row(Modifier.background(MaterialTheme.colors.background).wrapContentSize().padding(it)) {
                    fun buttonOnClick(items: List<GenericInfo>, title: String): () -> Unit = {
                        if (items.size == 1) {
                            //uiViewer(items.first(), title)
                            info.value = items.first()
                            appState.currentScreen = CurrentScreen.LIST
                        } else {
                            showDialog.value = DialogShowing(true, items, title)
                        }
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
fun uiViewer(info: GenericInfo, title: String, appState: AppState, data: MutableState<GenericInformation?>) /*= Window(
        title = "$title Viewer",
        centered = true
) */ {
    var textValue by mutableStateOf(TextFieldValue(""))
    var page = 1
    var progressAlpha by remember { mutableStateOf(1f) }
    val currentList = mutableStateListOf<GenericData>()//mutableStateOf<MutableList<com.programmersbox.jetpackcomposetestone.GenericData>>(emptyList<com.programmersbox.jetpackcomposetestone.GenericData>().toMutableList())

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
                    OutlinedTextField(value = textValue,
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

                        /*VerticalScrollbar(
                                style = ScrollbarStyleAmbient.current.copy(
                                        hoverColor = theme.value.onBackground,
                                        unhoverColor = theme.value.onBackground.copy(alpha = 0.5f),
                                        hoverDurationMillis = 250
                                ),
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                                adapter = rememberScrollbarAdapter(
                                        scrollState = listState,
                                        itemCount = currentList.size,
                                        averageItemSize = 37.dp
                                )
                        )*/
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var favorite by remember { mutableStateOf(false) }
    JetpackComposeTestOneTheme {
        Scaffold(
            topBar = {
                Row(
                    Modifier.background(MaterialTheme.colors.background).fillMaxWidth().padding(top = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                favorite = !favorite
                            }
                    ) {
                        Image(
                            asset = if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            modifier = Modifier.padding(5.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                        )
                        Text(
                            if (favorite) "Unfavorite" else "Favorite",
                            modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.button
                        )
                    }
                    Image(
                        asset = Icons.Outlined.Settings,
                        modifier = Modifier
                            .clickable { drawerState.open() }
                            .align(Alignment.CenterVertically)
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                }
            }
        ) {
            DrawerLayout(checked, drawerState) {
                Column(Modifier.background(MaterialTheme.colors.background)) {
                    TitleArea(item)
                    ItemRows(item)
                }
            }
        }
    }
    BackButtonHandler {
        appState.currentScreen = CurrentScreen.LIST
    }
}

@Composable
fun TitleArea(item: GenericInformation) = Card(modifier = Modifier.padding(5.dp), border = cardBorder()) {
    Row(modifier = Modifier.padding(5.dp)) {
        GlideImage(
            model = item.imageUrl as Any,
            modifier = Modifier
                .size(WIDTH_DEFAULT.dp, HEIGHT_DEFAULT.dp)
                .padding(top = 5.dp)
        ) { transform(RoundedCorners(30)) }
        Column(modifier = Modifier.padding(5.dp).height(HEIGHT_DEFAULT.dp)) {
            Text(
                item.title,
                style = MaterialTheme
                    .typography
                    .h4
                    .copy(textAlign = TextAlign.Center, fontSize = 20.sp)
            )
            Text(
                item.url,
                modifier = Modifier.clickable {  },
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
fun ItemRows(item: GenericInformation) = Box(
    modifier = Modifier.padding(5.dp),
    alignment = Alignment.TopCenter
) {
    val listState = rememberLazyListState()
    val items = item.rowData()
    val activity = (LifecycleOwnerAmbient.current as? ComponentActivity)
    LazyColumnFor(items, state = listState, modifier = Modifier.fillMaxHeight()) {
        Card(
            modifier = Modifier
                .padding(5.dp)
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
    /*VerticalScrollbar(
            style = ScrollbarStyleAmbient.current.copy(
                    hoverColor = theme.value.onBackground,
                    unhoverColor = theme.value.onBackground.copy(alpha = 0.5f),
                    hoverDurationMillis = 250
            ),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                    scrollState = listState,
                    itemCount = items.size,
                    averageItemSize = 37.dp
            )
    )*/
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
            verticalArrangement = Arrangement.SpaceEvenly
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

@Composable
fun <T> LazyGridFor(
    items: List<T>,
    rowSize: Int = 1,
    modifier: Modifier = Modifier,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val rows = items.chunked(rowSize)
    LazyColumnFor(rows) { row ->
        Row(Modifier.fillParentMaxWidth()) {
            for ((index, item) in row.withIndex()) {
                Box(
                    Modifier.fillMaxWidth(1f / (rowSize - index)).then(modifier),
                    alignment = Alignment.TopCenter
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

//fun imageFromFile(file: File): ImageBitmap = org.jetbrains.skija.Image.makeFromEncoded(file.readBytes()).asImageBitmap()