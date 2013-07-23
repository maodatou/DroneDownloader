/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader.Utility;

public class Utility {
    /**
     * change download speed to human-readable format
     * @param speedInByte speed in byte unit
     * @return formatted string
     */
    public static String formatSpeed(int speedInByte) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};   // it's enough.
        float speed = speedInByte;
        int i = 0;
        while (speed / 1000f >= 1) {
            speed /= 1000f;
            i++;
        }
        return String.format("%5.1f%2s/s", speed, units[i]);
    }
}
