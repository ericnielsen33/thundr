package com.thundr.functions

import org.apache.spark.sql.Column
import  org.apache.spark.sql.functions._

package object geo {

  private val EARTH_RADIUS_KM: Double = 6378.14

  private val EARTH_RADIUS_MILES: Double = 3963.19
  private def haversine(origin_lat: Column, origin_lon: Column, dest_lat: Column, dest_lon: Column, earth_radius: Double): Column = {
    def helper = {
      pow(sin(radians(dest_lat - origin_lat) / 2), 2) +
        cos(radians(origin_lat)) * cos(radians(dest_lat)) * pow(sin(radians(dest_lon - origin_lon) / 2), 2)
    }
    atan2(sqrt(helper), sqrt(-helper + 1)) * 2 * earth_radius
  }

  def haversine_km(origin_lat: Column, origin_lon: Column, dest_lat: Column, dest_lon: Column): Column = {
    haversine(origin_lat: Column, origin_lon: Column, dest_lat: Column, dest_lon: Column, EARTH_RADIUS_KM)
  }

  def haversine_miles(origin_lat: Column, origin_lon: Column, dest_lat: Column, dest_lon: Column): Column = {
    haversine(origin_lat: Column, origin_lon: Column, dest_lat: Column, dest_lon: Column, EARTH_RADIUS_MILES)
  }

}
