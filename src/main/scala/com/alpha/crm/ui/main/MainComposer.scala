package com.alpha.crm.ui.main

import com.alpha.crm.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 19/02/16.
  */
trait MainComposer extends TypedFindView {
  lazy val toolbar = Option(findView(TR.toolbar))
}
