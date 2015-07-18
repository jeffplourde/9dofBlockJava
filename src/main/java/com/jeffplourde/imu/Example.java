package com.jeffplourde.imu;

import java.util.Formatter;
import java.util.Locale;

public class Example {
    public static void main(String[] args) throws InterruptedException {
        System.loadLibrary("mraajava");
 
        mraa.mraa.init();
        
        System.out.println("mraa version:"+mraa.mraa.getVersion());
        
        IMU imu = new IMU(); // To select a specific I2C port, use IMU(n). Default is 1.
        
        // Enable accel, mag, gyro, and temperature
        imu.enable_accel();
        imu.enable_mag();
        imu.enable_gyro();
        imu.enable_temp();

        // Set range on accel, mag, and gyro

        // Specify Options: "2G", "4G", "6G", "8G", "16G"
        imu.accel_range("2G");       // leave blank for default of "2G" 

        // Specify Options: "2GAUSS", "4GAUSS", "8GAUSS", "12GAUSS"
        imu.mag_range("2GAUSS");     // leave blank for default of "2GAUSS"

        // Specify Options: "245DPS", "500DPS", "2000DPS" 
        imu.gyro_range("245DPS");    // leave blank for default of "245DPS"

        final Formatter formatter = new Formatter(System.out, Locale.US);
        
        Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() { public void run() { formatter.close(); } }));
        
        // Loop and read accel, mag, and gyro
        for(;;) {
            imu.read_accel();
            imu.read_mag();
            imu.read_gyro();
            imu.readTemp();
            
            // Print the results            
            formatter.format("Accel: %3.3f, %3.3f, %3.3f\n", imu.ax, imu.ay, imu.az);
            formatter.format("Mag:   %3.3f, %3.3f, %3.3f\n", imu.mx, imu.my, imu.mz);
            formatter.format("Gyro:  %3.3f, %3.3f, %3.3f\n", imu.gx, imu.gy, imu.gz);
            formatter.format("Temperature: %.3f\n", imu.temp);
            
            // Sleep for 1/10th of a second        
            Thread.sleep(100L);
        }   
    }
}
