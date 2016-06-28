package com.tadpole.poem.json;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by jerryjiang on 20/6/2016.
 */
public class FindJSFile {

    public static void main(String[] args) {

        File jsFile = new File("/Users/jerryjiang/Downloads/kit-es2016/jspm_packages");

        System.out.println( FileUtils.listFiles(jsFile, new String[]{"js"}, true).size());

    }
}
