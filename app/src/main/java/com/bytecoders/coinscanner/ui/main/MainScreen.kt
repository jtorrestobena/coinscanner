import androidx.compose.material.BottomNavigation
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bytecoders.coinscanner.ui.navigation.BottomNavigationView
import com.bytecoders.coinscanner.ui.navigation.NavigationGraph

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationView(navController = navController) }
    ) {

        NavigationGraph(navController = navController)
    }
}