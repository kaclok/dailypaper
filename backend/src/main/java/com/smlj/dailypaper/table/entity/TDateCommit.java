package com.smlj.dailypaper.table.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TDateCommit {
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
    private int userId_14;
    private int userId_15;
    private int userId_16;

    public TDateCommit() {
    }

    public TDateCommit(long date) {
        this.date = date;
        userId_1 = userId_2 = userId_3 = userId_4 = userId_5 = userId_6 = userId_7 = userId_8 = userId_9 = userId_10 = userId_11 = userId_12 = userId_13 = userId_14 = userId_15 = userId_16 = 0;
    }

    public static int GetFieldCount() {
        return 16;
    }

    public ArrayList<Integer> GetFieldIds() {
        ArrayList<Integer> rlt = new ArrayList<>(13);
        rlt.add(1);
        rlt.add(2);
        rlt.add(3);
        rlt.add(4);
        rlt.add(5);
        rlt.add(6);
        rlt.add(7);
        rlt.add(8);
        rlt.add(9);
        rlt.add(10);
        rlt.add(11);
        rlt.add(12);
        rlt.add(13);
        rlt.add(14);
        rlt.add(15);
        rlt.add(16);
        return rlt;
    }

    public ArrayList<Integer> GetAllIds() {
        ArrayList<Integer> arr = new ArrayList<Integer>(15);
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
        arr.add(userId_14);
        arr.add(userId_15);
        arr.add(userId_16);
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
        } else if (userId == 14) {
            return userId_14;
        } else if (userId == 15) {
            return userId_15;
        } else if (userId == 16) {
            return userId_16;
        }
        return 0;
    }
}
