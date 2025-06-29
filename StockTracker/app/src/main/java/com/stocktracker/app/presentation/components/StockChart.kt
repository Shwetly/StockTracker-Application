package com.stocktracker.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stocktracker.app.data.model.ChartDataPoint

@Composable
fun StockChart(
    data: List<ChartDataPoint>,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "$symbol Chart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AndroidView(
                factory = { context ->
                    LineChart(context).apply {
                        description.isEnabled = false
                        setTouchEnabled(true)
                        isDragEnabled = true
                        setScaleEnabled(true)
                        setPinchZoom(true)
                        
                        val entries = data.mapIndexed { index, point ->
                            Entry(index.toFloat(), point.price.toFloat())
                        }
                        
                        val dataSet = LineDataSet(entries, "Price").apply {
                            color = android.graphics.Color.BLUE
                            setCircleColor(android.graphics.Color.BLUE)
                            lineWidth = 2f
                            circleRadius = 3f
                            setDrawCircleHole(false)
                            valueTextSize = 9f
                            setDrawFilled(true)
                            fillAlpha = 65
                            fillColor = android.graphics.Color.BLUE
                        }
                        
                        this.data = LineData(dataSet)
                        invalidate()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}