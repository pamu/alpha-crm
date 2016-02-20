package com.alpha.crm.commons

import macroid.ContextWrapper

trait ContextWrapperProvider {

  implicit val contextProvider : ContextWrapper

}
