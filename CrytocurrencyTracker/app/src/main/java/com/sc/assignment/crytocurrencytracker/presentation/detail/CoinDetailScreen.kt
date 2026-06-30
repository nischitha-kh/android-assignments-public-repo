package com.sc.assignment.crytocurrencytracker.presentation.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sc.assignment.crytocurrencytracker.R
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    navController: NavController,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showPortfolioDialog by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        state.coin?.name ?: stringResource(R.string.coin_detail_default_title),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            state.coin?.let { coin ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = coin.image, contentDescription = coin.name, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = coin.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = coin.symbol.uppercase(), color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "$${String.format(Locale.US, "%.2f", coin.currentPrice)}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${String.format(Locale.US, "%.2f", coin.priceChangePercentage24h)}%",
                        color = if (coin.priceChangePercentage24h >= 0) Color.Green else Color.Red
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    state.chart?.let { chart ->
                        SimpleLineChart(prices = chart.prices.map { it.second }, modifier = Modifier.height(200.dp).fillMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showPortfolioDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(stringResource(R.string.add_to_portfolio_text))
                        }
                        OutlinedButton(
                            onClick = { showAlertDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.set_alert_text))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    MarketStats(coin = coin)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = coin.description.replace(Regex("<[^>]*>"), ""), fontSize = 14.sp)
                }

                if (showPortfolioDialog) {
                    AddPortfolioDialog(
                        onDismiss = { showPortfolioDialog = false },
                        onConfirm = { count, price ->
                            viewModel.addToPortfolio(count, price)
                            showPortfolioDialog = false
                        },
                        currentPrice = coin.currentPrice
                    )
                }

                if (showAlertDialog) {
                    SetAlertDialog(
                        onDismiss = { showAlertDialog = false },
                        onConfirm = { targetPrice, isAbove ->
                            viewModel.setAlert(targetPrice, isAbove)
                            showAlertDialog = false
                        },
                        currentPrice = coin.currentPrice
                    )
                }
            }
        }
    }
}

@Composable
fun AddPortfolioDialog(onDismiss: () -> Unit, onConfirm: (Double, Double) -> Unit, currentPrice: Double) {
    var count by remember { mutableStateOf("") }
    var buyPrice by remember { mutableStateOf(currentPrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_to_portfolio_text)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = count,
                    onValueChange = { count = it },
                    label = { Text(stringResource(R.string.amount_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = buyPrice,
                    onValueChange = { buyPrice = it },
                    label = { Text(stringResource(R.string.buy_price_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { 
                val a = count.toDoubleOrNull() ?: 0.0
                val p = buyPrice.toDoubleOrNull() ?: 0.0
                if (a > 0) onConfirm(a, p)
            }) {
                Text(stringResource(R.string.add_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_button)) }
        }
    )
}

@Composable
fun SetAlertDialog(onDismiss: () -> Unit, onConfirm: (Double, Boolean) -> Unit, currentPrice: Double) {
    var targetPrice by remember { mutableStateOf(currentPrice.toString()) }
    var isAbove by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.set_price_alert_dialog_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = targetPrice,
                    onValueChange = { targetPrice = it },
                    label = { Text(stringResource(R.string.target_price_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isAbove, onClick = { isAbove = true })
                    Text(stringResource(R.string.above_label))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = !isAbove, onClick = { isAbove = false })
                    Text(stringResource(R.string.below_label))
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                val p = targetPrice.toDoubleOrNull() ?: 0.0
                if (p > 0) onConfirm(p, isAbove)
            }) {
                Text(stringResource(R.string.set_alert_confirm_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_button)) }
        }
    )
}

@Composable
fun MarketStats(coin: CoinDetail) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatRow(label = "Market Cap", value = "$${String.format(Locale.US, "%.0f", coin.marketCap)}")
            StatRow(label = "Market Cap Rank", value = "#${coin.marketCapRank}")
            StatRow(label = "24h High", value = "$${String.format(Locale.US, "%.2f", coin.high24h)}")
            StatRow(label = "24h Low", value = "$${String.format(Locale.US, "%.2f", coin.low24h)}")
            StatRow(label = "Circulating Supply", value = String.format(Locale.US, "%.0f", coin.circulatingSupply))
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SimpleLineChart(prices: List<Double>, modifier: Modifier = Modifier) {
    if (prices.isEmpty()) return

    val minPrice = prices.minOrNull() ?: 0.0
    val maxPrice = prices.maxOrNull() ?: 0.0
    val range = maxPrice - minPrice

    Canvas(modifier = modifier) {
        val path = Path()
        val width = size.width
        val height = size.height

        prices.forEachIndexed { index, price ->
            val x = index * (width / (prices.size - 1))
            val y = height - ((price - minPrice) / range * height).toFloat()

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF2196F3),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
