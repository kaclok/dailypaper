package com.smlj.dailypaper.entity.po;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DateCommit {
    private long date;
    private int userId_1;
    private int userId_2;
    private int userId_3;
    private int userId_4;
    private int userId_5;
    private int userId_6;
    private int userId_7;
    private int userId_8;
    private int userId_9;
    private int userId_10;
    private int userId_11;
    private int userId_12;
    private int userId_13;

    public ArrayList<Integer> GetAllIds() {
        ArrayList<Integer> arr = new ArrayList<Integer>(13);
        arr.add(userId_1);
        arr.add(userId_2);
        arr.add(userId_3);
        arr.add(userId_4);
        arr.add(userId_5);
        arr.add(userId_6);
        arr.add(userId_7);
        arr.add(userId_8);
        arr.add(userId_9);
        arr.add(userId_10);
        arr.add(userId_11);
        arr.add(userId_12);
        arr.add(userId_13);
        return arr;
    }

    public int GetBy(short userId) {
        if (userId == 1) {
            return userId_1;
        } else if (userId == 2) {
            return userId_2;
        } else if (userId == 3) {
            return userId_3;
        } else if (userId == 4) {
            return userId_4;
        } else if (userId == 5) {
            return userId_5;
        } else if (userId == 6) {
            return userId_6;
        } else if (userId == 7) {
            return userId_7;
        } else if (userId == 8) {
            return userId_8;
        } else if (userId == 9) {
            return userId_9;
        } else if (userId == 10) {
            return userId_10;
        } else if (userId == 11) {
            return userId_11;
        } else if (userId == 12) {
            return userId_12;
        } else if (userId == 13) {
            return userId_13;
        }
        return 0;
    }
}
