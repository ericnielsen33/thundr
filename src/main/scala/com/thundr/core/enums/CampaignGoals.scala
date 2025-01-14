package com.thundr.core.enums


object  CampaignGoals {
  sealed trait CampaignGoal {
    def name: String = this.getClass.getName
  }

  case object BRAND_AWARENESS extends CampaignGoal
  case object SITE_TRAFFIC extends CampaignGoal
  case object STORE_TRAFFIC extends CampaignGoal
  case object ACQUSITION extends CampaignGoal


  case object CATEGORY_SALES extends CampaignGoal
  case object BRAND_SALES extends CampaignGoal
  case object PRODUCT_SALES extends CampaignGoal

  case object COMPETITIVE_CONQUEST extends CampaignGoal
  case object REENGAGEMENT extends CampaignGoal
  case object ACTION extends CampaignGoal

}
