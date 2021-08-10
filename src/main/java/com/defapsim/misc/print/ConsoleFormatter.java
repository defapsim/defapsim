package com.defapsim.misc.print;

/**
 * Formatting class which is used to format the command line output.
 */
public class ConsoleFormatter {

    public static String center(String text, Integer length) {
        String out = String.format("%" + length + "s%s%" + length + "s", "", text, "");
        int mid = (out.length() / 2);
        int start = mid - (length / 2);
        int end = start + length;
        return out.substring(start, end);
    }

    public static String leftPad(String text, Integer len) {
        String padding = "";
        for (int i = 0; i < len - text.length(); i++) {
            padding += " ";
        }
        return padding + text;
    }

    public static String rightPad(String text, Integer len) {
        String padding = "";
        for (int i = 0; i < len - text.length(); i++) {
            padding += " ";
        }
        return text + padding;
    }

    public static String center(Integer value, int len) {
        return center(value.toString(), 1);
    }

    public static String line(Integer length) {
        String line = "";
        for(int i = 0; i < length; i++) {
            line = line + "─";
        }
        return line;
    }
}
