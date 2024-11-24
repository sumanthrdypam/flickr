sealed class NavigationRoutes(val route: String) {
    object Home : NavigationRoutes("homeScreen")
    object Detail : NavigationRoutes("detailScreen")
    
    companion object {
        fun getRoute(route: NavigationRoutes) = route.route
    }
} 