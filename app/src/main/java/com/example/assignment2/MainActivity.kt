package com.example.assignment2

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            App()
        }
    }
}

data class Expense(
    val id: Int,
    val title: String,
    val amount: Float,
    val category: String
)
@Composable
fun PieChartComposable(categoryTotals: Map<String, Float>) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(androidx.compose.ui.graphics.Color(0xFF423D4E)),
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isRotationEnabled = true
                legend.isEnabled = true
            }
        },
        update = { pieChart ->

            val entries = categoryTotals.map { (category, total) ->
                PieEntry(total, category)
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.sliceSpace = 3f
            dataSet.valueTextSize = 13f

            dataSet.colors = listOf(
                Color.rgb(244, 67, 54),
                Color.rgb(33, 150, 243),
                Color.rgb(76, 175, 80),
                Color.rgb(255, 193, 7),
                Color.rgb(156, 39, 176)

            )

            val data = PieData(dataSet)
            pieChart.data = data
            pieChart.invalidate()

        }
    )
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(expense.title, fontSize = 16.sp)
                Text(expense.category, fontSize = 12.sp)
            }
            Column {
                Text("€${expense.amount}", fontSize = 16.sp)
                Text("Time", fontSize = 12.sp)
            }

        }
    }
}

sealed class Screen(val route: String) {
    object Dashboard: Screen("dashboard")
    object AllPurchases: Screen("allPurchases")
}


@Composable
fun App(){
    val navController = rememberNavController()

    val sample = listOf(
        Expense(1,"Borger",8.5f,"Food"),
        Expense(2,"Fuel",2.0f,"Transport"),
        Expense(3,"Movies",10.0f,"Entertainment"),
        Expense(4,"Arthur Ryskulov",30.5f,"Transfer"),
        Expense(5,"Soundcloud",10.0f,"Entertainment"),
        Expense(6,"Nothing" ,2.0f , "Other")
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    expenses = sample,
                    navController = navController
                )
            }
            composable(Screen.AllPurchases.route) {
                AllPurchasesScreen(
                    expenses = sample,
                    navController = navController
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(expenses: List<Expense>, navController: NavController) {

    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { group -> group.value.sumOf { it.amount.toDouble() }.toFloat()
        }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFF29252E)),
        containerColor = androidx.compose.ui.graphics.Color(0xFF29252E),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {Text("Dashboard")},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF423D4E),

                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(androidx.compose.ui.graphics.Color(0xFF29252E))
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Monthly Spending",
                    fontSize = 22.sp,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    PieChartComposable(categoryTotals)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Recent Purchases",
                    fontSize = 22.sp,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(expenses.takeLast(5).reversed()) { expense ->
                ExpenseItem(expense)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {navController.navigate(Screen.AllPurchases.route)},
                    ) {Text("Show All")}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPurchasesScreen(expenses: List<Expense>, navController: NavController) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFF29252E)),
        containerColor = androidx.compose.ui.graphics.Color(0xFF29252E),
        topBar = {
            TopAppBar(
                title = {Text("All Purchases")},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(expenses.reversed()) {expenses ->
                ExpenseItem(expenses)
            }
        }
    }
}


