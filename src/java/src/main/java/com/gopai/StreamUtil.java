package com.gopai;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
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

    public static byte[] read(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public static String readIntoString(InputStream in) {
        return new String(read(in));
    }
}
