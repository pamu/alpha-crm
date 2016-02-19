package com.alpha.crm.ui.main

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.alpha.crm.R
import macroid.Contexts

class MainActivity
  extends AppCompatActivity
  with Contexts[FragmentActivity]
  with MainComposer {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_main_acitivity)

    toolbar map setSupportActionBar
  }

}
