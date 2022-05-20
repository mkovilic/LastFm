package com.example.songkickprojektanz.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.example.songkickprojektanz.Constants.COLLAPSE_ANIMATION_DURATION
import com.example.songkickprojektanz.Constants.EXPAND_ANIMATION_DURATION
import com.example.songkickprojektanz.Constants.FADE_IN_ANIMATION_DURATION
import com.example.songkickprojektanz.Constants.FADE_OUT_ANIMATION_DURATION
import com.example.songkickprojektanz.R
import com.example.songkickprojektanz.model.Artist
import com.example.songkickprojektanz.navigation.RootScreen
import com.example.songkickprojektanz.paging.Resource
import com.example.songkickprojektanz.remote.responses.TopAlbumResponse
import com.example.songkickprojektanz.ui.theme.Black_light
import com.example.songkickprojektanz.ui.theme.Grey_light
import com.example.songkickprojektanz.ui.theme.White
import com.example.songkickprojektanz.utils.fonts
import com.example.songkickprojektanz.widgets.CustomDialogScrollable
import com.example.songkickprojektanz.widgets.FavoriteButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/*
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val popularMovies = viewModel.trendingMoviesDay.collectAsLazyPagingItems()

    val pagerStateFirstTab = rememberPagerState(initialPage = 0)
    val listSecondTab = listOf("Movies", "Tv")

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight()
            .padding(top = 20.dp),

    )
    {
        val textChipRememberOneState = remember {
            mutableStateOf(false)
        }

        Tabs(pagerState = pagerStateFirstTab, listSecondTab)
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 25.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            // creates a custom chip for active state
            CustomChip(
                selected = textChipRememberOneState.value,
                text = "Location",
                onChecked = {
                    textChipRememberOneState.value = it
                }
            )
            // Creates a custom chip for inactive state
            CustomChip(
                selected = false,
                text = "Dates",
                onChecked = {
                    textChipRememberOneState.value = it
                }
            )
        }
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            // creates a custom chip for active state
            Text(
                text = "March",
                color = Grey_light,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp),
                fontFamily = fonts,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "16 Concerts",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp),
                fontFamily = fonts,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
        }

            TabsContent(pagerState = pagerStateFirstTab, listSecondTab.size,
                popularMovies)

        Spacer(modifier = Modifier.padding(10.dp))


    }
}
*/

@ExperimentalCoroutinesApi
@Composable
fun HomeScreen(
    navController: NavController,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()
    val topArtists = viewModel.topArtists.collectAsLazyPagingItems()

    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.black_light
            )
        )
    ) {
        LazyColumn {
            items(topArtists) { item ->
                ExpandableCard(
                    card = item!!,
                    onCardArrowClick = { viewModel.onCardArrowClicked(item.listeners.toInt()) },
                    expanded = expandedCardIds.value.contains(item.listeners.toInt()),
                    navController,
                    artistName = item.name
                )
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, count: Int, list: LazyPagingItems<Artist>) {

    HorizontalPager(count, state = pagerState, verticalAlignment = Alignment.Top) { page ->
        when (page) {
            0 -> RowSectionItem(list)
            1 -> RowSectionItem(list)
            2 -> RowSectionItem(list)
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, list: List<String>) {

    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.Divider(
                thickness = 3.dp,
                color = Color.Transparent
            )
        },
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .wrapContentWidth(),
                height = 0.dp,
                color = Color.Transparent

            )

        },

        modifier = Modifier.padding(start = 10.dp)
    ) {
        list.forEachIndexed { index, _ ->

            Tab(
                text = {
                    Text(
                        text = list[index],
                        textAlign = TextAlign.Start,
                        color = if (pagerState.currentPage == index) Color(0xFFDBD9D9) else Color(
                            0xFF636366
                        ),
                        fontSize = 32.sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Bold
                    )

                },
                modifier = Modifier
                    .wrapContentWidth(),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }

    }
}


@ExperimentalPagerApi
@Composable
fun RowSectionItem(
    list: LazyPagingItems<Artist>
) {
    Column(

    ) {
        LazyColumn(
            // content padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            items(
                items = list
            ) { item ->

                item?.let { RowItem(moviesData = it) }

            }
        }
    }
}

@Composable
fun RowItem(
    moviesData: Artist
) {
    System.out.println(moviesData.name)
    val showDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .size(width = 250.dp, height = 150.dp)
            .padding(horizontal = 5.dp, vertical = 5.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(250.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = coil.request.ImageRequest.Builder(context = LocalContext.current)
                        .crossfade(true)
                        .data("https://lastfm.freetls.fastly.net/i/u/300x300/3b54885952161aaea4ce2965b2db1638.png")
                        .build(),
                    filterQuality = FilterQuality.High,
                    contentScale = ContentScale.FillBounds

                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(6.dp))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = true, color = Color.Black),
                        onClick = {
                            showDialog.value = true
                        }
                    ),
            )
            if (showDialog.value) {
                //AppDialog(dialogState = true, modifier = Modifier.fillMaxSize(.8f))
                CustomDialogScrollable(
                    onDismiss = { showDialog.value = false },
                    onConfirmClicked = { showDialog.value = false })


            }
            FavoriteButton(modifier = Modifier.padding(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            )
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    card: Artist,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    navController: NavController,
    artistName: String
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
        Black_light
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 48.dp else 24.dp
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 0.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }
    val viewModel: HomeViewModel = hiltViewModel()
    val topAlbums = produceState<Resource<TopAlbumResponse>>(initialValue = Resource.Loading()) {
        value = viewModel.initArtistsTopAlbums(card.name)
    }.value

    Card(
        backgroundColor = cardBgColor,
        contentColor = colorResource(id = R.color.accent_red),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column(Modifier.wrapContentHeight()) {

            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = card.name)
            }
            topAlbums.data?.topAlbums?.album?.take(5)?.forEach {

                ExpandableContent(
                    visible = expanded,
                    albumName = it.name,
                    albumCoverArt = it.image[0].photoUrl,
                    navController = navController,
                    artistName = artistName
                )

            }


        }

    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ExpandableContent(
    visible: Boolean = true,
    albumName: String,
    albumCoverArt: String,
    navController: NavController,
    artistName: String

) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            //Spacer(modifier = Modifier.heightIn(100.dp))
            Row(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(bounded = true, color = Color.Black),
                    onClick = {
                        navController.navigate("${RootScreen.AlbumInfo.route}/${artistName}/${albumName}")
                    }
                ),
            ) {
                Box(modifier = Modifier.wrapContentHeight()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = coil.request.ImageRequest.Builder(context = LocalContext.current)
                                .crossfade(true)
                                .data(albumCoverArt)
                                .build(),
                            filterQuality = FilterQuality.High,
                            contentScale = ContentScale.FillBounds

                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(shape = RoundedCornerShape(6.dp))
                    )
                }
                Text(
                    text = albumName,
                    textAlign = TextAlign.Center,
                    color = White,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
            Divider(color = Grey_light, thickness = 1.dp)
        }
    }
}
