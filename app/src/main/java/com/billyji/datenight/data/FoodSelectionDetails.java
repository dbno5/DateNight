package com.billyji.datenight.data;

public class FoodSelectionDetails
{
    private static int m_maxDistance;
    private static double m_minStars;
    private static String m_maxPrice;

    public static void setMaxPrice(String price)
    {
        m_maxPrice = Double.parseDouble(price) >= 5 ? "4.5" : price;
    }

    public static void setMaxDistance(double distance)
    {
        m_maxDistance = distance > 20 ? 20 : (int)distance;
    }

    public static void setMinStars(double stars)
    {
        m_minStars = stars >= 5 ? 4.5 : stars;
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
