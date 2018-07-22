package com.leothosthoren.go4lunch.utils;

public interface StringHelper {

    default String displayFirstName(String fullName){
        String [] arr = fullName.split(" ");
        return arr[0];
    }
}
