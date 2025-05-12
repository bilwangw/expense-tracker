package com.example.expensetracker

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController


class AddExpense {
}

@Composable
fun AddExpenseScreen(navController: NavController, expenses: MutableList<Expense>) {
    var context = LocalContext.current
    var expenseName by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.Empty) }
    var output by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp, 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = stringResource(id = R.string.add_expense),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = expenseName,
                onValueChange = { expenseName = it },
                placeholder = { Text(text = "Expense Name") },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = cost,
                onValueChange = { if (it.isDigitsOnly()) cost = it },
                placeholder = { Text(text = "Cost") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            category = categoryDropdown()

            Button (onClick = {
                // Error handling
                output = ""
                if (expenseName.isEmpty()) {
                    output += "Please enter an expense name\n"
                }
                if (!cost.all{char -> char.isDigit()} || cost.isEmpty()) {
                    output += "Please enter a number\n"
                }
                if(category.equals(Category.Empty)) {
                    output += "Please select a category\n"
                }

                if (output.isEmpty()) {
                    expenses.add(Expense(expenseName, cost.toInt(), category))
                    Toast.makeText(
                        context,
                        "Success!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(ExpenseScreen.AddExpense.name)
                }
                else {
                    Toast.makeText(
                        context,
                        output,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }) {
                Text("Submit")
            }

            Button(onClick = {navController.navigate(ExpenseScreen.Start.name)}) {
                Text("Return")
            }
        }
    }
}

@Composable
fun categoryDropdown(): Category {
    var expanded by remember { mutableStateOf(false) }
    var display by remember {mutableStateOf("Select a Category")}
    var category by remember {mutableStateOf(Category.Empty)}
    Box (modifier = Modifier.padding(16.dp)) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(display)
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
        }
    }
    return category
}


enum class Category {
    Empty,
    Food,
    Travel,
    Utilities
}

fun getRecurrenceList(): List<Category> {
    val recurrenceList = mutableListOf<Category>()
    recurrenceList.add(Category.Food)
    recurrenceList.add(Category.Travel)
    recurrenceList.add(Category.Utilities)

    return recurrenceList
}

@Composable
fun verifyInput(expenseName: String, cost: String, category: Category): String {
    var output by remember {mutableStateOf("")}

    if (expenseName.isEmpty()) {
        output += "Please enter an expense name\n"
    }
    if (!cost.all{char -> char.isDigit()} || cost.isEmpty()) {
        output += "Please enter a number\n"
    }
    if(category.equals(Category.Empty)) {
        output += "Please select a category\n"
    }

    return output
}