package com.thundr.core.enums

sealed trait CampaignGoal

case object ACQUSITION extends CampaignGoal
case object BRAND_SALES extends CampaignGoal
case object CATEGORY_SALES extends CampaignGoal
case object COMPETITIVE_CONQUEST extends CampaignGoal
case object PRODUCT_SALES extends CampaignGoal
case object SITE_TRAFFIC extends CampaignGoal
case object REENGAGEMENT extends CampaignGoal
case object ACTION extends CampaignGoal