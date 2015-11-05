package com.artifact.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by girish.kulkarni on 10/1/2015.
 */
public class AFUtils {

    /** Returns the four digit random number*/
    public static String getFourDigitRandomNumber(){
        List<Integer> numbers = new ArrayList<Integer>(0);
        for(int i = 0; i < 10; i++){
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        String result = "";
        for(int i = 0; i < 4; i++){
            result += numbers.get(i).toString();
        }
        System.out.println(result);
        return   result;
    }
}
