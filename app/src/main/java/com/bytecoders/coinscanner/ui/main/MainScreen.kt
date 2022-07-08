import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bytecoders.coinscanner.ui.navigation.BottomNavigationView
import com.bytecoders.coinscanner.ui.navigation.NavigationGraph
import com.bytecoders.coinscanner.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    AppTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationView(navController = navController) }
        ) { paddingValues ->
            NavigationGraph(
                navController = navController,
                bottomPadding = paddingValues.calculateBottomPadding()
            )
        }
    }
}
