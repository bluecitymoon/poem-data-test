package com.tadpole.poem.service.util;

import com.google.common.collect.Lists;

import java.util.List;
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

    public static List<Integer> getNumbers(String str) {

        List<Integer> list = Lists.newArrayList();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        int number = 0;
        while (matcher.find()) {
            number = Integer.parseInt(matcher.group());

            if (number > 31) {

                list.add(number);
            }
        }

        return list;
    }
}
