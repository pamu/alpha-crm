package com.alpha.crm.ui.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.{ViewPager, PagerAdapter}
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{TextView, ImageView}
import com.alpha.crm.R
import com.alpha.crm.commons.ContextWrapperProvider
import com.firebase.client.{DataSnapshot, FirebaseError, ValueEventListener, Firebase}
import com.squareup.picasso.Picasso
import macroid.{ContextWrapper, Contexts}
import macroid._
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._

/**
  * Created by pnagarjuna on 21/02/16.
  */
class FeedFragment
  extends Fragment
    with Contexts[Fragment]
    with ContextWrapperProvider {

  val ref = new Firebase("https://brilliant-heat-4158.firebaseio.com")

  override implicit lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val rootView = inflater.inflate(R.layout.feed_fragment_layout, container, false)
    val rvFeed = rootView.findViewById(R.id.rv_feed).asInstanceOf[RecyclerView]
    //    runUi(Some(rvFeed) <~ rvAdapter(new FeedAdapter(List(
    //      Feed("Hello", "989889889", "qqeqeqe", List())))) <~ rvLayoutManager(new LinearLayoutManager(contextProvider.application)))
    runUi(toast("Loading ...")(contextProvider) <~ fry)
    ref.addValueEventListener(new ValueEventListener {

      override def onDataChange(dataSnapshot: DataSnapshot): Unit = {
        val buffer = scala.collection.mutable.ListBuffer[Feed]()
        val topChildren = dataSnapshot.getChildren.iterator()
        Log.d("datasnapshot", dataSnapshot.toString)
        while (topChildren.hasNext) {
          val topChild = topChildren.next()
          Log.d("top child", topChild.toString)
          if (topChild.child("name").exists()) {
            if (topChild.child("complete_address").exists()) {
              if (topChild.child("phone").exists()) {
                if (topChild.child("images").exists()) {
                  val feed =
                    Feed(
                      topChild.child("name").getValue.toString,
                      topChild.child("complete_address").getValue.toString,
                      topChild.child("phone").getValue.toString,
                      topChild.child("images").getValue.toString.split(",").toList.map(_.trim)
                    )
                  buffer += feed
                }
              }
            }
          }
        }
        runUi(Some(rvFeed) <~ rvAdapter(new FeedAdapter(buffer.toList)) <~ rvLayoutManager(new LinearLayoutManager(contextProvider.application)))
      }

      override def onCancelled(firebaseError: FirebaseError): Unit = {
        if (firebaseError != null) {
          runUi(toast("Error !!!")(contextProvider) <~ fry)
        }
      }

    })
    rootView
  }

}

case class Feed(name: String,
                phone: String,
                address: String,
                images: List[String])

class FeedViewHolder(layout: View) extends ViewHolder(layout) {
  def bind(feed: Feed): Unit = {
    runUi {
      Ui {
        val name = layout.findViewById(R.id.name).asInstanceOf[TextView]
        name.setText(feed.name)
        val phone = layout.findViewById(R.id.phone).asInstanceOf[TextView]
        phone.setText(feed.phone)
        val address = layout.findViewById(R.id.address).asInstanceOf[TextView]
        address.setText(feed.address)
        val pager = layout.findViewById(R.id.pager).asInstanceOf[ViewPager]
        pager.setAdapter(new PrescriptionPagerAdapter(feed.images))
      }
    }
  }
}

class FeedAdapter(feeds: List[Feed]) extends RecyclerView.Adapter[FeedViewHolder] {

  override def getItemCount: Int = feeds.length

  override def onBindViewHolder(vh: FeedViewHolder, i: Int): Unit = vh.bind(feeds(i))

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): FeedViewHolder = {
    val rootView = LayoutInflater.from(viewGroup.getContext).inflate(R.layout.feed_item, viewGroup, false)
    new FeedViewHolder(rootView)
  }
}

//https://www.bignerdranch.com/blog/viewpager-without-fragments/

class PrescriptionPagerAdapter(images: List[String]) extends PagerAdapter {

  override def isViewFromObject(view: View, o: scala.Any): Boolean = view.hashCode() == o.hashCode()

  override def getCount: Int = images.length

  override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {
    val rootView = LayoutInflater.from(container.getContext).inflate(R.layout.prescription_item, container, false)
    val imageView = rootView.findViewById(R.id.prescription).asInstanceOf[ImageView]

    Picasso
      .`with`(container.getContext)
      .load(if (images(position).trim == "") "http://something.something" else images(position))
      .fit()
      .centerCrop()
      .placeholder(R.drawable.ic_launcher)
      .error(R.drawable.ic_launcher)
      .into(imageView)

    container.addView(rootView)
    rootView
  }

  override def destroyItem(container: ViewGroup, position: Int, `object`: scala.Any): Unit = {
    container.removeView(`object`.asInstanceOf[View])
  }

  override def getPageTitle(position: Int): CharSequence = {
    "Prescriptions"
  }

}