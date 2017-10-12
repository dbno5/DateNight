package com.billyji.datenight.data;

public class FoodSelectionDataModel
{
    private static int m_maxDistance;
    private static double m_minStars;
    private static String m_maxPrice;

    private static final int MAX_DISTANCE_ALLOWED = 20;
    private static final double MAX_STARS_ALLOWED = 5;
    private static final double MAX_PRICE_ALLOWED = 5;

    private static final int MAX_DISTANCE = 20;
    private static final double MAX_STARS = 4.5;
    private static final String MAX_PRICE = "4.5";

    public static void setMaxPrice(String price)
    {
        m_maxPrice = Double.parseDouble(price) >= MAX_PRICE_ALLOWED ? MAX_PRICE : price;
    }

    public static void setMaxDistance(double distance)
    {
        m_maxDistance = distance > MAX_DISTANCE_ALLOWED ? MAX_DISTANCE : (int)distance;
    }

    public static void setMinStars(double stars)
    {
        m_minStars = stars >= MAX_STARS_ALLOWED ? MAX_STARS : stars;
    }

    public static int getMaxDistance()
    {
        return m_maxDistance;
    }

    static String getMaxPrice()
    {
        return m_maxPrice;
    }

    static double getMinStars()
    {
        return m_minStars;
    }
}
