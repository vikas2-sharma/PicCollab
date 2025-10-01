package com.app.open.piccollab.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.open.piccollab.R
import com.app.open.piccollab.core.db.room.entities.EventFolder
import com.app.open.piccollab.core.models.event.NewEventItem
import com.app.open.piccollab.presentation.route.Home
import com.app.open.piccollab.presentation.route.Profile
import com.app.open.piccollab.presentation.route.Search

@Composable
fun BottomNavigation(
    selectedRoute: String,
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == Home::class.qualifiedName,
            onClick = { navigateToHome() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },

            )
        NavigationBarItem(
            selected = selectedRoute == Search::class.qualifiedName,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Home"
                )
            },

            )
        NavigationBarItem(
            selected = selectedRoute == Profile::class.qualifiedName,
            onClick = { navigateToProfile() },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Home"
                )
            },

            )
        NavigationBarItem(
            selected = selectedRoute == Search::class.qualifiedName,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Home"
                )
            },

            )

    }
}

@Composable
fun ButtonWithText(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(20.dp, 5.dp)
    ) {

        Text(
            text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp, 0.dp)
        )
    }
}


@Composable
fun EventFolderCard(
    eventItem: EventFolder,
    onRenameClick: (EventFolder) -> Unit,
    onDeleteClick: (EventFolder) -> Unit,
    onClick: (EventFolder) -> Unit,
) {

    var dropDownMenuOpen by remember { mutableStateOf(false) }

    Card(
        onClick = { onClick(eventItem) },
        modifier = Modifier
            .aspectRatio(1.5f)
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = eventItem.folderName,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        IconButton(onClick = { dropDownMenuOpen = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "options",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        DropdownMenu(
                            expanded = dropDownMenuOpen,
                            onDismissRequest = { dropDownMenuOpen = false }
                        ) {

                            DropdownMenuItem(
                                text = { Text("Rename") },
                                onClick = { onRenameClick(eventItem) }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = { onDeleteClick(eventItem) }
                            )

                        }
                    }
                }
                Icon(
                    painterResource(R.drawable.ic_folder_24),
                    contentDescription = "options",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}


@Composable
fun CreateNewEventCard(onCancel: () -> Unit, onSubmit: (newEventItem: NewEventItem) -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var isEventNameError by remember { mutableStateOf(false) }

    fun validateAndSubmit() {
        if (eventName.isBlank()) {
            isEventNameError = true
        } else {
            isEventNameError = false
            onSubmit(
                NewEventItem(
                    eventName = eventName,
                    eventDescription = eventDescription
                )
            )
        }
    }

    Dialog(onDismissRequest = onCancel) {

        Card {
            Box(
                modifier = Modifier
                    .padding(32.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Create New Event",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onCancel) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close dialog"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { newValue: String ->
                            isEventNameError = false
                            eventName = newValue
                        },
                        placeholder = {
                            Text(text = "Enter Event Name")
                        },
                        label = {
                            Text(text = "Event Name")
                        },
                        maxLines = 1,
                        isError = isEventNameError,
                        supportingText = {
                            if (isEventNameError) {
                                Text("event name can not be empty")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = eventDescription,
                        onValueChange = { newValue: String -> eventDescription = newValue },
                        placeholder = {
                            Text(text = "Enter Event Description")
                        },
                        label = {
                            Text(text = "Event Description")
                        },
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = {

                            validateAndSubmit()
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = {
                            eventName = ""
                            eventDescription = ""
                            isEventNameError = false
                            onCancel()
                        }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            validateAndSubmit()
                        }) {
                            Text("Create")
                        }
                    }

                }
            }
        }
    }
}

/*TODO: to be improved with inline progress*/
@Composable
fun ProgressDialog(progressMessage: String) {
    Dialog(onDismissRequest = {}) {
        Card {
            Box(
                modifier = Modifier
                    .padding(24.dp, 32.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = progressMessage)
                }
            }
        }

    }
}

@Preview()
@Composable
fun ComponentPreview() {
    Column {

        Spacer(Modifier.height(30.dp))
        CreateNewEventCard(
            onCancel = {},
            onSubmit = {}
        )
    }
}
