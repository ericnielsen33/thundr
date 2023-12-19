package com.thundr.core.enums

sealed trait AudienceEvent

case object CREATE extends AudienceEvent
case object PERSIST_XFER extends AudienceEvent
case object TRANSFER_START extends AudienceEvent
case object TRANSFER_OK extends AudienceEvent
case object TRANSFER_ERROR extends AudienceEvent
case object API_POLL_EXISTS extends AudienceEvent
case object API_POLL_ABSENT extends AudienceEvent
case object REMOVE_XFER_TABLE extends AudienceEvent
case object DELETE extends AudienceEvent


