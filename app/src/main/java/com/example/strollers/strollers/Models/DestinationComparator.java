package com.example.strollers.strollers.Models;

import java.util.Comparator;

/**
 * Created by nambiv on 4/18/2017.
 */

public class DestinationComparator implements Comparator<Destination> {
    @Override
    public int compare(Destination dest1, Destination dest2)
    {
        double distance1 = dest1.getDistance();
        double distance2 = dest2.getDistance();
        if(distance1 < distance2)
        {
            return 1;
        }
        else if (distance1 > distance2)
        {
            return -1;
        }
        return 0;
    }
}
