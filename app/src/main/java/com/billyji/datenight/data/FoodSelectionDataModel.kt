package com.billyji.datenight.data

object FoodSelectionDataModel {
    internal var maxDistance: Int = 0
        set(distance) {
            field = if (distance > MAX_DISTANCE_ALLOWED) MAX_DISTANCE_ALLOWED else distance
        }
    internal var minStars: Double = 0.toDouble()
        set(stars) {
            field = if (stars >= MAX_STARS_ALLOWED) 4.5 else stars
        }
    internal var maxPrice: String? = null
        set(price) {
            field = if (Integer.parseInt(price) >= MAX_PRICE_ALLOWED) MAX_PRICE else price
        }

    private const val MAX_DISTANCE_ALLOWED = 20
    private const val MAX_STARS_ALLOWED = 5.0
    private const val MAX_PRICE_ALLOWED = 5
    private const val MAX_PRICE = "4.5"
}
