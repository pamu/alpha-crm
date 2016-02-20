package com.alpha.crm.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.view.{MenuItem, Menu, View}
import com.alpha.crm.R
import com.alpha.crm.ui.menu.MenuFragment
import macroid.Contexts
import macroid.FullDsl._

import com.fortysevendeg.macroid.extras.FragmentExtras._

class MainActivity
  extends AppCompatActivity
    with Contexts[FragmentActivity]
    with MainComposer {

  var actionBarDrawerToggle: Option[ActionBarDrawerToggle] = None

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_main_acitivity)

    toolbar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar.setHomeButtonEnabled(true)

    drawerLayout map { drawerLayout =>
      val drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu) {

        override def onDrawerOpened(drawerView: View): Unit = {
          super.onDrawerOpened(drawerView)
          invalidateOptionsMenu()
          findFragmentById[MenuFragment](R.id.fragment_menu)
        }

        override def onDrawerClosed(drawerView: View): Unit = {
          super.onDrawerClosed(drawerView)
          invalidateOptionsMenu()
        }

      }

      actionBarDrawerToggle = Some(drawerToggle)
      drawerLayout.setDrawerListener(drawerToggle)

    }

    if (savedInstanceState == null) {
      runUi(
        replaceFragment(
          builder = f[MenuFragment],
          R.id.fragment_menu,
          tag = Some(classOf[MenuFragment].getSimpleName))
      )
    }
  }

  override def onNewIntent(intent: Intent): Unit = {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit =
    super.onActivityResult(requestCode, resultCode, data)

  override def onCreateOptionsMenu(menu: Menu): Boolean = super.onCreateOptionsMenu(menu)

  override def onPostCreate(savedInstanceState: Bundle): Unit = {
    super.onPostCreate(savedInstanceState)
    actionBarDrawerToggle map (_.syncState)

  }

  override def onConfigurationChanged(newConfig: Configuration): Unit = {
    super.onConfigurationChanged(newConfig)
    actionBarDrawerToggle map (_.onConfigurationChanged(newConfig))
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    if (actionBarDrawerToggle.isDefined && actionBarDrawerToggle.get.onOptionsItemSelected(item)) true
    else super.onOptionsItemSelected(item)
  }

}
