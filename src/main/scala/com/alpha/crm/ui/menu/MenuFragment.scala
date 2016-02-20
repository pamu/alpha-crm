package com.alpha.crm.ui.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View.OnClickListener
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{ImageView, TextView}
import com.alpha.crm.R
import com.alpha.crm.commons.ContextWrapperProvider
import macroid.{Ui, ActivityContextWrapper, ContextWrapper, Contexts}
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._

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
    val rvMenu = rootView.findViewById(R.id.rv_menu).asInstanceOf[RecyclerView]
    runUi(
      Some(rvMenu)
        <~ rvAdapter(new MenuAdapter(List(MenuItem("About", R.drawable.ic_launcher)))(Unit))
        <~ rvLayoutManager(new LinearLayoutManager(contextProvider.application)))
    rootView
  }
}

case class MenuItem(name: String, icon: Int)

class MenuViewHolder(layout: View)(clickListener: => Unit)
                    (implicit context: ActivityContextWrapper) extends ViewHolder(layout) {

  def bind(menuItem: MenuItem): Unit = {
    runUi {
      Ui {
        val icon = layout.findViewById(R.id.icon).asInstanceOf[ImageView]
        icon.setImageResource(menuItem.icon)
        val menuName = layout.findViewById(R.id.menu_name).asInstanceOf[TextView]
        menuName.setText(menuItem.name)
        layout.setOnClickListener(new OnClickListener {
          override def onClick(view: View): Unit = clickListener
        })
      }
    }
  }

}

class MenuAdapter(list: List[MenuItem])(clickListener: => Unit)
                 (implicit context: ActivityContextWrapper) extends RecyclerView.Adapter[MenuViewHolder] {

  override def getItemCount: Int = list.length

  override def onBindViewHolder(vh: MenuViewHolder, i: Int): Unit =
    vh.bind(list(i))

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): MenuViewHolder = {
    val rootView = LayoutInflater.from(context.application).inflate(
      R.layout.menu_item_layout,
      viewGroup,
      false)
    new MenuViewHolder(rootView)(clickListener)
  }

}
