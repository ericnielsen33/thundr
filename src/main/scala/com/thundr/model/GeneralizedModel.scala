package com.thundr.model

import com.thundr.config.ConfigProvider

trait GeneralizedModel
  extends ConfigProvider {

  def base_directory = thundr_root

  def project_name: String

  def run_name: String
}
