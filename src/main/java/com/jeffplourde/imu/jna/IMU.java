package com.jeffplourde.imu.jna;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.sun.jna.Pointer;

import static com.jeffplourde.imu.jna.MRAALibrary.mraa_i2c_address;
import static com.jeffplourde.imu.jna.MRAALibrary.mraa_i2c_init;
import static com.jeffplourde.imu.jna.MRAALibrary.mraa_i2c_read_byte_data;
import static com.jeffplourde.imu.jna.MRAALibrary.mraa_i2c_read_bytes_data;
import static com.jeffplourde.imu.jna.MRAALibrary.mraa_i2c_write_byte_data;
import static com.jeffplourde.imu.jna.MRAALibrary.mraa_init;

public class IMU {
    private static final double[] parsedata(byte[] b, double cal, double[] results) {
        ByteBuffer bb = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < 3; i++) {
            results[i] = bb.getShort()*cal;
        }
        return results;
    }

    private static void i2cAddress(Pointer x, byte address) {
        int r = mraa_i2c_address(x, address);
        if(mraa.MRAA_SUCCESS!=r) {
            throw new RuntimeException("Address Failed:"+r);
        }
    }
    
    private void i2cAddress(byte address) {
        i2cAddress(x, address);
    }
    
    private static void i2cReadBytes(Pointer x, byte command, byte[] data) {
        int length = mraa_i2c_read_bytes_data(x, command, data, data.length);
        if(data.length != length) {
            throw new RuntimeException("Unable to read bytes received:"+length);
        }
    }
    
    private void i2cReadBytes(byte command, byte[] data) {
        i2cReadBytes(x, command, data);
    }
    
    private static byte i2cReadByte(Pointer x, byte command) {
        byte resp = mraa_i2c_read_byte_data(x, command);
        if(0 == resp) {
            System.err.println("0 indicates failure in mraa_i2c_read_byte_data for " + command);
            return resp;
        } else {
            return resp;
        }
    }
    private byte i2cReadByte(byte command) {
        return i2cReadByte(x, command);
    }
    
    private static void i2cWriteByte(Pointer x, byte data, byte command) {
        int r = mraa_i2c_write_byte_data(x, data, command);
        if(r != mraa.MRAA_SUCCESS) {
            throw new RuntimeException("mraa_i2c_write_byte_data failed with:"+r);
        }
    }
    private void i2cWriteByte(byte data, byte command) {
        i2cWriteByte(x, data, command);
    }

    
    private static final double[] read3axis(byte[] data, Pointer x, byte address, short reg, double cal, double[] results) {
        i2cAddress(x, address);
        i2cReadBytes(x, (byte)(0x80 | reg), data);
        return parsedata(data, cal, results);
    }
    
    private Pointer x;
    
    public IMU() {
        this(1);
    }
    public IMU(int I2CPort) {
        int r = mraa_init();
        if(mraa.MRAA_SUCCESS!=r&&mraa.MRAA_ERROR_PLATFORM_ALREADY_INITIALISED!=r) {
            throw new RuntimeException("Unable to initialize library : " + r);
        }
        
        x = mraa_i2c_init(I2CPort);
        
        if(null == x) {
            throw new RuntimeException("Unable to initialize mraa_i2c_init");
        }
        
        i2cAddress(GYRO.ADDRESS);
        short resp = i2cReadByte(GYRO.WHO_AM_I);
        if(GYRO.WHO_AM_I_OK != resp) {
            throw new RuntimeException("Gyro init failed:"+resp);
        }
        
        i2cAddress(XM.ADDRESS);
        resp = i2cReadByte(XM.WHO_AM_I);
        if(XM.WHO_AM_I_OK != resp) {
            throw new RuntimeException("Accel/Mag init failed:"+resp);
        }
    }
    // Enables the accelerometer, 100 Hz continuous in X, Y, and Z
    public void enable_accel() {
        i2cAddress(XM.ADDRESS);
        i2cWriteByte((byte)0x67, XM.CTRL_REG1_XM); // 100 Hz, XYZ
        i2cAddress(XM.ADDRESS);
        i2cWriteByte((byte)0xF0, XM.CTRL_REG5_XM);
    }
    
    // Enables the gyro in normal mode on all axes
    public void enable_gyro() {
        i2cAddress(GYRO.ADDRESS);
        i2cWriteByte((byte)0x0F, GYRO.CTRL_REG1_G); // normal mode, all axes
    }

    // Enables the mag continuously on all axes
    public void enable_mag() {
        i2cAddress(XM.ADDRESS);
        i2cWriteByte((byte)0x00, XM.CTRL_REG7_XM); // continuous
    }

    // Enables temperature measurement at the same frequency as mag  
    public void enable_temp() {
        i2cAddress(XM.ADDRESS);
        byte rate = i2cReadByte(XM.CTRL_REG5_XM);
        i2cAddress(XM.ADDRESS);
        i2cWriteByte((byte)(rate | (1<<7)), XM.CTRL_REG5_XM);
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
            i2cAddress(XM.ADDRESS);
            int accelReg = 0xFF & i2cReadByte(XM.CTRL_REG2_XM);
            accelReg |= Arange;
            i2cAddress(XM.ADDRESS);
            i2cWriteByte( (byte)accelReg, XM.CTRL_REG2_XM);
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
            i2cAddress(XM.ADDRESS);
            int magReg = 0xFF&i2cReadByte(XM.CTRL_REG6_XM);
            magReg &= ~(0x60);
            magReg |= Mrange;
            i2cAddress(XM.ADDRESS);
            i2cWriteByte((byte)magReg, XM.CTRL_REG6_XM);
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
            i2cAddress(GYRO.ADDRESS);
            int gyroReg = 0xFF & i2cReadByte(x, GYRO.CTRL_REG4_G);
            gyroReg &= ~(0x30);
            gyroReg |= Grange;
            i2cAddress(GYRO.ADDRESS);
            i2cWriteByte((byte)gyroReg, GYRO.CTRL_REG4_G);
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
        ax = ay = az = 0;
        Arrays.fill(data, (byte) 0);
        double cal = XM.CAL_A.get(selected_a_range);
        double[] r = read3axis(data, x, XM.ADDRESS, XM.OUT_X_L_A, cal, results);
        ax = r[0];
        ay = r[1];
        az = r[2];
    }
    
    // Reads and calibrates the mag values into Gauss
    public void read_mag() {
        mx = my = mz = 0;
        Arrays.fill(data, (byte) 0);
        double cal = XM.CAL_M.get(selected_m_range); 
        double[] r = read3axis(data, x, XM.ADDRESS, XM.OUT_X_L_M, cal, results);
        mx = r[0];
        my = r[1];
        mz = r[2];
    }
    
    // Reads and calibrates the gyro values into degrees per second
    public void read_gyro() {
        gx = gy = gz = 0;
        Arrays.fill(data, (byte) 0);
        double cal = GYRO.CAL_G.get(selected_g_range); 
        double[] r = read3axis(data, x, XM.ADDRESS, GYRO.OUT_X_L_G, cal, results);
        gx = r[0];
        gy = r[1];
        gz = r[2];  
    }
    
    // Reads and calibrates the temperature in degrees C
    public void readTemp() {
        i2cAddress(XM.ADDRESS);
        i2cReadBytes((byte) (0x80 | XM.OUT_TEMP_L_XM), tempdata);
        ByteBuffer bb = ByteBuffer.wrap(tempdata).order(ByteOrder.LITTLE_ENDIAN);
        int temp = bb.getShort();
        this.temp = temp * XM.CAL_TEMP;
    }

}
