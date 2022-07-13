package com.example.posturev2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.await
import com.example.posturev2.admin.AdminActivity
import com.example.posturev2.notif.NotifWorker
import com.example.posturev2.notif.PostureNotifManager
import com.example.posturev2.ui.theme.PostureV2Theme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NotifSwitch(viewModel.getNotifWorkerState()) { checked ->
                                if (checked) viewModel.scheduleNotif()
                                else viewModel.cancelNotif()
                            }
                            NotifButton() {
                                viewModel.scheduleOneTimeNotif()
                                //viewModel.saveNotifInterval()
                            }
                        }
                    }

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
fun NotifSwitch(checked: Boolean = false, onSwitch: (Boolean) -> Unit = {}) {
    val checkedState = remember { mutableStateOf(checked) }
    Switch(
        checked = checkedState.value,
        onCheckedChange = {
            onSwitch.invoke(it)
            checkedState.value = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.DarkGray,
            uncheckedThumbColor = Color.DarkGray,
            checkedTrackColor = Color.Blue,
            uncheckedTrackColor = Color.Gray,
        ),
        modifier = Modifier.padding(Dp(16f))
    )
}


@Composable
fun NotifButton(onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        Modifier.padding(Dp(16f))
    ) {
        Text("Send notification")
    }
}

@Preview
@Composable
fun MainTabBar(adminClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_title)) },
        actions = {
            IconButton(
                onClick = { adminClick.invoke() },
                modifier = Modifier.padding(8.dp)
           ) {
                Icon(painter = painterResource(id = R.drawable.ic_admin), contentDescription = "")
            }
        }
    )
}