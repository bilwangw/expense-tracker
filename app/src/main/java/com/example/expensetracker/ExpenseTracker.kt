package com.example.expensetracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme


//class ExpenseTracker {
//    ExpenseTracker()
//}

@Composable
fun ExpenseTracker(navController: NavController, expenses: MutableList<Expense>) {
    ExpenseTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.padding(8.dp))

                Text("Expense Tracker", fontSize = 30.sp)

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {navController.navigate(ExpenseScreen.AddExpense.name)}, modifier = Modifier.padding(16.dp)) {
                    Text("Add Expense")
                }

                Text("Expenses", fontSize = 20.sp)

                Spacer(modifier = Modifier.padding(8.dp))

                ExpenseList(navController, expenses)
            }
        }
    }
}

@Composable
fun ExpenseList(navController: NavController, expenses: MutableList<Expense>) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var category by remember {mutableStateOf(Category.Empty)}
    var total by remember {mutableStateOf(0)}

    for (expense in expenses.filter { category == Category.Empty ||
            it.category.equals(category)
    }) {
        total += expense.amount
    }
    Text("Total: $$total")
    total = 0
    Row (modifier = Modifier.fillMaxWidth()) {
        Column (modifier = Modifier.size(width = screenWidth/3, height = 50.dp)) {
            Box {
                TextButton(onClick = {}) {
                    Text("Expense Name", fontSize = 15.sp, color = Color.Blue)
                }
            }
        }
        Column (modifier = Modifier.size(width = screenWidth/3, height = 50.dp)) {
            category = selectCategory()
        }
        Column (modifier = Modifier.size(width = screenWidth/6, height = 50.dp)) {
            Box {
                TextButton(onClick = {}) {
                    Text("Cost", fontSize = 15.sp, color = Color.Blue)
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))

    LazyColumn (modifier = Modifier.fillMaxWidth()) {
        items (
            items = expenses.filter { category == Category.Empty ||
                it.category.equals(category)
            }
        ) {
            expense -> ExpenseRow(navController, expense, expenses)
        }
    }
}

@Composable
fun ExpenseRow(navController: NavController, expense: Expense, expenses: MutableList<Expense>) {
    var context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var openDeleteDialog by remember { mutableStateOf(false) }

    if(openDeleteDialog) {
        DeleteDialog(
            onConfirmation = {
                expenses.remove(expense)
                navController.navigate(ExpenseScreen.Start.name)
            },
            onDismissRequest = { openDeleteDialog = false },
            itemName = expense.name.toString()
        )
    }

    Row (modifier = Modifier.fillMaxWidth()) {
        Column (modifier = Modifier.size(width = screenWidth/3, height = 35.dp)) {
            Text(expense.name.toString(), textAlign = TextAlign.Left)
        }
        Column (modifier = Modifier.size(width = screenWidth/3, height = 35.dp)) {
            Text(expense.category.toString(), textAlign = TextAlign.Center)
        }
        Column (modifier = Modifier.size(width = screenWidth/6, height = 35.dp)) {
            Text("$" + expense.amount.toString(), textAlign = TextAlign.Center)
        }
        Column (modifier = Modifier.size(width = screenWidth/6, height = 35.dp)) {
            TextButton(onClick = {
                openDeleteDialog = true
            }) {
                Text("X", fontSize = 20.sp, color = Color.Red)
            }
        }
    }
}

@Composable
fun selectCategory(): Category {
    var expanded by remember { mutableStateOf(false) }
    var display by remember {mutableStateOf("Category")}
    var category by remember {mutableStateOf(Category.Empty)}
    Box {
        TextButton(onClick = { expanded = !expanded }) {
            Text(display, fontSize = 15.sp, color = Color.Blue)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Food") },
                onClick = {
                    display = "Food"
                    category = Category.Food
                }
            )
            DropdownMenuItem(
                text = { Text("Travel") },
                onClick = {
                    display = "Travel"
                    category = Category.Travel
                }
            )
            DropdownMenuItem(
                text = { Text("Utilities") },
                onClick = {
                    display = "Utilities"
                    category = Category.Utilities
                }
            )
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    display = "All"
                    category = Category.Empty
                }
            )
        }
    }
    return category
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    itemName: String
) {
    AlertDialog(
        title = {
            Text(text = "Delete $itemName?")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        },
    )
}