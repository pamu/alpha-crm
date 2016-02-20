package com.alpha.crm.ui.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{View, ViewGroup, LayoutInflater}
import com.alpha.crm.R
import com.alpha.crm.commons.ContextWrapperProvider
import macroid.{ContextWrapper, Contexts}

/**
  * Created by pnagarjuna on 20/02/16.
  */
class MenuFragment
  extends Fragment
    with Contexts[Fragment]
    with MenuComposer
    with ContextWrapperProvider {

  override implicit lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  implicit var mainView: Option[View] = None

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    val rootView = inflater.inflate(R.layout.menu_fragment_layout, container, false)
    mainView = Some(rootView)
    rootView
  }

}

case class MenuItem(name: String, icon: Int)

class MenuAdapter(list: List[MenuItem])(implicit )
