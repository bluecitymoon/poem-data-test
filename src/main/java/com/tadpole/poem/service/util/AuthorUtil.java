package com.tadpole.poem.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jerryjiang on 14/6/2016.
 */
public class AuthorUtil {

    public static String getPossibleBirthdayString(String description) {

        String[] patterns = {"（(.*?)）", "\\((.*?)）", "（(.*?)\\)", "\\((.*?)\\)"};

        for (int i = 0; i < patterns.length; i++) {

            Pattern patternObject = Pattern.compile(patterns[i]);
            Matcher matcher = patternObject.matcher(description);

            while (matcher.find()) {

                String parsedString = matcher.group(0);

                Integer possibleNumber = MathUtil.getNumber(parsedString);

                if (possibleNumber != 0) {
                    return matcher.group(0);
                }

            }
        }

        return null;
    }
}
