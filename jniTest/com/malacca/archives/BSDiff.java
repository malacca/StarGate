package com.malacca.archives;

public class BSDiff {

    static {
        System.loadLibrary("bsdiff");
    }

    public static native byte[] patch(byte[] origin, byte[] patch);
}
