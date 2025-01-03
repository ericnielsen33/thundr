package com.thundr.core.services.dac

trait DacClientHTTP {

  def postNewAudience(audience_name: String, location: String): String
  def pollAudienceStatus(dac_id: String): String
  def refreshExistingAudience(audience_name: String, location: String, dac_id: String): String

}
