
package com.example.mymoneynotes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mymoneynotes.ui.theme.MyMoneyNotesTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Brush


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMoneyNotesTheme {
                AppNavigation()
            }
        }
    }
}

enum class TransactionType {
    INCOME, EXPENSE
}

enum class TransactionCategory(val type: TransactionType) {
    SALARY(TransactionType.INCOME),
    BONUS(TransactionType.INCOME),
    GIFT(TransactionType.INCOME),
    INVESTMENT(TransactionType.INCOME),
    OTHER_INCOME(TransactionType.INCOME),

    FOOD(TransactionType.EXPENSE),
    TRANSPORTATION(TransactionType.EXPENSE),
    ENTERTAINMENT(TransactionType.EXPENSE),
    SHOPPING(TransactionType.EXPENSE),
    BILLS(TransactionType.EXPENSE),
    EDUCATION(TransactionType.EXPENSE),
    HEALTH(TransactionType.EXPENSE),
    OTHER_EXPENSE(TransactionType.EXPENSE)
}

enum class SortOption {
    DATE_NEWEST,
    DATE_OLDEST,
    AMOUNT_HIGHEST,
    AMOUNT_LOWEST,
    TYPE
}

enum class FilterOption {
    ALL,
    INCOME_ONLY,
    EXPENSE_ONLY
}

data class Transaction (
    val id: UUID = UUID.randomUUID(),
    val amount: Double,
    val type: TransactionType,
    val category: TransactionCategory,
    val description: String = "",
    val date: Date = Date()
)

@Composable
fun AppNavigation() {
    var isStarted by remember { mutableStateOf(false) }

    if (!isStarted) {
        LandingScreen(onStartClick = {
            isStarted = true
        })
    } else {
        MyMoneyNotes()
    }
}
@Composable
fun LandingScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4DD5FF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🟢 Logo (sementara pakai icon / text)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFF2196F3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💰",
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📝 Nama App
        Text(
            text = "HepengKU",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Kelola keuanganmu dengan mudah",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 🔘 Tombol masuk
        Button(
            onClick = onStartClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Mulai",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoneyNotes() {
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var currentSortOption by remember { mutableStateOf(SortOption.DATE_NEWEST) }
    var currentFilterOption by remember { mutableStateOf(FilterOption.ALL) }
    var showAddTransactionDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showStatistics by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }

    fun sortAndFilterTransaction(): List<Transaction> {
        var result = transactions

        result = when (currentFilterOption) {
            FilterOption.ALL -> result
            FilterOption.INCOME_ONLY -> result.filter { it.type == TransactionType.INCOME }
            FilterOption.EXPENSE_ONLY -> result.filter { it.type == TransactionType.EXPENSE }
        }

        if(searchQuery.isNotEmpty()){
            result = result.filter { transaction ->
                transaction.description.contains(searchQuery, ignoreCase = true) ||
                transaction.category.name.contains(searchQuery, ignoreCase = true)
            }
        }

        result = when (currentSortOption) {
            SortOption.DATE_NEWEST -> result.sortedByDescending { it.date }
            SortOption.DATE_OLDEST -> result.sortedBy { it.date }
            SortOption.AMOUNT_LOWEST -> result.sortedBy { it.amount }
            SortOption.AMOUNT_HIGHEST -> result.sortedByDescending { it.amount }
            SortOption.TYPE -> result.sortedBy { it.type.name }
        }

        return result
    }

    fun addTransaction(transaction: Transaction){
        transactions = transactions + transaction
    }

    fun deleteTransaction(transactionId: UUID){
        transactions = transactions.filter { it.id != transactionId }
    }

    val filteredTransaction = sortAndFilterTransaction()
    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = totalIncome - totalExpense

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "HepengKU",
                            color = Color(0xFF2196F3),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
//                    colors = TopAppBarDefaults.mediumTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primary
//                    ),
                    actions = {
                        IconButton(onClick = { showSearchBar = !showSearchBar }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF2196F3)
                            )
                        }

                        Box {
                            IconButton(onClick = { sortExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Sort",
                                    tint = Color(0xFF2196F3)
                                )
                            }

                            DropdownMenu(
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false }
                            ) {
                                SortOption.values().forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                when (option) {
                                                    SortOption.DATE_NEWEST -> "Tanggal Terbaru"
                                                    SortOption.DATE_OLDEST -> "Tanggal Terlama"
                                                    SortOption.AMOUNT_HIGHEST -> "Nominal Tertinggi"
                                                    SortOption.AMOUNT_LOWEST -> "Nominal Terendah"
                                                    SortOption.TYPE -> "Jenis Transaksi"
                                                }
                                            )
                                        },
                                        onClick = {
                                            currentSortOption = option
                                            sortExpanded = false
                                        }
                                    )
                                }
                            }
                        }


                        IconButton(onClick = { showStatistics = !showStatistics }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = if (showStatistics)
                                    "Show Transactions"
                                else
                                    "Show Statistics",
                                tint = Color(0xFF2196F3)
                            )
                        }
                    }
                )

                AnimatedVisibility(visible = showSearchBar) {
                    Surface(
                        color = Color(0xFF2196F3),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Cari...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            },
                            trailingIcon = {
                                if(searchQuery.isNotEmpty()){
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Search"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTransactionDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Transaksi",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color(0xFF53D3FF))
                .padding(paddingValues)
                .padding(start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                    top = 0.dp)
        ) {
            SummaryCard(totalIncome, totalExpense, balance)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterOption.values().forEach { option ->
                    val isSelected = currentFilterOption == option
                    Button(
                        onClick = { currentFilterOption = option },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF2196F3) else Color.LightGray,
                            contentColor = if (isSelected) Color.White else Color.Black
                        )
                    ) {
                        Text(
                            when (option) {
                                FilterOption.ALL -> "Semua"
                                FilterOption.INCOME_ONLY -> "Pemasukan"
                                FilterOption.EXPENSE_ONLY -> "Pengeluaran"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if(showStatistics){
                StatisticsSection(transactions)
            } else {
                if(filteredTransaction.isEmpty()){
                    EmptyTransactionsMessage()
                } else {
                    TransactionList(
                        transactions = filteredTransaction,
                        onDelete = { deleteTransaction(it) }
                    )
                }
            }
        }

        if(showAddTransactionDialog){
            AddTransactionDialog(
                onAddTransaction = { transaction ->
                    addTransaction(transaction)
                    showAddTransactionDialog = false
                },
                onDismiss = {showAddTransactionDialog = false}
            )
        }
    }
}

@Composable
fun SummaryCard(income: Double, expense: Double, balance: Double) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
                .padding(16.dp)
        ) {

            // 🔝 Title
            Text(
                text = "Ringkasan Keuangan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White

            )

            Spacer(modifier = Modifier.height(16.dp))

            // 💰 Income & Expense Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 🟢 Pemasukan
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Pemasukan",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            numberFormat.format(income),
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // 🔴 Pengeluaran
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Pengeluaran",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            numberFormat.format(expense),
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 💵 Balance Highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (balance >= 0)
                            Color(0xFFE8F5E9)
                        else
                            Color(0xFFFFEBEE),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Saldo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = numberFormat.format(balance),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (balance >= 0) Color(0xFF2E7D32) else Color.Red
                    )
                }
            }
        }
    }
}
@Composable
fun StatisticsSection(transactions: List<Transaction>){
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    if (transactions.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Belum ada data transaksi untuk ditampilkan",
            )
        }

        return
    }

    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ringkasan Transaksi",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FinancialSummaryChart(totalIncome, totalExpense )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Transaksi per Kategori",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Pemasukan",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5B913B),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val incomeByCategory = TransactionCategory.values()
            .filter { it.type == TransactionType.INCOME }
            .map { category ->
                val amount = transactions
                    .filter { it.category == category }
                    .sumOf { it.amount }
                Pair(category, amount)
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }

        if (incomeByCategory.isEmpty()){
            Text("Belum ada pemasukan")
        } else {
            incomeByCategory.forEach { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCategoryName(category.name),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = numberFormat.format(amount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5B913B)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pengeluaran",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val expenseByCategory = TransactionCategory.values()
            .filter { it.type == TransactionType.EXPENSE }
            .map { category ->
                val amount = transactions
                    .filter { it.category == category }
                    .sumOf { it.amount }
                category to amount
            }.filter { it.second > 0 }.sortedByDescending { it.second }

        if (expenseByCategory.isEmpty()){
            Text("Belum ada pemasukan")
        } else {
            expenseByCategory.forEach { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCategoryName(category.name),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = numberFormat.format(amount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun FinancialSummaryChart(
    totalIncome: Double,
    totalExpense: Double
) {
    val total = totalIncome + totalExpense

    val incomePercent = if (total > 0) (totalIncome / total) else 0.0
    val expensePercent = if (total > 0) (totalExpense / total) else 0.0

    // ✅ FIX: pastikan weight tidak pernah 0
    val safeIncome = if (incomePercent > 0) incomePercent.toFloat() else 0.0001f
    val safeExpense = if (expensePercent > 0) expensePercent.toFloat() else 0.0001f

    val numberFormat = NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // 🔝 Label atas (judul + persen)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Pemasukan")
                Text(
                    numberFormat.format(incomePercent),
                    color = Color(0xFF5B913B),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Pengeluaran")
                Text(
                    numberFormat.format(expensePercent),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 📊 BAR UTAMA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(50))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // 🟢 Pemasukan
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(safeIncome)
                        .background(Color(0xFF5B913B))
                )

                // 🔴 Pengeluaran
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(safeExpense)
                        .background(Color.Red)
                )
            }
        }
    }
}

@Composable
fun EmptyTransactionsMessage(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Belum ada transaksi",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tekan tombol + untuk menambahkan transaksi baru",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

fun formatCategoryName(categoryName: String): String {
    return categoryName.replace("_", " ").split(" ").joinToString(" "){ word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
}

//@Composable
//fun SortingAndFilteringSection(
//    currentSortOption: SortOption,
//    currentFilterOption: FilterOption,
//    searchQuery: String,
//    onSortOptionSelected: (SortOption) -> Unit,
//    onFilterOptionSelected: (FilterOption) -> Unit,
//    onSearchQueryChanged: (String) -> Unit,
//    showStatistics: Boolean,
//    onToggleStatistics: (Boolean) -> Unit
//){
//    var sortExpanded by remember { mutableStateOf(false) }
//    var filterExpanded by remember { mutableStateOf(false) }
//
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        color = MaterialTheme.colorScheme.surfaceVariant,
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = onSearchQueryChanged,
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = { Text("Cari...") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search"
//                    )
//                },
//                trailingIcon = {
//                    if(searchQuery.isNotEmpty()){
//                        IconButton(onClick = {onSearchQueryChanged("")}) {
//                            Icon(
//                                imageVector = Icons.Default.Clear,
//                                contentDescription = "Clear Search"
//                            )
//                        }
//                    }
//                },
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {  }
//            }
//        }
//    }
//}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDelete: () -> Unit
){
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateFormatted = dateFormat.format(transaction.date)
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    val isIncome = transaction.type == TransactionType.INCOME
    val amountColor = if (isIncome) Color(0xFF2E7D32) else Color.Red
    val amountPrefix = if (isIncome) "+" else "-"
    val formattedAmount = numberFormat.format(transaction.amount)

    val bgColor = if (isIncome)
        Color(0xFFE8F5E9)
    else
        Color(0xFFFFEBEE)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔵 ICON BULAT
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (isIncome)
                            Color(0xFF2E7D32).copy(alpha = 0.15f)
                        else
                            Color.Red.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.category.name.first().toString(),
                    color = amountColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // 📝 DETAIL
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = formatCategoryName(transaction.category.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (transaction.description.isNotBlank()){
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateFormatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // 💰 NOMINAL + DELETE
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$amountPrefix$formattedAmount",
                    color = amountColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Transaksi",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionList(
    transactions: List<Transaction>,
    onDelete: (UUID) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                onDelete = {onDelete(transaction.id)}
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onAddTransaction: (Transaction) -> Unit,
    onDismiss: () -> Unit
) {
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<TransactionCategory?>(null) }

    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.time) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Tambah Transaksi Baru",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Transaction Type Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            type = TransactionType.EXPENSE
                            selectedCategory = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == TransactionType.EXPENSE)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text("Pengeluaran")
                    }

                    Button(
                        onClick = {
                            type = TransactionType.INCOME
                            selectedCategory = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == TransactionType.INCOME)
                                Color(0xFF5B913B)
                            else
                                Color(0xFF5B913B).copy(alpha = 0.6f)
                        )
                    ) {
                        Text("Pemasukan")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                            amountError = it.isEmpty() || it.toDoubleOrNull() == null || it.toDoubleOrNull() == 0.0
                        }
                    },
                    label = { Text("Nominal*") },
                    isError = amountError,
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("Rp ") },
                    supportingText = {
                        if (amountError) {
                            Text("Nominal harus diisi dengan benar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showCategoryPicker = true },
                    modifier = Modifier.fillMaxWidth()
//                    colors = OutlinedButtonDefaults.outlinedButtonColors(
//                        containerColor = if (categoryError)
//                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
//                        else
//                            MaterialTheme.colorScheme.surface
//                    )
                ) {
                    Text(
                        text = selectedCategory?.let { formatCategoryName(it.name) } ?: "Pilih Kategori*",
                        color = if (selectedCategory == null && categoryError)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Category"
                    )
                }

                if (categoryError) {
                    Text(
                        "Kategori harus dipilih",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi (opsional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Calendar Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Tanggal: ${dateFormatter.format(selectedDate)}",
                    modifier = Modifier.weight(1f)
                )
            }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            amountError = amount.isEmpty() || amount.toDoubleOrNull() == null ||
                                    amount.toDoubleOrNull() == 0.0
                            categoryError = selectedCategory == null

                            if (!amountError && !categoryError) {
                                val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                                onAddTransaction(
                                    Transaction(
                                        amount = parsedAmount,
                                        type = type,
                                        category = selectedCategory!!,
                                        description = description.trim(),
                                        date = selectedDate
                                    )
                                )
                            }
                        }
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = Date(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showCategoryPicker) {
        Dialog(
            onDismissRequest = { showCategoryPicker = false }
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Pilih Kategori",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val categories = TransactionCategory.values().filter { it.type == type }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCategory = category
                                        showCategoryPicker = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (type == TransactionType.INCOME)
                                                Color.Green.copy(alpha = 0.2f)
                                            else
                                                Color.Red.copy(alpha = 0.2f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category.name.first().toString(),
                                        color = if (type == TransactionType.INCOME) Color.Green else Color.Red
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = formatCategoryName(category.name),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            if (category != categories.last()) {
                                Divider(modifier = Modifier.padding(start = 48.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showCategoryPicker = false }) {
                            Text("Batal")
                        }
                    }
                }
            }
        }
    }
}