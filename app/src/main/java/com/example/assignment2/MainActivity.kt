package com.example.assignment2

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sample = listOf(
            Expense(1,"Borger",8.5f,"Food"),
            Expense(2,"Fuel",2.0f,"Transport"),
            Expense(3,"Movies",10.0f,"Entertainment"),
            Expense(4,"Arthur Ryskulov",30.5f,"Transfer"),
            Expense(3,"Soundcloud",10.0f,"Entertainment"),
        )
        setContent {
            DashboardScreen(sample)
        }
    }
}

data class Expense(
    val id: Int,
    val title: String,
    val amount: Float,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(expenses: List<Expense>) {

    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { group -> group.value.sumOf { it.amount.toDouble() }.toFloat()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Dashboard",
                    fontSize = 20.sp,
                    color = androidx.compose.ui.graphics.Color.Black
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFFE2DEE7),
                titleContentColor = androidx.compose.ui.graphics.Color.White
            )
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        Text(
            modifier = Modifier
            .padding(8.dp),
            text = "Monthly Spending",
            fontSize = 22.sp,
            color = androidx.compose.ui.graphics.Color.Black
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            PieChartComposable(categoryTotals)
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Recent Purchases",
            fontSize = 22.sp,
            color = androidx.compose.ui.graphics.Color.Black
        )
        Spacer(
            modifier = Modifier
            .height(10.dp)
        )
        LazyColumn (
            modifier = Modifier
                .padding(16.dp),
        ){
            items(expenses) { expense ->
                ExpenseItem(expense)
            }
        }
    }
}

@Composable
fun PieChartComposable(categoryTotals: Map<String, Float>) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
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
        elevation = CardDefaults.cardElevation(2.dp),
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
                Text(expense.category, fontSize = 14.sp)
            }

            Text("€${expense.amount}", fontSize = 16.sp)
        }
    }
}
