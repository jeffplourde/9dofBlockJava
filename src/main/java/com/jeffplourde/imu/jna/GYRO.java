package com.jeffplourde.imu.jna;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GYRO {
    private GYRO() {
        
    }
    public static final byte ADDRESS = 0x6b;
    public static final byte WHO_AM_I = 0x0F;
    public static final byte WHO_AM_I_OK = (byte) 0xD4;
    public static final byte CTRL_REG1_G = 0x20;
    public static final byte CTRL_REG2_G = 0x21;
    public static final byte CTRL_REG3_G = 0x22;
    public static final byte CTRL_REG4_G = 0x23;
    public static final byte CTRL_REG5_G = 0x24;
    public static final byte REFERENCE_G = 0x25;
    public static final byte STATUS_REG_G = 0x27;
    public static final byte OUT_X_L_G = 0x28;
    public static final byte OUT_X_H_G = 0x29;
    public static final byte OUT_Y_L_G = 0x2A;
    public static final byte OUT_Y_H_G = 0x2B;
    public static final byte OUT_Z_L_G = 0x2C;
    public static final byte OUT_Z_H_G = 0x2D;
    public static final byte FIFO_CTRL_REG_G = 0x2E;
    public static final byte FIFO_SRC_REG_G = 0x2F;
    public static final byte INT1_CFG_G = 0x30;
    public static final byte INT1_SRC_G = 0x31;
    public static final byte INT1_TSH_XH_G = 0x32;
    public static final byte INT1_TSH_XL_G = 0x33;
    public static final byte INT1_TSH_YH_G = 0x34;
    public static final byte INT1_TSH_YL_G = 0x35;
    public static final byte INT1_TSH_ZH_G = 0x36;
    public static final byte INT1_TSH_ZL_G = 0x37;
    public static final byte INT1_DURATION_G = 0x38;
    
    public static final Map<String,Integer> RANGE_G = createRangeG();
    
    private static final Map<String, Integer> createRangeG() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("245DPS", 0 << 4);
        result.put("500DPS", 1 << 4);
        result.put("2000DPS", 2 << 4);
        return Collections.unmodifiableMap(result);
    }
    
    public static final Map<String,Double> CAL_G = createCalG();
    
    private static final Map<String, Double> createCalG() {
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("245DPS", 245.0/32768.0);
        result.put("500DPS", 500.0/32768.0);
        result.put("2000DPS", 2000.0/32768.0);
        return Collections.unmodifiableMap(result);
    }    

}
