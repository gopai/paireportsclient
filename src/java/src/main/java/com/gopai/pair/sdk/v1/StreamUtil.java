package com.gopai.pair.sdk.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple streaming utility for the {@link PAIClient} return values.
 */
public class StreamUtil {
    /**
     * Copy from an InputStream to an OutputStream
     *
     * @param in
     * @param out
     */
    public static void copy(InputStream in, OutputStream out) {
        try {
            int v;
            while (((v = in.read()) != -1)) {
                out.write(v);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the InputStream as a byte array.
     *
     * @param in InputStream
     * @return byte[]
     */
    public static byte[] read(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * Returns the InputStream as a String
     *
     * @param in InputStream
     * @return String
     */
    public static String readIntoString(InputStream in) {
        return new String(read(in));
    }
}
