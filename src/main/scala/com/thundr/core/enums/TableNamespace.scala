package com.thundr.core.enums

sealed trait TableNamespace

case object prod_coremodel extends TableNamespace
case object public_works extends TableNamespace
case object audience_xfer extends TableNamespace