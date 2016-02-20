package com.alpha.crm.ui.main

import com.alpha.crm.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 19/02/16.
  */
trait MainComposer extends TypedFindView {
  lazy val toolbar = Option(findView(TR.toolbar))
  lazy val drawerLayout = Option(findView(TR.drawer_layout))
  lazy val fragmentMenu = Option(findView(TR.fragment_menu))

}
