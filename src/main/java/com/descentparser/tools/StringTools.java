package com.descentparser.tools;

/**
 *
 * @author krthr
 */
public class StringTools {

    public static String reverse(String str) {
        return new StringBuilder()
                .append(str)
                .reverse()
                .toString();
    }

}
