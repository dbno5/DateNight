package com.billyji.datenight;

public class FoodSelectionDetails
{

    private static double maxDistance;
    private static double minStars;
    private static String maxPrice;
    private static double minRating;

    public static String getMaxPrice()
    {
        return maxPrice;
    }

    public static void setMaxPrice(String maxPrice)
    {
        FoodSelectionDetails.maxPrice = maxPrice;
    }

    public static double getMinRating()
    {
        return minRating;
    }

    public static void setMinRating(double minRating)
    {
        FoodSelectionDetails.minRating = minRating;
    }

    public static double getMaxDistance()
    {
        return maxDistance;
    }

    public static double getMinStars()
    {
        return minStars;
    }

    public static void setMinStars(double stars)
    {
        minStars = stars;
    }

    public static void setMaxDistance(double distance)
    {
        maxDistance = distance;
    }


}
