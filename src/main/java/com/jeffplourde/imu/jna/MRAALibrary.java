package com.jeffplourde.imu.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class MRAALibrary {
    static {
        Native.register("mraa");
    }
    
    public static native int mraa_init();
    // TODO could use a specialized Pointer
    public static native Pointer mraa_i2c_init (int bus);
    public static native Pointer mraa_i2c_init_raw (int bus);
    
    // TODO could use enumerated mraa_result_t and mraa_i2c_mode_t
    public static native int mraa_i2c_frequency (Pointer dev, int mode);
     
    public static native int mraa_i2c_read (Pointer dev, byte[] data, int length);
    
    public static native byte mraa_i2c_read_byte (Pointer dev);
     
    public static native byte mraa_i2c_read_byte_data (Pointer dev, byte command);
     
    public static native short mraa_i2c_read_word_data (Pointer dev, byte command);
     
    public static native int mraa_i2c_read_bytes_data (Pointer dev, byte command, byte[] data, int length);
     
    public static native int mraa_i2c_write (Pointer dev, byte[] data, int length);
     
    public static native int mraa_i2c_write_byte (Pointer dev, byte data);
     
    public static native int mraa_i2c_write_byte_data (Pointer dev, byte data, byte command);
     
    public static native int mraa_i2c_write_word_data (Pointer dev, short data, byte command);
     
    public static native int mraa_i2c_address (Pointer dev, byte address);
     
    public static native int mraa_i2c_stop (Pointer dev);
}
