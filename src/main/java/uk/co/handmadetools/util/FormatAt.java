package uk.co.handmadetools.util;

import java.text.DecimalFormat;

public class FormatAt {

    public static String format(float at) {
        return new DecimalFormat("#,###.00").format(at);
    }

}
