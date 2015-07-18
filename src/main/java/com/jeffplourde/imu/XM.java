package com.jeffplourde.imu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class XM {
    private XM() {
        
    }
    public static final short ADDRESS = 0x1D;
    public static final short WHO_AM_I = 0x0F;
    public static final short WHO_AM_I_OK = 0x49;
    public static final short OUT_TEMP_L_XM = 0x05;
    public static final short OUT_TEMP_H_XM = 0x06;
    public static final short STATUS_REG_M = 0x07;
    public static final short OUT_X_L_M = 0x08;
    public static final short OUT_X_H_M = 0x09;
    public static final short OUT_Y_L_M = 0x0A;
    public static final short OUT_Y_H_M = 0x0B;
    public static final short OUT_Z_L_M = 0x0C;
    public static final short OUT_Z_H_M = 0x0D;
    public static final short INT_CTRL_REG_M = 0x12;
    public static final short INT_SRC_REG_M = 0x13;
    public static final short INT_THS_L_M = 0x14;
    public static final short INT_THS_H_M = 0x15;
    public static final short OFFSET_X_L_M = 0x16;
    public static final short OFFSET_X_H_M = 0x17;
    public static final short OFFSET_Y_L_M = 0x18;
    public static final short OFFSET_Y_H_M = 0x19;
    public static final short OFFSET_Z_L_M = 0x1A;
    public static final short OFFSET_Z_H_M = 0x1B;
    public static final short REFERENCE_X = 0x1C;
    public static final short REFERENCE_Y = 0x1D;
    public static final short REFERENCE_Z = 0x1E;
    public static final short CTRL_REG0_XM = 0x1F;
    public static final short CTRL_REG1_XM = 0x20;
    public static final short CTRL_REG2_XM = 0x21;
    public static final short CTRL_REG3_XM = 0x22;
    public static final short CTRL_REG4_XM = 0x23;
    public static final short CTRL_REG5_XM = 0x24;
    public static final short CTRL_REG6_XM = 0x25;
    public static final short CTRL_REG7_XM = 0x26;
    public static final short STATUS_REG_A = 0x27;
    public static final short OUT_X_L_A = 0x28;
    public static final short OUT_X_H_A = 0x29;
    public static final short OUT_Y_L_A = 0x2A;
    public static final short OUT_Y_H_A = 0x2B;
    public static final short OUT_Z_L_A = 0x2C;
    public static final short OUT_Z_H_A = 0x2D;
    public static final short FIFO_CTRL_REG = 0x2E;
    public static final short FIFO_SRC_REG = 0x2F;
    public static final short INT_GEN_1_REG = 0x30;
    public static final short INT_GEN_1_SRC = 0x31;
    public static final short INT_GEN_1_THS = 0x32;
    public static final short INT_GEN_1_DURATION = 0x33;
    public static final short INT_GEN_2_REG = 0x34;
    public static final short INT_GEN_2_SRC = 0x35;
    public static final short INT_GEN_2_THS = 0x36;
    public static final short INT_GEN_2_DURATION =  0x37;
    public static final short CLICK_CFG = 0x38;
    public static final short CLICK_SRC = 0x39;
    public static final short CLICK_THS = 0x3A;
    public static final short TIME_LIMIT = 0x3B;
    public static final short TIME_LATENCY = 0x3C;
    public static final short TIME_WINDOW = 0x3D;
    public static final short ACT_THS = 0x3E;
    public static final short ACT_DUR = 0x3F;
    
    public static final Map<String,Integer> RANGE_M = createRangeM();
    
    private static final Map<String, Integer> createRangeM() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("2GAUSS",  0 << 5);
        result.put("4GAUSS",  1 << 5);
        result.put("8GAUSS",  2 << 5);
        result.put("12GAUSS", 3 << 5);
        return Collections.unmodifiableMap(result);
    }
    
    public static final Map<String,Integer> RANGE_A = createRangeA();
    
    private static final Map<String, Integer> createRangeA() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("2G",  0 << 3);
        result.put("4G",  1 << 3);
        result.put("6G",  2 << 3);
        result.put("8G",  3 << 3);
        result.put("16G", 4 << 3);
        return Collections.unmodifiableMap(result);
    }    
    
    public static final Map<String,Double> CAL_M = createCalM();
    
    private static final Map<String, Double> createCalM() {
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("2GAUSS", 2.0/32768.0);
        result.put("4GAUSS", 4.0/32768.0);
        result.put("8GAUSS", 8.0/32768.0);
        result.put("12GAUSS", 12.0/32768.0);
        return Collections.unmodifiableMap(result);
    }

    public static final Map<String,Double> CAL_A = createCalA();
    
    private static final Map<String, Double> createCalA() {
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("2G", 2.0/32768.0);
        result.put("4G", 4.0/32768.0);
        result.put("6G", 6.0/32768.0);
        result.put("8G", 8.0/32768.0);
        result.put("16G", 16.0/32768.0);
        return Collections.unmodifiableMap(result);
    } 

    public static final double CAL_TEMP = 1.0/8.0;
}
