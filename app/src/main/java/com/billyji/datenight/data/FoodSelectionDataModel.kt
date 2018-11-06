package com.billyji.datenight.data

object FoodSelectionDataModel {
    private var distanceMap: HashMap<Int, Int> = hashMapOf(0 to 1, 1 to 2, 2 to 5, 3 to 10, 4 to 20)

    internal var maxDistance: Int = 0
        set(distance) {
            field = distanceMap[distance]!!
        }
    internal var minStars: Double = 0.toDouble()
    internal var maxDollarSigns: Int = 0
}
