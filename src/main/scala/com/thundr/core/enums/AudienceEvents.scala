package com.thundr.core.enums

object AudienceEvents {
  sealed trait AudienceEvent {
    def name: String = this.getClass.getName
  }

  case object CREATE_AUDIENCE extends AudienceEvent
  case object PERSIST_XFER extends AudienceEvent

  case object MERGE_AUDIECE_INTO_CATALOGUE extends AudienceEvent
  case object REPLACE_XFER_TABLE extends AudienceEvent

  case object TRANSFER_START extends AudienceEvent
  case object TRANSFER_OK extends AudienceEvent
  case object TRANSFER_ERROR extends AudienceEvent

  case object REFRESH_START extends AudienceEvent
  case object REFRESH_END extends AudienceEvent
  case object REFRESH_ERROR extends AudienceEvent

  case object API_POLL_EXISTS extends AudienceEvent
  case object API_POLL_ABSENT extends AudienceEvent

  case object DROP_XFER_TABLE extends AudienceEvent
  case object DELETE extends AudienceEvent

}



