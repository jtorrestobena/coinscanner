package com.bytecoders.coinscanner.ui.currency

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bytecoders.coinscanner.R
import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.currency.displayTitle
import com.bytecoders.coinscanner.data.coingecko.MarketItem
import com.bytecoders.coinscanner.ui.home.HomeViewModel
import com.bytecoders.coinscanner.ui.navigation.Navigator
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CurrencySelectionScreen(viewModel: HomeViewModel, navigator: Navigator) {
    val state: SearchState = rememberSearchState()

    val coinsItems: LazyPagingItems<MarketItem> = viewModel.markets.collectAsLazyPagingItems()

    BackHandler {
        if (state.focused) {
            state.focused = false
            state.query = state.query.copy(text = "")
        } else {
            navigator.goBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {

        SearchBar(
            query = state.query,
            onQueryChange = { state.query = it },
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            onBack = { state.query = TextFieldValue("") },
            searching = state.searching,
            focused = state.focused,
            modifier = Modifier
        )

        LaunchedEffect(state.query.text) {
            state.searching = true
            state.searchResults = CurrencyManager.getCurrencyList().filter {
                state.query.text.isEmpty() || it.displayTitle().contains(state.query.text, true)
            }
            state.suggestions = CurrencyManager.getCurrencyList().filter { state.query.text.contains(it.currencyCode, ignoreCase = true) }
            state.searching = false
        }

        when (state.searchDisplay) {
            SearchDisplay.InitialResults, SearchDisplay.Results -> {
                val currencies = state.searchResults
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = currencies,
                        key = { it.currencyCode }
                    ) { currency ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.changeCurrency(currency)
                                    coinsItems.refresh()
                                    navigator.goBack()
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                text = currency.displayTitle(),
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                            )
                        }
                    }
                }
            }
            SearchDisplay.NoResults -> {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .wrapContentSize(Alignment.Center),
                    text = stringResource(
                        id = R.string.currency_search_not_found,
                        state.query.text
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                )
            }

            SearchDisplay.Suggestions -> {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .wrapContentSize(Alignment.Center),
                    text = stringResource(
                        id = R.string.did_you_mean,
                        state.suggestions.joinToString(" ")
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}

/**
 * Enum class with different values to set search state based on text, focus, initial state and
 * results from search.
 *
 * **InitialResults** represents the initial state before search is initiated. This represents
 * the whole screen
 *
 */
enum class SearchDisplay {
    InitialResults, Suggestions, Results, NoResults
}

@Stable
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    suggestions: List<Any>,
    searchResults: List<Currency>
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    var suggestions by mutableStateOf(suggestions)
    var searchResults by mutableStateOf(searchResults)

    val searchDisplay: SearchDisplay
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplay.InitialResults
            searchResults.isNotEmpty() -> SearchDisplay.Results
            suggestions.isNotEmpty() -> SearchDisplay.Suggestions
            else -> SearchDisplay.NoResults
        }

    override fun toString(): String {
        return "🚀 State query: $query, focused: $focused, searching: $searching " +
            "suggestions: ${suggestions.size}, " +
            "searchResults: ${searchResults.size}, " +
            " searchDisplay: $searchDisplay"
    }
}

@Composable
fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    suggestions: List<Any> = emptyList(),
    searchResults: List<Currency> = emptyList()
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
            suggestions = suggestions,
            searchResults = searchResults
        )
    }
}

@Composable
private fun SearchHint(modifier: Modifier = Modifier, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)

    ) {
        Text(
            color = textColor,
            text = stringResource(id = R.string.currency_search),
        )
    }
}

const val SEARCH_FIELD_TAG = "SEARCH_FIELD_TAG"
/**
 * This is a stateless TextField for searching with a Hint when query is empty,
 * and clear and loading [IconButton]s to clear query or show progress indicator when
 * a query is in progress.
 */
@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    Surface(
        modifier = modifier
            .then(
                Modifier
                    .height(56.dp)
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = if (!focused) 16.dp else 0.dp,
                        end = 16.dp
                    )
            ),
        color = surfaceColor,
        contentColor = contentColorFor(surfaceColor),
        shape = RoundedCornerShape(percent = 50),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
        ) {

            if (query.text.isEmpty()) {
                SearchHint(
                    modifier.padding(start = 24.dp, end = 8.dp),
                    textColor = contentColorFor(surfaceColor)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        }
                        .focusRequester(focusRequester)
                        .padding(top = 9.dp, bottom = 8.dp, start = 24.dp, end = 8.dp)
                        .testTag(SEARCH_FIELD_TAG),
                    singleLine = true,
                    textStyle = TextStyle(contentColorFor(backgroundColor = surfaceColor)),
                    cursorBrush = SolidColor(contentColorFor(backgroundColor = surfaceColor))
                )

                when {
                    searching -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(36.dp)
                        )
                    }
                    query.text.isNotEmpty() -> {
                        IconButton(onClick = onClearQuery) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = contentColorFor(
                                    backgroundColor = surfaceColor
                                )
                            )
                        }
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = contentColorFor(
                                backgroundColor = surfaceColor
                            ),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    onBack: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = focused) {
            // Back button
            IconButton(
                modifier = Modifier.padding(start = 2.dp),
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBack()
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        }

        SearchTextField(
            query,
            onQueryChange,
            onSearchFocusChange,
            onClearQuery,
            searching,
            focused,
            modifier.weight(1f)
        )
    }
}
