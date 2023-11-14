package com.thundr.core.enums

sealed trait AudienceEvent

case object PERSIST_START extends AudienceEvent
case object PERSIST_END extends AudienceEvent
case object TRANSFER_START extends AudienceEvent
case object TRANSFER_END extends AudienceEvent
case object API_POLL_EXISTS extends AudienceEvent
case object API_POLL_ABSENT extends AudienceEvent
case object DELETE extends AudienceEvent


