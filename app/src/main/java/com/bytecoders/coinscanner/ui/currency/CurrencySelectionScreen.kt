package com.bytecoders.coinscanner.ui.currency

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bytecoders.coinscanner.currency.CurrencyManager
import com.bytecoders.coinscanner.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionScreen(viewModel: HomeViewModel, navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        CurrencyManager.getCurrencyList().forEach { currency ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ClickableText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = AnnotatedString("${currency.symbol} ${currency.displayName}"),
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                        onClick = {
                            viewModel.changeCurrency(currency)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
