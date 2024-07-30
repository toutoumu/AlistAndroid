package com.github.jing332.alistandroid.ui.nav.web

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.jing332.alistandroid.BuildConfig
import com.github.jing332.alistandroid.R
import com.github.jing332.alistandroid.model.alist.AListConfigManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(stringResource(R.string.app_name))
                        Text(" - " + BuildConfig.ALIST_VERSION)
                    }
                },
            )
        }
    ) {
        Column(modifier = modifier.padding(it)) {
            val url = remember { "http://localhost:${AListConfigManager.config().scheme.httpPort}" }
            WebView(url = url)

        }
    }
}