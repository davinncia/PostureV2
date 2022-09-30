package com.example.posturev2.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Range
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.posturev2.R
import com.example.posturev2.ui.theme.PostureV2Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: SettingsViewModel by viewModels()

        setContent {
            PostureV2Theme() {
                Scaffold(
                    topBar = { ActionBar() }
                ) {
                    HourRange(viewModel, viewModel.hourRange.collectAsState(initial = 1f..24f).value)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun HourRange(viewModel: SettingsViewModel, initialRange: ClosedFloatingPointRange<Float>) {

        var range by remember { mutableStateOf(initialRange) }

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = range.start.toInt().toString() + stringResource(id = R.string.hour_short),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = range.endInclusive.toInt().toString() + stringResource(id = R.string.hour_short),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            RangeSlider(
                values = range,
                onValueChange = {
                    range = it
                    viewModel.setHourRange(it)
                },
                valueRange = 0f..24f
            )
        }

    }

    @Composable
    private fun ActionBar() {
        TopAppBar(
            title = { Text(text = stringResource(R.string.settings)) },
            elevation = 0.dp
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}