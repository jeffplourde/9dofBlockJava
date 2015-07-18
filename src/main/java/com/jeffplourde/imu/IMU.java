package com.jeffplourde.imu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IMU {
    private static final double[] parsedata(byte[] b, double cal, double[] results) {
        ByteBuffer bb = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < 3; i++) {
            results[i] = bb.getShort()*cal;
        }
        return results;
    }
    
    private static final double[] read3axis(byte[] data, mraa.I2c x, short address, short reg, double cal, double[] results) {
        x.address(address);
        x.readBytesReg( (short)(0x80 | reg), data);
        return parsedata(data, cal, results);
    }

    private final mraa.I2c x;
    
    public IMU() {
        this(1);
    }
    public IMU(int I2CPort) {
        x = new mraa.I2c(I2CPort);
        x.address(GYRO.ADDRESS);
        short resp = x.readReg(GYRO.WHO_AM_I);
        if(GYRO.WHO_AM_I_OK != resp) {
            throw new RuntimeException("Gyro init failed:"+resp);
        }
        x.address(XM.ADDRESS);
        resp = x.readReg(XM.WHO_AM_I);
        if(XM.WHO_AM_I_OK != resp) {
            throw new RuntimeException("Accel/Mag init failed:"+resp);
        }
    }
    // Enables the accelerometer, 100 Hz continuous in X, Y, and Z
    public void enable_accel() {
        x.address(XM.ADDRESS);
        x.writeReg(XM.CTRL_REG1_XM, (short) 0x67);  // 100 Hz, XYZ
        x.address(XM.ADDRESS);
        x.writeReg(XM.CTRL_REG5_XM, (short) 0xF0);
    }
    
    // Enables the gyro in normal mode on all axes
    public void enable_gyro() {
        x.address(GYRO.ADDRESS);
        x.writeReg(GYRO.CTRL_REG1_G, (short) 0x0F); // normal mode, all axes
    }

    // Enables the mag continuously on all axes
    public void enable_mag() {
        x.address(XM.ADDRESS);
        x.writeReg(XM.CTRL_REG7_XM, (short) 0x00);  // continuous
    }

    // Enables temperature measurement at the same frequency as mag  
    public void enable_temp() {
        x.address(XM.ADDRESS);
        short rate = x.readReg(XM.CTRL_REG5_XM);
        x.address(XM.ADDRESS);
        x.writeReg(XM.CTRL_REG5_XM, (short)(rate | (1<<7)));      
    }
    
    // Sets the range on the accelerometer, default is +/- 2Gs
    public void accel_range() {
        accel_range("2G");
    }
    private String selected_a_range;
    private String selected_m_range;
    private String selected_g_range;
    
    public void accel_range(String AR) {
        if(XM.RANGE_A.containsKey(AR)) {
            int Arange = XM.RANGE_A.get(AR);
            x.address(XM.ADDRESS);
            short accelReg = x.readReg(XM.CTRL_REG2_XM);
            accelReg |= Arange;
            x.address(XM.ADDRESS);
            x.writeReg(XM.CTRL_REG2_XM, accelReg);
            selected_a_range = AR;
        } else {
            throw new RuntimeException("Invalid Range ["+AR+"]. Valid range keys are " + XM.RANGE_A.keySet());
        }
    }

    public void mag_range() {
        mag_range("2GAUSS");
    }
    
    // Sets the range on the mag - default is +/- 2 Gauss
    public void mag_range(String MR) {
        if(XM.RANGE_M.containsKey(MR)) {
            int Mrange = XM.RANGE_M.get(MR);
            x.address(XM.ADDRESS);
            short magReg = x.readReg(XM.CTRL_REG6_XM);
            magReg &= ~(0x60);
            magReg |= Mrange;
            x.address(XM.ADDRESS);
            x.writeReg(XM.CTRL_REG6_XM, magReg);
            selected_m_range = MR;
        } else {
            throw new RuntimeException("Invalid Range ["+MR+"]. Valid keys are " + XM.RANGE_M.keySet());
        }
    }

    public void gyro_range() {
        gyro_range("245DPS");
    }
    // Sets the range on the gyro - default is +/- 245 degrees per second
    public void gyro_range(String GR) {
        if(GYRO.RANGE_G.containsKey(GR)) {
            int Grange = GYRO.RANGE_G.get(GR);
            x.address(GYRO.ADDRESS);
            short gyroReg = x.readReg(GYRO.CTRL_REG4_G);
            gyroReg &= ~(0x30);
            gyroReg |= Grange;
            x.address(GYRO.ADDRESS);
            x.writeReg(GYRO.CTRL_REG4_G, gyroReg);
            selected_g_range = GR;
        } else {
            throw new RuntimeException("Invalid Range ["+GR+"]. Valid keys are " + GYRO.RANGE_G.keySet());
        }
    }
    
    public double ax, ay, az;
    public double mx, my, mz;
    public double gx, gy, gz;
    public double temp;
    
    // Not thread safe
    private byte[] data = new byte[6];
    private byte[] tempdata = new byte[2];
    private double[] results = new double[3];
    
    // Reads and calibrates the accelerometer values into Gs
    public void read_accel() {
        double cal = XM.CAL_A.get(selected_a_range);
        double[] r = read3axis(data, x, XM.ADDRESS, XM.OUT_X_L_A, cal, results);
        ax = r[0];
        ay = r[1];
        az = r[2];
    }
    
    // Reads and calibrates the mag values into Gauss
    public void read_mag() {
        double cal = XM.CAL_M.get(selected_m_range); 
        double[] r = read3axis(data, x, XM.ADDRESS, XM.OUT_X_L_M, cal, results);
        mx = r[0];
        my = r[1];
        mz = r[2];
    }
    
    // Reads and calibrates the gyro values into degrees per second
    public void read_gyro() {
        double cal = GYRO.CAL_G.get(selected_g_range); 
        double[] r = read3axis(data, x, XM.ADDRESS, GYRO.OUT_X_L_G, cal, results);
        gx = r[0];
        gy = r[1];
        gz = r[2];  
    }
    
    // Reads and calibrates the temperature in degrees C
    public void readTemp() {
        x.address(XM.ADDRESS);
        x.readBytesReg((short) (0x80 | XM.OUT_TEMP_L_XM), tempdata);
        ByteBuffer bb = ByteBuffer.wrap(tempdata).order(ByteOrder.LITTLE_ENDIAN);
        int temp = bb.getShort();
        this.temp = temp * XM.CAL_TEMP;
    }
}
