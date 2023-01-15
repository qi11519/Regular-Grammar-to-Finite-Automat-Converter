package com.example.rgtonfa;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class token {

    // no idea what this use for, have delim or not also no effect
    public static StringTokenizer tokenStr(String a){
        StringTokenizer st1 = new StringTokenizer(a," -> ");
        return st1;

    }
    //the function got some problem havent change
    public static boolean verifyInput(String a){
//        System.out.println(a);
//        String rule = a;
        Pattern pattern = Pattern.compile("^(\\w+) -> (\\w+( \\w+|\\|)+)*$");
        Matcher matcher = pattern.matcher(a);
        System.out.println(matcher);
        if (matcher.matches()) {
            System.out.println("The pattern was found in the input string.");
            return true;
        } else {
            System.out.println("The pattern was not found in the input string.");
            return false;
        }

    }



}

