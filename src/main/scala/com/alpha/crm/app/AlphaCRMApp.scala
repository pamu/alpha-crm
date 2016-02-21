package com.alpha.crm.app

import android.app.Application
import com.firebase.client.Firebase

/**
  * Created by pnagarjuna on 17/02/16.
  */
class AlphaCRMApp extends Application {

  override def onCreate(): Unit = {
    super.onCreate()
    Firebase.setAndroidContext(this)
  }

}
