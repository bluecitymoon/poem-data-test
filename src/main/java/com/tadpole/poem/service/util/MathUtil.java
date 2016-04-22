package com.tadpole.poem.service.util;

import java.util.regex.*;

/**
 * Created by jerryjiang on 22/4/2016.
 */
public class MathUtil {

    public static Integer getNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        int number = 0;
        while (matcher.find()) {
            number = Integer.parseInt(matcher.group());
        }
        return number;
    }
}
