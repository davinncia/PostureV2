package com.example.posturev2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.posturev2.admin.AdminActivity
import com.example.posturev2.settings.SettingsActivity
import com.example.posturev2.ui.theme.PostureV2Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//todo icon <v24
//todo handle phone turned off
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostureV2Theme {
                Scaffold(
                    topBar = { MainTabBar(
                        adminClick = {
                            startActivity(Intent(this, AdminActivity::class.java))
                        }
                    ) },
                ) {
                    Screen(viewModel = viewModel)
                }
            }

            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    launch {
                        viewModel.score.collect {
                            Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            
        }
    }
}

@Composable
private fun Screen(viewModel: MainViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primary,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NotifSwitch(viewModel.isNotifScheduled.collectAsState()) { checked ->
                if (checked) viewModel.scheduleNotif()
                else viewModel.cancelNotif()
            }
            if (BuildConfig.DEBUG) {
                NotifButton() {
                    viewModel.scheduleOneTimeNotif()
                }
            }
        }
        Score(viewModel.score.collectAsState(initial = ""))
        IntervalTime(intervalStr = viewModel.interval.collectAsState(initial = ""))
    }
}

@Composable
private fun Score(scoreStr: State<String>) {
    Box(modifier = Modifier.background(color = Color.Transparent)) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = scoreStr.value,
            style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        )
    }
}

@Composable
private fun IntervalTime(intervalStr: State<String>) {
    Box(modifier = Modifier.background(color = Color.Transparent)) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            text = intervalStr.value
        )
    }
}

@Composable
fun NotifSwitch(checked: State<Boolean> = mutableStateOf(false), onSwitch: (Boolean) -> Unit = {}) {

    Switch(
        checked = checked.value,
        onCheckedChange = {
            onSwitch.invoke(it)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.DarkGray,
            uncheckedThumbColor = Color.DarkGray,
            checkedTrackColor = Color.Gray,
            uncheckedTrackColor = Color.White
        ),
        modifier = Modifier.padding(Dp(16f))
    )
}


@Composable
fun NotifButton(onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        Modifier.padding(Dp(16f)),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
    ) {
        Text("Send notification")
    }
}

@Preview
@Composable
fun MainTabBar(adminClick: () -> Unit = {}) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_title)) },
        elevation = 0.dp,
        actions = {
            Row(modifier = Modifier.padding(12.dp)) {
                if (BuildConfig.DEBUG) {
                    IconButton(
                        onClick = { adminClick.invoke() }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_admin), contentDescription = "")
                    }
                }

                IconButton(
                    onClick = {
                        context.startActivity(SettingsActivity.newIntent(context))
                    }
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                }
            }
        }
    )
}