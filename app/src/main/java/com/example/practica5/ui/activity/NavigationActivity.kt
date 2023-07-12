package com.example.practica5.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.practica5.R
import com.example.practica5.common.util.MonumentsConstant.FAVORITES_TITLE
import com.example.practica5.common.util.MonumentsConstant.MONUMENTS_TITLE
import com.example.practica5.common.util.MonumentsConstant.MY_MONUMENTS_TITLE
import com.example.practica5.home.R as RHome
import com.example.practica5.databinding.ActivityNavigationBinding
import com.example.practica5.commonfeatures.util.ToolbarVisibilityListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity(), ToolbarVisibilityListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityNavigationBinding by lazy { ActivityNavigationBinding.inflate(layoutInflater) }

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        toolbar = binding.appBarNavigation.toolbar
        toolbar.title = MONUMENTS_TITLE
        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, binding.appBarNavigation.toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                RHome.id.monumentsFragment,
                com.example.practica5.navigation.R.id.nav_graph__favorites_feature,
                com.example.practica5.navigation.R.id.nav_graph__my_monuments_feature,
                com.example.practica5.navigation.R.id.nav_graph__map_feature
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(GravityCompat.START)

            when (menuItem.itemId) {
                R.id.nav_monuments -> {
                    navController.navigate(RHome.id.monumentsFragment)
                    binding.appBarNavigation.toolbar.title = MONUMENTS_TITLE
                    true
                }

                R.id.nav_favorites -> {
                    navController.navigate(com.example.practica5.navigation.R.id.nav_graph__favorites_feature)
                    binding.appBarNavigation.toolbar.title = FAVORITES_TITLE
                    true
                }

                R.id.nav_my_monuments -> {
                    navController.navigate(com.example.practica5.navigation.R.id.nav_graph__my_monuments_feature)
                    binding.appBarNavigation.toolbar.title = MY_MONUMENTS_TITLE
                    true
                }

                else -> false
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun showToolbar(): Toolbar {
        return toolbar
    }
}