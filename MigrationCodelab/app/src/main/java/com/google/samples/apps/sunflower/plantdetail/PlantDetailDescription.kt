/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    val plant by plantDetailViewModel.plant.observeAsState()
    plant?.let { PlantDetailContent(it) }
}

@Composable
fun PlantDetailContent(plant:Plant){
    Surface {
        Column(Modifier.padding(dimensionResource(id = R.dimen.margin_normal))) {
            PlantName(name = plant.name)
            PlantWatering(wateringInterval = plant.wateringInterval)
            PlantDescription(description = plant.description)
        }
    }

}

@Composable
fun PlantName(name:String){
    Text(
    text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun PlantWatering(wateringInterval: Int){
    Column(Modifier.fillMaxWidth()) {
        val centerWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)
        val normalPadding = dimensionResource(id = R.dimen.margin_normal)

        Text(
            text = stringResource(id = R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = centerWithPaddingModifier.padding(top = normalPadding)
        )
        val wateringIntervalText = pluralStringResource(
            R.plurals.watering_needs_suffix,wateringInterval,wateringInterval
        )
        Text(
            text = wateringIntervalText,
            modifier = centerWithPaddingModifier.padding(bottom = normalPadding)
        )
    }
}

@Composable
fun PlantDescription(description:String){
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description,HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(
        factory = {context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            it.text = htmlDescription
        }
    )
}
@Composable
fun SunflowerTheme(
    darkTheme:Boolean = isSystemInDarkTheme(),
    content: @Composable () ->Unit
){
    val lightColors  = lightColors(
        primary = colorResource(id = R.color.sunflower_green_500),
        primaryVariant = colorResource(id = R.color.sunflower_green_700),
        secondary = colorResource(id = R.color.sunflower_yellow_500),
        background = colorResource(id = R.color.sunflower_green_500),
        onSecondary = colorResource(id = R.color.sunflower_black),
        onPrimary = colorResource(id = R.color.sunflower_black),
    )
    val darkColors  = darkColors(
        primary = colorResource(id = R.color.sunflower_green_100),
        primaryVariant = colorResource(id = R.color.sunflower_green_200),
        secondary = colorResource(id = R.color.sunflower_yellow_300),
        onPrimary = colorResource(id = R.color.sunflower_black),
        onSecondary = colorResource(id = R.color.sunflower_black),
        onBackground = colorResource(id = R.color.sunflower_black),
        surface = colorResource(id = R.color.sunflower_green_100_8pc_over_surface),
        onSurface = colorResource(id = R.color.sunflower_white),
    )
    val colors = if (darkTheme) darkColors else lightColors
    MaterialTheme(
        content = content,
        colors = colors
    )
}


@Preview
@Composable
fun Preview(){
    PlantName(name = "Apple")
}
@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "description", 3, 30, "")
    SunflowerTheme {
        PlantDetailContent(plant)
    }
}

@Preview
@Composable
private fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(7)
    }
}

@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}