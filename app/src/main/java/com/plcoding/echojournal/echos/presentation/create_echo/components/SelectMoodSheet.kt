package com.plcoding.echojournal.echos.presentation.create_echo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.plcoding.echojournal.R
import com.plcoding.echojournal.core.presentation.designsystem.buttons.PrimaryButton
import com.plcoding.echojournal.core.presentation.designsystem.buttons.SecondaryButton
import com.plcoding.echojournal.echos.presentation.models.MoodUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMoodSheet(
    selectedMood: MoodUi,
    onMoodClick:(MoodUi)->Unit,
    onDismiss:()-> Unit,
    onConfirmClick:()->Unit,
    modifier: Modifier = Modifier
) {
    val allMoods = MoodUi.entries.toList()
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                allMoods.forEach {
                    mood->
                    MoodItem(
                        selected = mood == selectedMood,
                        moodUi = mood,
                        onClick = {
                            onMoodClick(mood)
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )
                PrimaryButton(
                    text = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.confirm)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun MoodItem(
    selected:Boolean,
    moodUi: MoodUi,
    onClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick=onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            imageVector = if(selected) {
                ImageVector.vectorResource(moodUi.iconSet.fill)
            }
            else {
                ImageVector.vectorResource(moodUi.iconSet.outline)
            },
            contentDescription = moodUi.title.asString(),
            modifier = Modifier.height(40.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            moodUi.title.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = if(selected){
                MaterialTheme.colorScheme.onSurface
            }
            else{
                MaterialTheme.colorScheme.outline
            }
        )
    }
}