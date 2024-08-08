package com.smlj.dailypaper.entity.vo.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class To_Excel implements Serializable {
    private ArrayList<String> colNames = new ArrayList<String>();
    private ArrayList<To_ExcelRow> rows = new ArrayList<To_ExcelRow>();
}
