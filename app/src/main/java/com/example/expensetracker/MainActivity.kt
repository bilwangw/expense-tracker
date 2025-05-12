/**
 * Bill Wang
 * Expense Tracker – Android Coding Assignment
 *
 * Optional Bonus
 * • Display per-category totals
 * • Ability to delete an expense
 *
 */


package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                NavigationStack()
            }
        }
    }
}


data class Expense(
    var name: String? = null,
    var amount: Int = 0,
    var category: Category = Category.Empty,
)

class ExpenseViewModel : ViewModel() {

    // Expose screen UI state
    private var _uiState = MutableStateFlow(Expense())
    var uiState: StateFlow<Expense> = _uiState.asStateFlow()

    fun updateExpense(name: String, amount: Int, category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                amount = amount,
                category = category,
            )
        }
    }
}

enum class ExpenseScreen() {
    Start,
    AddExpense,
}

@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    var expenses: MutableList<Expense> = ArrayList()

    // Add expenses for testing purposes
    AddExpenses(expenses)
    NavHost(
        navController = navController,
        startDestination = ExpenseScreen.Start.name,
        modifier = Modifier.padding(8.dp)
    ) {
        composable(route = ExpenseScreen.Start.name) {
            ExpenseTracker(navController, expenses)
        }
        composable(route = ExpenseScreen.AddExpense.name) {
            AddExpenseScreen(navController, expenses)
        }
    }
}


// Add some items for debugging purposes
@Composable
fun AddExpenses(expenses: MutableList<Expense>) {
    expenses.add(Expense("bagel",45,Category.Food))
    expenses.add(Expense("pizza",4,Category.Food))
    expenses.add(Expense("soda",5,Category.Food))
    expenses.add(Expense("Bread",35,Category.Food))
    expenses.add(Expense("Apple",25,Category.Food))
    expenses.add(Expense("trip to somewhere",53,Category.Travel))
    expenses.add(Expense("food",354,Category.Food))
    expenses.add(Expense("Electricity",525,Category.Utilities))
    expenses.add(Expense("Item9",44,Category.Food))
    expenses.add(Expense("Water",355,Category.Utilities))
    expenses.add(Expense("Trip to awinagvawfwa",3,Category.Travel))
    expenses.add(Expense("F",34,Category.Food))
    expenses.add(Expense("E",525,Category.Utilities))
    expenses.add(Expense("Item10",44,Category.Food))
    expenses.add(Expense("Item11",35,Category.Utilities))
    expenses.add(Expense("Item12",24,Category.Travel))
    expenses.add(Expense("Item13",33,Category.Utilities))
    expenses.add(Expense("Item14",354,Category.Utilities))
    expenses.add(Expense("Item15",5235,Category.Utilities))
}