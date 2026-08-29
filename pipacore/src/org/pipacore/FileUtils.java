/*
 * Copyright (C) 2026 nullpointer1101
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class FileUtils {

    private static final String TAG = "FileUtils";

    private FileUtils() {}

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    public static String readOneLine(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            Log.e(TAG, "readOneLine failed: " + path, e);
            return null;
        }
    }

    public static boolean writeLine(String path, String value) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(value + "\n");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "writeLine failed: " + path + ", trying shell fallback", e);
            return writeLineShell(path, value);
        }
    }

    public static boolean writeLine(String path, int value) {
        return writeLine(path, Integer.toString(value));
    }

    public static int readLineInt(String path) {
        String line = readOneLine(path);
        if (line != null) {
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public static boolean writeLineShell(String path, String value) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "echo " + value + " > " + path});
            if (process.waitFor() == 0) return true;
        } catch (Exception ignored) {}
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "echo " + value + " > " + path});
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            Log.e(TAG, "writeLineShell failed: " + path, e);
            return false;
        }
    }

    public static boolean executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            if (process.waitFor() == 0) return true;
        } catch (Exception ignored) {}
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            Log.e(TAG, "executeCommand failed: " + command, e);
            return false;
        }
    }
}
