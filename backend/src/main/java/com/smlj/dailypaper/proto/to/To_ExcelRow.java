package com.smlj.dailypaper.proto.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class To_ExcelRow implements Serializable {
    private long time = 0;
    private ArrayList<String> contents = new ArrayList<String>();
}
