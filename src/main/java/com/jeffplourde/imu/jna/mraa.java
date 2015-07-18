package com.jeffplourde.imu.jna;

public final class mraa {
    private mraa() {
        
    }

    public static final int MRAA_SUCCESS = 0;                             /**< Expected response */
    public static final int MRAA_ERROR_FEATURE_NOT_IMPLEMENTED = 1;       /**< Feature TODO */
    public static final int MRAA_ERROR_FEATURE_NOT_SUPPORTED = 2;         /**< Feature not supported by HW */
    public static final int MRAA_ERROR_INVALID_VERBOSITY_LEVEL = 3;       /**< Verbosity level wrong */
    public static final int MRAA_ERROR_INVALID_PARAMETER = 4;             /**< Parameter invalid */
    public static final int MRAA_ERROR_INVALID_HANDLE = 5;                /**< Handle invalid */
    public static final int MRAA_ERROR_NO_RESOURCES = 6;                  /**< No resource of that type avail */
    public static final int MRAA_ERROR_INVALID_RESOURCE = 7;              /**< Resource invalid */
    public static final int MRAA_ERROR_INVALID_QUEUE_TYPE = 8;            /**< Queue type incorrect */
    public static final int MRAA_ERROR_NO_DATA_AVAILABLE = 9;             /**< No data available */
    public static final int MRAA_ERROR_INVALID_PLATFORM = 10;             /**< Platform not recognised */
    public static final int MRAA_ERROR_PLATFORM_NOT_INITIALISED = 11;     /**< Board information not initialised */
    public static final int MRAA_ERROR_PLATFORM_ALREADY_INITIALISED = 12; /**< Board is already initialised */
    public static final int MRAA_ERROR_UNSPECIFIED = 99; /**< Unknown Error */
}
